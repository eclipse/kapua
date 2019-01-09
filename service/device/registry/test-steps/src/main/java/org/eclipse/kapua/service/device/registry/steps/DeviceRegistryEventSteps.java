/*******************************************************************************
 * Copyright (c) 2011, 2019 Eurotech and/or its affiliates and others
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
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventFactoryImpl;
import org.eclipse.kapua.service.device.registry.event.internal.DeviceEventServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.HashSet;

/**
 * Implementation of Gherkin steps used in DeviceRegistry.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service -
 */
@ScenarioScoped
public class DeviceRegistryEventSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegistryEventSteps.class);

    // Various device registry related service references
    private DeviceRegistryService deviceService;
    private DeviceFactory deviceFactory;
    private DeviceEventService eventService;
    private DeviceEventFactory eventFactory;

//    private Set<Device> devices = new HashSet<>();

    // Default constructor
    @Inject
    public DeviceRegistryEventSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

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
                bind(DeviceEventService.class).toInstance(new DeviceEventServiceImpl());
                bind(DeviceEventFactory.class).toInstance(new DeviceEventFactoryImpl());
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
        deviceService = locator.getService(DeviceRegistryService.class);
        deviceFactory = locator.getFactory(DeviceFactory.class);
        eventService = locator.getService(DeviceEventService.class);
        eventFactory = locator.getFactory(DeviceEventFactory.class);

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

//    @Given("^A \"(.+)\" device$")
//    public void createDevice(String clientId)
//            throws Exception {
//        assertNotNull(clientId);
//        assertTrue(!clientId.isEmpty());
//
//        DeviceCreator deviceCreator = prepareRegularDeviceCreator(scopeId, clientId);
//        assertNotNull(deviceCreator);
//
//        primeException();
//        try {
//            device = deviceService.create(deviceCreator);
//            assertNotNull(device);
//            deviceId = device.getId();
//            assertNotNull(deviceId);
//
//            devices.add(device);
//        } catch (KapuaException ex) {
//            verifyException(ex);
//            fail("Device: " + ex.getMessage());
//        }
//    }

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
            stepData.remove("EventList");
            DeviceEventListResult eventList = eventService.query(tmpQuery);
            stepData.put("EventList", eventList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
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

    @Then("^I find (\\d+) device events?$")
    public void checkListForNumberOfItems(int number) {

        DeviceEventListResult eventList = (DeviceEventListResult) stepData.get("EventList");
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
        Domain tmpDomain = new DomainImpl();

        tmpDomain.setName("test_name");
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));

        assertEquals("test_name", tmpDomain.getName());
        assertEquals(2, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.connect));
        assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a event creator object. The creator is pre-filled with default data.
    private DeviceCreator prepareRegularDeviceCreator(KapuaId accountId, String clientId) {

        return deviceFactory.newCreator(accountId, clientId);
    }

    // Create a event creator object. The creator is pre-filled with default data.
    private DeviceEventCreator prepareRegularDeviceEventCreator(KapuaId accountId, KapuaId deviceId) {

        DeviceEventCreator tmpCreator = eventFactory.newCreator(accountId);
        KapuaPosition tmpPosition = new KapuaPositionImpl();
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

//    private KapuaId getDeviceId(String clientId) {
//        assertNotNull(clientId);
//        for (Device d : devices) {
//            if (clientId.equals(d.getClientId())) {
//                return d.getId();
//            }
//        }
//        fail("Not found device with clientId: " + clientId);
//        return null;
//    }

    private Device getDeviceWithClientId(String clientId) throws KapuaException {

        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("clientId", clientId));

        DeviceListResult deviceList = deviceService.query(tmpQuery);

        return deviceList.getFirstItem();
    }
}
