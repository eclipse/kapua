/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.GroupAttributes;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of Gherkin steps used in GroupService.feature scenarios.
 */
@ScenarioScoped
public class GroupServiceTestSteps extends AbstractAuthorizationServiceTest {

    // Test data scratchpads
    CommonTestData commonData;
    GroupServiceTestData groupData;

    // Various domain related service references
    GroupService groupService;
    GroupFactory groupFactory;

    // Currently executing scenario.
    Scenario scenario;

    @Inject
    public GroupServiceTestSteps(GroupServiceTestData groupData, CommonTestData commonData) {
        this.groupData = groupData;
        this.commonData = commonData;
    }

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        this.scenario = scenario;

        // Instantiate all the services and factories that are required by the tests
        groupService = new GroupServiceImpl();
        groupFactory = new GroupFactoryImpl();

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        // Clean up the test data scratchpads
        commonData.clearData();
        commonData.scenario = this.scenario;
        groupData.clearData();

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, new KapuaEid(BigInteger.ONE), new KapuaEid(BigInteger.ONE));
        KapuaSecurityUtils.setSession(kapuaSession);

        // Setup JAXB context
        XmlUtil.setContextProvider(new ShiroJAXBContextProvider());
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************
    @When("^I configure$")
    public void setConfigurationValue(List<CucConfig> cucConfigs)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            Map<String, Object> valueMap = new HashMap<>();
            KapuaEid scopeId = null;
            KapuaEid parentScopeId = null;

            for (CucConfig config : cucConfigs) {
                config.addConfigToMap(valueMap);
                scopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getScopeId())));
                parentScopeId = new KapuaEid(BigInteger.valueOf(Long.valueOf(config.getParentScopeId())));
            }
            try {
                commonData.primeException();
                groupService.setConfigValues(scopeId, parentScopeId, valueMap);
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }

            return null;
        });
    }

    @When("^I count the group entries in the database$")
    public void countDomainEntries()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = groupService.count(groupFactory.newQuery(ROOT_SCOPE_ID));
            return null;
        });
    }

    @Given("^I create the group(?:|s)$")
    public void createAListOfDomains(List<CucGroup> groups)
            throws Exception {
        for (CucGroup tmpGrp : groups) {
            commonData.primeException();
            tmpGrp.doParse();
            groupData.groupCreator = groupFactory.newCreator(tmpGrp.getScopeId(), tmpGrp.getName());

            KapuaSecurityUtils.doPrivileged(() -> {
                try {
                    groupData.group = groupService.create(groupData.groupCreator);
                } catch (KapuaException ex) {
                    commonData.verifyException(ex);
                    return null;
                }
                assertNotNull(groupData.group);
                assertNotNull(groupData.group.getId());
                groupData.groupId = groupData.group.getId();
                return null;
            });
            if (commonData.exceptionCaught) {
                break;
            }
        }
    }

    @When("^I update the group name to \"(.+)\"$")
    public void updateLastGroupName(String name)
            throws KapuaException {

        groupData.group.setName(name);

        KapuaSecurityUtils.doPrivileged(() -> {
            // Sleep for a bit to make sure the time stamps are really different!
            Thread.sleep(50);
            groupData.groupSecond = groupService.update(groupData.group);
            assertNotNull(groupData.groupSecond);
            return null;
        });
    }

    @When("^I update the group with an incorrect ID$")
    public void updateGroupWithFalseId()
            throws Exception {

        groupData.group.setId(generateId());
        commonData.primeException();

        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                groupService.update(groupData.group);
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }
            return null;
        });
    }

    @When("^I delete the last created group$")
    public void deleteLastCreatedGroup()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            groupService.delete(groupData.group.getScopeId(), groupData.group.getId());
            return null;
        });
    }

    @When("^I try to delete a random group id$")
    public void deleteGroupWithRandomId()
            throws Exception {

        commonData.primeException();
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                groupService.delete(ROOT_SCOPE_ID, generateId());
            } catch (KapuaException ex) {
                commonData.verifyException(ex);
            }
            return null;
        });
    }

    @When("^I search for the last created group$")
    public void findDomainByRememberedId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            groupData.groupSecond = groupService.find(groupData.group.getScopeId(), groupData.group.getId());
            return null;
        });
    }

    @When("^I count all the groups in scope (\\d+)$")
    public void countGroupsInScope(int scope)
            throws KapuaException {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);

        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = groupService.count(tmpQuery);
            return null;
        });
    }

    @When("^I query for the group \"(.+)\" in scope (\\d+)$")
    public void queryForGroup(String name, int scope)
            throws KapuaException {
        KapuaId tmpId = new KapuaEid(BigInteger.valueOf(scope));
        GroupQuery tmpQuery = groupFactory.newQuery(tmpId);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(GroupAttributes.NAME, name));

        KapuaSecurityUtils.doPrivileged(() -> {
            groupData.groupList = groupService.query(tmpQuery);
            return null;
        });

        assertNotNull(groupData.groupList);
        commonData.count = groupData.groupList.getSize();
        if (groupData.groupList.getSize() > 0) {
            groupData.group = groupData.groupList.getFirstItem();
        }
    }

    @Then("^A group was created$")
    public void checkGroupNotNull() {
        assertNotNull(groupData.group);
    }

    @Then("^No group was created$")
    public void checkGroupIsNull() {
        assertNull(groupData.group);
    }

    @Then("^No group was found$")
    public void checkNoGroupWasFound() {
        assertNull(groupData.groupSecond);
    }

    @Then("^The group name is \"(.+)\"$")
    public void checkGroupName(String name) {
        assertEquals(groupData.group.getName(), name.trim());
    }

    @Then("^The group matches the creator$")
    public void checkGroupAgainstCreator() {
        assertNotNull(groupData.group);
        assertNotNull(groupData.group.getId());
        assertNotNull(groupData.groupCreator);
        assertEquals(groupData.groupCreator.getScopeId(), groupData.group.getScopeId());
        assertEquals(groupData.groupCreator.getName(), groupData.group.getName());
        assertNotNull(groupData.group.getCreatedBy());
        assertNotNull(groupData.group.getCreatedOn());
        assertNotNull(groupData.group.getModifiedBy());
        assertNotNull(groupData.group.getModifiedOn());
    }

    @Then("^The group was correctly updated$")
    public void checkUpdatedGroup() {
        assertNotNull(groupData.groupSecond);
        assertNotNull(groupData.groupSecond.getId());
        assertEquals(groupData.group.getScopeId(), groupData.groupSecond.getScopeId());
        assertEquals(groupData.group.getName(), groupData.groupSecond.getName());
        assertEquals(groupData.group.getCreatedBy(), groupData.groupSecond.getCreatedBy());
        assertEquals(groupData.group.getCreatedOn(), groupData.groupSecond.getCreatedOn());
        assertEquals(groupData.group.getModifiedBy(), groupData.groupSecond.getModifiedBy());
        assertNotEquals(groupData.group.getModifiedOn(), groupData.groupSecond.getModifiedOn());
    }

    @Then("^The group was correctly found$")
    public void checkFoundGroup() {
        assertNotNull(groupData.groupSecond);
        assertNotNull(groupData.groupSecond.getId());
        assertEquals(groupData.group.getScopeId(), groupData.groupSecond.getScopeId());
        assertEquals(groupData.group.getName(), groupData.groupSecond.getName());
        assertEquals(groupData.group.getCreatedBy(), groupData.groupSecond.getCreatedBy());
        assertEquals(groupData.group.getCreatedOn(), groupData.groupSecond.getCreatedOn());
        assertEquals(groupData.group.getModifiedBy(), groupData.groupSecond.getModifiedBy());
        assertEquals(groupData.group.getModifiedOn(), groupData.groupSecond.getModifiedOn());
    }
}
