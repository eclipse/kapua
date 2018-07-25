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

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.service.StepData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.junit.Assert;

@ScenarioScoped
public class RestClientSteps extends Assert {

    private static final Logger logger = LoggerFactory.getLogger(RestClientSteps.class);

    /**
     * Scenario scoped step data.
     */
    private StepData stepData;

    @Inject
    public RestClientSteps(StepData stepData) {
        this.stepData = stepData;
    }

    @Given("^Server with host \"(.+)\" on port \"(.+)\"$")
    public void setHostPort(String host, String port) {
        stepData.put("host", host);
        stepData.put("port", port);
    }

    @When("^REST call at \"(.*)\" and \"(.*)\"")
    public void restCallStatusOfIndex(String resource, String resourceApndx) {

        String host = (String) stepData.get("host");
        String port = (String) stepData.get("port");
        try {
            URL url = new URL("http://" + host + ":" + port + resource + resourceApndx);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
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

    @Then("^REST response containing \"(.*)\"")
    public void restResponseStatusOfIndex(String checkStr) {

        String restResponse = (String) stepData.get("restResponse");
        assertTrue(String.format("Response %s doesn't include %s.", restResponse, checkStr),
                restResponse.contains(checkStr));
    }
}
