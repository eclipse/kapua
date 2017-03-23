/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.FieldSortCriteria;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
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
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.common.steps.CommonTestData;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceCredentialsMode;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventQueryImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceListResultImpl;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.service.user.steps.UserTestData;
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

    // Path to root of full DB schema scripts.
    public static final String FULL_SCHEMA_PATH = "../dev-tools/src/main/database/";

    // Default filter for dropping, creating and seeding DB schema.
    public static final String DB_CREATE_FILTER = "all_*.sql";
    public static final String DB_SEED_FILTER = "all_*.sql";
    public static final String DB_DROP_FILTER = "all_drop.sql";

    // Device registry services
    private DeviceRegistryService deviceRegistryService = null;
    private DeviceEventService deviceEventsService = null;
    private DeviceLifeCycleService deviceLifeCycleService = null;

    // Scenario scoped Device Registry test data
    DeviceTestData devData = null;
    CommonTestData commonData = null;
    UserTestData usrData = null;

    @Inject
    public DeviceServiceSteps(DeviceTestData devData, CommonTestData commonData, UserTestData usrData) {
        this.devData = devData;
        this.commonData = commonData;
        this.usrData = usrData;
    }

    // Database setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) throws KapuaException {
        // Create the Device Registry tables.
        // First drop everything so to start with a clean database for each test
        // scenario.
        enableH2Connection();
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DB_DROP_FILTER);
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DB_CREATE_FILTER);
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DB_SEED_FILTER);

        // Find all the required services with the default Locator
        KapuaLocator locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceEventsService = locator.getService(DeviceEventService.class);
        deviceLifeCycleService = locator.getService(DeviceLifeCycleService.class);

        // Initialize all the private variables
        devData.clearData();
        commonData.clearData();
        usrData.clearData();
    }

    @After
    public void afterScenario() throws KapuaException {

        // Drop the Device Registry tables
        KapuaConfigurableServiceSchemaUtils.scriptSession(FULL_SCHEMA_PATH, DB_DROP_FILTER);
        KapuaSecurityUtils.clearSession();
    }

    // Cucumber test steps

    @Given("^A birth message from device \"(.+)\"$")
    public void createABirthMessage(String clientId)
            throws KapuaException {

        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
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
        tmpMsg.setScopeId(usrData.lastAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(usrData.lastAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        deviceLifeCycleService.birth(tmpId, tmpMsg);
    }

    @Given("^A disconnect message from device \"(.+)\"$")
    public void createADeathMessage(String clientId)
            throws KapuaException {

        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        Device tmpDev = null;
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
        tmpMsg.setScopeId(usrData.lastAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(usrData.lastAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            commonData.exceptionCaught = false;
            deviceLifeCycleService.death(tmpId, tmpMsg);
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @Given("^A missing message from device \"(.+)\"$")
    public void createAMissingMessage(String clientId)
            throws KapuaException {

        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        Device tmpDev = null;
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
        tmpMsg.setScopeId(usrData.lastAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(usrData.lastAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            commonData.exceptionCaught = false;
            deviceLifeCycleService.missing(tmpId, tmpMsg);
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @Given("^An application message from device \"(.+)\"$")
    public void createAnApplicationMessage(String clientId)
            throws KapuaException {

        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        Device tmpDev = null;
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
        tmpMsg.setScopeId(usrData.lastAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());

        tmpDev = deviceRegistryService.findByClientId(usrData.lastAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }

        try {
            commonData.exceptionCaught = false;
            deviceLifeCycleService.applications(tmpId, tmpMsg);
        } catch (KapuaException ex) {
            commonData.exceptionCaught = true;
        }
    }

    @Given("^A device such as$")
    public void createADeviceAsSpecified(List<CucDevice> devLst)
            throws KapuaException {

        assertNotNull(devLst);
        assertEquals(1, devLst.size());

        DeviceCreator devCr = prepareDeviceCreatorFromCucDevice(devLst.get(0));

        devData.device = deviceRegistryService.create(devCr);
    }

    @When("^I search for the device \"(.+)\" in account \"(.+)\"$")
    public void searchForDeviceWithClientID(String clientId, String Account)
            throws KapuaException {

        Account tmpAcc = null;
        devData.deviceList = new DeviceListResultImpl();
        devData.deviceList.clearItems();

        tmpAcc = KapuaLocator.getInstance().getService(AccountService.class).findByName(Account);
        assertNotNull(tmpAcc);
        assertNotNull(tmpAcc.getId());

        devData.device = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
        if (devData.device != null) {
            Vector<Device> dv = new Vector<Device>();
            dv.add(devData.device);
            devData.deviceList.addItems(dv);
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
        tmpList = (DeviceEventListResult) deviceEventsService.query(tmpQuery);
        assertNotNull(tmpList);
        devData.eventList = tmpList;
    }

    @Then("^I find (\\d+) events?$")
    public void checkEventListLength(int cnt) {
        assertNotNull(devData.eventList);
        assertEquals(cnt, devData.eventList.getSize());
    }

    @Then("^I find (\\d+) devices?$")
    public void checkDeviceListLength(int cnt) {
        assertNotNull(devData.deviceList);
        assertEquals(cnt, devData.deviceList.getSize());
    }

    @Then("^The type of the last event is \"(.+)\"$")
    public void checkLastEventType(String type) {
        assertNotNull(devData.eventList);
        assertNotEquals(0, devData.eventList.getSize());
        assertEquals(type.trim().toUpperCase(), devData.eventList.getItem(devData.eventList.getSize() - 1).getResource().trim().toUpperCase());
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

    private KapuaDisconnectPayload prepareDefaultDeathPayload() {
        KapuaDisconnectPayload tmpPayload = new KapuaDisconnectPayloadImpl(
                "1000", // uptime
                "ReliaGate 10-20" // displayName
        );

        return tmpPayload;
    }

    private KapuaMissingPayload prepareDefaultMissingPayload() {
        KapuaMissingPayload tmpPayload = new KapuaMissingPayloadImpl();
        return tmpPayload;
    }

    private KapuaAppsPayload prepareDefaultApplicationPayload() {
        KapuaAppsPayload tmpPayload = new KapuaAppsPayloadImpl(
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
        DeviceCreator tmpCr = null;
        KapuaId tmpScope = null;

        if (dev.scopeId != null) {
            tmpScope = dev.scopeId;
        } else {
            assertNotNull(usrData.lastAccount);
            assertNotNull(usrData.lastAccount.getId());
            tmpScope = usrData.lastAccount.getId();
        }

        assertNotNull(dev.clientId);
        assertNotEquals(0, dev.clientId.length());

        tmpCr = prepareDefaultDeviceCreator(tmpScope, dev.clientId);

        if (dev.groupId != null) {
            tmpCr.setGroupId(dev.groupId);
        }
        if (dev.connectionId != null) {
            tmpCr.setConnectionId(dev.connectionId);
        }
        if (dev.preferredUserId != null) {
            tmpCr.setPreferredUserId(dev.preferredUserId);
        }
        if (dev.displayName != null) {
            tmpCr.setDisplayName(dev.displayName);
        }
        if (dev.status != null) {
            tmpCr.setStatus(dev.status);
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
            tmpCr.setCredentialsMode(dev.credentialsMode);
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
