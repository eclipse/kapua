/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionFactoryImpl;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionServiceImpl;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Gherkin steps used in DeviceRegistryConnection.features scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service -
 */
@ScenarioScoped
public class DeviceRegistryConnectionSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRegistryConnectionSteps.class);

    private static final String CLIENT_NAME = "test_client";
    private static final String CLIENT_IP = "127.1.1.10";
    private static final String SERVER_IP = "127.1.1.100";

    // Various device connection related service references
    private DeviceConnectionService deviceConnectionService;
    private DeviceConnectionFactory deviceConnectionFactory;

    // Default constructor
    @Inject
    public DeviceRegistryConnectionSteps(StepData stepData, DBHelper dbHelper) {

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

                // Inject actual user service related services
                DeviceEntityManagerFactory deviceEntityManagerFactory = DeviceEntityManagerFactory.instance();
                bind(DeviceEntityManagerFactory.class).toInstance(deviceEntityManagerFactory);
                bind(DeviceConnectionService.class).toInstance(new DeviceConnectionServiceImpl());
                bind(DeviceConnectionFactory.class).toInstance(new DeviceConnectionFactoryImpl());
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
        deviceConnectionService = locator.getService(DeviceConnectionService.class);
        deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

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

    @When("^I configure the device connection service$")
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
            DeviceConnectionCreator connectionCreator;
            DeviceConnection connection = null;
            stepData.remove("DeviceConnection");
            stepData.remove("DeviceConnectionId");

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

            stepData.put("DeviceConnection", connection);
            stepData.put("DeviceConnectionId", connection.getId());
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
    public void checkConnectionStatus(String status) {

        DeviceConnection connection = (DeviceConnection) stepData.get("DeviceConnection");

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

    @Then("^I find (\\d+) connection(?:|s)$")
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
}
