/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.steps;

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
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.internal.JobStepFactoryImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobStepService.feature scenarios.     *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobStepServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStepServiceSteps.class);

    // Step service objects
    private JobStepService jobStepService;
    private JobStepFactory jobStepFactory;

    // Default constructor
    @Inject
    public JobStepServiceSteps(StepData stepData, DBHelper dbHelper) {

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

                // Inject actual user service related services
                JobEntityManagerFactory jobEntityManagerFactory = JobEntityManagerFactory.getInstance();
                bind(JobEntityManagerFactory.class).toInstance(jobEntityManagerFactory);
                bind(JobStepService.class).toInstance(new JobStepServiceImpl());
                bind(JobStepFactory.class).toInstance(new JobStepFactoryImpl());
            }
        };

        Injector injector = Guice.createInjector(module);
        mockedLocator.setInjector(injector);
    }

    // ************************************************************************************
    // ************************************************************************************
    // * Definition of Cucumber scenario steps                                            *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Setup and tear-down steps                                                        *
    // ************************************************************************************

    @Before
    public void beforeScenario(Scenario scenario) {

        if (isUnitTest()) {
            setupDI();
        }

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();
        jobStepService = locator.getService(JobStepService.class);
        jobStepFactory = locator.getFactory(JobStepFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new JobJAXBContextProvider());
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
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    @When("^I configure the job step service$")
    public void setConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getCurrentScopeId();
        KapuaId scopeId = getCurrentParentId();

        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getParentId() != null) {
                scopeId = getKapuaId(config.getParentId());
            }
            if (config.getScopeId() != null) {
                accId = getKapuaId(config.getScopeId());
            }
        }
        try {
            primeException();
            jobStepService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular step creator with the name \"(.*)\" and the following properties$")
    public void prepareARegularStepCreatorWithPropertyList(String name, List<CucStepProperty> list) {

        JobStepCreator stepCreator;
        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        stepCreator = prepareDefaultCreator();
        stepCreator.setName(name);
        stepCreator.setJobStepDefinitionId(currentStepDefId);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for (CucStepProperty prop : list) {
            tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
        }
        stepCreator.setJobStepProperties(tmpPropLst);

        stepData.put("JobStepCreator", stepCreator);
    }

    @When("^I create a new step entity from the existing creator$")
    public void createAStepFromTheCreator()
            throws Exception {

        KapuaId currentJobId = (KapuaId) stepData.get("CurrentJobId");
        JobStepCreator stepCreator = (JobStepCreator) stepData.get("JobStepCreator");

        primeException();
        try {
            stepCreator.setJobId(currentJobId);
            stepData.remove("JobStep");
            stepData.remove("CurrentStepId");
            JobStep step = jobStepService.create(stepCreator);
            stepData.put("JobStep", step);
            stepData.put("CurrentStepId", step.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step name to \"(.+)\"$")
    public void updateStepName(String name)
            throws Exception {

        JobStep step = (JobStep) stepData.get("JobStep");

        step.setName(name);

        primeException();
        try {
            step = jobStepService.update(step);
            stepData.put("JobStep", step);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the step with a new definition$")
    public void updateStepDefinition() throws Exception {

        JobStep step = (JobStep) stepData.get("JobStep");
        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        step.setJobStepDefinitionId(currentStepDefId);

        primeException();
        try {
            step = jobStepService.update(step);
            stepData.put("JobStep", step);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last step in the database$")
    public void findLastStep()
            throws Exception {

        JobStep step = (JobStep) stepData.get("JobStep");

        primeException();
        try {
            JobStep newStep = jobStepService.find(step.getScopeId(), step.getId());
            stepData.put("JobStep", newStep);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for a step with the name \"(.+)\"$")
    public void queryForNamedStep(String name)
            throws Exception {

        JobStepQuery tmpQuery = jobStepFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("name", name));

        primeException();
        try {
            stepData.remove("JobStepList");
            stepData.remove("Count");
            JobStepListResult stepList = jobStepService.query(tmpQuery);
            Long itemCount = (long) stepList.getSize();
            stepData.put("JobStepList", stepList);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the steps in the scope$")
    public void countStepsInScope()
            throws Exception {

        JobStepQuery tmpQuery = jobStepFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = jobStepService.count(tmpQuery);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last step$")
    public void deleteLastStep() throws Exception {

        JobStep step = (JobStep) stepData.get("JobStep");

        primeException();
        try {
            jobStepService.delete(step.getScopeId(), step.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The step item matches the creator$")
    public void checkStepItemAgainstCreator() {

        JobStep step = (JobStep) stepData.get("JobStep");
        JobStepCreator stepCreator = (JobStepCreator) stepData.get("JobStepCreator");

        assertEquals(stepCreator.getJobId(), step.getJobId());
        assertEquals(stepCreator.getJobStepDefinitionId(), step.getJobStepDefinitionId());
        assertEquals(stepCreator.getName(), step.getName());
        assertEquals(stepCreator.getDescription(), step.getDescription());
        assertEquals(stepCreator.getStepIndex(), (Integer) step.getStepIndex());
        assertEquals(stepCreator.getStepProperties().size(), step.getStepProperties().size());
    }

    @Then("^There is no such step item in the database$")
    public void checkThatNoStepWasFound() {
        assertNull("Unexpected step item found!", stepData.get("JobStep"));
    }

    @When("^I test the sanity of the step factory$")
    public void testTheStepFactory() {

        assertNotNull(jobStepFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(jobStepFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(jobStepFactory.newListResult());
        assertNotNull(jobStepFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull(jobStepFactory.newStepProperty("TestName", "TestType", "TestValue"));
    }

    // ************************************************************************************
    // * Private helper functions                                                         *
    // ************************************************************************************

    private JobStepCreator prepareDefaultCreator() {

        JobStepCreator tmpCr = jobStepFactory.newCreator(getCurrentScopeId());
        tmpCr.setName(String.format("StepName_%d", random.nextInt()));
        tmpCr.setDescription("StepDescription");
        //        tmpCr.setStepIndex(0);

        return tmpCr;
    }
}
