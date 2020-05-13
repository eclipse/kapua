/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.sso.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.shiro.realm.JwtAuthenticatingRealm;
import org.eclipse.kapua.service.authentication.shiro.registration.RegistrationServiceImpl;
import org.eclipse.kapua.sso.provider.ProviderSingleSignOnLocator;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.jose4j.json.internal.json_simple.parser.JSONParser;
import org.jose4j.json.internal.json_simple.parser.ParseException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@ScenarioScoped
public class SsoServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(SsoServiceSteps.class);
    private static final String KEYCLOAK_TOKEN_REST_URI = "http://localhost:9090/auth/realms/kapua/protocol/openid-connect/token";
    private static JwtAuthenticatingRealm jwtAuthenticatingRealm;
    public static SecurityManager securityManager;
    private CredentialsFactory credentialsFactory;
    private AuthenticationService authenticationService;

    // Default constructor
    @Inject
    public SsoServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
    }

    // ************************************************************************************
    // * Setup and tear-down steps                                                        *
    // ************************************************************************************

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        credentialsFactory = locator.getFactory(CredentialsFactory.class);
        authenticationService = locator.getService(AuthenticationService.class);

        // Setup JAXB context
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
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
    // * Definition of Cucumber scenario steps                                            *
    // ************************************************************************************

    @And("^Set environment variables for keycloak provider$")
    public void setEnvVariablesForKeycloakProvider() throws UnknownHostException {
        InetAddress address = InetAddress.getLocalHost();
//        System.setProperty("sso.keycloak.uri", address.getHostAddress() + ":9090");
        System.setProperty("sso.keycloak.uri", "http://localhost:9090");
        System.setProperty("site.home.uri", "http://localhost:8080");
        System.setProperty("sso.keycloak.realm", "kapua");
        System.setProperty("sso.openid.client.id", "console");
        System.setProperty("sso.provider", "keycloak");
        System.setProperty("authentication.registration.service.enabled", "true");
    }

    @And("^Set environment variables for generic provider$")
    public void setEnvVariablesForGenericProvider() {
        System.setProperty("sso.generic.openid.server.endpoint.auth", "http://localhost:9090/auth/realms/master/protocol/openid-connect/auth");
        System.setProperty("sso.generic.openid.server.endpoint.token", "http://localhost:9090/auth/realms/master/protocol/openid-connect/token");
        System.setProperty("sso.openid.client.id", "console");
        System.setProperty("site.home.uri", "http://localhost:8080");
        System.setProperty("sso.provider", "generic");
    }

    @Given("^Configure the SSO service$")
    public void configureTheSso() throws Exception {
        primeException();
        try {
            JwtCredentials credentials = (JwtCredentials) stepData.get("jwtCredentials");
            RegistrationServiceImpl registrationService = new RegistrationServiceImpl();
            registrationService.createAccount(credentials);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("^Configure JWT authenticating realm$")
    public void configureJwtAuthenticatingRealm() throws Exception {
        primeException();
    }

    @Given("^Check is SSO enabled$")
    public void checkIsSsoEnabled() {
        primeException();
        try {
            ProviderSingleSignOnLocator providerSingleSignOnLocator = new ProviderSingleSignOnLocator();
            boolean isEnabled = providerSingleSignOnLocator.getService().isEnabled();
            Assert.assertTrue(isEnabled);
        } catch (AssertionError ae) {
            verifyAssertionError(ae);
        }
    }

    @Given("^Get access token for user with username \"(.*)\" and password \"(.*)\"$")
    public void getSsoLoginURL(String username, String password) throws Exception {
        primeException();
        try {
            JSONObject response = getKeycloakResponse(username, password);
            String accessToken = getKeycloakAccessToken(response);
            stepData.put("accessToken", accessToken);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^Create a jwt credential using the access token$")
    public void createJwtCredential() throws KapuaException {
        String accessToken = (String) stepData.get("accessToken");
        JwtCredentials jwtCredentials = credentialsFactory.newJwtCredentials(accessToken, KapuaId.ONE.toStringId());
        stepData.put("jwtCredentials", jwtCredentials);
    }



    private JSONObject getKeycloakResponse(String username, String password) throws IOException, ParseException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(KEYCLOAK_TOKEN_REST_URI);
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("client_id", "console"));
        nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("password", password));
        UrlEncodedFormEntity formEntity;
        formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
        httpPost.setEntity(formEntity);
        HttpResponse response = httpClient.execute(httpPost);
        String content = EntityUtils.toString(response.getEntity());
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse = (JSONObject) parser.parse(content);
        return jsonResponse;
    }

    private String getKeycloakAccessToken(JSONObject response) {
        return response.get("access_token").toString();
    }
}
