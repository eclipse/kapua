/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import static org.eclipse.kapua.locator.KapuaLocator.getInstance;
import static org.eclipse.kapua.qa.utils.Suppressed.closeAll;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.MqttAsyncTransport;
import org.eclipse.kapua.kura.simulator.Simulator;
import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.annotated.AnnotatedApplication;
import org.eclipse.kapua.kura.simulator.app.command.SimpleCommandApplication;
import org.eclipse.kapua.kura.simulator.app.deploy.SimpleDeployApplication;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.qa.steps.EmbeddedBroker;
import org.eclipse.kapua.qa.utils.Starting;
import org.eclipse.kapua.service.TestJAXBContextProvider;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundle;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundleManagementService;
import org.eclipse.kapua.service.device.management.bundle.DeviceBundles;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.scada.utils.concurrent.NamedThreadFactory;
import org.junit.Assert;

import com.google.inject.Inject;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class SimulatorSteps {

    private static final long BUNDLE_TIMEOUT = Duration.ofSeconds(Integer.getInteger("org.eclipse.kapua.service.device.steps.timeoutBundleOperation", 5)).toMillis();

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();
    private String clientId;
    private String accountName;

    private List<DeviceBundle> bundles;

    private ScheduledExecutorService downloadExecutor;

    @FunctionalInterface
    public interface ThrowingConsumer<T> {

        public void consume(T t) throws Exception;
    }

    @Inject
    public SimulatorSteps(/* dependency */ final EmbeddedBroker broker, /* dependency */ final DBHelper dbHelper) {
    }

    @Before
    public void beforeScenario(final Scenario scenario) throws Exception {
        XmlUtil.setContextProvider(new TestJAXBContextProvider());

        this.downloadExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("DownloadSimulator"));
    }

    @After
    public void cleanup() {
        closeAll(closables.values().stream().flatMap(Collection::stream));
        closables.clear();

        this.downloadExecutor.shutdown();
    }

    @Given("The account name is (.*) and the client ID is (.*)")
    public void setClientId(final String accountName, final String clientId) {
        this.clientId = clientId;
        this.accountName = accountName;
    }

    @When("I start the simulator connecting to: (.*)")
    public void startSimulator(final String url) throws Exception {
        final GatewayConfiguration configuration = new GatewayConfiguration(url, accountName, clientId);

        final Set<Application> apps = new HashSet<>();
        apps.add(new SimpleCommandApplication(s -> String.format("Command '%s' not found", s)));
        apps.add(AnnotatedApplication.build(new SimpleDeployApplication(downloadExecutor)));

        try (Starting starting = new Starting()) {
            final MqttAsyncTransport transport = new MqttAsyncTransport(configuration);
            starting.add(transport);
            final Simulator simulator = new Simulator(configuration, transport, apps);
            starting.add(simulator);

            this.closables.put("simulator/" + clientId, starting.started());
        }

    }

    @When("I stop the simulator")
    public void stopSimulator() {
        closeAll(closables.remove("simulator/" + clientId));
    }

    @Then("Device (.*) for account (.*) is registered")
    public void deviceIsRegistered(final String clientId, final String accountName) throws KapuaException {
        assertConnectionStatus(clientId, accountName, DeviceConnectionStatus.CONNECTED);
    }

    @Then("Device (.*) for account (.*) is not registered")
    public void deviceIsNotRegistered(final String clientId, final String accountName) throws KapuaException {
        assertConnectionStatus(clientId, accountName, DeviceConnectionStatus.DISCONNECTED);
    }

    @Then("Device (.*) for account (.*) should report simulator device information")
    public void checkApplication(String clientId, String accountName) throws KapuaException {
        withUserAccount(accountName, account -> {
            DeviceRegistryService service = getInstance().getService(DeviceRegistryService.class);
            Device device = service.findByClientId(account.getId(), clientId);

            Assert.assertNotNull(device);

            Assert.assertEquals("Kura Simulator (Display Name)", device.getDisplayName());
            // Assert.assertEquals("Kura Simulator (Model Name)", device.getModelName());
            Assert.assertEquals("kura-simulator-" + clientId, device.getModelId());
            // Assert.assertEquals("ksim-part-123456-" + clientId, device.getPartNumber());
            Assert.assertEquals("ksim-serial-123456-" + clientId, device.getSerialNumber());
            // Assert.assertEquals( "1", device.getAvailableProcessors () );
            // Assert.assertEquals( "640", device.getTotalMemory());
            Assert.assertEquals("fw.v42", device.getFirmwareVersion());
            Assert.assertEquals("bios.v42", device.getBiosVersion());
            // Assert.assertEquals( "Kura Simulator (OS)", device.getOperatingSystem());
            // Assert.assertEquals("ksim-os-v42", device.getOperatingSystemVersion());
            // Assert.assertEquals("ksim-arch", device.getOperatingSystemArchitecture());
            // Assert.assertEquals("Kura Simulator (Java)", device.getJvmName());
            Assert.assertEquals("ksim-java-v42", device.getJvmVersion());
            // Assert.assertEquals( "Kura Simulator (Java Profile)", device.getJvmProfile());
            Assert.assertEquals("ksim-kura-v42", device.getApplicationFrameworkVersion());
            // Assert.assertEquals("Kura Simulator (OSGi version)", device.getOsgiFrameworkName());
            Assert.assertEquals("ksim-osgi-v42", device.getOsgiFrameworkVersion());

            Set<String> apps = new HashSet<>(Arrays.asList(device.getApplicationIdentifiers().split(",")));
            Assert.assertEquals(new HashSet<>(Arrays.asList("CMD-V1", "DEPLOY-V2")), apps);
        });
    }

    @When("I start the bundle (.*) with version (.*)")
    public void startBundle(String bundleSymbolicName, String version) throws KapuaException {
        withUserAccount(accountName, account -> {
            withDevice(account, clientId, device -> {
                DeviceBundle bundle = findBundle(bundleSymbolicName, version);
                DeviceBundleManagementService service = getInstance().getService(DeviceBundleManagementService.class);
                service.start(account.getId(), device.getId(), Long.toString(bundle.getId()), BUNDLE_TIMEOUT);
            });
        });
    }

    @When("I stop the bundle (.*) with version (.*)")
    public void stopBundle(String bundleSymbolicName, String version) throws KapuaException {
        withUserAccount(accountName, account -> {
            withDevice(account, clientId, device -> {
                DeviceBundle bundle = findBundle(bundleSymbolicName, version);
                DeviceBundleManagementService service = getInstance().getService(DeviceBundleManagementService.class);
                service.stop(account.getId(), device.getId(),  Long.toString(bundle.getId()), BUNDLE_TIMEOUT);
            });
        });
    }

    @When("I fetch the bundle states")
    public void fetchBundlesState() throws KapuaException {
        withUserAccount(accountName, account -> {
            withDevice(account, clientId, device -> {
                DeviceBundleManagementService service = getInstance().getService(DeviceBundleManagementService.class);
                DeviceBundles bundles = service.get(account.getId(), device.getId(), BUNDLE_TIMEOUT);

                this.bundles = bundles.getBundles();
            });
        });
    }

    @Then("The bundle (.*) with version (.*) is present and (.*)")
    public void hasBundle(String bundleSymbolicName, String version, String state) throws KapuaException {
        DeviceBundle bundle = findBundle(bundleSymbolicName, version);

        Assert.assertEquals(state, bundle.getState());
    }

    private DeviceBundle findBundle(String bundleSymbolicName, String version) {
        List<DeviceBundle> bundles = this.bundles.stream()
                .filter(bundle -> bundle.getName().equals(bundleSymbolicName))
                .filter(bundle -> bundle.getVersion().equals(version))
                .collect(Collectors.toList());

        if (bundles.isEmpty()) {
            Assert.fail(String.format("Bundle %s/%s is not present", bundleSymbolicName, version));
        }
        if (bundles.size() > 1) {
            Assert.fail(String.format("There is more than one entry for bundle %s/%s", bundleSymbolicName, version));
        }
        return bundles.get(0);
    }

    private void assertConnectionStatus(final String clientId, final String accountName, final DeviceConnectionStatus expectedState) throws KapuaException {
        final DeviceConnectionService service = getInstance().getService(DeviceConnectionService.class);

        withUserAccount(accountName, account -> {

            final DeviceConnection result = service.findByClientId(account.getId(), clientId);
            Assert.assertNotNull(result);
            Assert.assertEquals(clientId, result.getClientId());
            Assert.assertEquals(expectedState, result.getStatus());
        });
    }

    private void withUserAccount(final String accountName, final ThrowingConsumer<User> consumer) throws KapuaException {
        final UserService userService = getInstance().getService(UserService.class);

        KapuaSecurityUtils.doPrivileged(() -> {

            final User account = userService.findByName(accountName);

            if (account == null) {
                Assert.fail("Unable to find account: " + accountName);
                return;
            }

            consumer.consume(account);
        });
    }

    private void withDevice(User account, String clientId, ThrowingConsumer<Device> consumer) throws Exception {
        DeviceRegistryService service = getInstance().getService(DeviceRegistryService.class);
        Device device = service.findByClientId(account.getId(), clientId);
        if (device == null) {
            throw new IllegalStateException(String.format("Unable to find device '%s' for account '%s'", clientId, account.getId()));
        }

        consumer.consume(device);
    }
}
