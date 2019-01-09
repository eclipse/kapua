/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceFactoryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class DeviceRegistrySteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegistrySteps.class);

    private static final String TEST_DEVICE_NAME = "test_name";
    private static final String TEST_BIOS_VERSION_1 = "bios_version_1";
    private static final String TEST_BIOS_VERSION_2 = "bios_version_2";
    private static final String TEST_BIOS_VERSION_3 = "bios_version_3";

    // Strings for client ID character set and length checks
    private static String simpleClientId = "simpleClientIdWith64Chars_12345678901234567890123456789012345678";
    private static String fullClientId = "fullClientIdWith64Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓";
    private static String simpleClientIdTooLong = "simpleClientIdWith65Chars_123456789012345678901234567890123456789";
    private static String fullClientIdTooLong = "fullClientIdWith65Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔";

    // Various device registry related service references
    private DeviceRegistryService deviceRegistryService;
    private DeviceFactory deviceFactory;

    // Default constructor
    @Inject
    public DeviceRegistrySteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    /**
     * Setup DI with Google Guice DI.
     * Create mocked and non mocked service under test and bind them with Guice.
     * It is based on custom MockedLocator locator that is meant for sevice unit tests.
     */
    private void setupDI() {

        MockedLocator mockedLocator = (MockedLocator) KapuaLocator.getInstance();

        AbstractModule module = new AbstractModule() {

            @Override
            protected void configure() {

                // Inject mocked Authorization Service method checkPermission
                AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
                try {
                    Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
                } catch (KapuaException e) {
                    // skip
                }
                bind(AuthorizationService.class).toInstance(mockedAuthorization);
                // Inject mocked Permission Factory
                bind(PermissionFactory.class).toInstance(Mockito.mock(PermissionFactory.class));
                // Set KapuaMetatypeFactory for Metatype configuration
                bind(KapuaMetatypeFactory.class).toInstance(new KapuaMetatypeFactoryImpl());

                // Inject actual Device registry service related services
                DeviceEntityManagerFactory deviceEntityManagerFactory = DeviceEntityManagerFactory.instance();
                bind(DeviceEntityManagerFactory.class).toInstance(deviceEntityManagerFactory);
                bind(DeviceRegistryService.class).toInstance(new DeviceRegistryServiceImpl());
                bind(DeviceFactory.class).toInstance(new DeviceFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // ************************************************************************************
    // ************************************************************************************
    // * Definition of Cucumber scenario steps                                            *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Setup and tear-down steps                                                        *
    // ************************************************************************************

    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceFactory = locator.getFactory(DeviceFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new DeviceRegistryJAXBContextProvider());
    }

    @After
    public void afterScenario() {

        // Clean up the database
        try {
            LOGGER.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            LOGGER.error("Failed to log out in @After", e);
        }
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    @When("^I configure the device registry service$")
    public void setConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getCurrentScopeId();
        KapuaId scopeId = getCurrentParentId();

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getParentId() != null) {
                scopeId = getKapuaId(config.getParentId());
            }
            if (config.getScopeId() != null) {
                accId = getKapuaId(config.getScopeId());
            }
        }

        primeException();
        try {
            deviceRegistryService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular device creator$")
    public void prepareDefaultDeviceCreator() {

        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), "device_1");
        stepData.put("DeviceCreator", deviceCreator);
    }

    @Given("^A null device creator$")
    public void createANullDeviceCreator() {

        stepData.put("DeviceCreator", null);
    }

    @When("^I set the creator scope ID to null$")
    public void setDeviceCreatorScopeToNull() {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");
        deviceCreator.setScopeId(null);
        stepData.put("DeviceCreator", deviceCreator);
    }

    @When("^I set the creator client ID to null$")
    public void setDeviceCreatorClientToNull() {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");
        deviceCreator.setClientId(null);
        stepData.put("DeviceCreator", deviceCreator);
    }

    @Given("^A regular device$")
    public void createRegularDevice() {

        Device device = prepareRegularDevice(getCurrentParentId(), getKapuaId());
        stepData.put("Device", device);
    }

    @Given("^A null device$")
    public void createANullDevice() {

        stepData.put("Device", null);
    }

    @When("^I set the device scope ID to null$")
    public void setDeviceScopeToNull() {

        Device device = (Device) stepData.get("Device");
        device.setScopeId(null);
        stepData.put("Device", device);
    }

    @When("^I set the device ID to null$")
    public void setDeviceIdToNull() {

        Device device = (Device) stepData.get("Device");
        device.setId(null);
        stepData.put("Device", device);
    }

    @Given("^A regular query$")
    public void createRegularQuery() {

        DeviceQuery query = deviceFactory.newQuery(getCurrentScopeId());
        stepData.put("DeviceQuery", query);
    }

    @Given("^A query with a null Scope ID$")
    public void createQueryWithNullScopeId() {

        DeviceQuery query = deviceFactory.newQuery(null);
        stepData.put("DeviceQuery", query);
    }

    @Given("^A null query$")
    public void createNullQuery() {

        stepData.put("DeviceQuery", null);
    }

    @Given("^A device named \"(.*)\"$")
    public void createNamedDevice(String name)
            throws Exception {

        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), name);
        stepData.put("DeviceCreator", deviceCreator);

        primeException();
        try {
            stepData.remove("Device");
            stepData.remove("DeviceId");
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put("Device", device);
            stepData.put("DeviceId", device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^A device with BIOS version \"(.*)\" named \"(.*)\"$")
    public void createNamedDeviceWithBiosVersion(String version, String name)
            throws Exception {

        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), name);
        deviceCreator.setBiosVersion(version);
        stepData.put("DeviceCreator", deviceCreator);

        primeException();
        try {
            stepData.remove("Device");
            stepData.remove("DeviceId");
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put("Device", device);
            stepData.put("DeviceId", device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) randomly named devices with BIOS version \"(.*)\"$")
    public void generateABunchOfTestDevices(int number, String version)
            throws Exception {

        DeviceCreator tmpDevCr;

        primeException();
        try {
            for (int i = 0; i < number; i++) {
                tmpDevCr = deviceFactory.newCreator(getCurrentScopeId(), "test_" + String.valueOf(random.nextInt()));
                tmpDevCr.setBiosVersion(version);
                deviceRegistryService.create(tmpDevCr);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) randomly named devices in scope (\\d+)$")
    public void generateABunchOfTestDevicesInScope(int number, int scope)
            throws Exception {

        DeviceCreator tmpDevCr;
        KapuaId tmpId;
        String tmpClient;

        primeException();
        try {
            for (int i = 0; i < number; i++) {
                tmpId = new KapuaEid(BigInteger.valueOf(scope));
                tmpClient = "test_" + String.valueOf(random.nextInt());
                tmpDevCr = deviceFactory.newCreator(tmpId, tmpClient);
                deviceRegistryService.create(tmpDevCr);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create a device from the existing creator$")
    public void createDeviceFromExistingCreator()
            throws Exception {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");

        primeException();
        try {
            stepData.remove("Device");
            stepData.remove("DeviceId");
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put("Device", device);
            stepData.put("DeviceId", device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a device with the remembered ID$")
    public void findDeviceWithRememberedId()
            throws Exception {

        KapuaId deviceId = (KapuaId) stepData.get("DeviceId");

        primeException();
        try {
            stepData.remove("Device");
            Device device = deviceRegistryService.find(getCurrentScopeId(), deviceId);
            stepData.put("Device", device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a device with the client ID \"(.+)\"$")
    public void findDeviceWithClientId(String clientId)
            throws Exception {

        primeException();
        try {
            stepData.remove("Device");
            Device device = deviceRegistryService.findByClientId(getCurrentScopeId(), clientId);
            stepData.put("Device", device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a device with a random ID$")
    public void findDeviceWithRandomId()
            throws Exception {

        primeException();
        try {
            stepData.remove("Device");
            Device device = deviceRegistryService.find(getCurrentScopeId(), getKapuaId());
            stepData.put("Device", device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a device with a random client ID$")
    public void findDeviceWithRandomClientId()
            throws Exception {

        primeException();
        try {
            stepData.remove("Device");
            Device device = deviceRegistryService.findByClientId(getCurrentScopeId(), String.valueOf(random.nextLong()));
            stepData.put("Device", device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for devices with BIOS version \"(.*)\"$")
    public void queryForDevicesBasedOnBiosVersion(String version)
            throws Exception {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("biosVersion", version));

        primeException();
        try {
            stepData.remove("DeviceList");
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for devices with BIOS different from \"(.*)\"$")
    public void queryForDevicesWithDifferentBiosVersion(String version)
            throws Exception {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsNotEqualTo("biosVersion", version));

        primeException();
        try {
            stepData.remove("DeviceList");
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for devices with Client Id \"(.*)\"$")
    public void queryForDevicesBasedOnClientId(String id)
            throws Exception {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("clientId", id));

        primeException();
        try {
            stepData.remove("DeviceList");
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I extract the first device$")
    public void getFirstDeviceFromList() {

        DeviceListResult deviceList = (DeviceListResult) stepData.get("DeviceList");

        // A device should have been found
        assertNotEquals(0, deviceList.getSize());
        stepData.put("Device", deviceList.getFirstItem());
    }

    @When("^I count the devices in scope (\\d+)$")
    public void countDevicesInScope(int scope)
            throws Exception {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getKapuaId(scope));

        primeException();
        try {
            stepData.remove("Count");
            Long count = deviceRegistryService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count devices with BIOS version \"(.*)\"$")
    public void countDevicesWithBIOSVersion(String version)
            throws Exception {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("biosVersion", version));

        primeException();
        try {
            stepData.remove("Count");
            Long count = deviceRegistryService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update some device parameters$")
    public void updateDeviceParameters()
            throws Exception {

        Device device = (Device) stepData.get("Device");
        device.setBiosVersion(device.getBiosVersion() + "_upd");
        device.setCustomAttribute1(device.getCustomAttribute1() + "_upd");

        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the device cleint ID to \"(.+)\"$")
    public void updateDeviceClientId(String newId)
            throws Exception {

        Device device = (Device) stepData.get("Device");
        stepData.put("Text", device.getClientId());
        device.setClientId(newId);

        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update a device with an invalid ID$")
    public void updateDeviceWithInvalidId()
            throws Exception {

        Device device = (Device) stepData.get("Device");
        device.setId(getKapuaId());

        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the device with the cleint id \"(.+)\"$")
    public void deleteDeviceWithClientId(String clientId)
            throws Exception {

        primeException();
        try {
            Device tmpDev = deviceRegistryService.findByClientId(getCurrentScopeId(), clientId);
            deviceRegistryService.delete(getCurrentScopeId(), tmpDev.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete a device with random IDs$")
    public void deleteDeviceWithRandomIds()
            throws Exception {

        primeException();
        try {
            deviceRegistryService.delete(getKapuaId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The device has a non-null ID$")
    public void checkCreatedDeviceId() {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");
        Device device = (Device) stepData.get("Device");

        assertNotNull(device.getId());
        assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        assertEquals(deviceCreator.getClientId(), device.getClientId());
    }

    @Then("^It is possible to find the device based on its registry ID$")
    public void findDeviceByRememberedId()
            throws Exception {

        Device device = (Device) stepData.get("Device");

        primeException();
        try {
            Device tmpDev = deviceRegistryService.find(getCurrentScopeId(), device.getId());
            assertEquals(device.getClientId(), tmpDev.getClientId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^It is possible to find the device based on its client ID$")
    public void findDeviceByRememberedClientId()
            throws Exception {

        Device device = (Device) stepData.get("Device");

        primeException();
        try {
            Device tmpDev = deviceRegistryService.findByClientId(getCurrentScopeId(), device.getClientId());
            assertEquals(device.getId(), tmpDev.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^Named device registry searches are case sensitive$")
    public void checkCaseSensitivnessOfRegistrySearches()
            throws Exception {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");

        primeException();
        try {
            assertNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId().toLowerCase()));
            assertNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId().toUpperCase()));
            assertNotNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The device matches the creator parameters$")
    public void checkCreatedDeviceAgainstCreatorParameters() {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");
        Device device = (Device) stepData.get("Device");

        assertNotNull(device.getId());
        assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        assertEquals(deviceCreator.getClientId().length(), device.getClientId().length());
        assertEquals(deviceCreator.getClientId(), device.getClientId());
        assertEquals(deviceCreator.getConnectionId(), device.getConnectionId());
        assertEquals(deviceCreator.getDisplayName(), device.getDisplayName());
        assertEquals(deviceCreator.getSerialNumber(), device.getSerialNumber());
        assertEquals(deviceCreator.getModelId(), device.getModelId());
        assertEquals(deviceCreator.getModelName(), device.getModelName());
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
        assertEquals(deviceCreator.getStatus(), device.getStatus());
    }

    @Then("^The device was correctly updated$")
    public void checkUpdatedDeviceAgainstOriginal()
            throws Exception {

        Device device = (Device) stepData.get("Device");
        Device tmpDevice;

        primeException();
        try {
            tmpDevice = deviceRegistryService.find(device.getScopeId(), device.getId());
            assertEquals(tmpDevice.getScopeId(), device.getScopeId());
            assertEquals(tmpDevice.getClientId().length(), device.getClientId().length());
            assertEquals(tmpDevice.getClientId(), device.getClientId());
            assertEquals(tmpDevice.getConnectionId(), device.getConnectionId());
            assertEquals(tmpDevice.getDisplayName(), device.getDisplayName());
            assertEquals(tmpDevice.getSerialNumber(), device.getSerialNumber());
            assertEquals(tmpDevice.getModelId(), device.getModelId());
            assertEquals(tmpDevice.getModelName(), device.getModelName());
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
            assertEquals(tmpDevice.getStatus(), device.getStatus());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The device client id is \"(.*)\"$")
    public void checkDeviceClientName(String name) {

        Device device = (Device) stepData.get("Device");
        assertEquals(name, device.getClientId());
    }

    @Then("^I find (\\d+) devices?$")
    public void checkListForNumberOfItems(int number) {

        DeviceListResult deviceList = (DeviceListResult) stepData.get("DeviceList");
        assertEquals(number, deviceList.getSize());
    }

    @Then("^The client ID was not changed$")
    public void checkDeviceClientIdForChanges()
            throws Exception {

        Device device = (Device) stepData.get("Device");
        String stringValue = (String) stepData.get("Text");

        primeException();
        try {
            Device tmpDevice = deviceRegistryService.find(getCurrentScopeId(), device.getId());
            assertNotEquals(device.getClientId(), tmpDevice.getClientId());
            assertEquals(stringValue, tmpDevice.getClientId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^There is no device with the client ID \"(.+)\"$")
    public void checkWhetherNamedDeviceStillExists(String clientId)
            throws KapuaException {
        Device tmpDevice = deviceRegistryService.findByClientId(getCurrentScopeId(), clientId);
        assertNull(tmpDevice);
    }

    @Then("^There is no such device$")
    public void noSuchDevice() {

        assertNull(stepData.get("Device"));
    }

    @Then("^I find the device$")
    public void deviceIsNotNull() {

        assertNotNull(stepData.get("Device"));
    }

    @Then("^All device factory functions must return non null values$")
    public void exerciseAllDeviceFactoryFunctions() {
        Device tmpDevice;
        DeviceCreator tmpCreator;
        DeviceQuery tmpQuery;
        DeviceListResult tmpListRes;

        tmpDevice = deviceFactory.newEntity(SYS_SCOPE_ID);
        tmpCreator = deviceFactory.newCreator(SYS_SCOPE_ID, "TestDevice");
        tmpQuery = deviceFactory.newQuery(SYS_SCOPE_ID);
        tmpListRes = deviceFactory.newListResult();

        assertNotNull(tmpDevice);
        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
        assertNotNull(tmpListRes);
    }

    @When("^I validate the device creator$")
    public void validateExistingDeviceCreator()
            throws Exception {

        DeviceCreator deviceCreator = (DeviceCreator) stepData.get("DeviceCreator");

        primeException();
        try {
            DeviceValidation.validateCreatePreconditions(deviceCreator);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I validate the device for updates$")
    public void validateExistingDeviceForUpdates()
            throws Exception {

        Device device = (Device) stepData.get("Device");

        primeException();
        try {
            DeviceValidation.validateUpdatePreconditions(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^Validating a find operation for scope (.+) and device (.+)$")
    public void validateDeviceSearch(String scopeId, String deviceId)
            throws Exception {

        KapuaId scope;
        KapuaId dev;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = getKapuaId(scopeId);
        }

        if (deviceId.trim().toLowerCase().equals("null")) {
            dev = null;
        } else {
            dev = getKapuaId(deviceId);
        }

        primeException();
        try {
            DeviceValidation.validateFindPreconditions(scope, dev);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^Validating a find operation for scope (.+) and client \"(.*)\"$")
    public void validateDeviceSearchByClientId(String scopeId, String clientId)
            throws Exception {

        KapuaId scope;
        String client;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = getKapuaId(scopeId);
        }

        if (clientId.trim().toLowerCase().equals("null")) {
            client = null;
        } else {
            client = clientId;
        }

        primeException();
        try {
            DeviceValidation.validateFindByClientIdPreconditions(scope, client);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^Validating a delete operation for scope (.+) and device (.+)$")
    public void validateDeviceDelete(String scopeId, String deviceId)
            throws Exception {

        KapuaId scope;
        KapuaId dev;

        if (scopeId.trim().toLowerCase().equals("null")) {
            scope = null;
        } else {
            scope = getKapuaId(scopeId);
        }

        if (deviceId.trim().toLowerCase().equals("null")) {
            dev = null;
        } else {
            dev = getKapuaId(deviceId);
        }

        primeException();
        try {
            DeviceValidation.validateDeletePreconditions(scope, dev);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I validate a query operation$")
    public void checkQueryOperation()
            throws Exception {

        DeviceQuery query = (DeviceQuery) stepData.get("DeviceQuery");

        primeException();
        try {
            DeviceValidation.validateQueryPreconditions(query);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I validate a count operation$")
    public void checkCountOperation()
            throws Exception {

        DeviceQuery query = (DeviceQuery) stepData.get("DeviceQuery");

        primeException();
        try {
            DeviceValidation.validateCountPreconditions(query);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a device creator object. The creator is pre-filled with default data.
    private DeviceCreator prepareRegularDeviceCreator(KapuaId accountId, String client) {

        DeviceCreator tmpDeviceCreator = deviceFactory.newCreator(accountId, client);

        tmpDeviceCreator.setConnectionId(getKapuaId());
        tmpDeviceCreator.setDisplayName(TEST_DEVICE_NAME);
        tmpDeviceCreator.setSerialNumber("serialNumber");
        tmpDeviceCreator.setModelId("modelId");
        tmpDeviceCreator.setImei(getRandomString());
        tmpDeviceCreator.setImsi(getRandomString());
        tmpDeviceCreator.setIccid(getRandomString());
        tmpDeviceCreator.setBiosVersion("biosVersion");
        tmpDeviceCreator.setFirmwareVersion("firmwareVersion");
        tmpDeviceCreator.setOsVersion("osVersion");
        tmpDeviceCreator.setJvmVersion("jvmVersion");
        tmpDeviceCreator.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDeviceCreator.setApplicationFrameworkVersion("kapuaVersion");
        tmpDeviceCreator.setApplicationIdentifiers("applicationIdentifiers");
        tmpDeviceCreator.setAcceptEncoding("acceptEncoding");
        tmpDeviceCreator.setCustomAttribute1("customAttribute1");
        tmpDeviceCreator.setCustomAttribute2("customAttribute2");
        tmpDeviceCreator.setCustomAttribute3("customAttribute3");
        tmpDeviceCreator.setCustomAttribute4("customAttribute4");
        tmpDeviceCreator.setCustomAttribute5("customAttribute5");
        tmpDeviceCreator.setStatus(DeviceStatus.ENABLED);

        return tmpDeviceCreator;
    }

    // Create a device object. The device is pre-filled with default data.
    private Device prepareRegularDevice(KapuaId accountId, KapuaId deviceId) {

        Device tmpDevice = deviceFactory.newEntity(accountId);

        tmpDevice.setId(deviceId);
        tmpDevice.setConnectionId(getKapuaId());
        tmpDevice.setDisplayName("test_name");
        tmpDevice.setSerialNumber("serialNumber");
        tmpDevice.setModelId("modelId");
        tmpDevice.setImei(getRandomString());
        tmpDevice.setImsi(getRandomString());
        tmpDevice.setIccid(getRandomString());
        tmpDevice.setBiosVersion("biosVersion");
        tmpDevice.setFirmwareVersion("firmwareVersion");
        tmpDevice.setOsVersion("osVersion");
        tmpDevice.setJvmVersion("jvmVersion");
        tmpDevice.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDevice.setApplicationFrameworkVersion("kapuaVersion");
        tmpDevice.setApplicationIdentifiers("applicationIdentifiers");
        tmpDevice.setAcceptEncoding("acceptEncoding");
        tmpDevice.setCustomAttribute1("customAttribute1");
        tmpDevice.setCustomAttribute2("customAttribute2");
        tmpDevice.setCustomAttribute3("customAttribute3");
        tmpDevice.setCustomAttribute4("customAttribute4");
        tmpDevice.setCustomAttribute5("customAttribute5");
        tmpDevice.setStatus(DeviceStatus.ENABLED);

        return tmpDevice;
    }
}
