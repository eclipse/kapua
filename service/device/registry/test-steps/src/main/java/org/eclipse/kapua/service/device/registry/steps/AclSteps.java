/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.user.User;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.junit.Assert;

import com.google.inject.Singleton;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Steps for testing Access Control List functionality on Broker service.
 */
@Singleton
public class AclSteps extends TestBase {

    public static final int BROKER_START_WAIT_MILLIS = 5000;

    private static final String SYS_USERNAME = "kapua-sys";

    private static final String SYS_PASSWORD = "kapua-password";

    protected KapuaLocator locator;

    private static final String ACCOUNT = "acme";
    private static final String MAIL = "john@acme.org";
    private static final String ORG = "ACME Corp.";
    private static final String NAME = "luise";

    /**
     * Mqtt device for listening and sending data from/to broker
     */
    private static MqttDevice mqttDevice;

    /**
     * Topic / value pair containing message that was received from broker by client.
     */
    private static Map<String, Map<String, String>> clientMqttMessage;

    /**
     * Topic / value pair containing message that was received from broker by listener.
     */
    private static Map<String, String> listenerMqttMessage;

    /**
     * Authentication service.
     */
    private static AuthenticationService authenticationService;

    /**
     * Credentials factory.
     */
    private static CredentialsFactory credentialsFactory;

    /**
     * Accessinfo service.
     */

    /**
     * Helper for creating Accoutn, User and other artifacts needed in tests.
     */
    private static AclCreator aclCreator;

    @Inject
    public AclSteps(StepData stepData) {
        super(stepData);
    }

    @After(value="@setup")
    public void setServices() {
        locator = KapuaLocator.getInstance();
        authenticationService = locator.getService(AuthenticationService.class);
        credentialsFactory = locator.getFactory(CredentialsFactory.class);

        mqttDevice = new MqttDevice();
        clientMqttMessage = new HashMap<>();
        listenerMqttMessage = new HashMap<>();

        aclCreator = new AclCreator();
    }

    @Before(value="@env_docker or @env_docker_base or @env_none", order=10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @Given("string {string} is published to topic {string} with client {string}")
    public void clientPublishString(String payload, String topic, String clientId) {
        mqttDevice.mqttClientPublishString(clientId, payload, topic, clientMqttMessage, listenerMqttMessage);
    }

    @Given("Mqtt Device is started")
    public void startMqttDevice() throws KapuaException {
        mqttDevice.mqttSubscriberConnect();
        // Wait for broker to start
        waitInMillis(BROKER_START_WAIT_MILLIS);
        // Login with system user
        String passwd = SYS_PASSWORD;
        LoginCredentials credentials = credentialsFactory.newUsernamePasswordCredentials(SYS_USERNAME, passwd);
        authenticationService.login(credentials);
    }

    @When("Mqtt Device is stoped")
    public void stopMqttDevice() throws KapuaException {
        mqttDevice.mqttClientsDisconnect();
        mqttDevice.mqttSubscriberDisconnect();
        // Logout system user
        authenticationService.logout();
    }

    @Given("broker with clientId {string} and user {string} and password {string} is listening on topic {string}")
    public void connectClientToBroker(String clientId, String userName, String password, String topicFilter) throws Exception {
        try {
            primeException();
            mqttDevice.mqttClientConnect(clientId, userName, password, topicFilter);
        } catch (MqttException mqtte) {
            verifyException(mqtte);
        }
    }

    @Given("clients are disconnected")
    public void disconnectClientsFromBroker() {
        mqttDevice.mqttClientsDisconnect();
    }

    @Then("Broker receives string {string} on topic {string}")
    public void brokerReceivesStringOnTopic(String payload, String topic) {
        if ((listenerMqttMessage != null) && (listenerMqttMessage.size() == 1)) {
            String message = listenerMqttMessage.get(topic);
            Assert.assertEquals(payload, message);
        } else {
            Assert.fail("Message not received by broker.");
        }
    }

    @Then("Broker doesn't receive string {string} on topic {string}")
    public void brokerDoesntReceiveStringOnTopic(String payload, String topic) {
        if ((listenerMqttMessage != null) && (listenerMqttMessage.size() >= 1)) {
            String message = listenerMqttMessage.get(topic);
            Assert.assertNotEquals(payload, message);
        }
    }

    @Then("client {string} receives string {string} on topic {string}")
    public void iReceiveStringOnTopic(String clientId, String payload, String topic) {
        Map<String, String> messages = clientMqttMessage.get(clientId);
        if ((messages != null) && (messages.size() >= 1)) {
            String message = messages.get(topic);
            Assert.assertEquals(payload, message);
        } else {
            // TODO log (or append in the failure message) this error in a better way
            Assert.fail("Message not received by broker." + (listenerMqttMessage != null && listenerMqttMessage.size() > 0 ? listenerMqttMessage.get(0) : " NULL"));
        }
    }

    @Then("client {string} doesn't receive string {string} on topic {string}")
    public void clientDoesntReceiveStringOnTopic(String clientId, String payload, String topic) {
        Map<String, String> messages = clientMqttMessage.get(clientId);
        if ((messages != null) && (messages.size() >= 1)) {
            String message = messages.get(topic);
            Assert.assertNotEquals(payload, message);
        }
    }

    @And("broker account and user are created")
    public void createBrokerAccountAndUser() throws Exception {
        Account account = aclCreator.createAccount(ACCOUNT, ORG, MAIL);
        User user = aclCreator.createUser(account, NAME);
        aclCreator.attachUserCredentials(account, user);
        aclCreator.attachBrokerPermissions(account, user);
    }

    @And("device account and user are created")
    public void createDeviceAccountAndUser() throws Exception {
        Account account = aclCreator.createAccount(ACCOUNT, ORG, MAIL);
        User user = aclCreator.createUser(account, NAME);
        aclCreator.attachUserCredentials(account, user);
        aclCreator.attachDevicePermissions(account, user);
    }

    @And("data view account and user are created")
    public void createDataViewAccountAndUser() throws Exception {
        Account account = aclCreator.createAccount(ACCOUNT, ORG, MAIL);
        User user = aclCreator.createUser(account, NAME);
        aclCreator.attachUserCredentials(account, user);
        aclCreator.attachDataViewPermissions(account, user);
    }

    @And("data manage account and user are created")
    public void createDataManageAccountAndUser() throws Exception {
        Account account = aclCreator.createAccount(ACCOUNT, ORG, MAIL);
        User user = aclCreator.createUser(account, NAME);
        aclCreator.attachUserCredentials(account, user);
        aclCreator.attachDataManagePermissions(account, user);
    }

    @And("other broker account and user are created")
    public void createOtherBrokerAccountAndUser() throws Exception {
        Account account = aclCreator.createAccount("domino","Domino Corp.", "lisa@domino.org");
        User user = aclCreator.createUser(account, "domina");
        aclCreator.attachUserCredentials(account, user);
        aclCreator.attachBrokerPermissions(account, user);
    }

    @Then("exception is thrown")
    public void exceptionIsThrown() {
        Exception e = (Exception) stepData.get("exception");
        Assert.assertNotNull("Exception expected!", e);
    }

    @Then("exception is not thrown")
    public void exceptionIsNotThrown() {
        Exception e = (Exception) stepData.get("exception");
        Assert.assertNull("Exception not expected!", e);
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

}
