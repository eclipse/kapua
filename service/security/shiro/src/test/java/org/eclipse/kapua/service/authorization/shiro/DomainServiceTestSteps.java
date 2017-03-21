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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authorization.shiro;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
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

/**
 * Implementation of Gherkin steps used in DomainService.feature scenarios.
 *
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Domain Service dependens on.
 *
 */

public class DomainServiceTestSteps extends AbstractAuthorizationServiceTest {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(DomainServiceTestSteps.class);

    KapuaId rootScopeId = new KapuaEid(BigInteger.ONE);

    // Various domain related service references
    DomainService domainService = null;
    DomainFactory domainFactory = null;
    GroupService groupService = null;
    GroupFactory groupFactory = null;

    // Currently executing scenario.
    Scenario scenario;

    // Domain service related objects
    Domain domain = null;
    DomainCreator domainCreator = null;
    DomainListResult domainList = null;
    KapuaId domainId = null;

    // Check if exception was fired in step.
    boolean exceptionCaught = false;

    // Interstep data scratchpads
    int intVal;
    String strVal;
    long count;
    long initial_count;
    KapuaId lastId;

    // Setup and tear-down steps
    @Before
    public void beforeScenario(Scenario scenario)
            throws Exception {
        this.scenario = scenario;
        exceptionCaught = false;

        // Instantiate all the services and factories that are required by the tests
        domainService = new DomainServiceImpl();
        domainFactory = new DomainFactoryImpl();
        groupService = new GroupServiceImpl();
        groupFactory = new GroupFactoryImpl();

        // Clean up the database. A clean slate is needed for truly independent
        // test case executions!
        dropDatabase();
        setupDatabase();
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Given("^I create the domain(?:|s)$")
    public void createAListOfDomains(List<CucDomain> domains)
            throws KapuaException {
        for (CucDomain tmpDom : domains) {
            exceptionCaught = false;
            tmpDom.doParse();
            domainCreator = domainFactory.newCreator(tmpDom.getName(), tmpDom.getServiceName());
            if (tmpDom.getActionSet() != null) {
                domainCreator.setActions(tmpDom.getActionSet());
            }
            KapuaSecurityUtils.doPrivileged(() -> {
                try {
                    domain = domainService.create(domainCreator);
                } catch (KapuaException ex) {
                    exceptionCaught = true;
                    return null;
                }
                assertNotNull(domain);
                assertNotNull(domain.getId());
                domainId = domain.getId();
                return null;
            });
            if (exceptionCaught) {
                break;
            }
        }
    }

    @When("^I search for the last created domain$")
    public void findDomainByRememberedId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domain = domainService.find(null, domainId);
            return null;
        });
    }

    @When("^I delete the last created domain$")
    public void deleteLastCreatedDomain()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domainService.delete(null, domainId);
            return null;
        });
    }

    @When("^I try to delete domain with a random ID$")
    public void deleteRandomDomainId()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                exceptionCaught = false;
                domainService.delete(null, generateId());
            } catch (KapuaException ex) {
                exceptionCaught = true;
            }
            return null;
        });
    }

    @When("^I count the domain entries in the database$")
    public void countDomainEntries()
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            count = domainService.count(domainFactory.newQuery());
            return null;
        });
    }

    @When("^I query for domains with the name \"(.+)\"$")
    public void queryForNamedDomain(String name)
            throws KapuaException {
        DomainQuery query = domainFactory.newQuery();
        query.setPredicate(new AttributePredicate<>(DomainPredicates.NAME, name));
        KapuaSecurityUtils.doPrivileged(() -> {
            domainList = domainService.query(query);
            return null;
        });
        assertNotNull(domainList);
        count = domainList.getSize();
    }

    @When("^I query for domains with the service name \"(.+)\"$")
    public void queryForDomainsWithServiceName(String service_name)
            throws KapuaException {
        DomainQuery query = domainFactory.newQuery();
        query.setPredicate(new AttributePredicate<>(DomainPredicates.SERVICE_NAME, service_name));
        KapuaSecurityUtils.doPrivileged(() -> {
            domainList = domainService.query(query);
            return null;
        });
        assertNotNull(domainList);
        count = domainList.getSize();
    }

    @When("^I search for the domains for the service \"(.+)\"$")
    public void findDomainsByServiceName(String serviceName)
            throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            domain = domainService.findByServiceName(serviceName);
            return null;
        });
    }

    @Then("^This is the initial count$")
    public void setInitialCount() {
        initial_count = count;
    }

    @Then("^A domain was created$")
    public void checkDomainNotNull() {
        assertNotNull(domain);
    }

    @Then("^There is no domain$")
    public void checkDomainIsNull() {
        assertNull(domain);
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
        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertNotNull(domainCreator);
        assertEquals(domainCreator.getName(), domain.getName());
        assertEquals(domainCreator.getServiceName(), domain.getServiceName());
        if (domainCreator.getActions() != null) {
            assertNotNull(domain.getActions());
            assertEquals(domainCreator.getActions().size(), domain.getActions().size());
            for (Actions a : domainCreator.getActions()) {
                assertTrue(domain.getActions().contains(a));
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
            assertEquals(tmpDom.getName(), domain.getName());
        }
        if (tmpDom.getServiceName() != null) {
            assertEquals(tmpDom.getServiceName(), domain.getServiceName());
        }
        if (tmpDom.getActionSet() != null) {
            assertEquals(tmpDom.getActionSet().size(), domain.getActions().size());
            for (Actions a : tmpDom.getActionSet()) {
                assertTrue(domain.getActions().contains(a));
            }
        }
    }

    @Then("^There (?:is|are) (\\d+) domain(?:|s)$")
    public void checkCountResult(int cnt) {
        assertEquals(cnt, count);
    }

    @Then("^(\\d+) more domains (?:was|were) created$")
    public void checkIncreasedCountResult(int cnt) {
        assertEquals(cnt, count - initial_count);
    }

    @Then("^An exception was caught$")
    public void exceptionWasCaught() {
        assertTrue(exceptionCaught);
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Generate a random KapuaId
    private KapuaId generateId() {
        return new KapuaEid(BigInteger.valueOf(random.nextLong()));
    }
}
