/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.steps;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.authorization.AuthorizationJAXBContextProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupAttributes;
import org.eclipse.kapua.service.authorization.group.GroupCreator;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Gherkin steps used in GroupService.feature scenarios.
 *
 */
@ScenarioScoped
public class GroupServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceSteps.class);

    // Various domain related service references
    private GroupService groupService ;
    private GroupFactory groupFactory ;

    @Inject
    public GroupServiceSteps(StepData stepData, DBHelper dbHelper) {

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

                // Inject actual account related services
                bind(AuthorizationEntityManagerFactory.class).toInstance(AuthorizationEntityManagerFactory.getInstance());
                bind(GroupService.class).toInstance(new GroupServiceImpl());
                bind(GroupFactory.class).toInstance(new GroupFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }


    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        groupService = locator.getService(GroupService.class);
        groupFactory = locator.getFactory(GroupFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        XmlUtil.setContextProvider(new AuthorizationJAXBContextProvider());
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************
    @When("^I configure the group service$")
    public void setConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId scopeId;
        KapuaId parentScopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

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
            groupService.setConfigValues(scopeId, parentScopeId, valueMap);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the group entries in the database$")
    public void countDomainEntries()
            throws Exception {

        stepData.remove("Count");
        primeException();
        try {
            long count = groupService.count(groupFactory.newQuery(SYS_SCOPE_ID));
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create the group(?:|s)$")
    public void createAListOfDomains(List<CucGroup> groups)
            throws Exception {

        Group group = null;
        GroupCreator groupCreator = null;
        stepData.remove("GroupCreator");
        stepData.remove("Group");
        stepData.remove("GroupId");

        primeException();
        for (CucGroup tmpGrp : groups) {
            tmpGrp.doParse();
            groupCreator = groupFactory.newCreator(tmpGrp.getScopeId(), tmpGrp.getName());

            try {
                group = groupService.create(groupCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }

        stepData.put("GroupCreator", groupCreator);
        stepData.put("Group", group);
        stepData.put("GroupId", group.getId());
    }

    @When("^I update the group name to \"(.+)\"$")
    public void updateLastGroupName(String name)
            throws Exception {

        Group group = (Group) stepData.get("Group");
        group.setName(name);
        // Sleep for a bit to make sure the time stamps are really different!
        Thread.sleep(50);

        try {
            Group groupSecond = groupService.update(group);
            stepData.put("GroupSecond", groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I update the group with an incorrect ID$")
    public void updateGroupWithFalseId()
            throws Exception {

        Group group = (Group) stepData.get("Group");
        group.setId(getKapuaId());

        primeException();
        try {
            groupService.update(group);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created group$")
    public void deleteLastCreatedGroup()
            throws Exception {

        Group group = (Group) stepData.get("Group");

        primeException();
        try {
            groupService.delete(group.getScopeId(), group.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to delete a random group id$")
    public void deleteGroupWithRandomId()
            throws Exception {

        primeException();
        try {
            groupService.delete(SYS_SCOPE_ID, getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last created group$")
    public void findDomainByRememberedId()
            throws Exception {

        Group group = (Group) stepData.get("Group");

        primeException();
        try {
            Group groupSecond = groupService.find(group.getScopeId(), group.getId());
            stepData.put("GroupSecond", groupSecond);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("^I count all the groups in scope (\\d+)$")
    public void countGroupsInScope(int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);

        primeException();
        try {
            long count = groupService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the group \"(.+)\" in scope (\\d+)$")
    public void queryForGroup(String name, int scope)
            throws Exception {

        KapuaId tmpId = getKapuaId(scope);
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);
        tmpQuery.setPredicate(new AttributePredicateImpl<>(GroupAttributes.NAME, name));

        stepData.remove("GroupList");
        stepData.remove("Group");
        stepData.remove("Count");

        primeException();
        try {
            GroupListResult groupList = groupService.query(tmpQuery);
            stepData.put("GroupList", groupList);
            stepData.put("Group", groupList.getFirstItem());
            stepData.put("Count", groupList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^A group was created$")
    public void checkGroupNotNull() {

        assertNotNull(stepData.get("Group"));
    }

    @Then("^No group was created$")
    public void checkGroupIsNull() {

        assertNull(stepData.get("Group"));
    }

    @Then("^No group was found$")
    public void checkNoGroupWasFound() {

        assertNull(stepData.get("GroupSecond"));
    }

    @Then("^The group name is \"(.+)\"$")
    public void checkGroupName(String name) {

        Group group = (Group) stepData.get("Group");
        assertEquals(group.getName(), name.trim());
    }

    @Then("^The group matches the creator$")
    public void checkGroupAgainstCreator() {

        Group group = (Group) stepData.get("Group");
        GroupCreator groupCreator = (GroupCreator) stepData.get("GroupCreator");

        assertNotNull(group);
        assertNotNull(group.getId());
        assertNotNull(groupCreator);
        assertEquals(groupCreator.getScopeId(), group.getScopeId());
        assertEquals(groupCreator.getName(), group.getName());
        assertNotNull(group.getCreatedBy());
        assertNotNull(group.getCreatedOn());
        assertNotNull(group.getModifiedBy());
        assertNotNull(group.getModifiedOn());
    }

    @Then("^The group was correctly updated$")
    public void checkUpdatedGroup() {

        Group group = (Group) stepData.get("Group");
        Group groupSecond = (Group) stepData.get("GroupSecond");

        assertNotNull(groupSecond);
        assertNotNull(groupSecond.getId());
        assertEquals(group.getScopeId(), groupSecond.getScopeId());
        assertEquals(group.getName(), groupSecond.getName());
        assertEquals(group.getCreatedBy(), groupSecond.getCreatedBy());
        assertEquals(group.getCreatedOn(), groupSecond.getCreatedOn());
        assertEquals(group.getModifiedBy(), groupSecond.getModifiedBy());
        assertNotEquals(group.getModifiedOn(), groupSecond.getModifiedOn());
    }

    @Then("^The group was correctly found$")
    public void checkFoundGroup() {

        Group group = (Group) stepData.get("Group");
        Group groupSecond = (Group) stepData.get("GroupSecond");

        assertNotNull(groupSecond);
        assertNotNull(groupSecond.getId());
        assertEquals(group.getScopeId(), groupSecond.getScopeId());
        assertEquals(group.getName(), groupSecond.getName());
        assertEquals(group.getCreatedBy(), groupSecond.getCreatedBy());
        assertEquals(group.getCreatedOn(), groupSecond.getCreatedOn());
        assertEquals(group.getModifiedBy(), groupSecond.getModifiedBy());
        assertEquals(group.getModifiedOn(), groupSecond.getModifiedOn());
    }
}
