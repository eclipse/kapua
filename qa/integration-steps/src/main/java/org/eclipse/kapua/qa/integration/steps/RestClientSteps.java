/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.integration.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserListResult;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@ScenarioScoped
public class RestClientSteps extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(RestClientSteps.class);

    /**
     * Scenario scoped step data.
     */
    private StepData stepData;

    @Before
    public void setupJaxb() {
        XmlUtil.setContextProvider(new RestJAXBContextProvider());
    }

    @Inject
    public RestClientSteps(StepData stepData) {
        this.stepData = stepData;
    }

    @Given("^Server with host \"(.+)\" on port \"(.+)\"$")
    public void setHostPort(String host, String port) {
        stepData.put("host", host);
        stepData.put("port", port);
    }

    @When("^REST GET call at \"(.*)\"")
    public void restGetCall(String resource) throws Exception {

        String host = (String) stepData.get("host");
        String port = (String) stepData.get("port");
        String tokenId = (String) stepData.get("tokenId");
        resource = insertStepData(resource);
        URL url = new URL("http://" + host + ":" + port + resource);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (AutoCloseable cconn = () -> conn.disconnect()) {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Language", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if (tokenId != null) {
                conn.setRequestProperty("Authorization", "Bearer " + tokenId);
            }
            int httpRespCode = conn.getResponseCode();
            if (httpRespCode == 200) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                }
                stepData.put("restResponse", sb.toString());
            }
            stepData.put("restResponseCode", httpRespCode);
        } catch ( IOException ioe) {
            logger.error("Exception on REST GET call execution: " + resource);
            throw ioe;
        }
    }

    @When("^REST POST call at \"(.*)\" with JSON \"(.*)\"")
    public void restPostCallWithJson(String resource, String json) throws Exception {

        String host = (String) stepData.get("host");
        String port = (String) stepData.get("port");
        String tokenId = (String) stepData.get("tokenId");
        resource = insertStepData(resource);
        json = insertStepData(json);
        URL url = new URL("http://" + host + ":" + port + resource);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        try (AutoCloseable cconn = () -> conn.disconnect()) {
            conn.setRequestProperty("Accept-Language", "UTF-8");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "application/json");
            if (tokenId != null) {
                conn.setRequestProperty("Authorization", "Bearer " + tokenId);
            }
            conn.setDoOutput(true);
            try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream())) {
                outputStreamWriter.write(json);
                outputStreamWriter.flush();
            }
            int httpRespCode = conn.getResponseCode();
            if (httpRespCode == 200) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())))) {
                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }
                }
                stepData.put("restResponse", sb.toString());
            }
            stepData.put("restResponseCode", httpRespCode);
        } catch (IOException ioe) {
            logger.error("Exception on REST POST call execution: " + resource);
            throw ioe;
        }
    }

    @Then("^REST response containing text \"(.*)\"$")
    public void restResponseContaining(String checkStr) throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        assertTrue(String.format("Response %s doesn't include %s.", restResponse, checkStr),
                restResponse.contains(checkStr));
    }

    @Then("^REST response containing Account$")
    public void restResponseContainingAccount() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        Account account = XmlUtil.unmarshalJson(restResponse, Account.class, null);
        KapuaId accId = account.getId();
        System.out.println("Account Id = " + accId);
        stepData.put("lastAccountId", accId.toStringId());
        stepData.put("lastAccountCompactId", accId.toCompactId());
    }

    @Then("^REST response containing \"(.*)\" with prefix account \"(.*)\"$")
    public void restResponseContainingPrefixVar(String checkStr, String var) {

        String restResponse = (String) stepData.get("restResponse");
        Account account = (Account) stepData.get(var);
        assertTrue(String.format("Response %s doesn't include %s.", restResponse, account.getId() + checkStr),
                restResponse.contains(account.getId() + checkStr));
    }

    @Then("^REST response containing AccessToken$")
    public void restResponseContainingAccessToken() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        XmlUtil.setContextProvider(new RestJAXBContextProvider());
        AccessToken token = XmlUtil.unmarshalJson(restResponse, AccessToken.class, null);
        assertTrue("Token is null.", token.getTokenId() != null);
        stepData.put("tokenId", token.getTokenId());
    }

    @Then("^REST response containing User")
    public void restResponseContainingUser() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        User user = XmlUtil.unmarshalJson(restResponse, User.class, null);
        stepData.put("lastUserCompactId", user.getId().toCompactId());
    }

    @Then("^REST response contains list of Users")
    public void restResponseContainsUsers() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        UserListResult userList = XmlUtil.unmarshalJson(restResponse, UserListResult.class, null);
        Assert.assertFalse("Retrieved user list should NOT be empty.", userList.isEmpty());
    }

    @Then("^REST response doesn't contain User")
    public void restResponseDoesntContainUser() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        User user = XmlUtil.unmarshalJson(restResponse, User.class, null);
        Assert.assertTrue("There should be NO User retrieved.", user == null);
    }

    @Then("^REST response code is (\\d+)$")
    public void restResponseDoesntContainUser(int expeted) throws Exception {

        int restResponseCode = (Integer) stepData.get("restResponseCode");
        Assert.assertEquals("Wrong response code.", expeted, restResponseCode);
    }

    /**
     * Take input parameter and replace its $var$ with value of var that is stored
     * in step data.
     *
     * @param template string that gets parameters replaced with value
     * @return string with inserted parameter values
     */
    private String insertStepData(String template) {
        List<String> keys = stepData.getKeys();
        for (String key: keys) {
            Object oValue = stepData.get(key);
            if (oValue instanceof String) {
                String value = (String) oValue;
                template = template.replace("$" + key + "$", value);
            }
        }

        return template;
    }

    // TODO move this step in common steps
    @Given("^Move step data \"(.*)\" to \"(.*)\"$")
    public void moveStepData(String keyFrom, String keyTo) {

        Object valueFrom = stepData.get(keyFrom);
        stepData.put(keyTo, valueFrom);
    }

    // TODO move this step in common steps
    @Given("^Move Account compact id from step data \"(.*)\" to \"(.*)\"$")
    public void moveAccountCompactIdStepData(String keyFrom, String keyTo) {

        Account account = (Account) stepData.get(keyFrom);
        stepData.put(keyTo, account.getId().toCompactId());
    }

    // TODO move this step in common steps
    @Given("^Clear step data with key \"(.*)\"$")
    public void clearStepData(String key) {

        stepData.remove(key);
    }
}
