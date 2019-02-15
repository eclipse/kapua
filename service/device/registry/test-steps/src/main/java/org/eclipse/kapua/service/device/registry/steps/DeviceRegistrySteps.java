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

import com.google.common.collect.Lists;
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
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaAppsPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaDisconnectPayloadImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaMissingPayloadImpl;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestDomain;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.qa.common.cucumber.CucConnection;
import org.eclipse.kapua.qa.common.cucumber.CucDevice;
import org.eclipse.kapua.qa.common.cucumber.CucUser;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.common.DeviceValidation;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagAttributes;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

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

    // Various device connection details
    private static final String CLIENT_NAME = "test_client";
    private static final String CLIENT_IP = "127.1.1.10";
    private static final String SERVER_IP = "127.1.1.100";

    // Various device registry service related references
    private DeviceRegistryService deviceRegistryService;
    private DeviceFactory deviceFactory;

    // Various device connection service related references
    private DeviceConnectionService deviceConnectionService;
    private DeviceConnectionFactory deviceConnectionFactory;

    // Various device event service related references
    private DeviceEventService eventService;
    private DeviceEventFactory eventFactory;


    // Additional service references for integration testing
    private DeviceLifeCycleService deviceLifeCycleService;
    private AuthenticationService authenticationService;
    private DomainFactory domainFactory;
    private AccountService accountService;
    private AccountFactory accountFactory;
    private UserService userService;
    private UserFactory userFactory;
    private CredentialService credentialService;
    private AccessInfoService accessInfoService;
    private TagService tagService;
    private TagFactory tagFactory;
    private KapuaMessageFactory messageFactory;

    private AclCreator aclCreator;

    // Default constructor
    @Inject
    public DeviceRegistrySteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
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

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceFactory = locator.getFactory(DeviceFactory.class);

        deviceConnectionService = locator.getService(DeviceConnectionService.class);
        deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        eventService = locator.getService(DeviceEventService.class);
        eventFactory = locator.getFactory(DeviceEventFactory.class);

        messageFactory = locator.getFactory(KapuaMessageFactory.class);

        deviceLifeCycleService = locator.getService(DeviceLifeCycleService.class);
        authenticationService = locator.getService(AuthenticationService.class);
        domainFactory = locator.getFactory(DomainFactory.class);
        accountService = locator.getService(AccountService.class);
        accountFactory = locator.getFactory(AccountFactory.class);
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);
        tagService = locator.getService(TagService.class);
        tagFactory = locator.getFactory(TagFactory.class);

        aclCreator = new AclCreator();

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
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

    // ************************************************************************************
    // * Device Registry steps                                                            *
    // ************************************************************************************

    @When("^I configure the device registry service$")
    public void setDeviceRegistryConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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

    @Given("^(?:A d|D)evices? such as$")
    public void createADevicesAsSpecified(List<CucDevice> devLst)
            throws Exception {

        primeException();
        try {
            stepData.remove("LastDevice");
            Device tmpDevice = null;
            for (CucDevice tmpCDev : devLst) {
                tmpCDev.parse();
                DeviceCreator devCr = prepareDeviceCreatorFromCucDevice(tmpCDev);
                tmpDevice = deviceRegistryService.create(devCr);
            }
            stepData.put("LastDevice", tmpDevice);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^The device \"(.*)\"$")
    public void createDeviceWithName(String clientId) throws KapuaException {

        Account tmpAcc = (Account) stepData.get("LastAccount");
        DeviceCreator tmpDevCr = deviceFactory.newCreator(tmpAcc.getId(), clientId);
        Device tmpDev = deviceRegistryService.create(tmpDevCr);
        stepData.put("LastDevice", tmpDev);
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

    @Then("^I find device \"([^\"]*)\"$")
    public void iFindDeviceWithTag(String deviceName) {

        DeviceListResult deviceList = (DeviceListResult) stepData.get("DeviceList");
        Device device = deviceList.getFirstItem();

        Assert.assertNotNull(device);
        Assert.assertEquals(deviceName, device.getClientId());
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

    // ************************************************************************************
    // * Device Connection steps                                                          *
    // ************************************************************************************

    @When("^I configure the device connection service$")
    public void setDeviceConnectionConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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
            deviceConnectionService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular connection creator$")
    public void createRegularCreator() {

        DeviceConnectionCreator connectionCreator = prepareRegularConnectionCreator(SYS_SCOPE_ID, getKapuaId());
        stepData.put("DeviceConnectionCreator", connectionCreator);
    }

    @Given("^A connection for scope (d+)$")
    public void createConnectionInScope(int scope)
            throws Exception {

        DeviceConnectionCreator tmpCreator = prepareRegularConnectionCreator(getKapuaId(scope), getKapuaId());

        primeException();
        try {
            stepData.remove("DeviceConnection");
            DeviceConnection connection = deviceConnectionService.create(tmpCreator);
            stepData.put("DeviceConnection", connection);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^I have the following connection(?:|s)$")
    public void createConnections(List<CucConnection> connections)
            throws Exception {

        KapuaId scopeId = getCurrentScopeId();
        KapuaId userId = getCurrentUserId();

        primeException();
        try {
            DeviceConnectionCreator connectionCreator = null;
            DeviceConnection connection = null;
            stepData.remove("DeviceConnection");
            stepData.remove("DeviceConnectionId");
            stepData.remove("DeviceConnectionList");

            for (CucConnection connItem : connections) {
                connectionCreator = deviceConnectionFactory.newCreator(scopeId);
                connectionCreator.setStatus(DeviceConnectionStatus.CONNECTED);
                connectionCreator.setUserId(userId);
                connectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.LOOSE);
                connectionCreator.setClientId(connItem.getClientId());
                connectionCreator.setClientIp(connItem.getClientIp());
                connectionCreator.setServerIp(connItem.getServerIp());
                connectionCreator.setProtocol(connItem.getProtocol());
                connectionCreator.setAllowUserChange(false);
                connection = deviceConnectionService.create(connectionCreator);
            }

            stepData.put("DeviceConnectionCreator", connectionCreator);
            stepData.put("DeviceConnection", connection);
            stepData.put("DeviceConnectionId", connection.getId());
            DeviceConnectionListResult connList = deviceConnectionFactory.newListResult();
            Vector<DeviceConnection> vec = new Vector<>();
            vec.add(connection);
            connList.addItems(vec);
            stepData.put("DeviceConnectionList", connList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I modify the connection details to$")
    public void updateConnectionDetails(List<CucConnection> connections)
            throws Exception {

        // Only a single connection must be specified for this test!
        assertNotNull(connections);
        assertEquals(1, connections.size());

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get("DeviceConnectionCreator");

        primeException();
        try {
            stepData.remove("DeviceConnection");
            stepData.remove("DeviceConnectionCreator");

            // try to modify the existing connection
            // Slight workaround for cucumber limitations: Remember the desired
            // connection settings via the global connectionCreator variable
            if (connections.get(0).getClientId() != null) {
                connection.setClientId(connections.get(0).getClientId());
                connectionCreator.setClientId(connections.get(0).getClientId());
            }
            if (connections.get(0).getClientIp() != null) {
                connection.setClientIp(connections.get(0).getClientIp());
                connectionCreator.setClientIp(connections.get(0).getClientIp());
            }
            if (connections.get(0).getServerIp() != null) {
                connection.setServerIp(connections.get(0).getServerIp());
                connectionCreator.setServerIp(connections.get(0).getServerIp());
            }
            if (connections.get(0).getProtocol() != null) {
                connection.setProtocol(connections.get(0).getProtocol());
                connectionCreator.setProtocol(connections.get(0).getProtocol());
            }
            DeviceConnection newConnection = deviceConnectionService.update(connection);

            stepData.put("DeviceConnection", newConnection);
            stepData.put("DeviceConnectionCreator", connectionCreator);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to modify the connection client Id to \"(.+)\"$")
    public void changeConnectionClientId(String client)
            throws Exception {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        // Remember the old client ID for later checking
        stepData.put("Text", connection.getClientId());
        // Update the connection client ID
        connection.setClientId(client);

        primeException();
        try {
            DeviceConnection newConnection = deviceConnectionService.update(connection);
            stepData.put("DeviceConnection", newConnection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to modify the connection Id$")
    public void changeConnectionIdRandomly()
            throws Exception {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        // Try to update the connection ID
        connection.setId(getKapuaId());

        primeException();
        try {
            deviceConnectionService.update(connection);
        } catch (KapuaException ex) {
            // Since the ID is not updatable there should be an exception
            verifyException(ex);
        }
    }

    @When("^I create a new connection from the existing creator$")
    public void createConnectionFromExistingCreator()
            throws Exception {

        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get("DeviceConnectionCreator");

        primeException();
        try {
            stepData.remove("DeviceConnection");
            DeviceConnection connection = deviceConnectionService.create(connectionCreator);
            stepData.put("DeviceConnection", connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The connection object is regular$")
    public void checkConnectionObject() {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        assertNotNull(connection);
        assertNotNull(connection.getId());
    }

    @Then("^The connection object matches the creator$")
    public void checkConnectionObjectAgainstCreator() {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get("DeviceConnectionCreator");

        assertNotNull(connection);
        assertNotNull(connectionCreator);
        assertEquals(connectionCreator.getScopeId(), connection.getScopeId());
        assertEquals(connectionCreator.getClientId(), connection.getClientId());
        assertEquals(connectionCreator.getUserId(), connection.getUserId());
        assertEquals(connectionCreator.getUserCouplingMode(), connection.getUserCouplingMode());
        assertEquals(connectionCreator.getReservedUserId(), connection.getReservedUserId());
        assertEquals(connectionCreator.getAllowUserChange(), connection.getAllowUserChange());
        assertEquals(connectionCreator.getClientIp(), connection.getClientIp());
        assertEquals(connectionCreator.getServerIp(), connection.getServerIp());
        assertEquals(connectionCreator.getProtocol(), connection.getProtocol());
    }

    @Then("^The connection status is \"(.+)\"$")
    public void checkDeviceConnectionStatus(String status) {

        DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);
        DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");

        Assert.assertNotNull(tmpConnLst);
        Assert.assertNotEquals(0, tmpConnLst.getSize());

        DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
        Assert.assertEquals(tmpStat, tmpConnection.getStatus());
    }

    @Then("^I count (\\d+) connections in scope (-?\\d+)$")
    public void countConnectioncInScope(int target, int scope)
            throws Exception {

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(getKapuaId(scope));

        primeException();
        try {
            long tmpCount = deviceConnectionService.count(query);
            assertEquals(target, tmpCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a connection by scope and connection IDs$")
    public void findConnectionByScopeAndConnectionId()
            throws Exception {

        KapuaId scopeId = getCurrentScopeId();
        KapuaId connectionId = (KapuaId) stepData.get("DeviceConnectionId");

        primeException();
        try {
            stepData.remove("DeviceConnection");
            DeviceConnection connection = deviceConnectionService.find(scopeId, connectionId);
            stepData.put("DeviceConnection", connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a random connection ID$")
    public void searchForARandomConnectionId()
            throws Exception {

        primeException();
        try {
            stepData.remove("DeviceConnection");
            DeviceConnection connection = deviceConnectionService.find(getCurrentScopeId(), getKapuaId());
            stepData.put("DeviceConnection", connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for a connection with the client ID \"(.+)\"$")
    public void findConnectionByClientId(String client)
            throws Exception {

        primeException();
        try {
            stepData.remove("DeviceConnection");
            DeviceConnection connection = deviceConnectionService.findByClientId(getCurrentScopeId(), client);
            stepData.put("DeviceConnection", connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the existing connection$")
    public void deleteExistingConnection()
            throws Exception {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");

        primeException();
        try {
            deviceConnectionService.delete(connection.getScopeId(), connection.getId());
            stepData.remove("DeviceConnection");
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I try to delete a random connection ID$")
    public void deleteRandomConnection()
            throws Exception {

        primeException();
        try {
            deviceConnectionService.delete(getCurrentScopeId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for all connections with the parameter \"(.+)\" set to \"(.+)\"$")
    public void cueryForConnections(String parameter, String value)
            throws Exception {

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(getCurrentScopeId());
        query.setPredicate(AttributePredicateImpl.attributeIsEqualTo(parameter, value));

        primeException();
        try {
            stepData.remove("DeviceConnectionList");
            DeviceConnectionListResult connectionList = deviceConnectionService.query(query);
            assertNotNull(connectionList);
            stepData.put("DeviceConnectionList", connectionList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I find (\\d+) connections?$")
    public void checkResultListLength(int num) {

        DeviceConnectionListResult connectionList = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");
        assertNotNull(connectionList);
        assertEquals(num, connectionList.getSize());
    }

    @Then("^The connection details match$")
    public void checkConnectionDetails(List<CucConnection> connections) {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        // Only a single connection must be specified for this test!
        assertNotNull(connections);
        assertEquals(1, connections.size());
        // Slight workaround for cucumber limitations: The connection settings are
        // remembered via the global connection variable
        if (connections.get(0).getClientId() != null) {
            assertEquals(connections.get(0).getClientId(), connection.getClientId());
        }
        if (connections.get(0).getClientIp() != null) {
            assertEquals(connections.get(0).getClientIp(), connection.getClientIp());
        }
        if (connections.get(0).getServerIp() != null) {
            assertEquals(connections.get(0).getServerIp(), connection.getServerIp());
        }
        if (connections.get(0).getProtocol() != null) {
            assertEquals(connections.get(0).getProtocol(), connection.getProtocol());
        }
    }

    @Then("^The connection client ID remains unchanged$")
    public void checkThatClientIdHasNotChanged() {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");
        String text = (String) stepData.get("Text");

        assertEquals(text, connection.getClientId());
    }

    @Then("^No connection was found$")
    public void checkThatConnectionIsNull() {

        assertNull(stepData.get("DeviceConnection"));
    }

    @Then("^All connection factory functions must return non null values$")
    public void exerciseAllConnectionFactoryFunctions() {
        DeviceConnectionCreator tmpCreator = null;
        DeviceConnectionQuery tmpQuery = null;

        tmpCreator = deviceConnectionFactory.newCreator(SYS_SCOPE_ID);
        tmpQuery = deviceConnectionFactory.newQuery(SYS_SCOPE_ID);

        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
    }

    @Then("^The device connection domain defaults are correctly initialized$")
    public void checkConnectionDomainInitialization() {
        DeviceConnectionDomain tmpDomain = new DeviceConnectionDomain();

        assertEquals("device_connection", tmpDomain.getName());
        assertEquals(3, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.read));
        assertTrue(tmpDomain.getActions().contains(Actions.write));
        assertTrue(tmpDomain.getActions().contains(Actions.delete));
    }

    @Then("^The device connection domain data can be updated$")
    public void checkDeviceConnectionDomainUpdate() {

        Domain tmpDomain = new TestDomain();
        tmpDomain.setName("test_name");
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));

        assertEquals("test_name", tmpDomain.getName());
        assertEquals(2, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.connect));
        assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // ************************************************************************************
    // * Device Event steps                                                               *
    // ************************************************************************************

    @Given("^An event creator with null action$")
    public void prepareCreatorWithNullAction() {

        DeviceEventCreator eventCreator = prepareRegularDeviceEventCreator(getCurrentScopeId(), getKapuaId());
        eventCreator.setAction(null);

        stepData.put("DeviceEventCreator", eventCreator);
    }

    @Given("^A \"(.+)\" event from device \"(.+)\"$")
    public void createRegularEvent(String eventType, String clientId)
            throws Exception {

        primeException();
        try {
            Device tmpDev = getDeviceWithClientId(clientId);
            DeviceEventCreator eventCreator = prepareRegularDeviceEventCreator(getCurrentScopeId(), tmpDev.getId());
            KapuaMethod tmpMeth = getMethodFromString(eventType);
            eventCreator.setAction(tmpMeth);
            stepData.put("DeviceEventCreator", eventCreator);

            stepData.remove("DeviceEvent");
            stepData.remove("DeviceEventId");
            DeviceEvent event = eventService.create(eventCreator);
            stepData.put("DeviceEvent", event);
            stepData.put("DeviceEventId", event.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I have (\\d+) \"(.+)\" events? from device \"(.+)\"$")
    public void createANumberOfEvents(int num, String eventType, String clientId)
            throws Exception {

        primeException();
        try {
            KapuaId currScopeId = getCurrentScopeId();
            KapuaId tmpDevId = getDeviceWithClientId(clientId).getId();
            KapuaMethod tmpMeth = getMethodFromString(eventType);
            DeviceEventCreator tmpCreator;
            DeviceEvent tmpEvent;

            for (int i = 0; i < num; i++) {
                tmpCreator = prepareRegularDeviceEventCreator(currScopeId, tmpDevId);
                assertNotNull(tmpCreator);
                tmpCreator.setAction(tmpMeth);
                tmpEvent = eventService.create(tmpCreator);
                assertNotNull(tmpEvent);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create an event from the existing creator$")
    public void createEventFromCreator()
            throws Exception {

        DeviceEventCreator eventCreator = (DeviceEventCreator) stepData.get("DeviceEventCreator");

        primeException();
        try {
            stepData.remove("DeviceEvent");
            stepData.remove("DeviceEventId");
            DeviceEvent event = eventService.create(eventCreator);
            stepData.put("DeviceEvent", event);
            stepData.put("DeviceEventId", event.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for an event with the remembered ID$")
    public void findEventById()
            throws Exception {

        KapuaId eventId = (KapuaId) stepData.get("DeviceEventId");

        primeException();
        try {
            stepData.remove("DeviceEvent");
            DeviceEvent event = eventService.find(getCurrentScopeId(), eventId);
            stepData.put("DeviceEvent", event);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for an event with a random ID$")
    public void findEventByRandomId()
            throws Exception {

        primeException();
        try {
            stepData.remove("DeviceEvent");
            DeviceEvent event = eventService.find(getCurrentScopeId(), getKapuaId());
            stepData.put("DeviceEvent", event);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the event with the remembered ID$")
    public void deleteEvent()
            throws Exception {

        KapuaId eventId = (KapuaId) stepData.get("DeviceEventId");

        primeException();
        try {
            eventService.delete(getCurrentScopeId(), eventId);
            stepData.remove("DeviceEvent");
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete an event with a random ID$")
    public void deleteEventWithRandomId()
            throws Exception {

        primeException();
        try {
            eventService.delete(getCurrentScopeId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count events for scope (\\d+)$")
    public void countEventsInScope(int scpId)
            throws Exception {

        DeviceEventQuery tmpQuery = eventFactory.newQuery(getKapuaId(scpId));

        primeException();
        try {
            stepData.remove("Count");
            Long count = eventService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for \"(.+)\" events$")
    public void queryForSpecificEvents(String eventType)
            throws Exception {

        DeviceEventQuery tmpQuery = eventFactory.newQuery(getCurrentScopeId());
        assertNotNull(tmpQuery);
        KapuaMethod tmpMeth = getMethodFromString(eventType);
        assertNotNull(tmpMeth);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("action", tmpMeth));

        primeException();
        try {
            stepData.remove("DeviceEventList");
            DeviceEventListResult eventList = eventService.query(tmpQuery);
            stepData.put("DeviceEventList", eventList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I untag device with \"([^\"]*)\" tag$")
    public void iDeleteTag(String deviceTagName) throws Throwable {

        Tag foundTag = (Tag) stepData.get("tag");
        Assert.assertEquals(deviceTagName, foundTag.getName());
        Device device = (Device) stepData.get("Device");
        stepData.remove("tag");
        stepData.remove("tags");
        Set<KapuaId> tags = new HashSet<>();
        device.setTagIds(tags);
        Device updatedDevice = deviceRegistryService.update(device);
        stepData.put("Device", updatedDevice);
        Assert.assertEquals(device.getTagIds().isEmpty(), true);
    }

    @And("^I verify that tag \"([^\"]*)\" is deleted$")
    public void iVerifyTagIsDeleted(String deviceTagName) throws Throwable {

        Tag foundTag = (Tag) stepData.get("tag");
        Assert.assertEquals(null, foundTag);
    }

    @When("^I search for events from device \"(.+)\" in account \"(.+)\"$")
    public void searchForEventsFromDeviceWithClientID(String clientId, String account)
            throws KapuaException {

        DeviceEventQuery tmpQuery;
        Device tmpDev;
        DeviceEventListResult tmpList;
        Account tmpAcc;

        tmpAcc = accountService.findByName(account);
        Assert.assertNotNull(tmpAcc);
        Assert.assertNotNull(tmpAcc.getId());

        tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
        Assert.assertNotNull(tmpDev);
        Assert.assertNotNull(tmpDev.getId());

        tmpQuery = eventFactory.newQuery(tmpAcc.getId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("deviceId", tmpDev.getId()));
        tmpQuery.setSortCriteria(new FieldSortCriteria("receivedOn", FieldSortCriteria.SortOrder.ASCENDING));
        tmpList = eventService.query(tmpQuery);

        Assert.assertNotNull(tmpList);
        stepData.put("DeviceEventList", tmpList);
    }

    @Then("^The event matches the creator parameters$")
    public void checkCreatedEventAgainstCreatorParameters() {

        DeviceEventCreator eventCreator = (DeviceEventCreator) stepData.get("DeviceEventCreator");
        DeviceEvent event = (DeviceEvent) stepData.get("DeviceEvent");

        assertNotNull(event.getId());
        assertEquals(eventCreator.getScopeId(), event.getScopeId());
        assertEquals(eventCreator.getDeviceId(), event.getDeviceId());
        assertEquals(eventCreator.getSentOn(), event.getSentOn());
        assertEquals(eventCreator.getReceivedOn(), event.getReceivedOn());
        assertEquals(eventCreator.getResource(), event.getResource());
        assertEquals(eventCreator.getResponseCode(), event.getResponseCode());
        assertEquals(eventCreator.getEventMessage(), event.getEventMessage());
        assertEquals(eventCreator.getAction(), event.getAction());
        assertEquals(eventCreator.getPosition().toDisplayString(),
                event.getPosition().toDisplayString());
    }

    @Then("^The type of the last event is \"(.+)\"$")
    public void checkLastEventType(String type) {

        DeviceEventListResult tmpList;

        Assert.assertNotNull(stepData.get("DeviceEventList"));
        Assert.assertNotEquals(0, ((DeviceEventListResult) stepData.get("DeviceEventList")).getSize());
        tmpList = (DeviceEventListResult) stepData.get("DeviceEventList");
        Assert.assertEquals(type.trim().toUpperCase(), tmpList.getItem(tmpList.getSize() - 1).getResource().trim().toUpperCase());
    }

    @Then("^I find (\\d+) device events?$")
    public void checkEventListForNumberOfItems(int number) {

        DeviceEventListResult eventList = (DeviceEventListResult) stepData.get("DeviceEventList");
        assertEquals(number, eventList.getSize());
    }

    @Then("^There is no such event$")
    public void eventIsNull() {

        assertNull(stepData.get("DeviceEvent"));
    }

    @Then("^All device event factory functions must return non null objects$")
    public void exerciseAllEventFactoryFunctions() {
        DeviceEvent tmpEvent = null;
        DeviceEventCreator tmpCreator = null;
        DeviceEventQuery tmpQuery = null;
        DeviceEventListResult tmpList = null;

        tmpEvent = eventFactory.newEntity(SYS_SCOPE_ID);
        tmpCreator = eventFactory.newCreator(SYS_SCOPE_ID, getKapuaId(), new Date(), "");
        tmpQuery = eventFactory.newQuery(SYS_SCOPE_ID);
        tmpList = eventFactory.newListResult();

        assertNotNull(tmpEvent);
        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
        assertNotNull(tmpList);
    }

    @Then("^The device event domain data can be updated$")
    public void checkDeviceEventDomainUpdate() {
        Domain tmpDomain = new TestDomain();

        tmpDomain.setName("test_name");
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));

        assertEquals("test_name", tmpDomain.getName());
        assertEquals(2, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.connect));
        assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // ************************************************************************************
    // * Device integration steps                                                         *
    // ************************************************************************************

    @When("^I search for the device \"(.+)\" in account \"(.+)\"$")
    public void searchForDeviceWithClientID(String clientId, String account)
            throws Exception {

        DeviceListResult tmpList = deviceFactory.newListResult();

        primeException();
        try {
            stepData.remove("Device");
            stepData.remove("DeviceList");
            Account tmpAcc = accountService.findByName(account);
            Device tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
            if (tmpDev != null) {
                Vector<Device> dv = new Vector<>();
                dv.add(tmpDev);
                tmpList.addItems(dv);
            }
            stepData.put("Device", tmpDev);
            stepData.put("DeviceList", tmpList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I tag device with \"([^\"]*)\" tag$")
    public void iTagDeviceWithTag(String deviceTagName)
            throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        Device device = (Device) stepData.get("Device");

        primeException();
        try {
            TagCreator tagCreator = tagFactory.newCreator(account.getId());
            tagCreator.setName(deviceTagName);
            Tag tag = tagService.create(tagCreator);
            Set<KapuaId> tags = new HashSet<>();
            tags.add(tag.getId());
            device.setTagIds(tags);
            Device updatedDevice = deviceRegistryService.update(device);
            stepData.put("tag", tag);
            stepData.put("tags", tags);
            stepData.put("Device", updatedDevice);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for device with tag \"([^\"]*)\"$")
    public void iSearchForDeviceWithTag(String deviceTagName)
            throws Exception {

        Account lastAcc = (Account) stepData.get("LastAccount");
        DeviceQuery deviceQuery = deviceFactory.newQuery(lastAcc.getId());
        TagQuery tagQuery = tagFactory.newQuery(lastAcc.getId());
        tagQuery.setPredicate(new AttributePredicateImpl<>(TagAttributes.NAME, deviceTagName, AttributePredicate.Operator.EQUAL));

        primeException();
        try {
            stepData.remove("DeviceList");
            TagListResult tagQueryResult = tagService.query(tagQuery);
            Tag tag = tagQueryResult.getFirstItem();
            deviceQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo(DeviceAttributes.TAG_IDS, tag.getId()));
            DeviceListResult deviceList = deviceRegistryService.query(deviceQuery);
            stepData.put("DeviceList", deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^A birth message from device \"(.+)\"$")
    public void createABirthMessage(String clientId)
            throws KapuaException {

        Account tmpAccount = (Account) stepData.get("LastAccount");

        Assert.assertNotNull(clientId);
        Assert.assertFalse(clientId.isEmpty());
        Assert.assertNotNull(tmpAccount);
        Assert.assertNotNull(tmpAccount.getId());

        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaBirthMessage tmpMsg = new KapuaBirthMessageImpl();
        KapuaBirthChannel tmpChan = new KapuaBirthChannelImpl();
        KapuaBirthPayload tmpPayload = prepareDefaultBirthPayload();

        tmpChan.setClientId(clientId);
        tmpSemParts.add("part1");
        tmpSemParts.add("part2");
        tmpChan.setSemanticParts(tmpSemParts);

        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        deviceLifeCycleService.birth(getKapuaId(), tmpMsg);
    }

    @Given("^A disconnect message from device \"(.+)\"$")
    public void createADeathMessage(String clientId)
            throws Exception {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaDisconnectMessage tmpMsg = new KapuaDisconnectMessageImpl();
        KapuaDisconnectChannel tmpChan = new KapuaDisconnectChannelImpl();
        KapuaDisconnectPayload tmpPayload = prepareDefaultDeathPayload();

        tmpChan.setClientId(clientId);
        tmpSemParts.add("part1");
        tmpSemParts.add("part2");
        tmpChan.setSemanticParts(tmpSemParts);

        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            primeException();
            deviceLifeCycleService.death(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^A missing message from device \"(.+)\"$")
    public void createAMissingMessage(String clientId)
            throws Exception {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaMissingMessage tmpMsg = new KapuaMissingMessageImpl();
        KapuaMissingChannel tmpChan = new KapuaMissingChannelImpl();
        KapuaMissingPayload tmpPayload = prepareDefaultMissingPayload();

        tmpChan.setClientId(clientId);
        tmpSemParts.add("part1");
        tmpSemParts.add("part2");
        tmpChan.setSemanticParts(tmpSemParts);

        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            primeException();
            deviceLifeCycleService.missing(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^An application message from device \"(.+)\"$")
    public void createAnApplicationMessage(String clientId)
            throws Exception {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaAppsMessage tmpMsg = new KapuaAppsMessageImpl();
        KapuaAppsChannel tmpChan = new KapuaAppsChannelImpl();
        KapuaAppsPayload tmpPayload = prepareDefaultApplicationPayload();

        tmpChan.setClientId(clientId);
        tmpSemParts.add("part1");
        tmpSemParts.add("part2");
        tmpChan.setSemanticParts(tmpSemParts);

        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            primeException();
            deviceLifeCycleService.applications(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }


    @Given("^Such a set of privileged users for account \"(.+)\"$")
    public void createPrivilegedUsers(String accName, List<CucUser> users) throws Throwable {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(accName);

            for (CucUser tmpTestUsr : users) {
                User tmpUser = aclCreator.createUser(account, tmpTestUsr.getName());
                if ((tmpTestUsr.getPassword() != null) && !tmpTestUsr.getPassword().isEmpty()) {
                    aclCreator.attachUserCredentials(account, tmpUser, tmpTestUsr.getPassword());
                } else {
                    aclCreator.attachUserCredentials(account, tmpUser);
                }
                aclCreator.attachFullPermissions(account, tmpUser);
            }
        });
    }

    @Given("^A full set of device privileges for account \"(.+)\"$")
    public void setAccountDevicePrivileges(String name) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(name);

            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("infiniteChildEntities", true);
            valueMap.put("maxNumberChildEntities", 1000);

            deviceRegistryService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        });
    }

    @Given("^The default connection coupling mode for account \"(.+)\" is set to \"(.+)\"$")
    public void setDeviceConnectionCouplingMode(String name, String mode) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(name);

            Map<String, Object> valueMap = new HashMap<>();
            //            valueMap.put("infiniteChildEntities", true);
            //            valueMap.put("maxNumberChildEntities", 1000);
            valueMap.put("deviceConnectionUserCouplingDefaultMode", mode);

            deviceConnectionService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        });
    }

    @Given("^The following device connections?$")
    public void createConnectionForDevice(List<CucConnection> connections) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            for (CucConnection tmpConn : connections) {
                DeviceConnectionCreator tmpCreator = deviceConnectionFactory.newCreator(tmpConn.getScopeId());
                tmpCreator.setStatus(DeviceConnectionStatus.CONNECTED);
                tmpCreator.setClientId(tmpConn.getClientId());
                tmpCreator.setUserId(tmpConn.getUserId());
                tmpCreator.setReservedUserId(tmpConn.getReservedUserId());
                tmpCreator.setAllowUserChange(tmpConn.getAllowUserChange());
                tmpCreator.setUserCouplingMode(tmpConn.getUserCouplingMode());

                DeviceConnection tmpDevConn = deviceConnectionService.create(tmpCreator);

                tmpDevConn.setStatus(DeviceConnectionStatus.DISCONNECTED);
                deviceConnectionService.update(tmpDevConn);
            }
        });
    }

    @Given("^I wait for (\\d+) seconds?$")
    public void waitForSpecifiedTime(int delay) throws InterruptedException {

        Thread.sleep(delay * 1000);
    }

    @When("^I search for a connection from the device \"(.+)\" in account \"(.+)\"$")
    public void searchForConnectionFromDeviceWithClientID(String clientId, String account)
            throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc;
            DeviceConnection tmpConn;
            DeviceConnectionListResult tmpConnLst = deviceConnectionFactory.newListResult();

            tmpAcc = accountService.findByName(account);
            Assert.assertNotNull(tmpAcc);
            Assert.assertNotNull(tmpAcc.getId());

            tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), clientId);
            Map<String, Object> props = deviceRegistryService.getConfigValues(tmpAcc.getId());
            stepData.put("DeviceConnection", tmpConn);
            if (tmpConn != null) {
                Vector<DeviceConnection> dcv = new Vector<>();
                dcv.add(tmpConn);
                tmpConnLst.addItems(dcv);
            }
            stepData.put("DeviceConnectionList", tmpConnLst);
        });
    }

    @Then("^The connection user is \"(.+)\"$")
    public void checkDeviceConnectionUser(String user) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");
            User tmpUsr = userService.findByName(user);

            Assert.assertNotNull(tmpConnLst);
            Assert.assertNotEquals(0, tmpConnLst.getSize());

            DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
            Assert.assertEquals(tmpUsr.getId(), tmpConnection.getUserId());
        });
    }

    @When("^I set the connection status from the device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionStatus(String device, String account, String status) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);

            Assert.assertNotNull(tmpStat);
            Assert.assertNotNull(tmpConn);

            tmpConn.setStatus(tmpStat);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("^I set the user coupling mode for the connection from device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionCouplingMode(String device, String account, String mode) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            ConnectionUserCouplingMode tmpMode = parseConnectionCouplingString(mode);
            Assert.assertNotNull(tmpMode);

            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);

            Assert.assertNotNull(tmpConn);

            tmpConn.setUserCouplingMode(tmpMode);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("^I set the user change flag for the connection from device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionUserChangeFlag(String device, String account, String flag) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);

            Assert.assertNotNull(tmpConn);

            tmpConn.setAllowUserChange(parseBooleanFromString(flag));
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("^I set the reserved user for the connection from device \"(.+)\" in account \"(.+)\" to \"(.*)\"$")
    public void modifyDeviceConnectionReservedUser(String device, String scope, String resUser) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(scope);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            KapuaId userId;

            Assert.assertNotNull(tmpConn);

            if (resUser.isEmpty() || resUser.trim().toLowerCase().contains("null")) {
                userId = null;
            } else {
                userId = userService.findByName(resUser).getId();
            }

            tmpConn.setReservedUserId(userId);
            stepData.put("ExceptionCaught", false);
            try {
                primeException();
                deviceConnectionService.update(tmpConn);
            } catch (KapuaException ex) {
                verifyException(ex);
            } catch (Exception e) {
                int a = 10;
            }
        });
    }

    @Then("^The user for the connection from device \"(.+)\" in scope \"(.+)\" is \"(.+)\"$")
    public void checkUserForExistingConnection(String device, String scope, String name) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(scope);
            DeviceConnection connection = deviceConnectionService.findByClientId(account.getId(), device);
            User user = userService.findByName(name);

            Assert.assertNotNull(connection);
            Assert.assertNotNull(user);
            Assert.assertEquals(user.getId(), connection.getUserId());
        });
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

    // Create a connection creator object. The creator is pre-filled with default data.
    private DeviceConnectionCreator prepareRegularConnectionCreator(KapuaId scopeId, KapuaId userId) {

        DeviceConnectionCreator creator = deviceConnectionFactory.newCreator(scopeId);

        creator.setUserId(userId);
        creator.setUserCouplingMode(ConnectionUserCouplingMode.LOOSE);
        creator.setReservedUserId(userId);
        creator.setClientId(CLIENT_NAME);
        creator.setClientIp(CLIENT_IP);
        creator.setServerIp(SERVER_IP);
        creator.setProtocol("tcp");
        creator.setAllowUserChange(false);

        return creator;
    }

    // Create a event creator object. The creator is pre-filled with default data.
    private DeviceEventCreator prepareRegularDeviceEventCreator(KapuaId accountId, KapuaId deviceId) {

        DeviceEventCreator tmpCreator = eventFactory.newCreator(accountId);
        KapuaPosition tmpPosition = messageFactory.newPosition();
        Date timeReceived = new Date();
        Date timeSent = new Date(System.currentTimeMillis() - 5 * 60 * 1000);

        tmpCreator.setDeviceId(deviceId);
        tmpCreator.setSentOn(timeSent);
        tmpCreator.setReceivedOn(timeReceived);
        tmpCreator.setAction(KapuaMethod.CREATE);
        tmpCreator.setResource("resource");
        tmpCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        tmpCreator.setEventMessage("test_message_hello_world");

        tmpPosition.setLatitude(46.4);
        tmpPosition.setLongitude(13.02);
        tmpPosition.setAltitude(323.0);
        tmpPosition.setSpeed(50.0);
        tmpPosition.setHeading(0.0);
        tmpPosition.setPrecision(0.15);
        tmpPosition.setSatellites(16);
        tmpPosition.setStatus(7);
        tmpPosition.setTimestamp(timeSent);

        tmpCreator.setPosition(tmpPosition);

        return tmpCreator;
    }

    private KapuaMethod getMethodFromString(String name) {
        KapuaMethod tmpMeth = null;

        switch (name.trim().toUpperCase()) {
            case "READ":
                tmpMeth = KapuaMethod.READ;
                break;
            case "CREATE":
                tmpMeth = KapuaMethod.CREATE;
                break;
            case "WRITE":
                tmpMeth = KapuaMethod.WRITE;
                break;
            case "DELETE":
                tmpMeth = KapuaMethod.DELETE;
                break;
            case "OPTIONS":
                tmpMeth = KapuaMethod.OPTIONS;
                break;
            case "EXECUTE":
                tmpMeth = KapuaMethod.EXECUTE;
                break;
        }
        assertNotNull(tmpMeth);

        return tmpMeth;
    }

    private Device getDeviceWithClientId(String clientId) throws KapuaException {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("clientId", clientId));

        DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);

        return deviceList.getFirstItem();
    }

    private KapuaPosition getDefaultPosition() {
        KapuaPosition tmpPos = messageFactory.newPosition();

        tmpPos.setAltitude(250.0);
        tmpPos.setHeading(90.0);
        tmpPos.setLatitude(45.5);
        tmpPos.setLongitude(13.6);
        tmpPos.setPrecision(0.3);
        tmpPos.setSatellites(12);
        tmpPos.setSpeed(120.0);
        tmpPos.setStatus(2);
        tmpPos.setTimestamp(new Date());

        return tmpPos;
    }

    private KapuaBirthPayload prepareDefaultBirthPayload() {
        return new KapuaBirthPayloadImpl(
                "500", // uptime
                "ReliaGate 10-20", // displayName
                "ReliaGate", // modelName
                "ReliaGate 10-20", // modelId
                "ABC123456", // partNumber
                "12312312312", // serialNumber
                "Kura", // firmware
                "2.0", // firmwareVersion
                "BIOStm", // bios
                "1.2.3", // biosVersion
                "linux", // os
                "4.9.18", // osVersion
                "J9", // jvm
                "2.4", // jvmVersion
                "J8SE", // jvmProfile
                "OSGi", // containerFramework
                "1.2.3", // containerFrameworkVersion
                "Kura", // applicationFramework
                "2.0", // applicationFrameworkVersion
                "eth0", // connectionInterface
                "192.168.1.2", // connectionIp
                "gzip", // acceptEncoding
                "CLOUD-V1", // applicationIdentifiers
                "1", // availableProcessors
                "1024", // totalMemory
                "linux", // osArch
                "123456789ABCDEF", // modemImei
                "123456789", // modemImsi
                "ABCDEF" // modemIccid
        );

    }

    private KapuaDisconnectPayload prepareDefaultDeathPayload() {
        return new KapuaDisconnectPayloadImpl(
                "1000", // uptime
                "ReliaGate 10-20" // displayName
        );
    }

    private KapuaMissingPayload prepareDefaultMissingPayload() {
        KapuaMissingPayload tmpPayload = new KapuaMissingPayloadImpl();
        return tmpPayload;
    }

    private KapuaAppsPayload prepareDefaultApplicationPayload() {
        return new KapuaAppsPayloadImpl(
                "500", // uptime
                "ReliaGate 10-20", // displayName
                "ReliaGate", // modelName
                "ReliaGate 10-20", // modelId
                "ABC123456", // partNumber
                "12312312312", // serialNumber
                "Kura", // firmware
                "2.0", // firmwareVersion
                "BIOStm", // bios
                "1.2.3", // biosVersion
                "linux", // os
                "4.9.18", // osVersion
                "J9", // jvm
                "2.4", // jvmVersion
                "J8SE", // jvmProfile
                "OSGi", // containerFramework
                "1.2.3", // containerFrameworkVersion
                "Kura", // applicationFramework
                "2.0", // applicationFrameworkVersion
                "eth0", // connectionInterface
                "192.168.1.2", // connectionIp
                "gzip", // acceptEncoding
                "CLOUD-V1", // applicationIdentifiers
                "1", // availableProcessors
                "1024", // totalMemory
                "linux", // osArch
                "123456789ABCDEF", // modemImei
                "123456789", // modemImsi
                "ABCDEF" // modemIccid
        );
    }

    private DeviceCreator prepareDeviceCreatorFromCucDevice(CucDevice dev) {
        Account tmpAccount = (Account) stepData.get("LastAccount");
        DeviceCreator tmpCr;
        KapuaId tmpScope;

        if (dev.getScopeId() != null) {
            tmpScope = dev.getScopeId();
        } else {
            Assert.assertNotNull(tmpAccount);
            Assert.assertNotNull(tmpAccount.getId());
            tmpScope = tmpAccount.getId();
        }

        Assert.assertNotNull(dev.getClientId());
        Assert.assertNotEquals(0, dev.getClientId().length());

        tmpCr = prepareRegularDeviceCreator(tmpScope, dev.getClientId());

        if (dev.getGroupId() != null) {
            tmpCr.setGroupId(dev.getGroupId());
        }
        if (dev.getConnectionId() != null) {
            tmpCr.setConnectionId(dev.getConnectionId());
        }
        if (dev.getDisplayName() != null) {
            tmpCr.setDisplayName(dev.getDisplayName());
        }
        if (dev.getStatus() != null) {
            tmpCr.setStatus(dev.getStatus());
        }
        if (dev.getModelId() != null) {
            tmpCr.setModelId(dev.getModelId());
        }
        if (dev.getSerialNumber() != null) {
            tmpCr.setSerialNumber(dev.getSerialNumber());
        }
        if (dev.getImei() != null) {
            tmpCr.setImei(dev.getImei());
        }
        if (dev.getImsi() != null) {
            tmpCr.setImsi(dev.getImsi());
        }
        if (dev.getIccid() != null) {
            tmpCr.setIccid(dev.getIccid());
        }
        if (dev.getBiosVersion() != null) {
            tmpCr.setBiosVersion(dev.getBiosVersion());
        }
        if (dev.getFirmwareVersion() != null) {
            tmpCr.setFirmwareVersion(dev.getFirmwareVersion());
        }
        if (dev.getOsVersion() != null) {
            tmpCr.setOsVersion(dev.getOsVersion());
        }
        if (dev.getJvmVersion() != null) {
            tmpCr.setJvmVersion(dev.getJvmVersion());
        }
        if (dev.getOsgiFrameworkVersion() != null) {
            tmpCr.setOsgiFrameworkVersion(dev.getOsgiFrameworkVersion());
        }
        if (dev.getApplicationFrameworkVersion() != null) {
            tmpCr.setApplicationFrameworkVersion(dev.getApplicationFrameworkVersion());
        }
        if (dev.getApplicationIdentifiers() != null) {
            tmpCr.setApplicationIdentifiers(dev.getApplicationIdentifiers());
        }
        if (dev.getAcceptEncoding() != null) {
            tmpCr.setAcceptEncoding(dev.getAcceptEncoding());
        }

        return tmpCr;
    }

    DeviceConnectionStatus parseConnectionStatusString(String stat) {
        switch (stat.trim().toUpperCase()) {
            case "CONNECTED":
                return DeviceConnectionStatus.CONNECTED;
            case "DISCONNECTED":
                return DeviceConnectionStatus.DISCONNECTED;
            case "MISSING":
                return DeviceConnectionStatus.MISSING;
        }
        return null;
    }

    ConnectionUserCouplingMode parseConnectionCouplingString(String mode) {
        switch (mode.trim().toUpperCase()) {
            case "INHERITED":
                return ConnectionUserCouplingMode.INHERITED;
            case "LOOSE":
                return ConnectionUserCouplingMode.LOOSE;
            case "STRICT":
                return ConnectionUserCouplingMode.STRICT;
        }
        return null;
    }

    boolean parseBooleanFromString(String value) {
        switch (value.trim().toLowerCase()) {
            case "true":
                return true;
            case "false":
                return false;
        }
        return false;
    }
}
