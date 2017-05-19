/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.internal;

import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;
import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsNotEqualTo;
import static org.eclipse.kapua.service.device.registry.DeviceCredentialsMode.LOOSE;
import static org.eclipse.kapua.service.device.registry.DeviceCredentialsMode.STRICT;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.security.acl.Permission;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.RegistryJAXBContextProvider;
import org.eclipse.kapua.service.device.registry.TestConfig;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in DeviceRegistry.feature scenarios.
 *
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service -
 *
 *
 */
@ScenarioScoped
public class DeviceRegistryServiceTestSteps extends AbstractKapuaSteps {

    public static String DEFAULT_PATH = "src/main/sql/H2";
    public static String DEFAULT_COMMONS_PATH = "../../../commons";
    public static String CREATE_DEVICE_TABLES = "dvc_*_create.sql";
    public static String DROP_DEVICE_TABLES = "dvc_*_drop.sql";

    public static String TEST_DEVICE_NAME = "test_name";
    public static String TEST_BIOS_VERSION_1 = "bios_version_1";
    public static String TEST_BIOS_VERSION_2 = "bios_version_2";
    public static String TEST_BIOS_VERSION_3 = "bios_version_3";

    // Strings for client ID character set and length checks
    public static String simpleClientId = "simpleClientIdWith64Chars_12345678901234567890123456789012345678";
    public static String fullClientId = "fullClientIdWith64Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓";
    public static String simpleClientIdTooLong = "simpleClientIdWith65Chars_123456789012345678901234567890123456789";
    public static String fullClientIdTooLong = "fullClientIdWith65Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔";

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DeviceRegistryServiceTestSteps.class);

    // Currently executing scenario.
    Scenario scenario;

    // Various device registry related service references
    DeviceRegistryService deviceRegistryService = null;
    DeviceFactory deviceFactory = null;

    // Device registry related objects
    DeviceCreator deviceCreator = null;
    Device device = null;

    // The registry ID of a device
    KapuaId deviceId = null;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

    // A list result for device query operations
    DeviceListResult deviceList = null;

    // Item count
    long count = 0;

    // String scratchpad
    String stringValue;

    // Default constructor
    public DeviceRegistryServiceTestSteps() {
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    // Setup and tear-down steps

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {
        container.startup();
        locator = KapuaLocatorImpl.getInstance();

        this.scenario = scenario;
        exceptionCaught = false;

        // Create User Service tables
        enableH2Connection();
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        // Drop the Device Registry Service tables
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_DEVICE_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);

        // Create the Device Registry Service tables
        KapuaConfigurableServiceSchemaUtils.createSchemaObjects(DEFAULT_COMMONS_PATH);
        // XmlUtil.setContextProvider(new AccountsJAXBContextProvider());

        MockedLocator mockLocator = (MockedLocator) locator;

        // Inject mocked Authorization Service method checkPermission
        AuthorizationService mockedAuthorization = mock(AuthorizationService.class);
        // TODO: Check why does this line needs an explicit cast!
        Mockito.doNothing().when(mockedAuthorization).checkPermission(
                (org.eclipse.kapua.service.authorization.permission.Permission) any(Permission.class));
        mockLocator.setMockedService(org.eclipse.kapua.service.authorization.AuthorizationService.class,
                mockedAuthorization);

        // Inject mocked Permission Factory
        PermissionFactory mockedPermissionFactory = mock(PermissionFactory.class);
        mockLocator.setMockedFactory(org.eclipse.kapua.service.authorization.permission.PermissionFactory.class,
                mockedPermissionFactory);

        // Inject actual device registry related services
        deviceRegistryService = new DeviceRegistryServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.device.registry.DeviceRegistryService.class, deviceRegistryService);
        deviceFactory = new DeviceFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.device.registry.DeviceFactory.class, deviceFactory);

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);

        // Setup JAXB context
        XmlUtil.setContextProvider(new RegistryJAXBContextProvider());
    }

    @After
    public void afterScenario()
            throws Exception {
        // Drop the Device Registry Service tables
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_DEVICE_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();

        container.shutdown();
    }

    // The Cucumber test steps

    @Given("^A default device creator$")
    public void prepareDefaultDeviceCreator()
            throws KapuaException {
        deviceCreator = prepareRegularDeviceCreator(rootScopeId, "device_1");
        assertNotNull(deviceCreator);
    }

    @Given("^A device named \"(.*)\"$")
    public void createNamedDevice(String name)
            throws KapuaException {
        deviceCreator = prepareRegularDeviceCreator(rootScopeId, name);
        assertNotNull(deviceCreator);
        device = deviceRegistryService.create(deviceCreator);
        assertNotNull(device);
        deviceId = device.getId();
    }

    @Given("^A device with BIOS version \"(.*)\" named \"(.*)\"$")
    public void createNamedDeviceWithBiosVersion(String version, String name)
            throws KapuaException {
        deviceCreator = prepareRegularDeviceCreator(rootScopeId, name);
        assertNotNull(deviceCreator);
        deviceCreator.setBiosVersion(version);
        device = deviceRegistryService.create(deviceCreator);
        assertNotNull(device);
        deviceId = device.getId();
    }

    @Given("^I create (\\d+) randomly named devices with BIOS version \"(.*)\"$")
    public void generateABunchOfTestDevices(int number, String version)
            throws KapuaException {
        DeviceCreator tmpDevCr = null;

        for (int i = 0; i < number; i++) {
            tmpDevCr = deviceFactory.newCreator(rootScopeId, "test_" + String.valueOf(random.nextInt()));
            tmpDevCr.setBiosVersion(version);
            deviceRegistryService.create(tmpDevCr);
        }
    }

    @Given("^I create (\\d+) randomly named devices in scope (\\d+)$")
    public void generateABunchOfTestDevicesInScope(int number, int scope)
            throws KapuaException {
        DeviceCreator tmpDevCr = null;
        KapuaId tmpId;
        String tmpClient;

        for (int i = 0; i < number; i++) {
            tmpId = new KapuaEid(BigInteger.valueOf(scope));
            tmpClient = "test_" + String.valueOf(random.nextInt());
            tmpDevCr = deviceFactory.newCreator(tmpId, tmpClient);
            deviceRegistryService.create(tmpDevCr);
        }
    }

    @When("^I create a device from the existing creator$")
    public void createDeviceFromExistingCreator()
            throws KapuaException {
        device = deviceRegistryService.create(deviceCreator);
        assertNotNull(device);
        deviceId = device.getId();
    }

    @When("^I configure$")
    public void setConfigurationValue(List<TestConfig> testConfigs)
            throws KapuaException {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaEid scopeId = null;
        KapuaEid parentScopeId = null;

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
            scopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getScopeId())));
            parentScopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getParentScopeId())));
        }
        try {
            exceptionCaught = false;
            deviceRegistryService.setConfigValues(scopeId,
                    parentScopeId, valueMap);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I search for a device with the remembered ID$")
    public void findDeviceWithRememberedId()
            throws KapuaException {
        device = deviceRegistryService.find(rootScopeId, deviceId);
    }

    @When("^I search for a device with the client ID \"(.+)\"$")
    public void findDeviceWithClientId(String clientId)
            throws KapuaException {
        device = deviceRegistryService.findByClientId(rootScopeId, clientId);
        assertNotNull(device);
    }

    @When("^I search for a device with a random ID$")
    public void findDeviceWithRandomId()
            throws KapuaException {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        device = deviceRegistryService.find(rootScopeId, tmpId);
    }

    @When("^I search for a device with a random client ID$")
    public void findDeviceWithRandomClientId()
            throws KapuaException {
        String tmpClientId = String.valueOf(random.nextLong());
        device = deviceRegistryService.findByClientId(rootScopeId, tmpClientId);
    }

    @When("^I query for devices with BIOS version \"(.*)\"$")
    public void queryForDevicesBasedOnBiosVersion(String version)
            throws KapuaException {
        DeviceQuery tmpQuery = new DeviceQueryImpl(rootScopeId);

        // Search for the known bios version string
        tmpQuery.setPredicate(attributeIsEqualTo("biosVersion", version));
        deviceList = (DeviceListResult) deviceRegistryService.query(tmpQuery);
        assertNotNull(deviceList);
    }

    @When("^I query for devices with BIOS different from \"(.*)\"$")
    public void queryForDevicesWithDifferentBiosVersion(String version)
            throws KapuaException {
        DeviceQuery tmpQuery = new DeviceQueryImpl(rootScopeId);

        // Search for the known bios version string
        tmpQuery.setPredicate(attributeIsNotEqualTo("biosVersion", version));
        deviceList = (DeviceListResult) deviceRegistryService.query(tmpQuery);
        assertNotNull(deviceList);
    }

    @When("^I query for devices with Client Id \"(.*)\"$")
    public void queryForDevicesBasedOnClientId(String id)
            throws KapuaException {
        DeviceQuery tmpQuery = new DeviceQueryImpl(rootScopeId);

        // Search for the known bios version string
        tmpQuery.setPredicate(attributeIsEqualTo("clientId", id));
        deviceList = (DeviceListResult) deviceRegistryService.query(tmpQuery);
        assertNotNull(deviceList);
    }

    @And("^I extract the first device$")
    public void getFirstDeviceFromList() {
        // A device should have been found
        assertNotEquals(0, deviceList.getSize());
        device = deviceList.getItem(0);
        assertNotNull(device);
    }

    @When("^I count the devices in scope (\\d+)$")
    public void countDevicesInScope(int scope)
            throws KapuaException {
        DeviceQuery tmpQuery = new DeviceQueryImpl(new KapuaEid(BigInteger.valueOf(scope)));
        count = 0;
        assertNotNull(tmpQuery);
        count = deviceRegistryService.count(tmpQuery);
    }

    @When("^I count devices with BIOS version \"(.*)\"$")
    public void countDevicesWithBIOSVersion(String version)
            throws KapuaException {
        DeviceQuery tmpQuery = new DeviceQueryImpl(rootScopeId);
        assertNotNull(tmpQuery);
        tmpQuery.setPredicate(attributeIsEqualTo("biosVersion", version));
        count = 0;
        count = deviceRegistryService.count(tmpQuery);
    }

    @When("^I update some device parameters$")
    public void updateDeviceParameters()
            throws KapuaException {
        device.setBiosVersion(device.getBiosVersion() + "_upd");
        device.setCustomAttribute1(device.getCustomAttribute1() + "_upd");
        device.setCredentialsMode(STRICT);
        deviceRegistryService.update(device);
    }

    @When("^I update the device cleint ID to \"(.+)\"$")
    public void updateDeviceClientId(String newId)
            throws KapuaException {
        stringValue = device.getClientId();
        device.setClientId(newId);
        deviceRegistryService.update(device);
    }

    @When("^I update a device with an invalid ID$")
    public void updateDeviceWithInvalidId()
            throws KapuaException {
        device.setId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        try {
            exceptionCaught = false;
            deviceRegistryService.update(device);
        } catch (KapuaEntityNotFoundException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I delete the device with the cleint id \"(.+)\"$")
    public void deleteDeviceWithClientId(String clientId)
            throws KapuaException {
        Device tmpDev = deviceRegistryService.findByClientId(rootScopeId, clientId);
        assertNotNull(tmpDev);
        deviceRegistryService.delete(rootScopeId, tmpDev.getId());
    }

    @When("^I delete a device with random IDs$")
    public void deleteDeviceWithRandomIds()
            throws KapuaException {
        KapuaId rndScope = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        KapuaId rndDev = new KapuaEid(BigInteger.valueOf(random.nextLong()));

        try {
            exceptionCaught = false;
            deviceRegistryService.delete(rndScope, rndDev);
        } catch (KapuaEntityNotFoundException ex) {
            exceptionCaught = true;
        }
    }

    @Then("^The device has a non-null ID$")
    public void checkCreatedDeviceId()
            throws KapuaException {
        assertNotNull(device.getId());
        assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        assertEquals(deviceCreator.getClientId(), device.getClientId());
    }

    @Then("^It is possible to find the device based on its registry ID$")
    public void fincDeviceByRememberedId()
            throws KapuaException {
        Device tmpDev = deviceRegistryService.find(rootScopeId, device.getId());

        assertNotNull(tmpDev);
        assertEquals(device.getClientId(), tmpDev.getClientId());
    }

    @Then("^It is possible to find the device based on its client ID$")
    public void fincDeviceByRememberedClientId()
            throws KapuaException {
        Device tmpDev = deviceRegistryService.findByClientId(rootScopeId, device.getClientId());

        assertNotNull(tmpDev);
        assertEquals(device.getId(), tmpDev.getId());
    }

    @Then("^Named device registry searches are case sesntitive$")
    public void checkCaseSensitivnessOfRegistrySearches()
            throws KapuaException {
        assertNull(deviceRegistryService.findByClientId(rootScopeId, deviceCreator.getClientId().toLowerCase()));
        assertNull(deviceRegistryService.findByClientId(rootScopeId, deviceCreator.getClientId().toUpperCase()));
        assertNotNull(deviceRegistryService.findByClientId(rootScopeId, deviceCreator.getClientId()));
    }

    @Then("^The device matches the creator parameters$")
    public void checkCreatedDeviceAgainstCreatorParameters()
            throws KapuaException {
        assertNotNull(device.getId());
        assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        assertEquals(deviceCreator.getClientId().length(), device.getClientId().length());
        assertEquals(deviceCreator.getClientId(), device.getClientId());
        assertEquals(deviceCreator.getConnectionId(), device.getConnectionId());
        assertEquals(deviceCreator.getDisplayName(), device.getDisplayName());
        assertEquals(deviceCreator.getSerialNumber(), device.getSerialNumber());
        assertEquals(deviceCreator.getModelId(), device.getModelId());
        assertEquals(deviceCreator.getImei(), device.getImei());
        assertEquals(deviceCreator.getImsi(), device.getImsi());
        assertEquals(deviceCreator.getIccid(), device.getIccid());
        assertEquals(deviceCreator.getBiosVersion(), device.getBiosVersion());
        assertEquals(deviceCreator.getFirmwareVersion(), device.getFirmwareVersion());
        assertEquals(deviceCreator.getOsVersion(), device.getOsVersion());
        assertEquals(deviceCreator.getJvmVersion(), device.getJvmVersion());
        assertEquals(deviceCreator.getOsgiFrameworkVersion(), device.getOsgiFrameworkVersion());
        assertEquals(deviceCreator.getApplicationFrameworkVersion(), device.getApplicationFrameworkVersion());
        assertEquals(deviceCreator.getApplicationIdentifiers(), device.getApplicationIdentifiers());
        assertEquals(deviceCreator.getAcceptEncoding(), device.getAcceptEncoding());
        assertEquals(deviceCreator.getCustomAttribute1(), device.getCustomAttribute1());
        assertEquals(deviceCreator.getCustomAttribute2(), device.getCustomAttribute2());
        assertEquals(deviceCreator.getCustomAttribute3(), device.getCustomAttribute3());
        assertEquals(deviceCreator.getCustomAttribute4(), device.getCustomAttribute4());
        assertEquals(deviceCreator.getCustomAttribute5(), device.getCustomAttribute5());
        assertEquals(deviceCreator.getCredentialsMode(), device.getCredentialsMode());
        assertEquals(deviceCreator.getPreferredUserId(), device.getPreferredUserId());
        assertEquals(deviceCreator.getStatus(), device.getStatus());
    }

    @Then("^The device was correctly updated$")
    public void checkUpdatedDeviceAgainstOriginal()
            throws KapuaException {
        Device tmpDevice = null;

        tmpDevice = deviceRegistryService.find(device.getScopeId(), device.getId());
        assertNotNull(tmpDevice);

        assertEquals(tmpDevice.getScopeId(), device.getScopeId());
        assertEquals(tmpDevice.getClientId().length(), device.getClientId().length());
        assertEquals(tmpDevice.getClientId(), device.getClientId());
        assertEquals(tmpDevice.getConnectionId(), device.getConnectionId());
        assertEquals(tmpDevice.getDisplayName(), device.getDisplayName());
        assertEquals(tmpDevice.getSerialNumber(), device.getSerialNumber());
        assertEquals(tmpDevice.getModelId(), device.getModelId());
        assertEquals(tmpDevice.getImei(), device.getImei());
        assertEquals(tmpDevice.getImsi(), device.getImsi());
        assertEquals(tmpDevice.getIccid(), device.getIccid());
        assertEquals(tmpDevice.getBiosVersion(), device.getBiosVersion());
        assertEquals(tmpDevice.getFirmwareVersion(), device.getFirmwareVersion());
        assertEquals(tmpDevice.getOsVersion(), device.getOsVersion());
        assertEquals(tmpDevice.getJvmVersion(), device.getJvmVersion());
        assertEquals(tmpDevice.getOsgiFrameworkVersion(), device.getOsgiFrameworkVersion());
        assertEquals(tmpDevice.getApplicationFrameworkVersion(), device.getApplicationFrameworkVersion());
        assertEquals(tmpDevice.getApplicationIdentifiers(), device.getApplicationIdentifiers());
        assertEquals(tmpDevice.getAcceptEncoding(), device.getAcceptEncoding());
        assertEquals(tmpDevice.getCustomAttribute1(), device.getCustomAttribute1());
        assertEquals(tmpDevice.getCustomAttribute2(), device.getCustomAttribute2());
        assertEquals(tmpDevice.getCustomAttribute3(), device.getCustomAttribute3());
        assertEquals(tmpDevice.getCustomAttribute4(), device.getCustomAttribute4());
        assertEquals(tmpDevice.getCustomAttribute5(), device.getCustomAttribute5());
        assertEquals(tmpDevice.getCredentialsMode(), device.getCredentialsMode());
        assertEquals(tmpDevice.getPreferredUserId(), device.getPreferredUserId());
        assertEquals(tmpDevice.getStatus(), device.getStatus());
    }

    @Then("^The device client id is \"(.*)\"$")
    public void checkDeviceClientName(String name)
            throws KapuaException {
        assertEquals(name, device.getClientId());
    }

    @Then("^I find (\\d+) devices?$")
    public void checkListForNumberOfItems(int number) {
        assertEquals(number, deviceList.getSize());
    }

    @Then("^There (?:are|is) (\\d+) devices?$")
    public void checkNumberOfDevices(int number) {
        assertEquals(number, count);
    }

    @Then("^The client ID was not changed$")
    public void checkDeviceClientIdForChanges()
            throws KapuaException {
        Device tmpDevice = deviceRegistryService.find(rootScopeId, device.getId());
        assertNotEquals(device.getClientId(), tmpDevice.getClientId());
        assertEquals(stringValue, tmpDevice.getClientId());
    }

    @Then("^There is no device with the client ID \"(.+)\"$")
    public void checkWhetherNamedDeviceStillExists(String clientId)
            throws KapuaException {
        Device tmpDevice = deviceRegistryService.findByClientId(rootScopeId, clientId);
        assertNull(tmpDevice);
    }

    @Then("^There is no such device$")
    public void deviceMustBeNull() {
        assertNull(device);
    }

    @Then("^An exception is caught$")
    public void checkThatAnExceptionWasCaught() {
        assertTrue(exceptionCaught);
    }

    @Then("^All device factory functions must return non null values$")
    public void exerciseAllDeviceFactoryFunctions() {
        Device tmpDevice = null;
        DeviceCreator tmpCreator = null;
        DeviceQuery tmpQuery = null;
        DeviceListResult tmpListRes = null;

        tmpDevice = deviceFactory.newEntity(rootScopeId);
        tmpCreator = deviceFactory.newCreator(rootScopeId, "TestDevice");
        tmpQuery = deviceFactory.newQuery(rootScopeId);
        tmpListRes = deviceFactory.newListResult();

        assertNotNull(tmpDevice);
        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
        assertNotNull(tmpListRes);
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a device creator object. The creator is pre-filled with default data.
    private DeviceCreator prepareRegularDeviceCreator(KapuaId accountId, String client) {
        // DeviceCreator tmpDeviceCreator = deviceFactory.newCreator(accountId, client);
        DeviceCreatorImpl tmpDeviceCreator = new DeviceCreatorImpl(accountId);

        tmpDeviceCreator.setClientId(client);
        tmpDeviceCreator.setConnectionId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDeviceCreator.setDisplayName(TEST_DEVICE_NAME);
        tmpDeviceCreator.setSerialNumber("serialNumber");
        tmpDeviceCreator.setModelId("modelId");
        tmpDeviceCreator.setImei(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setImsi(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setIccid(String.valueOf(random.nextInt()));
        tmpDeviceCreator.setBiosVersion("biosVersion");
        tmpDeviceCreator.setFirmwareVersion("firmwareVersion");
        tmpDeviceCreator.setOsVersion("osVersion");
        tmpDeviceCreator.setJvmVersion("jvmVersion");
        tmpDeviceCreator.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDeviceCreator.setApplicationFrameworkVersion("kapuaVersion");
        tmpDeviceCreator.setApplicationIdentifiers("applicationIdentifiers");
        tmpDeviceCreator.setAcceptEncoding("acceptEncoding");
        tmpDeviceCreator.setGpsLatitude(45.2);
        tmpDeviceCreator.setGpsLongitude(26.3);
        tmpDeviceCreator.setCustomAttribute1("customAttribute1");
        tmpDeviceCreator.setCustomAttribute2("customAttribute2");
        tmpDeviceCreator.setCustomAttribute3("customAttribute3");
        tmpDeviceCreator.setCustomAttribute4("customAttribute4");
        tmpDeviceCreator.setCustomAttribute5("customAttribute5");
        tmpDeviceCreator.setCredentialsMode(LOOSE);
        tmpDeviceCreator.setPreferredUserId(new KapuaEid(BigInteger.valueOf(random.nextLong())));
        tmpDeviceCreator.setStatus(DeviceStatus.ENABLED);

        return tmpDeviceCreator;
    }
}
