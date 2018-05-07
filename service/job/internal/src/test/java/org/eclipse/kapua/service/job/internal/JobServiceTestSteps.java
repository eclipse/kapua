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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.job.internal;

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
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.JobPredicates;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.common.CommonData;
import org.eclipse.kapua.service.job.common.TestConfig;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.StepData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobService.feature scenarios.                *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobServiceTestSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceTestSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_JOB_TABLES = "job_drop.sql";

    private static final KapuaId ROOT_ID = new KapuaEid(BigInteger.ONE);

    // Job service objects
    private JobFactory jobFactory;
    private JobService jobService;

    // Interstep scratchpads
    CommonData commonData;
    JobData jobData;
    StepData stepData;

    // Default constructor
    @Inject
    public JobServiceTestSteps(CommonData commonData, JobData jobData, StepData stepData) {
        this.commonData = commonData;
        this.jobData = jobData;
        this.stepData = stepData;
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
        jobFactory = new JobFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.JobFactory.class, jobFactory);
        jobService = new JobServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.JobService.class, jobService);

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
        scriptSession(JobEntityManagerFactory.getInstance(), DROP_JOB_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    @When("^I configure$")
    public void setConfigurationValue(List<TestConfig> testConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }
        try {
            commonData.primeException();
            jobService.setConfigValues(commonData.currentScopeId,
                    new KapuaEid(BigInteger.ONE), valueMap);
        } catch (KapuaException ke) {
            commonData.verifyException(ke);
        }
    }

    @Given("^A regular job creator with the name \"(.+)\"$")
    public void prepareARegularJobCreator(String name) {
        jobData.jobCreator = jobFactory.newCreator(commonData.currentScopeId);
        jobData.jobCreator.setName(name);
        jobData.jobCreator.setDescription("Test job");
    }

    @Given("^A job creator with a null name$")
    public void prepareAJobCreatorWithNullName() {
        jobData.jobCreator = jobFactory.newCreator(commonData.currentScopeId);
        jobData.jobCreator.setName(null);
        jobData.jobCreator.setDescription("Test job");
    }

    @Given("^A job creator with an empty name$")
    public void prepareAJobCreatorWithAnEmptyName() {
        jobData.jobCreator = jobFactory.newCreator(commonData.currentScopeId);
        jobData.jobCreator.setName("");
        jobData.jobCreator.setDescription("Test job");
    }

    @Given("^I create a job with the name \"(.+)\"$")
    public void createANamedJob(String name)
            throws Exception {

        prepareARegularJobCreator(name);

        try {
            commonData.primeException();
            jobData.job = jobService.create(jobData.jobCreator);
            jobData.currentJobId = jobData.job.getId();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Given("^I create (\\d+) job items$")
    public void createANumberOfJobs(int num)
            throws Exception {

        JobCreator tmpCreator;
        try {
            commonData.primeException();
            for (int i = 0; i < num; i++) {
                tmpCreator = jobFactory.newCreator(commonData.currentScopeId);
                tmpCreator.setName(String.format("TestJobNum%d", i));
                tmpCreator.setDescription("TestJobDescription");
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Given("^I create (\\d+) job items with the name \"(.+)\"$")
    public void createANumberOfJobsWithName(int num, String name)
            throws Exception {

        JobCreator tmpCreator = jobFactory.newCreator(commonData.currentScopeId);
        tmpCreator.setDescription("TestJobDescription");

        try {
            commonData.primeException();
            for (int i = 0; i < num; i++) {
                tmpCreator.setName(name + "_" + i);
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I create a new job entity from the existing creator$")
    public void createJobFromCreator()
            throws Exception {

        try {
            commonData.primeException();
            jobData.job = jobService.create(jobData.jobCreator);
            jobData.currentJobId = jobData.job.getId();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the job name to \"(.+)\"$")
    public void updateExistingJobName(String newName)
            throws Exception {

        jobData.job.setName(newName);

        try {
            commonData.primeException();
            jobData.job = jobService.update(jobData.job);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the job description to \"(.+)\"$")
    public void updateExistingJobDescription(String newDescription)
            throws Exception {

        jobData.job.setDescription(newDescription);

        try {
            commonData.primeException();
            jobData.job = jobService.update(jobData.job);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the job XML definition to \"(.+)\"$")
    public void updateExistingJobXMLDefinition(String newDefinition)
            throws Exception {

        jobData.job.setJobXmlDefinition(newDefinition);

        try {
            commonData.primeException();
            jobData.job = jobService.update(jobData.job);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I add the current step to the last job$")
    public void updateJobWithSteps()
            throws Exception {

        List<JobStep> tmpStepList = jobData.job.getJobSteps();

        tmpStepList.add(stepData.step);
        jobData.job.setJobSteps(tmpStepList);

        try {
            commonData.primeException();
            jobData.job = jobService.update(jobData.job);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I delete the job$")
    public void deleteJobFromDatabase()
            throws Exception {

        try {
            commonData.primeException();
            jobService.delete(jobData.job.getScopeId(), jobData.job.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I search for the job in the database$")
    public void findJobInDatabase()
            throws Exception {

        try {
            commonData.primeException();
            jobData.job = jobService.find(commonData.currentScopeId, jobData.currentJobId);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the jobs in the database$")
    public void countJobsInDatabase()
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(commonData.currentScopeId);

        try {
            commonData.primeException();
            commonData.itemCount = jobService.count(tmpQuery);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query for jobs in scope (\\d+)$")
    public void countJobsInScope(int id)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(new KapuaEid(BigInteger.valueOf(id)));
        try {
            commonData.primeException();
            commonData.itemCount = jobService.query(tmpQuery).getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the jobs with the name starting with \"(.+)\"$")
    public void countJobsWithName(String name)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(new AttributePredicateImpl<>(JobPredicates.NAME, name, Operator.STARTS_WITH));

        try {
            commonData.primeException();
            commonData.itemCount = jobService.query(tmpQuery).getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query for the job with the name \"(.+)\"$")
    public void queryForJobWithName(String name)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo(JobPredicates.NAME, name));

        try {
            commonData.primeException();
            jobData.job = jobService.query(tmpQuery).getFirstItem();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Then("^The job entity matches the creator$")
    public void checkJobAgainstCreator() {

        assertEquals("The job scope does not match the creator.", jobData.jobCreator.getScopeId(), jobData.job.getScopeId());
        assertEquals("The job name does not match the creator.", jobData.jobCreator.getName(), jobData.job.getName());
        assertEquals("The job description does not match the creator.", jobData.jobCreator.getDescription(), jobData.job.getDescription());
    }

    @Then("^The job has (\\d+) steps$")
    public void checkNumberOfJobSteps(int num) {
        assertEquals("The job item has the wrong number of steps", num, jobData.job.getJobSteps().size());
    }

    @Then("^I find a job item in the database$")
    public void checkThatAJobWasFound() {
        assertNotNull("Unexpected null value for the job.", jobData.job);
    }

    @Then("^There is no such job item in the database$")
    public void checkThatNoJobWasFound() {
        assertNull("Unexpected job item was found!", jobData.job);
    }

    @Then("The job name is \"(.+)\"")
    public void checkJobItemName(String name) {
        assertEquals("The job name does not match!", name, jobData.job.getName());
    }

    @Then("The job description is \"(.+)\"")
    public void checkJobItemDescription(String description) {
        assertEquals("The job description does not match!", description, jobData.job.getDescription());
    }

    @Then("The job XML definition is \"(.+)\"")
    public void checkJobItemXMLDefinition(String definition) {
        assertEquals("The job XML definition does not match!", definition, jobData.job.getJobXmlDefinition());
    }

    @When("^I test the sanity of the job factory$")
    public void testJobFactorySanity() {

        commonData.primeException();
        assertNotNull("The job factory returned a null creator!", jobFactory.newCreator(ROOT_ID));
        assertNotNull("The job factory returned a null job object!", jobFactory.newEntity(ROOT_ID));
        assertNotNull("The job factory returned a null job query!", jobFactory.newQuery(ROOT_ID));
        assertNotNull("The job factory returned a null job list result!", jobFactory.newListResult());
    }
}
