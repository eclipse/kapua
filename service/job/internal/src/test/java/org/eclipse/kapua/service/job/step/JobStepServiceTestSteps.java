/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.step;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.KapuaConfigurableServiceSchemaUtils;
import org.eclipse.kapua.commons.configuration.metatype.KapuaMetatypeFactoryImpl;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicateImpl;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.common.CommonData;
import org.eclipse.kapua.service.job.internal.JobData;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.StepDefinitionData;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepFactoryImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepServiceImpl;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;

import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobStepService.feature scenarios.     *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobStepServiceTestSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(JobStepServiceTestSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_STEP_TABLES = "job_step_drop.sql";

    private static final KapuaId ROOT_ID = new KapuaEid(BigInteger.ONE);

    // Interstep scratchpads
    private CommonData commonData;
    private JobData jobData;
    private StepData stepData;
    private StepDefinitionData stepDefinitionData;

    // Step service objects
    private JobStepService stepService;
    private JobStepFactory stepFactory;

    // Default constructor
    @Inject
    public JobStepServiceTestSteps(CommonData commonData, JobData jobData, StepData stepData, StepDefinitionData stepDefinitionData) {
        this.commonData = commonData;
        this.jobData = jobData;
        this.stepData = stepData;
        this.stepDefinitionData = stepDefinitionData;
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
    public void beforeScenario(Scenario scenario)
            throws Exception {

        commonData.scenario = scenario;
        locator = KapuaLocator.getInstance();

        // Create User Service tables
        enableH2Connection();

        // Create the account service tables
        new KapuaLiquibaseClient("jdbc:h2:mem:kapua;MODE=MySQL", "kapua", "kapua").update();

        MockedLocator mockLocator = (MockedLocator) locator;

        // Inject mocked Authorization Service method checkPermission
        AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
        // TODO: Check why does this line needs an explicit cast!
        Mockito.doNothing().when(mockedAuthorization).checkPermission(
                (org.eclipse.kapua.service.authorization.permission.Permission) Matchers.any(Permission.class));
        mockLocator.setMockedService(org.eclipse.kapua.service.authorization.AuthorizationService.class,
                mockedAuthorization);

        // Inject mocked Permission Factory
        PermissionFactory mockedPermissionFactory = Mockito.mock(PermissionFactory.class);
        mockLocator.setMockedFactory(org.eclipse.kapua.service.authorization.permission.PermissionFactory.class,
                mockedPermissionFactory);

        // Inject actual service implementations
        JobStepDefinitionFactory jobStepDefinitionFactory = new JobStepDefinitionFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory.class, jobStepDefinitionFactory);
        JobStepDefinitionService jobStepDefinitionService = new JobStepDefinitionServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService.class, jobStepDefinitionService);
        stepFactory = new JobStepFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.step.JobStepFactory.class, stepFactory);
        stepService = new JobStepServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.step.JobStepService.class, stepService);

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

        // All operations on database are performed using system user.
        KapuaSession kapuaSession = new KapuaSession(null, ROOT_ID, ROOT_ID);
        KapuaSecurityUtils.setSession(kapuaSession);

        XmlUtil.setContextProvider(new JobJAXBContextProvider());

        commonData.currentScopeId = ROOT_ID;
    }

    @After
    public void afterScenario()
            throws Exception {
        // Drop the Job Service tables
        scriptSession(JobEntityManagerFactory.getInstance(), DROP_STEP_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    @Given("^A regular step creator with the name \"(.*)\" and the following properties$")
    public void prepareARegularStepCreatorWithPropertyList(String name, List<CucStepProperty> list) {

        stepData.stepCreator = prepareDefaultCreator();
        stepData.stepCreator.setName(name);
        stepData.stepCreator.setJobStepDefinitionId(stepDefinitionData.currentStepDefinitionId);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for(CucStepProperty prop : list) {
            tmpPropLst.add(stepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
        }
        stepData.stepCreator.setJobStepProperties(tmpPropLst);
    }

    @When("^I create a new step entity from the existing creator$")
    public void createAStepFromTheCreator()
            throws Exception {

        try {
            commonData.primeException();
            stepData.stepCreator.setJobId(jobData.currentJobId);
            stepData.step = stepService.create(stepData.stepCreator);
            stepData.currentStepId = stepData.step.getId();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the step name to \"(.+)\"$")
    public void updateStepName(String name)
            throws Exception {

        stepData.step.setName(name);

        try {
            commonData.primeException();
            stepData.step = stepService.update(stepData.step);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the step with a new definition$")
    public void updateStepDefinition() throws Exception {

        stepData.step.setJobStepDefinitionId(stepDefinitionData.currentStepDefinitionId);

        try {
            commonData.primeException();
            stepData.step = stepService.update(stepData.step);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I search for the last step in the database$")
    public void findLastStep()
            throws Exception {

        try {
            commonData.primeException();
            stepData.step = stepService.find(stepData.step.getScopeId(), stepData.step.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query for a step with the name \"(.+)\"$")
    public void queryForNamedStep(String name)
            throws Exception {

        JobStepQuery tmpQuery = stepFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("name", name));

        try {
            commonData.primeException();
            stepData.stepList = stepService.query(tmpQuery);
            commonData.itemCount = stepData.stepList.getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the steps in the scope$")
    public void countStepsInScope()
            throws Exception {

        JobStepQuery tmpQuery = stepFactory.newQuery(commonData.currentScopeId);

        try {
            commonData.primeException();
            commonData.itemCount = stepService.count(tmpQuery);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I delete the last step$")
    public void deleteLastStep() throws Exception {

        try {
            commonData.primeException();
            stepService.delete(stepData.step.getScopeId(), stepData.step.getId());
        } catch(KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Then("^The step item matches the creator$")
    public void checkStepItemAgainstCreator() {

        assertEquals(stepData.stepCreator.getJobId(), stepData.step.getJobId());
        assertEquals(stepData.stepCreator.getJobStepDefinitionId(), stepData.step.getJobStepDefinitionId());
        assertEquals(stepData.stepCreator.getName(), stepData.step.getName());
        assertEquals(stepData.stepCreator.getDescription(), stepData.step.getDescription());
        assertEquals(stepData.stepCreator.getStepIndex(), stepData.step.getStepIndex());
        assertEquals(stepData.stepCreator.getStepProperties().size(), stepData.step.getStepProperties().size());
    }

    @Then("^There is no such step item in the database$")
    public void checkThatNoStepWasFound() {
        assertNull("Unexpected step item found!", stepData.step);
    }

    @When("^I test the sanity of the step factory$")
    public void testTheStepFactory() {

        assertNotNull(stepFactory.newCreator(ROOT_ID));
        assertNotNull(stepFactory.newEntity(ROOT_ID));
        assertNotNull(stepFactory.newListResult());
        assertNotNull(stepFactory.newQuery(ROOT_ID));
        assertNotNull(stepFactory.newStepProperty("TestName", "TestType", "TestValue"));
    }

// ************************************************************************************
// * Private helper functions                                                         *
// ************************************************************************************

    private JobStepCreator prepareDefaultCreator() {

        JobStepCreator tmpCr = stepFactory.newCreator(commonData.currentScopeId);
        tmpCr.setName(String.format("StepName_%d", random.nextInt()));
        tmpCr.setDescription("StepDescription");
        tmpCr.setStepIndex(0);

        return tmpCr;
    }
}
