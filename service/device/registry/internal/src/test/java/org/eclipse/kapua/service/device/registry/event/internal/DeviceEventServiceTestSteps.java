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
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.event.internal;

import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.security.acl.Permission;
import java.util.Date;
import java.util.HashSet;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.management.KapuaMethod;
import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
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
public class DeviceEventServiceTestSteps extends AbstractKapuaSteps {

    public static String DEFAULT_PATH = "src/main/sql/H2";
    public static String DEFAULT_COMMONS_PATH = "../../../commons";
    public static String CREATE_DEVICE_TABLES = "dvc_*_create.sql";
    public static String DROP_DEVICE_TABLES = "dvc_*_drop.sql";

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);
    KapuaId sysUserId = new KapuaEid(BigInteger.ONE);

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DeviceEventServiceTestSteps.class);

    // Currently executing scenario.
    Scenario scenario;

    // Various device registry related service references
    DeviceEventService eventService = null;
    DeviceEventFactory eventFactory = null;

    // Device registry related objects
    DeviceEvent event = null;
    DeviceEventCreator eventCreator = null;

    // The entity ID of the last event
    KapuaId eventId = null;

    KapuaId scopeId = null;
    KapuaId userId = null;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

    // A list result for device query operations
    DeviceEventListResult eventList = null;

    // Item count
    long count = 0;

    // String scratchpad
    String stringValue;

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

        // Drop the Device Registry Service tables
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_DEVICE_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);

        // Create the Device Registry Service tables
        KapuaConfigurableServiceSchemaUtils.createSchemaObjects(DEFAULT_COMMONS_PATH);
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

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
        eventService = new DeviceEventServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.device.registry.event.DeviceEventService.class, eventService);
        eventFactory = new DeviceEventFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.device.registry.event.DeviceEventFactory.class, eventFactory);

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);

        scopeId = rootScopeId;
        userId = sysUserId;
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

    @Given("^Scope (\\d+)$")
    public void setScopeId(int scope)
            throws Exception {
        scopeId = new KapuaEid(BigInteger.valueOf(scope));

        assertNotNull(scopeId);
    }

    @Given("^User (\\d+)$")
    public void setUserId(int user)
            throws Exception {
        userId = new KapuaEid(BigInteger.valueOf(user));

        assertNotNull(userId);
    }

    @Given("^Null scope ID$")
    public void setNullScopeId() {
        scopeId = null;
    }

    @Given("^Null user ID$")
    public void setNullUserId() {
        userId = null;
    }

    @Given("^An event creator with null action$")
    public void prepareCreatorWithNullAction() {
        eventCreator = prepareRegularDeviceEventCreator(scopeId, createRandomId());
        assertNotNull(eventCreator);
        eventCreator.setAction(null);
    }

    @Given("^A \"(.+)\" event from device (\\d+)$")
    public void createRegularEvent(String eventType, int device)
            throws KapuaException {
        KapuaId tmpDevId = new KapuaEid(BigInteger.valueOf(device));
        KapuaMethod tmpMeth = getMethodFromString(eventType);

        eventCreator = prepareRegularDeviceEventCreator(scopeId, tmpDevId);
        assertNotNull(eventCreator);
        eventCreator.setAction(tmpMeth);

        try {
            exceptionCaught = false;
            event = eventService.create(eventCreator);
            assertNotNull(event);
            eventId = event.getId();
            assertNotNull(eventId);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @Given("^I have (\\d+) \"(.+)\" events? from device (\\d+)$")
    public void createANumberOfEvents(int num, String eventType, int devId)
            throws KapuaException {
        KapuaId tmpDevId = new KapuaEid(BigInteger.valueOf(devId));
        KapuaMethod tmpMeth = getMethodFromString(eventType);
        DeviceEventCreator tmpCreator = null;
        DeviceEvent tmpEvent = null;

        for (int i = 0; i < num; i++) {
            tmpCreator = prepareRegularDeviceEventCreator(scopeId, tmpDevId);
            assertNotNull(tmpCreator);
            tmpCreator.setAction(tmpMeth);
            tmpEvent = eventService.create(tmpCreator);
            assertNotNull(tmpEvent);
        }
    }

    @When("^I create an event from the existing creator$")
    public void createEventFromCreator() {
        try {
            exceptionCaught = false;
            event = eventService.create(eventCreator);
            assertNotNull(event);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I search for an event with the remembered ID$")
    public void findEventById()
            throws KapuaException {
        event = null;
        event = eventService.find(scopeId, eventId);
    }

    @When("^I search for an event with a random ID$")
    public void findEventByRandomId()
            throws KapuaException {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        event = eventService.find(scopeId, tmpId);
    }

    @When("^I delete the event with the remembered ID$")
    public void deleteEvent()
            throws KapuaException {
        eventService.delete(scopeId, eventId);
    }

    @When("^I delete an event with a random ID$")
    public void deleteEventWithRandomId() {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        try {
            exceptionCaught = false;
            eventService.delete(scopeId, tmpId);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I count events for scope (\\d+)$")
    public void countEventsInScope(int scpId)
            throws KapuaException {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scpId));
        DeviceEventQuery tmpQuery = eventFactory.newQuery(tmpId);
        count = 0;

        count = eventService.count(tmpQuery);
    }

    @When("^I query for \"(.+)\" events$")
    public void queryForSpecificEvents(String eventType)
            throws KapuaException {
        KapuaMethod tmpMeth = getMethodFromString(eventType);
        DeviceEventQuery tmpQuery = eventFactory.newQuery(scopeId);
        assertNotNull(tmpQuery);
        assertNotNull(tmpMeth);

        tmpQuery.setPredicate(attributeIsEqualTo("action", tmpMeth));
        eventList = (DeviceEventListResult) eventService.query(tmpQuery);
    }

    @Then("^The event matches the creator parameters$")
    public void checkCreatedEventAgainstCreatorParameters()
            throws KapuaException {
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

    @Then("^I find (\\d+) events?$")
    public void checkListForNumberOfItems(int number) {
        assertEquals(number, eventList.getSize());
    }

    @Then("^There (?:are|is) (\\d+) events?$")
    public void checkNumberOfEvents(int number) {
        assertEquals(number, count);
    }

    @Then("^There is no such event$")
    public void eventIsNull() {
        assertNull(event);
    }

    @Then("^An event exception is caught$")
    public void checkThatAnExceptionWasCaught() {
        assertTrue(exceptionCaught);
    }

    @Then("^All device event factory functions must return non null objects$")
    public void exerciseAllEventFactoryFunctions() {
        DeviceEvent tmpEvent = null;
        DeviceEventCreator tmpCreator = null;
        DeviceEventQuery tmpQuery = null;
        DeviceEventListResult tmpList = null;

        tmpEvent = eventFactory.newEntity(rootScopeId);
        tmpCreator = eventFactory.newCreator(rootScopeId, new KapuaEid(BigInteger.valueOf(random.nextLong())), new Date(), "");
        tmpQuery = eventFactory.newQuery(rootScopeId);
        tmpList = eventFactory.newListResult();

        assertNotNull(tmpEvent);
        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
        assertNotNull(tmpList);
    }

    @Then("^The device event domain data can be updated$")
    public void checkDeviceEventDomainUpdate() {
        DeviceEventDomain tmpDomain = new DeviceEventDomain();

        tmpDomain.setName("test_name");
        tmpDomain.setServiceName("test_service_name");
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));

        assertEquals("test_name", tmpDomain.getName());
        assertEquals("test_service_name", tmpDomain.getServiceName());
        assertEquals(2, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.connect));
        assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a event creator object. The creator is pre-filled with default data.
    private DeviceEventCreator prepareRegularDeviceEventCreator(KapuaId accountId, KapuaId deviceId) {
        DeviceEventCreatorImpl tmpCreator = new DeviceEventCreatorImpl(accountId);
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

    private KapuaId createRandomId() {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        return tmpId;
    }
}
