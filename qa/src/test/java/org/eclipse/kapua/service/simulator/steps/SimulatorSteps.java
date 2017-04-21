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
package org.eclipse.kapua.service.simulator.steps;

import static java.time.Duration.ofSeconds;
import static org.eclipse.kapua.locator.KapuaLocator.getInstance;
import static org.eclipse.kapua.service.simulator.steps.Suppressed.withException;

import java.net.BindException;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.MqttAsyncTransport;
import org.eclipse.kapua.kura.simulator.Simulator;
import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.command.SimpleCommandApplication;
import org.eclipse.kapua.service.DBHelper;
import org.eclipse.kapua.service.TestJAXBContextProvider;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;

import com.google.inject.Inject;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

@ScenarioScoped
public class SimulatorSteps {

    private Map<String, List<AutoCloseable>> closables = new HashMap<>();

    /**
     * Embedded broker configuration file from classpath resources.
     */
    public static final String ACTIVEMQ_XML = "xbean:activemq.xml";

    /**
     * Single point to database access.
     */
    private final DBHelper dbHelper;

    private BrokerService broker;

    @FunctionalInterface
    public interface ThrowingConsumer<T> {

        public void consume(T t) throws Exception;
    }

    @Inject
    public SimulatorSteps(final DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Before
    public void beforeScenario(final Scenario scenario) throws Exception {

        // test if port is already open

        try (ServerSocket socket = new ServerSocket(1883)) {
        } catch (BindException e) {
            throw new IllegalStateException("Broker port is already in use");
        }

        // start the broker

        broker = BrokerFactory.createBroker(ACTIVEMQ_XML);
        broker.start();

        // wait for the broker

        if (!broker.waitUntilStarted(Duration.ofSeconds(20).toMillis())) {
            throw new IllegalStateException("Failed to start up broker in time");
        }

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
    }

    @After
    public void afterScenario() throws Exception {

        try (final Suppressed<Exception> s = withException()) {

            // close all resources

            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);

            // shut down broker

            if (broker != null) {
                broker.stop();
                broker = null;
            }

            // clear database

            dbHelper.deleteAll();
            KapuaSecurityUtils.clearSession();
        }
    }

    @When("^I start the simulator named (.*) for account (.*) connecting to: (.*)$")
    public void startSimulator(final String clientId, final String accountName, final String url) throws Exception {
        final GatewayConfiguration configuration = new GatewayConfiguration(url, accountName, clientId);

        final Set<Application> apps = new HashSet<>();
        apps.add(new SimpleCommandApplication(s -> String.format("Command '%s' not found", s)));

        try (Starting starting = new Starting()) {
            final MqttAsyncTransport transport = new MqttAsyncTransport(configuration);
            starting.add(transport);
            final Simulator simulator = new Simulator(configuration, transport, apps);
            starting.add(simulator);

            this.closables.put("simulator/" + clientId, starting.started());
        }

    }

    @When("^I stop the simulator named (.*)$")
    public void stopSimulator(final String clientId) {
        final List<AutoCloseable> list = closables.remove("simulator/" + clientId);
        Suppressed.closeAll(list);
    }

    @When("^I wait (\\d+) seconds?$")
    public void waitSeconds(int seconds) throws InterruptedException {
        Thread.sleep(ofSeconds(seconds).toMillis());
    }

    @Then("^Device (.*) for account (.*) is registered$")
    public void deviceIsRegistered(final String clientId, final String accountName) throws KapuaException {
        assertConnectionStatus(clientId, accountName, DeviceConnectionStatus.CONNECTED);
    }

    @Then("^Device (.*) for account (.*) is not registered$")
    public void deviceIsNotRegistered(final String clientId, final String accountName) throws KapuaException {
        assertConnectionStatus(clientId, accountName, DeviceConnectionStatus.DISCONNECTED);
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
}
