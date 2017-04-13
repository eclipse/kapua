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
package org.eclipse.kapua.service.authorization.shiro;

import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainServiceImpl;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.group.shiro.GroupFactoryImpl;
import org.eclipse.kapua.service.authorization.group.shiro.GroupServiceImpl;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Implementation of Gherkin steps used in DomainService.feature scenarios.
 *
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Domain Service dependens on.
 *
 */

@ScenarioScoped
public class DomainServiceTestSteps extends AbstractAuthorizationServiceTest {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DomainServiceTestSteps.class);

    // Various domain related service references
    DomainService domainService = null;
    DomainFactory domainFactory = null;
    GroupService groupService = null;
    GroupFactory groupFactory = null;

    // Currently executing scenario.
    Scenario scenario;

    // Test data scratchpads
    CommonTestData commonData = null;
    DomainServiceTestData domainData = null;

    @Inject
    public DomainServiceTestSteps(DomainServiceTestData domainData, CommonTestData commonData) {
        this.domainData = domainData;
        this.commonData = commonData;
    }

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        this.scenario = scenario;

        // Instantiate all the services and factories that are required by the tests
        domainService = new DomainServiceImpl();
        domainFactory = new DomainFactoryImpl();
        groupService = new GroupServiceImpl();
        groupFactory = new GroupFactoryImpl();

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();

        // Clean up the test data scratchpads
        commonData.clearData();
        domainData.clearData();
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Given("^I create the domain(?:|s)$")
    public void createAListOfDomains(List<CucDomain> domains)
            throws KapuaException {
        for (CucDomain tmpDom : domains) {
            commonData.exceptionCaught = false;
            tmpDom.doParse();
            domainData.domainCreator = domainFactory.newCreator(tmpDom.getName(), tmpDom.getServiceName());
            if (tmpDom.getActionSet() != null) {
                domainData.domainCreator.setActions(tmpDom.getActionSet());
            }
            KapuaSecurityUtils.doPrivileged(() -> {
                try {
                    domainData.domain = domainService.create(domainData.domainCreator);
                } catch (KapuaException ex) {
                    commonData.exceptionCaught = true;
                    return null;
                }
                assertNotNull(domainData.domain);
                assertNotNull(domainData.domain.getId());
                domainData.domainId = domainData.domain.getId();
                return null;
            });
            if (commonData.exceptionCaught) {
                break;
            }
        }
    }

    @When("^I search for the last created domain$")
    public void findDomainByRememberedId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domainData.domain = domainService.find(null, domainData.domainId);
            return null;
        });
    }

    @When("^I delete the last created domain$")
    public void deleteLastCreatedDomain()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domainService.delete(null, domainData.domainId);
            return null;
        });
    }

    @When("^I try to delete domain with a random ID$")
    public void deleteRandomDomainId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                commonData.exceptionCaught = false;
                domainService.delete(null, generateId());
            } catch (KapuaException ex) {
                commonData.exceptionCaught = true;
            }
            return null;
        });
    }

    @When("^I count the domain entries in the database$")
    public void countDomainEntries()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            commonData.count = domainService.count(domainFactory.newQuery(null));
            return null;
        });
    }

    @When("^I query for domains with the name \"(.+)\"$")
    public void queryForNamedDomain(String name)
            throws KapuaException {
        DomainQuery query = domainFactory.newQuery(null);
        query.setPredicate(new AttributePredicate<>(DomainPredicates.NAME, name));
        KapuaSecurityUtils.doPrivileged(() -> {
            domainData.domainList = domainService.query(query);
            return null;
        });
        assertNotNull(domainData.domainList);
        commonData.count = domainData.domainList.getSize();
    }

    @When("^I query for domains with the service name \"(.+)\"$")
    public void queryForDomainsWithServiceName(String service_name)
            throws KapuaException {
        DomainQuery query = domainFactory.newQuery(null);
        query.setPredicate(new AttributePredicate<>(DomainPredicates.SERVICE_NAME, service_name));
        KapuaSecurityUtils.doPrivileged(() -> {
            domainData.domainList = domainService.query(query);
            return null;
        });
        assertNotNull(domainData.domainList);
        commonData.count = domainData.domainList.getSize();
    }

    @When("^I search for the domains for the service \"(.+)\"$")
    public void findDomainsByServiceName(String serviceName)
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domainData.domain = domainService.findByServiceName(serviceName);
            return null;
        });
    }

    @Then("^This is the initial count$")
    public void setInitialCount() {
        domainData.initial_count = commonData.count;
    }

    @Then("^A domain was created$")
    public void checkDomainNotNull() {
        assertNotNull(domainData.domain);
    }

    @Then("^There is no domain$")
    public void checkDomainIsNull() {
        assertNull(domainData.domain);
    }

    // The following test step is more of a filler. The only purpose is to achieve some coverage
    // of the Domain object equals function.
    // It must be noted that full coverage should be impossible, since the function tests for
    // object member combinations that should be impossible to create.
    // Some examples are domain objects with the same name but different service name members
    // (the name entry is defined as unique in the database). Also it tests for null
    // values for all 3 members, but the Domain service create method will reject any domain
    // creator with a null value for any member variable.
    // As such this step is of limited usefulness and should be taken with a grain of salt.
    @Then("^I can compare domain objects$")
    public void checkDomainComparison()
            throws KapuaException {

        KapuaSecurityUtils.doPrivileged(() -> {
            DomainCreator tmpCreator = domainFactory.newCreator("name_1", "svc_1");
            HashSet<Actions> tmpAct = new HashSet<>();
            tmpAct.add(Actions.read);
            tmpAct.add(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom1 = domainService.create(tmpCreator);
            assertNotNull(tmpDom1);

            assertTrue(tmpDom1.equals(tmpDom1));
            assertFalse(tmpDom1.equals(null));
            assertFalse(tmpDom1.equals(new String("")));

            Domain tmpDom2 = null;
            tmpDom2 = domainService.find(null, tmpDom1.getId());
            assertNotNull(tmpDom2);

            tmpCreator.setName("name_2");
            tmpCreator.setServiceName("svc_2");
            Domain tmpDom3 = domainService.create(tmpCreator);
            assertNotNull(tmpDom3);

            tmpCreator.setName("name_3");
            tmpAct.remove(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom4 = domainService.create(tmpCreator);
            assertNotNull(tmpDom4);

            assertTrue(tmpDom1.equals(tmpDom2));
            assertFalse(tmpDom1.equals(tmpDom3));
            assertFalse(tmpDom1.equals(tmpDom4));
            return null;
        });
    }

    @Then("^The domain matches the creator$")
    public void checkDomainAgainstCreator() {
        assertNotNull(domainData.domain);
        assertNotNull(domainData.domain.getId());
        assertNotNull(domainData.domainCreator);
        assertEquals(domainData.domainCreator.getName(), domainData.domain.getName());
        assertEquals(domainData.domainCreator.getServiceName(), domainData.domain.getServiceName());
        if (domainData.domainCreator.getActions() != null) {
            assertNotNull(domainData.domain.getActions());
            assertEquals(domainData.domainCreator.getActions().size(), domainData.domain.getActions().size());
            for (Actions a : domainData.domainCreator.getActions()) {
                assertTrue(domainData.domain.getActions().contains(a));
            }
        }
    }

    @Then("^The domain matches the parameters$")
    public void checkDomainAgainstParameters(List<CucDomain> domains) {

        assertNotNull(domains);
        assertEquals(1, domains.size());
        CucDomain tmpDom = domains.get(0);

        tmpDom.doParse();
        if (tmpDom.getName() != null) {
            assertEquals(tmpDom.getName(), domainData.domain.getName());
        }
        if (tmpDom.getServiceName() != null) {
            assertEquals(tmpDom.getServiceName(), domainData.domain.getServiceName());
        }
        if (tmpDom.getActionSet() != null) {
            assertEquals(tmpDom.getActionSet().size(), domainData.domain.getActions().size());
            for (Actions a : tmpDom.getActionSet()) {
                assertTrue(domainData.domain.getActions().contains(a));
            }
        }
    }

    @Then("^(\\d+) more domains (?:was|were) created$")
    public void checkIncreasedCountResult(int cnt) {
        assertEquals(cnt, commonData.count - domainData.initial_count);
    }
}
