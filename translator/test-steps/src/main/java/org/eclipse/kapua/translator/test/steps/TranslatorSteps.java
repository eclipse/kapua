/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.translator.test.steps;

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
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.jms.kura.TranslatorDataJmsKura;
import org.eclipse.kapua.translator.kura.jms.TranslatorDataKuraJms;
import org.eclipse.kapua.translator.kura.mqtt.TranslatorDataKuraMqtt;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorDataMqttKura;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorResponseMqttKura;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of Gherkin steps used in TranslatorUnitTests.feature scenarios.
 */
@ScenarioScoped
public class TranslatorSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(TranslatorSteps.class);


    private DBHelper database;
    private ExampleTranslator exampleTranslator;
    private TranslatorDataMqttKura translatorDataMqttKura;
    private TranslatorResponseMqttKura translatorResponseMqttKura;
    private TranslatorDataKuraMqtt translatorDataKuraMqtt;
    private TranslatorDataJmsKura translatorDataJmsKura;
    private TranslatorDataKuraJms translatorDataKuraJms;

    @Inject
    public TranslatorSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        exampleTranslator = new ExampleTranslator();

        translatorDataMqttKura = new TranslatorDataMqttKura();
        translatorResponseMqttKura = new TranslatorResponseMqttKura();
        translatorDataKuraMqtt = new TranslatorDataKuraMqtt();
        translatorDataJmsKura = new TranslatorDataJmsKura();
        translatorDataKuraJms = new TranslatorDataKuraJms();

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
            logger.info("Logging out in cleanup");
            if (isIntegrationTest()) {
                database.deleteAll();
                SecurityUtils.getSubject().logout();
            } else {
                database.dropAll();
                database.close();
            }
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    @Given("^I try to translate from \"([^\"]*)\" to \"([^\"]*)\"$")
    public void iFindTranslator(String from, String to) throws Exception {
        Class fromClass;
        Class toClass;
        try {
            if (!from.equals("") && !to.equals("")) {
                fromClass = Class.forName(from);
                toClass = Class.forName(to);
            } else {
                fromClass = null;
                toClass = null;
            }
            Translator translator = Translator.getTranslatorFor(exampleTranslator.getClass(fromClass), exampleTranslator.getClass(toClass));
            stepData.put("Translator", translator);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^Translator \"([^\"]*)\" is found$")
    public void translatorIsFound(String translatorName) {
        Translator translator = (Translator) stepData.get("Translator");
        assertEquals(translatorName, translator.getClass().getSimpleName());
    }

    @Given("^I create mqtt message with (?:valid|invalid|empty) payload \"([^\"]*)\" and (?:valid|invalid) topic \"([^\"]*)\"$")
    public void creatingMqttMessage(String payload, String topic) throws Exception{
        try {
            Date date = new Date();
            MqttTopic mqttTopic = new MqttTopic(topic);

            KuraPayload kuraPayload = new KuraPayload();
            if (payload.equals("invalidPayload") || payload.equals("")) {
                kuraPayload.setBody(payload.getBytes());
            } else {
                kuraPayload.getMetrics().put(payload, 200);
            }

            MqttPayload mqttPayload = new MqttPayload(kuraPayload.toByteArray());
            MqttMessage mqttMessage = new MqttMessage(mqttTopic, date, mqttPayload);

            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }

    @When("^I try to translate mqtt response$")
    public void iTryToTranslateMqttResponse() throws Exception {
        MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");

        try {
            KuraResponseMessage kuraResponseMessage = translatorResponseMqttKura.translate(mqttMessage);
            stepData.put("KuraResponseMessage", kuraResponseMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I got kura response message with \"([^\"]*)\" payload body$")
    public void kuraResponseMessageWithPayloadBody(String payloadType) {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");

        assertTrue(kuraResponseMessage.getPayload().getBody().getClass().getSimpleName().equals(payloadType));
    }

    @Then("^I got kura response message with proper payload metrics$")
    public void kuraResponseMessageWithPayloadAndChannelAndData() {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");

        assertTrue(kuraResponseMessage.getPayload().getMetrics() != null);
    }

    @Given("^I create kura data message with channel with scope \"([^\"]*)\", client id \"([^\"]*)\" and payload without body and metrics$")
    public void iCreateKuraDataMessage(String scope, String clientId) throws Exception {
        try {
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            Date date = new Date();
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I try to translate kura data message to mqtt message$")
    public void iTryToTranslateKuraDataMessageToMqttMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            MqttMessage mqttMessage = translatorDataKuraMqtt.translate(kuraDataMessage);
            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }

    }

    @Then("^I get mqtt message with channel with scope \"([^\"]*)\", client id \"([^\"]*)\" and (?:empty body|non empty body)$")
    public void mqttMessageWithChanneScopeClienIDandBody(String scope, String clientId) {
        MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");

        String requestTopic = scope.concat("/" + clientId);
        assertEquals(requestTopic, mqttMessage.getRequestTopic().getTopic());

        if (mqttMessage.getPayload().getBody().length == 0) {
            assertTrue(mqttMessage.getPayload().getBody().length == 0);
        } else {
            assertTrue(mqttMessage.getPayload().getBody().length != 0);
        }
    }

    @And("^I got kura response message channel with \"(.+)\", \"(.+)\", \"(.+)\", \"(.+)\", \"(.+)\" and \"(.+)\" data$")
    public void kuraResponseMessageWithChannelAndData(String replyPart, String requestId, String appId, String messageClassification, String scope, String clientId) {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");

        assertTrue(kuraResponseMessage.getChannel().getReplyPart().equals(replyPart));
        assertTrue(kuraResponseMessage.getChannel().getRequestId().equals(requestId));
        assertTrue(kuraResponseMessage.getChannel().getAppId().equals(appId));
        assertTrue(kuraResponseMessage.getChannel().getMessageClassification().equals(messageClassification));
        assertTrue(kuraResponseMessage.getChannel().getScope().equals(scope));
        assertTrue(kuraResponseMessage.getChannel().getClientId().equals(clientId));
    }

    @Given("^I create kura data message with channel with scope \"([^\"]*)\", client id \"([^\"]*)\", valid payload and metrics but without body$")
    public void kuraDataMessageWithoutBodyAndMetrics(String scope, String clientId) throws Exception {
        try {
            Date date = new Date();
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            kuraDataPayload.getMetrics().put("response.code", 200);
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("^I create kura data message with channel with scope \"([^\"]*)\", client id \"([^\"]*)\" and payload with body and metrics$")
    public void fullKuraDataMessage(String scope, String clientId) throws Exception {
        try {
            Date date = new Date();
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            kuraDataPayload.setBody("Payload".getBytes());
            kuraDataPayload.getMetrics().put("response.code", 200);
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("^I try to translate mqtt message to kura data message$")
    public void iTryToTranslateMqttMessageToKuraMessage() throws Exception {
        try {
            MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
            KuraDataMessage kuraDataMessage = translatorDataMqttKura.translate(mqttMessage);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^I got kura data message with \"([^\"]*)\" payload body$")
    public void iGotKuraDataMessageWithPayloadBody(String payloadType) throws Throwable {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");

        assertTrue(kuraDataMessage.getPayload().getBody().getClass().getSimpleName().equals(payloadType));
    }

    @And("^I got kura data message channel with \"(.+)\" and \"(.+)\" data$")
    public void iGotKuraDataMessageChannelWithAndData(String scope, String clientId) {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");

        assertTrue(kuraDataMessage.getChannel().getScope().equals(scope));
        assertTrue(kuraDataMessage.getChannel().getClientId().equals(clientId));
    }

    @Then("^I got kura data message with proper payload metrics response code (\\d+)$")
    public void iGotKuraDataMessageWithProperPayloadMetrics(int responseCode) {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");

        assertEquals(kuraDataMessage.getPayload().getMetrics().get("response.code"), responseCode);
    }

    @Then("^I got kura data message with empty payload$")
    public void iGotKuraDataMessageWithEmptyPayload() {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");

        assertEquals(null, kuraDataMessage.getPayload().getBody());
    }

    @Given("^I create jms message with (?:valid|invalid|empty) payload \"([^\"]*)\" and (?:valid|invalid) topic \"([^\"]*)\"$")
    public void iCreateJmsMessageWithInvalidPayloadAndInvalidTopic(String payload, String topic) throws Exception {
        try {
            Date date = new Date();
            JmsTopic jmsTopic = new JmsTopic(topic);

            KuraPayload kuraPayload = new KuraPayload();
            if (payload.equals("invalidPayload") || payload.equals("")) {
                kuraPayload.setBody(payload.getBytes());
            } else {
                kuraPayload.getMetrics().put(payload, 200);
            }

            JmsPayload jmsPayload = new JmsPayload(kuraPayload.toByteArray());
            JmsMessage jmsMessage = new JmsMessage(jmsTopic, date, jmsPayload);

            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I try to translate jms message to kura data message$")
    public void iTryToTranslateJmsMessageToKuraMessage() throws Exception {
        JmsMessage jmsMessage = (JmsMessage) stepData.get("JmsMessage");

        try {
            KuraDataMessage kuraDataMessage = translatorDataJmsKura.translate(jmsMessage);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to translate kura data message to jms message$")
    public void iTryToTranslateKuraDataMessageToJmsMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            JmsMessage jmsMessage = translatorDataKuraJms.translate(kuraDataMessage);
            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^I got kura data message channel with \"([^\"]*)\" scope, \"([^\"]*)\" client id and proper semanticPart$")
    public void iCreateJmsMessageWithInvalidPayloadAndTopic(String scope, String clientId, List<String> semanticParts) {

        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");

        assertEquals(scope, kuraDataMessage.getChannel().getScope());
        assertEquals(clientId, kuraDataMessage.getChannel().getClientId());

        for (String semanticPart : semanticParts) {
            assertTrue(kuraDataMessage.getChannel().getSemanticParts().contains(semanticPart));
        }
    }

    @Then("^I got jms message with topic \"([^\"]*)\" and (?:empty body|non empty body)$")
    public void iGotJmsMessageWithTopicAndEmptyPayload(JmsTopic topic) {
        JmsMessage jmsMessage = (JmsMessage) stepData.get("JmsMessage");
        assertEquals(topic.getTopic(), jmsMessage.getTopic().getTopic());

        if (jmsMessage.getPayload().getBody().length == 0) {
            assertTrue(jmsMessage.getPayload().getBody().length == 0);
        } else {
            assertTrue(jmsMessage.getPayload().getBody().length != 0);
        }
    }

    @When("^I try to translate mqtt null message to kura data message$")
    public void iTryToTranslateMqttNullMessageToKuraDataMessage() throws Exception {
        try {
            MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
            KuraDataMessage kuraDataMessage = translatorDataMqttKura.translate((MqttMessage) null);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }

    @Given("^I create kura data message with channel with scope \"([^\"]*)\", client id \"([^\"]*)\" and null payload$")
    public void iCreateKuraDataMessageWithChannelWithScopeClientIdAndNullPayload(String scope, String clientId) {
        KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
        Date date = new Date();
        KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, null);
        stepData.put("KuraDataMessage", kuraDataMessage);
    }

    @Given("^I create kura data message with null channel and payload without body and with metrics$")
    public void iCreateKuraDataMessageWithNullChannelAndPayloadWithoutBodyAndWithMetrics() {
        Date date = new Date();
        KuraDataPayload kuraDataPayload = new KuraDataPayload();
        kuraDataPayload.getMetrics().put("response.code", 200);
        KuraDataMessage kuraDataMessage = new KuraDataMessage(null, date, kuraDataPayload);

        stepData.put("KuraDataMessage", kuraDataMessage);
    }

    @And("^I try to translate invalid kura data message to mqtt message$")
    public void iTryToTranslateInvalidKuraDataMessageToMqttMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            MqttMessage mqttMessage = translatorDataKuraMqtt.translate((KuraDataMessage) null);
            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("^I try to translate invalid jms message to kura data message$")
    public void iTryToTranslateInvalidJmsMessageToKuraDataMessage() throws Exception{
        try {
            KuraDataMessage kuraDataMessage = translatorDataJmsKura.translate((JmsMessage) null);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to translate invalid kura data message to jms message$")
    public void iTryToTranslateInvalidKuraDataMessageToJmsMessage() throws Exception {
        try {
            JmsMessage jmsMessage = translatorDataKuraJms.translate((KuraDataMessage) null);
            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }
}
