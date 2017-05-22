/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.TestJAXBContextProvider;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceCredentialsMode;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventListResultImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventQueryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceListResultImpl;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.service.user.steps.TestConfig;
import org.eclipse.kapua.test.KapuaTest;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

// Implementation of Gherkin steps used in DeviceRegistryI9n.feature scenarios.
@ScenarioScoped
public class DeviceServiceSteps extends KapuaTest {

    // Device registry services
    private DeviceRegistryService deviceRegistryService;
    private DeviceEventService deviceEventsService;
    private DeviceLifeCycleService deviceLifeCycleService;

    // Scenario scoped Device Registry test data
    StepData stepData;

    // Single point to database access.
    private DBHelper dbHelper;

    @Inject
    public DeviceServiceSteps(StepData stepData, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.stepData = stepData;
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) throws KapuaException {

        // Find all the required services with the default Locator
        KapuaLocator locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceEventsService = locator.getService(DeviceEventService.class);
        deviceLifeCycleService = locator.getService(DeviceLifeCycleService.class);

        // Initialize the database
        dbHelper.setup();

        XmlUtil.setContextProvider(new TestJAXBContextProvider());
    }

    @After
    public void afterScenario() throws Exception {

        // Clean up the database
        dbHelper.deleteAll();
        KapuaSecurityUtils.clearSession();
    }

    // Cucumber test steps

    @Given("^A birth message from device \"(.+)\"$")
    public void createABirthMessage(String clientId)
            throws KapuaException {

        Account tmpAccount = (Account) stepData.get("LastAccount");

        assertNotNull(clientId);
        assertFalse(clientId.isEmpty());
        assertNotNull(tmpAccount);
        assertNotNull(tmpAccount.getId());

        Device tmpDev = null;
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

        deviceLifeCycleService.birth(generateRandomId(), tmpMsg);
    }

    @When("^I configure the device service$")
    public void setDeviceServiceConfig(List<TestConfig> testConfigs)
            throws KapuaException {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        Map<String, Object> valueMap = new HashMap<>();

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }
        try {
            stepData.put("ExceptionCaught", false);
            deviceRegistryService.setConfigValues(tmpAccount.getId(), tmpAccount.getScopeId(), valueMap);
        } catch (KapuaException ex) {
            stepData.put("ExceptionCaught", true);
        }
    }

    @Given("^A device such as$")
    public void createADeviceAsSpecified(List<CucDevice> devLst)
            throws KapuaException {

        assertNotNull(devLst);
        assertEquals(1, devLst.size());

        CucDevice tmpCDev = devLst.get(0);
        tmpCDev.parse();
        DeviceCreator devCr = prepareDeviceCreatorFromCucDevice(tmpCDev);

        stepData.put("LastDevice", deviceRegistryService.create(devCr));
    }

    @When("^I search for the device \"(.+)\" in account \"(.+)\"$")
    public void searchForDeviceWithClientID(String clientId, String Account)
            throws KapuaException {

        Account tmpAcc = null;
        Device tmpDev = null;
        DeviceListResult tmpList = new DeviceListResultImpl();

        tmpAcc = KapuaLocator.getInstance().getService(AccountService.class).findByName(Account);
        assertNotNull(tmpAcc);
        assertNotNull(tmpAcc.getId());

        tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
        if (tmpDev != null) {
            Vector<Device> dv = new Vector<>();
            dv.add(tmpDev);
            tmpList.addItems(dv);
            stepData.put("Device", tmpDev);
            stepData.put("DeviceList", tmpList);
        }
    }

    @When("^I search for events from device \"(.+)\" in account \"(.+)\"$")
    public void searchForEventsFromDeviceWithClientID(String clientId, String Account)
            throws KapuaException {

        DeviceEventQuery tmpQuery = null;
        Device tmpDev = null;
        DeviceEventListResult tmpList = null;
        Account tmpAcc = null;

        tmpAcc = KapuaLocator.getInstance().getService(AccountService.class).findByName(Account);
        assertNotNull(tmpAcc);
        assertNotNull(tmpAcc.getId());

        tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
        assertNotNull(tmpDev);
        assertNotNull(tmpDev.getId());

        tmpQuery = new DeviceEventQueryImpl(tmpAcc.getId());
        tmpQuery.setPredicate(attributeIsEqualTo("deviceId", tmpDev.getId()));
        tmpQuery.setSortCriteria(new FieldSortCriteria("receivedOn", FieldSortCriteria.SortOrder.ASCENDING));
        tmpList = deviceEventsService.query(tmpQuery);
        assertNotNull(tmpList);
        stepData.put("DeviceEventList", tmpList);
    }

    @Then("^I find (\\d+) events?$")
    public void checkEventListLength(int cnt) {
        assertNotNull(stepData.get("DeviceEventList"));
        assertEquals(cnt, ((DeviceEventListResultImpl) stepData.get("DeviceEventList")).getSize());
    }

    @Then("^I find (\\d+) devices?$")
    public void checkDeviceListLength(int cnt) {
        assertNotNull(stepData.get("DeviceList"));
        assertEquals(cnt, ((DeviceListResultImpl) stepData.get("DeviceList")).getSize());
    }

    @Then("^The type of the last event is \"(.+)\"$")
    public void checkLastEventType(String type) {
        DeviceEventListResult tmpList = null;

        assertNotNull(stepData.get("DeviceEventList"));
        assertNotEquals(0, ((DeviceEventListResultImpl) stepData.get("DeviceEventList")).getSize());
        tmpList = (DeviceEventListResultImpl) stepData.get("DeviceEventList");
        assertEquals(type.trim().toUpperCase(), tmpList.getItem(tmpList.getSize() - 1).getResource().trim().toUpperCase());

    }

    // *******************
    // * Private Helpers *
    // *******************

    private KapuaPosition getDefaultPosition() {
        KapuaPosition tmpPos = new KapuaPositionImpl();

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
        KapuaBirthPayload tmpPayload = new KapuaBirthPayloadImpl(
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

        return tmpPayload;
    }

    private DeviceCreator prepareDeviceCreatorFromCucDevice(CucDevice dev) {
        Account tmpAccount = (Account) stepData.get("LastAccount");
        DeviceCreator tmpCr = null;
        KapuaId tmpScope = null;

        if (dev.scopeId != null) {
            tmpScope = dev.getScopeId();
        } else {
            assertNotNull(tmpAccount);
            assertNotNull(tmpAccount.getId());
            tmpScope = tmpAccount.getId();
        }

        assertNotNull(dev.clientId);
        assertNotEquals(0, dev.clientId.length());

        tmpCr = prepareDefaultDeviceCreator(tmpScope, dev.clientId);

        if (dev.groupId != null) {
            tmpCr.setGroupId(dev.getGroupId());
        }
        if (dev.connectionId != null) {
            tmpCr.setConnectionId(dev.getConnectionId());
        }
        if (dev.preferredUserId != null) {
            tmpCr.setPreferredUserId(dev.getPreferredUserId());
        }
        if (dev.displayName != null) {
            tmpCr.setDisplayName(dev.displayName);
        }
        if (dev.status != null) {
            tmpCr.setStatus(dev.getStatus());
        }
        if (dev.modelId != null) {
            tmpCr.setModelId(dev.modelId);
        }
        if (dev.serialNumber != null) {
            tmpCr.setSerialNumber(dev.serialNumber);
        }
        if (dev.imei != null) {
            tmpCr.setImei(dev.imei);
        }
        if (dev.imsi != null) {
            tmpCr.setImsi(dev.imsi);
        }
        if (dev.iccid != null) {
            tmpCr.setIccid(dev.iccid);
        }
        if (dev.biosVersion != null) {
            tmpCr.setBiosVersion(dev.biosVersion);
        }
        if (dev.firmwareVersion != null) {
            tmpCr.setFirmwareVersion(dev.firmwareVersion);
        }
        if (dev.osVersion != null) {
            tmpCr.setOsVersion(dev.osVersion);
        }
        if (dev.jvmVersion != null) {
            tmpCr.setJvmVersion(dev.jvmVersion);
        }
        if (dev.osgiFrameworkVersion != null) {
            tmpCr.setOsgiFrameworkVersion(dev.osgiFrameworkVersion);
        }
        if (dev.applicationFrameworkVersion != null) {
            tmpCr.setApplicationFrameworkVersion(dev.applicationFrameworkVersion);
        }
        if (dev.applicationIdentifiers != null) {
            tmpCr.setApplicationIdentifiers(dev.applicationIdentifiers);
        }
        if (dev.acceptEncoding != null) {
            tmpCr.setAcceptEncoding(dev.acceptEncoding);
        }
        if (dev.credentialsMode != null) {
            tmpCr.setCredentialsMode(dev.getCredentialsMode());
        }

        return tmpCr;
    }

    private DeviceCreator prepareDefaultDeviceCreator(KapuaId scopeId, String clientId) {
        DeviceCreator tmpCr = null;

        tmpCr = KapuaLocator.getInstance().getFactory(DeviceFactory.class).newCreator(
                scopeId,
                clientId);

        tmpCr.setConnectionId(generateRandomId());
        tmpCr.setDisplayName("display_name");
        tmpCr.setSerialNumber("serialNumber");
        tmpCr.setModelId("modelId");
        tmpCr.setImei(String.valueOf(random.nextInt()));
        tmpCr.setImsi(String.valueOf(random.nextInt()));
        tmpCr.setIccid(String.valueOf(random.nextInt()));
        tmpCr.setBiosVersion("biosVersion");
        tmpCr.setFirmwareVersion("firmwareVersion");
        tmpCr.setOsVersion("osVersion");
        tmpCr.setJvmVersion("jvmVersion");
        tmpCr.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpCr.setApplicationFrameworkVersion("kapuaVersion");
        tmpCr.setApplicationIdentifiers("applicationIdentifiers");
        tmpCr.setAcceptEncoding("acceptEncoding");
        tmpCr.setCustomAttribute1("customAttribute1");
        tmpCr.setCustomAttribute2("customAttribute2");
        tmpCr.setCustomAttribute3("customAttribute3");
        tmpCr.setCustomAttribute4("customAttribute4");
        tmpCr.setCustomAttribute5("customAttribute5");
        tmpCr.setCredentialsMode(DeviceCredentialsMode.LOOSE);
        tmpCr.setPreferredUserId(generateRandomId());
        tmpCr.setStatus(DeviceStatus.ENABLED);

        return tmpCr;
    }

    private KapuaId generateRandomId() {
        return new KapuaEid(BigInteger.valueOf(random.nextLong()));
    }
}
