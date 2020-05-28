/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.steps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import org.junit.Assert;

import com.google.inject.Singleton;

// Implementation of Gherkin steps used to test miscellaneous Shiro
// authorization functionality.

@Singleton
public class AuthenticationServiceSteps extends TestBase {

    protected KapuaLocator locator;

    private static final String LAST_ACCOUNT = "LastAccount";

    private CredentialService credentialService;
    private CredentialFactory credentialFactory;
    private UserService userService;
    private UserFactory userFactory;

    @Inject
    public AuthenticationServiceSteps(StepData stepData) {
        super(stepData);
    }

    @Before(value="@env_docker or @env_embedded_minimal or @env_none", order=10)
    public void beforeScenarioNone(Scenario scenario) throws KapuaException {
        updateScenario(scenario);
    }

    @After(value="@setup")
    public void setServices() {
        locator = KapuaLocator.getInstance();
        credentialService = locator.getService(CredentialService.class);
        credentialFactory = locator.getFactory(CredentialFactory.class);
        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);
    }

    @When("I create default test-user")
    public void createDefualtUser() throws KapuaException {
        UserCreator userCreator = userFactory.newCreator(KapuaId.ONE, "test-user");
        User user = userService.create(userCreator);
        stepData.put("User", user);
    }

    @When("^I configure the credential service$")
    public void setConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId scopeId;
        KapuaId parentScopeId;
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);

        if (tmpAccount != null) {
            scopeId = tmpAccount.getId();
            parentScopeId = tmpAccount.getScopeId();
        } else {
            scopeId = SYS_SCOPE_ID;
            parentScopeId = SYS_SCOPE_ID;
        }

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getScopeId() != null) {
                scopeId = getKapuaId(config.getScopeId());
            }
            if (config.getParentId() != null) {
                parentScopeId = getKapuaId(config.getParentId());
            }
        }
        try {
            primeException();
            credentialService.setConfigValues(scopeId, parentScopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I read credential service configuration$")
    public void getConfigurationValue()
            throws Exception {

        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);

        if (tmpAccount != null) {
            scopeId = tmpAccount.getId();
        } else {
            scopeId = SYS_SCOPE_ID;
        }

        try {
            primeException();
            Map<String, Object> configValues = credentialService.getConfigValues(scopeId);
            stepData.put("configValues", configValues);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I check that service configuration \"(.*)\" matches value \"(.*)\"")
    public void checkConfigValue(String key, String value) {
        Map<String, Object> configValues = (Map<String, Object>)stepData.get("configValues");
        Object configValue = configValues.get(key);
        Assert.assertEquals(value, configValue);
    }

    @When("I check that service configuration \"(.*)\" has no value")
    public void checkNullConfigValue(String key) {
        Map<String, Object> configValues = (Map<String, Object>)stepData.get("configValues");
        Object configValue = configValues.get(key);
        Assert.assertEquals(configValue, null);
    }

    @When("^I configure the credential service for the account with the id (\\d+)$")
    public void setCredentialServiceConfig(int accountId, List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getKapuaId(accountId);

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
        }

        primeException();
        try {
            credentialService.setConfigValues(accId, SYS_SCOPE_ID, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create a new PASSWORD credential for the default user with password \"(.+)\"$")
    public void createPasswordCredential(String password)
            throws Exception {
        primeException();

        User user = (User) stepData.get("User");
        CredentialCreator credentialCreator = credentialFactory.newCreator(user.getScopeId(), user.getId(), CredentialType.PASSWORD, password, CredentialStatus.ENABLED, null);
        try {
            credentialService.create(credentialCreator);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

}
