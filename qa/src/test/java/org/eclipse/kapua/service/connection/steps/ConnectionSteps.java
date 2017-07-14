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
package org.eclipse.kapua.service.connection.steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.TestJAXBContextProvider;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.connection.internal.DeviceConnectionListResultImpl;
import org.eclipse.kapua.service.device.steps.AclCreator;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.steps.TestUser;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@ScenarioScoped
public class ConnectionSteps {

    /**
     * Authentication service.
     */
    private static AuthenticationService authenticationService;

    /**
     * Account service.
     */
    private static AccountService accountService;

    /**
     * Account factory.
     */
    private static AccountFactory accountFactory;

    /**
     * User service.
     */
    private static UserService userService;

    /**
     * User factory.
     */
    private static UserFactory userFactory;

    /**
     * Credential service.
     */
    private static CredentialService credentialService;

    /**
     * Accessinfo service.
     */
    private static AccessInfoService accessInfoService;

    private static DeviceRegistryService deviceRegistryService;
    private static DeviceConnectionService deviceConnectionService;
    private static DeviceConnectionFactory deviceConnectionFactory;

    /**
     * Helper for creating Accoutn, User and other artifacts needed in tests.
     */
    private static AclCreator aclCreator;

    // Scenario scoped Device Registry test data
    private static StepData stepData;

    // Single point to database access.
    private static DBHelper dbHelper;

    @Inject
    public ConnectionSteps(StepData stepData, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.stepData = stepData;
    }

    @Before
    public void beforeScenario() {

        KapuaLocator locator = KapuaLocator.getInstance();
        authenticationService = locator.getService(AuthenticationService.class);
        accountService = locator.getService(AccountService.class);
        accountFactory = locator.getFactory(AccountFactory.class);
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceConnectionService = locator.getService(DeviceConnectionService.class);
        deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        aclCreator = new AclCreator(accountService, accountFactory, userService, accessInfoService, credentialService);

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

    @Given("^Such a set of privileged users for account \"(.+)\"$")
    public void createPrivilegedUsers(String accName, List<TestUser> users) throws Throwable {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(accName);

            for (TestUser tmpTestUsr : users) {
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
            DeviceConnectionListResult tmpConnLst = new DeviceConnectionListResultImpl();

            tmpAcc = accountService.findByName(account);
            assertNotNull(tmpAcc);
            assertNotNull(tmpAcc.getId());

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

    @Then("^I find (\\d+) connections?$")
    public void countNumberOfConnections(int cnt) {

        DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");
        assertEquals(cnt, tmpConnLst.getSize());
    }

    @Then("^The connection status is \"(.+)\"$")
    public void checkDeviceConnectionStatus(String status) {

        DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);
        DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");

        assertNotNull(tmpConnLst);
        assertNotEquals(0, tmpConnLst.getSize());

        DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
        assertEquals(tmpStat, tmpConnection.getStatus());
    }

    @Then("^The connection user is \"(.+)\"$")
    public void checkDeviceConnectionUser(String user) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get("DeviceConnectionList");
            User tmpUsr = userService.findByName(user);

            assertNotNull(tmpConnLst);
            assertNotEquals(0, tmpConnLst.getSize());

            DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
            assertEquals(tmpUsr.getId(), tmpConnection.getUserId());
        });
    }

    @When("^I set the connection status from the device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionStatus(String device, String account, String status) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);

            assertNotNull(tmpStat);
            assertNotNull(tmpConn);

            tmpConn.setStatus(tmpStat);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("^I set the user coupling mode for the connection from device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionCouplingMode(String device, String account, String mode) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            ConnectionUserCouplingMode tmpMode = parseConnectionCouplingString(mode);
            assertNotNull(tmpMode);

            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);

            assertNotNull(tmpConn);

            tmpConn.setUserCouplingMode(tmpMode);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("^I set the user change flag for the connection from device \"(.+)\" in account \"(.+)\" to \"(.+)\"$")
    public void modifyDeviceConnectionUserChangeFlag(String device, String account, String flag) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);

            assertNotNull(tmpConn);

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

            assertNotNull(tmpConn);

            if (resUser.isEmpty() || resUser.trim().toLowerCase().contains("null")) {
                userId = null;
            } else {
                userId = userService.findByName(resUser).getId();
            }

            tmpConn.setReservedUserId(userId);
            stepData.put("ExceptionCaught", false);
            try {
                deviceConnectionService.update(tmpConn);
            } catch (KapuaException ex) {
                stepData.put("ExceptionCaught", true);
            }
        });
    }

    @Then("^The user for the connection from device \"(.+)\" in scope \"(.+)\" is \"(.+)\"$")
    public void checkUserForExistingConnection(String device, String scope, String name) throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(scope);
            DeviceConnection connection = deviceConnectionService.findByClientId(account.getId(), device);
            User user = userService.findByName(name);

            assertNotNull(connection);
            assertNotNull(user);
            assertEquals(user.getId(), connection.getUserId());
        });
    }

    // Private helper functions

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
