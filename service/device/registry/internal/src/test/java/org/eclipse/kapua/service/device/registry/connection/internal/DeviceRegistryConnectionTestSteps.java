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
package org.eclipse.kapua.service.device.registry.connection.internal;

import static org.eclipse.kapua.commons.model.query.predicate.AttributePredicate.attributeIsEqualTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

import java.math.BigInteger;
import java.security.acl.Permission;
import java.util.HashSet;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.locator.guice.KapuaLocatorImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionSummary;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryServiceTestSteps;
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
 * Implementation of Gherkin steps used in DeviceRegistryConnection.features scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service -
 */
@ScenarioScoped
public class DeviceRegistryConnectionTestSteps extends AbstractKapuaSteps {

    public static String DEFAULT_PATH = "src/main/sql/H2";
    public static String DEFAULT_COMMONS_PATH = "../../../commons";
    public static String CREATE_DEVICE_TABLES = "dvc_*_create.sql";
    public static String DROP_DEVICE_TABLES = "dvc_*_drop.sql";

    public static String CLIENT_NAME = "test_client";
    public static String CLIENT_IP = "127.1.1.10";
    public static String SERVER_IP = "127.1.1.100";

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);
    KapuaId rootUserId = new KapuaEid(BigInteger.ONE);

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DeviceRegistryServiceTestSteps.class);

    // Currently executing scenario.
    Scenario scenario;

    // Various device connection related service references
    DeviceConnectionService deviceConnectionService = null;
    DeviceConnectionFactory deviceConnectionFactory = null;

    // Device connection related objects
    DeviceConnection connection = null;
    DeviceConnectionListResult connectionList = null;

    // Device registry related objects
    DeviceConnectionCreator connectionCreator = null;
    Device device = null;

    // The registry IDs
    KapuaId userId = null;
    KapuaId scopeId = null;
    KapuaId connectionId = null;
    KapuaId deviceId = null;

    // Scratchpad data
    String stringVal = "";
    int intVal = 0;
    boolean boolVal = false;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

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

        // Create the account service tables
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_DEVICE_TABLES);
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
        deviceConnectionService = new DeviceConnectionServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService.class,
                deviceConnectionService);
        deviceConnectionFactory = new DeviceConnectionFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory.class,
                deviceConnectionFactory);

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);

        // Default the scope ID to the root ID and the user ID to the system user ID
        scopeId = rootScopeId;
        userId = rootUserId;
    }

    @After
    public void afterScenario()
            throws Exception {
        // Drop the Account Service tables
        scriptSession(DeviceEntityManagerFactory.instance(), DROP_DEVICE_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();

        container.shutdown();
    }

    // The Cucumber test steps

    @Given("^A regular connection creator$")
    public void createRegularCreator() {
        connectionCreator = prepareRegularConnectionCreator(rootScopeId,
                new KapuaEid(BigInteger.valueOf(random.nextLong())));
    }

    @Given("^A connection for scope (d+)$")
    public void createConnectionInScope(int scope)
            throws KapuaException {
        DeviceConnectionCreator tmpCreator = prepareRegularConnectionCreator(new KapuaEid(BigInteger.valueOf(scope)),
                new KapuaEid(BigInteger.valueOf(random.nextLong())));
        connection = deviceConnectionService.create(tmpCreator);
    }

    @Given("^A scope with id (\\d+)$")
    public void setCustomScopeId(int scope) {
        scopeId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(scopeId);
    }

    @Given("^User (-?\\d+) in scope (-?\\d+)$")
    public void setCustomUserAndScopeId(int user, int scope) {
        userId = new KapuaEid(BigInteger.valueOf(user));
        scopeId = new KapuaEid(BigInteger.valueOf(scope));
        assertNotNull(userId);
        assertNotNull(scopeId);
    }

    @Given("^I have the following connection(?:|s)$")
    public void createConnections(List<DeviceConnectionImpl> connections)
            throws KapuaException {
        try {
            exceptionCaught = false;
            for (DeviceConnection connItem : connections) {
                connectionCreator = new DeviceConnectionCreatorImpl(scopeId);
                connectionCreator.setUserId(userId);
                connectionCreator.setClientId(connItem.getClientId());
                connectionCreator.setClientIp(connItem.getClientIp());
                connectionCreator.setServerIp(connItem.getServerIp());
                connectionCreator.setProtocol(connItem.getProtocol());
                connection = deviceConnectionService.create(connectionCreator);
                connectionId = connection.getId();
            }
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @Given("^I modify the connection details to$")
    public void updateConnectionDetails(List<DeviceConnectionImpl> connections)
            throws KapuaException {
        // Only a single connection must be specified for this test!
        assertNotNull(connections);
        assertEquals(1, connections.size());
        try {
            exceptionCaught = false;
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
            connection = deviceConnectionService.update(connection);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I try to modify the connection client Id to \"(.+)\"$")
    public void changeConnectionClientId(String client)
            throws KapuaException {
        // Remember the old client ID for later checking
        stringVal = connection.getClientId();
        // Update the connection client ID
        connection.setClientId(client);
        connection = deviceConnectionService.update(connection);
    }

    @When("^I try to modify the connection Id$")
    public void changeConnectionIdRandomly()
            throws KapuaException {
        // Try to update the connection ID
        KapuaId newId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        connection.setId(newId);
        try {
            exceptionCaught = false;
            connection = deviceConnectionService.update(connection);
        } catch (KapuaException ex) {
            // Since the ID is not updatable there should be an exception
            exceptionCaught = true;
        }
    }

    @When("^I create a new connection from the existing creator$")
    public void createConnectionFromExistingCreator()
            throws KapuaException {
        connection = deviceConnectionService.create(connectionCreator);
    }

    @Then("^The connection object is regular$")
    public void checkConnectionObject() {
        assertNotNull(connection);
        assertNotNull(connection.getId());
    }

    @Then("^The connection object matches the creator$")
    public void checkConnectionObjectAgainstCreator()
            throws KapuaException {
        assertNotNull(connection);
        assertNotNull(connectionCreator);
        assertEquals(connectionCreator.getScopeId(), connection.getScopeId());
        assertEquals(connectionCreator.getClientId(), connection.getClientId());
        assertEquals(connectionCreator.getUserId(), connection.getUserId());
        assertEquals(connectionCreator.getClientIp(), connection.getClientIp());
        assertEquals(connectionCreator.getServerIp(), connection.getServerIp());
        assertEquals(connectionCreator.getProtocol(), connection.getProtocol());
    }

    @Then("^The connection status is \"(.+)\"$")
    public void checkConnectionStatus(String status) {
        if (status.trim().toUpperCase().equals("CONNECTED")) {
            assertEquals(DeviceConnectionStatus.CONNECTED, connection.getStatus());
        } else if (status.trim().toUpperCase().equals("DISCONNECTED")) {
            assertEquals(DeviceConnectionStatus.DISCONNECTED, connection.getStatus());
        } else if (status.trim().toUpperCase().equals("MISSING")) {
            assertEquals(DeviceConnectionStatus.MISSING, connection.getStatus());
        } else {
            fail();
        }
    }

    @Then("^I count (\\d+) connections in scope (-?\\d+)$")
    public void countConnectioncInScope(int target, int scope)
            throws KapuaException {
        DeviceConnectionQuery tmpQuery = new DeviceConnectionQueryImpl(new KapuaEid(BigInteger.valueOf(scope)));
        long tmpCount = 0;

        assertNotNull(tmpQuery);
        tmpCount = deviceConnectionService.count(tmpQuery);
        assertEquals(target, tmpCount);
    }

    @When("^I search for a connection by scope and connection IDs$")
    public void findConnectionByScopeAndConnectionId()
            throws KapuaException {
        connection = deviceConnectionService.find(scopeId, connectionId);
    }

    @When("^I search for a random connection ID$")
    public void searchForARandomConnectionId()
            throws KapuaException {
        KapuaId tmpConnId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        connection = deviceConnectionService.find(scopeId, tmpConnId);
    }

    @When("^I search for a connection with the client ID \"(.+)\"$")
    public void findConnectionByClientId(String client)
            throws KapuaException {
        connection = deviceConnectionService.findByClientId(scopeId, client);
    }

    @When("^I delete the existing connection$")
    public void deleteExistingConnection()
            throws KapuaException {
        deviceConnectionService.delete(connection.getScopeId(), connection.getId());
        connection = null;
    }

    @When("^I try to delete a random connection ID$")
    public void deleteRandomConnection() {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(random.nextLong()));
        try {
            exceptionCaught = false;
            deviceConnectionService.delete(scopeId, tmpId);
        } catch (KapuaException ex) {
            exceptionCaught = true;
        }
    }

    @When("^I query for all connections with the parameter \"(.+)\" set to \"(.+)\"$")
    public void cueryForConnections(String parameter, String value)
            throws KapuaException {
        DeviceConnectionQuery tmpQuery = new DeviceConnectionQueryImpl(scopeId);
        tmpQuery.setPredicate(attributeIsEqualTo(parameter, value));

        connectionList = (DeviceConnectionListResult) deviceConnectionService.query(tmpQuery);
        assertNotNull(connectionList);
    }

    @Then("^I find (\\d+) connection(?:|s)$")
    public void checkResultListLength(int num) {
        assertNotNull(connectionList);
        assertEquals(num, connectionList.getSize());
    }

    @Then("^The connection details match$")
    public void checkConnectionDetails(List<DeviceConnectionImpl> connections)
            throws KapuaException {
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
        assertEquals(stringVal, connection.getClientId());
    }

    @Then("^No connection was found$")
    public void checkThatConnectionIsNull() {
        assertNull(connection);
    }

    @Then("^An exception was thrown$")
    public void checkThatExceptionWasThrown() {
        assertTrue(exceptionCaught);
    }

    @Then("^No exception was thrown$")
    public void checkThatExceptionWasNotThrown() {
        assertFalse(exceptionCaught);
    }

    @Then("^All connection factory functions must return non null values$")
    public void exerciseAllConnectionFactoryFunctions() {
        DeviceConnectionCreator tmpCreator = null;
        DeviceConnectionQuery tmpQuery = null;
        DeviceConnectionSummary tmpSummary = null;

        tmpCreator = deviceConnectionFactory.newCreator(rootScopeId);
        tmpQuery = deviceConnectionFactory.newQuery(rootScopeId);
        tmpSummary = deviceConnectionFactory.newConnectionSummary();

        assertNotNull(tmpCreator);
        assertNotNull(tmpQuery);
        assertNotNull(tmpSummary);
    }

    @Then("^The device connection domain defaults are correctly initialized$")
    public void checkConnectionDomainInitialization() {
        DeviceConnectionDomain tmpDomain = new DeviceConnectionDomain();

        assertEquals("device_connection", tmpDomain.getName());
        assertEquals("DeviceConnectionService", tmpDomain.getServiceName());
        assertEquals(3, tmpDomain.getActions().size());
        assertTrue(tmpDomain.getActions().contains(Actions.read));
        assertTrue(tmpDomain.getActions().contains(Actions.write));
        assertTrue(tmpDomain.getActions().contains(Actions.delete));
    }

    @Then("^The device connection domain data can be updated$")
    public void checkDeviceConnectionDomainUpdate() {
        DeviceConnectionDomain tmpDomain = new DeviceConnectionDomain();

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

    // Create a connection creator object. The creator is pre-filled with default data.
    DeviceConnectionCreator prepareRegularConnectionCreator(KapuaId scopeId, KapuaId userId) {
        DeviceConnectionCreatorImpl tmpCreator = new DeviceConnectionCreatorImpl(scopeId);

        tmpCreator.setUserId(userId);
        tmpCreator.setClientId(CLIENT_NAME);
        tmpCreator.setClientIp(CLIENT_IP);
        tmpCreator.setServerIp(SERVER_IP);
        tmpCreator.setProtocol("tcp");

        return tmpCreator;
    }
}
