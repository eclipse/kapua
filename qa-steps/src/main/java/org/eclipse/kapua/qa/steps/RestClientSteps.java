/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.steps;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.service.RestJAXBContextProvider;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.token.AccessToken;
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
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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
        try {
            URL url = new URL("http://" + host + ":" + port + resource);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Language", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            if (tokenId != null) {
                conn.setRequestProperty("Authorization", "Bearer " + tokenId);
            }
            assertFalse("Wrong response.", conn.getResponseCode() != 200);
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            stepData.put("restResponse", sb.toString());
        } catch ( IOException ioe) {
            logger.error("Exception on REST call execution: " + resource);
            throw ioe;
        }
    }

    @When("^REST POST call at \"(.*)\" with JSON \"(.*)\"")
    public void restPostCallWithJson(String resource, String json) {

        String host = (String) stepData.get("host");
        String port = (String) stepData.get("port");
        try {
            URL url = new URL("http://" + host + ":" + port + resource);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept-Language", "UTF-8");
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(json);
            outputStreamWriter.flush();

            assertFalse("Wrong response.", conn.getResponseCode() != 200);
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String output;
            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            conn.disconnect();
            stepData.put("restResponse", sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Then("^REST response containing text \"(.*)\"$")
    public void restResponseContaining(String checkStr) throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        assertTrue(String.format("Response %s doesn't include %s.", restResponse, checkStr),
                restResponse.contains(checkStr));
    }

    @Then("^REST response containing \"(.*)\" with prefix account \"(.*)\"")
    public void restResponseContainingPrefixVar(String checkStr, String var) {

        String restResponse = (String) stepData.get("restResponse");
        Account account = (Account) stepData.get(var);
        assertTrue(String.format("Response %s doesn't include %s.", restResponse, account.getId() + checkStr),
                restResponse.contains(account.getId() + checkStr));
    }

    @Then("^REST response containing AccessToken$")
    public void restResponseContainingAccessToken() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        AccessToken token = XmlUtil.unmarshalJson(restResponse, AccessToken.class, null);
        assertTrue("Token is null.", token.getTokenId() != null);
        stepData.put("tokenId", token.getTokenId());
    }

    @Then("^REST response containing Users")
    public void restResponseContainingUsers() throws Exception {

        String restResponse = (String) stepData.get("restResponse");
        UserListResult users = XmlUtil.unmarshalJson(restResponse, UserListResult.class, null);
        assertFalse("Users list is empty.", users.isEmpty());
    }

}
