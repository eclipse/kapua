/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authentication.shiro.UsernamePasswordCredentialsImpl;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionFactoryImpl;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.UserFactoryImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Steps for testing Access Control List functionality on Broker service.
 */
public class AclSteps {

    public static final int BROKER_START_WAIT_MILLIS = 5000;

    private static final String SYS_USERNAME = "kapua-sys";

    private static final String SYS_PASSWORD = "kapua-password";

    private static final KapuaId SYS_ID = new KapuaEid(BigInteger.ONE);

    private static final KapuaId ROOT_SCOPE_ID = new KapuaEid(BigInteger.ONE);

    /**
     * Mqtt device for listening and sending data from/to broker
     */
    private static MqttDevice mqttDevice;

    /**
     * Topic / value pair containing message that was received from broker by client.
     */
    private static Map<String, String> clientMqttMessage;

    /**
     * Topic / value pair containing message that was received from broker by listener.
     */
    private static Map<String, String> listenerMqttMessage;

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

    @Before
    public void aclStepsBefore() {

        KapuaLocator locator = KapuaLocator.getInstance();
        authenticationService = locator.getService(AuthenticationService.class);
        accountService = locator.getService(AccountService.class);
        accountFactory = locator.getFactory(AccountFactory.class);
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
        credentialService = locator.getService(CredentialService.class);
        accessInfoService = locator.getService(AccessInfoService.class);

        mqttDevice = new MqttDevice();
        clientMqttMessage = new HashMap<>();
        listenerMqttMessage = new HashMap<>();
    }

    @Given("I publish string \"(.*)\" to topic \"(.*)\"$")
    public void clientPublishString(String payload, String topic) {

        mqttDevice.mqttClientPublishString(payload, topic, clientMqttMessage, listenerMqttMessage);
    }

    @Given("^I start Mqtt Device$")
    public void startMqttDevice() throws KapuaException {

        mqttDevice.mqttSubscriberConnect();
        // Wait for broker to start
        waitInMillis(BROKER_START_WAIT_MILLIS);
        // Login with system user
        char[] passwd = SYS_PASSWORD.toCharArray();
        LoginCredentials credentials = new UsernamePasswordCredentialsImpl(SYS_USERNAME, passwd);
        authenticationService.login(credentials);
    }

    @When("^I stop Mqtt Device$")
    public void stopMqttDevice() throws KapuaException {

        mqttDevice.mqttClientDisconnect();
        mqttDevice.mqttSubscriberDisconnect();
        // Logout system user
        authenticationService.logout();
    }

    @Given("^I connect to broker with clientId \"(.*)\" and user \"(.*)\" and password \"(.*)\" and listening on topic \"(.*)\"$")
    public void connectClientToBroker(String clientId, String userName, String password, String topicFilter) {

        mqttDevice.mqttClientConnect(clientId, userName, password, topicFilter);
    }

    @Given("^I disconnect client$")
    public void disconnectClientFromBroker() {

        mqttDevice.mqttClientDisconnect();
    }

    @Then("I receive string \"(.*)\" on topic \"(.*)\"$")
    public void clientReceiveString(String payload, String topic) {

        if ((clientMqttMessage != null) && (clientMqttMessage.size() == 1)) {
            String message = clientMqttMessage.get(topic);
            assertEquals(payload, message);
        } else {
            fail("Message not received by client.");
        }
    }

    @Then("^Broker receives string \"([^\"]*)\" on topic \"([^\"]*)\"$")
    public void brokerReceivesStringOnTopic(String payload, String topic) throws Throwable {

        if ((listenerMqttMessage != null) && (listenerMqttMessage.size() == 1)) {
            String message = listenerMqttMessage.get(topic);
            assertEquals(payload, message);
        } else {
            fail("Message not received by broker.");
        }
    }

    @And("^I Create broker account and user$")
    public void createBrokerAccountAndUser() throws Throwable {

        // Create Account
        configureAccountService(ROOT_SCOPE_ID, SYS_ID);
        AccountCreator accountCreator = accountFactory.newCreator(ROOT_SCOPE_ID, "acme");
        accountCreator.setOrganizationName("ACME Corp.");
        accountCreator.setOrganizationPersonName("John");
        accountCreator.setOrganizationCountry("Italy");
        accountCreator.setOrganizationStateProvinceCounty("Friuli");
        accountCreator.setOrganizationCity("Amaro");
        accountCreator.setOrganizationAddressLine1("Some address 1");
        accountCreator.setOrganizationAddressLine2("Second address line");
        accountCreator.setOrganizationEmail("john@acme.org");
        accountCreator.setOrganizationZipPostCode("1000");
        accountCreator.setOrganizationPhoneNumber("012/555-456-789");
        Account account = accountService.create(accountCreator);
        // Add user to account
        configureUserService(account.getId(), SYS_ID);
        UserCreator userCreator = new UserFactoryImpl().newCreator(account.getId(), "luise");
        userCreator.setName("luise");
        userCreator.setDisplayName("Luise Doe");
        userCreator.setEmail("luise@acme.org");
        userCreator.setPhoneNumber("012/555-456-789");
        User user = userService.create(userCreator);
        // Attach credentials to user
        KapuaSecurityUtils.doPrivileged(() -> {
            CredentialCreator credentialCreator;
            credentialCreator = new CredentialFactoryImpl().newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, "kapua-password");
            try {
                Credential credential = credentialService.create(credentialCreator);
            } catch (KapuaException ke) {
                // skip
            }

            return null;
        });
        // Attach permissions to user
        List<PermissionData> permissionList = new ArrayList<>();
        permissionList.add(new PermissionData(new BrokerDomain(), Actions.connect, (KapuaEid) user.getScopeId()));
//        permissionList.add(new PermissionData(new DeviceManagementDomain(), Actions.write, (KapuaEid) user.getScopeId()));
//        permissionList.add(new PermissionData(new DatastoreDomain(), Actions.read, (KapuaEid) user.getScopeId()));
//        permissionList.add(new PermissionData(new DatastoreDomain(), Actions.write, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    /**
     * Simple wait implementation.
     *
     * @param millis milli seconds
     */
    private void waitInMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // ignore
        }
    }

    /**
     * Configure user service with reasonable default values.
     *
     * @param accId account id
     * @param scopeId scope id
     */
    private void configureUserService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true );
        valueMap.put("maxNumberChildEntities", 5);
        valueMap.put("lockoutPolicy.enabled", false);
        valueMap.put("lockoutPolicy.maxFailures", 3);
        valueMap.put("lockoutPolicy.resetAfter", 300);
        valueMap.put("lockoutPolicy.lockDuration", 3);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Configure account service with reasonable default values.
     *
     * @param accId account id
     * @param scopeId scope id
     */
    private void configureAccountService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true );
        valueMap.put("maxNumberChildEntities", 5);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates permissions for user with specified account. Permissions are created in privileged mode.
     *
     * @param permissionList
     *            list of permissions for user, if targetScopeId is not set user scope that is
     *            specified as account
     * @param user
     *            user for whom permissions are set
     * @param account
     *            account in which user is defined
     * @throws Exception
     */
    private void createPermissions(List<PermissionData> permissionList, User user, Account account)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                accessInfoService.create(accessInfoCreatorCreator(permissionList, user, account));
            } catch (KapuaException ke) {
                ke.printStackTrace();
                //skip
            }

            return null;
        });
    }

    /**
     * Create accessInfoCreator instance with data about user permissions.
     * If target scope is not defined in permission list use account scope.
     *
     * @param permissionList
     *            list of all permissions
     * @param user
     *            user for which permissions are set
     * @param account
     *            that user belongs to
     * @return AccessInfoCreator instance for creating user permissions
     */
    private AccessInfoCreator accessInfoCreatorCreator(List<PermissionData> permissionList,
                                                       User user, Account account) {

        PermissionFactory permissionFactory = new PermissionFactoryImpl();
        AccessInfoCreator accessInfoCreator = new AccessInfoFactoryImpl().newCreator(account.getId());
        accessInfoCreator.setUserId(user.getId());
        accessInfoCreator.setScopeId(user.getScopeId());
        Set<Permission> permissions = new HashSet<>();
        for (PermissionData permissionData : permissionList) {
            Actions action = permissionData.getAction();
            KapuaEid targetScopeId = permissionData.getTargetScopeId();
            if (targetScopeId == null) {
                targetScopeId = (KapuaEid) account.getId();
            }
            Domain domain = permissionData.getDomain();
            Permission permission = permissionFactory.newPermission(domain,
                    action, targetScopeId);
            permissions.add(permission);
        }
        accessInfoCreator.setPermissions(permissions);

        return accessInfoCreator;
    }

}
