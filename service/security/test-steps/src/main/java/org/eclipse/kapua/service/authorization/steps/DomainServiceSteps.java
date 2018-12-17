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
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationJAXBContextProvider;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainFactory;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.domain.DomainAttributes;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainFactoryImpl;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainRegistryServiceImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;

/**
 * Implementation of Gherkin steps used in DomainRegistryService.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Domain Service dependens on.
 */

@ScenarioScoped
public class DomainServiceSteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(DomainServiceSteps.class);

    // Various domain related service references
    private DomainRegistryService domainRegistryService;
    private DomainFactory domainFactory;

    @Inject
    public DomainServiceSteps(StepData stepData, DBHelper dbHelper) {

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

                bind(DomainRegistryService.class).toInstance(new DomainRegistryServiceImpl());
                bind(DomainFactory.class).toInstance(new DomainFactoryImpl());
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

        // Instantiate all the services and factories that are required by the tests
        locator = KapuaLocator.getInstance();
        domainRegistryService = locator.getService(DomainRegistryService.class);
        domainFactory = locator.getFactory(DomainFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        XmlUtil.setContextProvider(new AuthorizationJAXBContextProvider());
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

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Given("^I create the domain(?:|s)$")
    public void createAListOfDomains(List<CucDomain> domains)
            throws Exception {

        DomainCreator domainCreator = null;
        Domain domain = null;

        stepData.remove("DomainCreator");
        stepData.remove("Domain");
        stepData.remove("DomainId");

        primeException();
        for (CucDomain tmpDom : domains) {
            tmpDom.doParse();

            domainCreator = domainFactory.newCreator(tmpDom.getName());
            if (tmpDom.getActionSet() != null) {
                domainCreator.setActions(tmpDom.getActionSet());
            }
            stepData.put("DomainCreator", domainCreator);

            try {
                domain = domainRegistryService.create(domainCreator);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }

        stepData.put("DomainCreator", domainCreator);
        stepData.put("Domain", domain);
        stepData.put("DomainId", domain.getId());
    }

    @When("^I search for the last created domain$")
    public void findDomainByRememberedId()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get("DomainId");
        stepData.remove("Domain");

        try {
            primeException();
            Domain domain = domainRegistryService.find(null, domainId);
            stepData.put("Domain", domain);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last created domain$")
    public void deleteLastCreatedDomain()
            throws Exception {

        KapuaId domainId = (KapuaId) stepData.get("DomainId");

        try {
            primeException();
            domainRegistryService.delete(null, domainId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I try to delete domain with a random ID$")
    public void deleteRandomDomainId()
            throws Exception {

        try {
            primeException();
            domainRegistryService.delete(null, getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the domain entries in the database$")
    public void countDomainEntries()
            throws Exception {

        stepData.remove("Count");

        try {
            primeException();
            DomainQuery query = domainFactory.newQuery(null);
            long count = domainRegistryService.count(query);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for domains with the name \"(.+)\"$")
    public void queryForNamedDomain(String name)
            throws Exception {

        DomainQuery query = domainFactory.newQuery(null);
        query.setPredicate(new AttributePredicateImpl<>(DomainAttributes.NAME, name));

        stepData.remove("DomainList");
        stepData.remove("Count");

        try {
            primeException();
            DomainListResult domainList = domainRegistryService.query(query);
            stepData.put("DomainList", domainList);
            stepData.put("Count", domainList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^This is the initial count$")
    public void setInitialCount() {

        long startCount = (long) stepData.get("Count");
        stepData.put("InitialCount", startCount);
    }

    @Then("^A domain was created$")
    public void checkDomainNotNull() {

        Domain domain = (Domain) stepData.get("Domain");
        assertNotNull(domain);
    }

    @Then("^There is no domain$")
    public void checkDomainIsNull() {

        Domain domain = (Domain) stepData.get("Domain");
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
            DomainCreator tmpCreator = domainFactory.newCreator("name_1");
            HashSet<Actions> tmpAct = new HashSet<>();
            tmpAct.add(Actions.read);
            tmpAct.add(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom1 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom1);

            assertTrue(tmpDom1.equals(tmpDom1));
            assertFalse(tmpDom1.equals(null));
            assertFalse(tmpDom1.equals(String.valueOf("")));

            Domain tmpDom2 = null;
            tmpDom2 = domainRegistryService.find(null, tmpDom1.getId());
            assertNotNull(tmpDom2);

            tmpCreator.setName("name_2");
            Domain tmpDom3 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom3);

            tmpCreator.setName("name_3");
            tmpAct.remove(Actions.write);
            tmpCreator.setActions(tmpAct);
            Domain tmpDom4 = domainRegistryService.create(tmpCreator);
            assertNotNull(tmpDom4);

            assertTrue(tmpDom1.equals(tmpDom2));
            assertFalse(tmpDom1.equals(tmpDom3));
            assertFalse(tmpDom1.equals(tmpDom4));
            return null;
        });
    }

    @Then("^The domain matches the creator$")
    public void checkDomainAgainstCreator() {

        Domain domain = (Domain) stepData.get("Domain");
        DomainCreator domainCreator = (DomainCreator) stepData.get("DomainCreator");

        assertNotNull(domain);
        assertNotNull(domain.getId());
        assertNotNull(domainCreator);
        assertEquals(domainCreator.getName(), domain.getName());
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

        Domain domain = (Domain) stepData.get("Domain");

        assertEquals(1, domains.size());
        CucDomain tmpDom = domains.get(0);
        tmpDom.doParse();

        if (tmpDom.getName() != null) {
            assertEquals(tmpDom.getName(), domain.getName());
        }
        if (tmpDom.getActionSet() != null) {
            assertEquals(tmpDom.getActionSet().size(), domain.getActions().size());
            for (Actions a : tmpDom.getActionSet()) {
                assertTrue(domain.getActions().contains(a));
            }
        }
    }

    @Then("^(\\d+) more domains (?:was|were) created$")
    public void checkIncreasedCountResult(int cnt) {

        long count = (long) stepData.get("Count");
        long initialCount = (long) stepData.get("InitialCount");

        assertEquals(cnt, count - initialCount);
    }
}
