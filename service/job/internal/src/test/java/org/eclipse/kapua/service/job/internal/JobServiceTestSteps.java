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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.job.internal;

import com.google.common.base.MoreObjects;
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
import org.eclipse.kapua.commons.jpa.JdbcConnectionUrlResolvers;
import org.eclipse.kapua.commons.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.jbatch.JobEngineServiceJbatch;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaMetatypeFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.common.CommonData;
import org.eclipse.kapua.service.job.common.CucConfig;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.StepData;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerFactoryImpl;
import org.eclipse.kapua.service.scheduler.trigger.quartz.TriggerServiceImpl;
import org.eclipse.kapua.test.KapuaTest;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobService.feature scenarios.                *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobServiceTestSteps extends KapuaTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceTestSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_JOB_TABLES = "job_drop.sql";
    private static final String DROP_JOB_ENGINE_TABLES = "job_engine_drop.sql";
    private static final String DROP_TRIGGER_TABLES = "trigger_drop.sql";

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

        // Create the Job Service database tables
        enableH2Connection();
        SystemSetting config = SystemSetting.getInstance();
        String schema = MoreObjects.firstNonNull(config.getString(SystemSettingKey.DB_SCHEMA_ENV), config.getString(SystemSettingKey.DB_SCHEMA));
        String jdbcUrl = JdbcConnectionUrlResolvers.resolveJdbcUrl();

        // Create the account service tables
        new KapuaLiquibaseClient(jdbcUrl, "kapua", "kapua", Optional.ofNullable(schema)).update();

        MockedLocator mockLocator = (MockedLocator) locator;

        // Inject mocked Authorization Service method checkPermission
        AuthorizationService mockedAuthorization = Mockito.mock(AuthorizationService.class);
        Mockito.doNothing().when(mockedAuthorization).checkPermission(Matchers.any(Permission.class));
        mockLocator.setMockedService(AuthorizationService.class, mockedAuthorization);

        // Inject mocked Permission Factory
        mockLocator.setMockedFactory(PermissionFactory.class, Mockito.mock(PermissionFactory.class));

        // Inject actual service implementations
        jobFactory = new JobFactoryImpl();
        mockLocator.setMockedFactory(JobFactory.class, jobFactory);
        jobService = new JobServiceImpl();
        mockLocator.setMockedService(JobService.class, jobService);

        // Inject the implementations of the depending services
        mockLocator.setMockedService(JobEngineService.class, new JobEngineServiceJbatch());
        mockLocator.setMockedFactory(TriggerFactory.class, new TriggerFactoryImpl());
        mockLocator.setMockedService(TriggerService.class, new TriggerServiceImpl());

        // Set KapuaMetatypeFactory for Metatype configuration
        mockLocator.setMockedFactory(KapuaMetatypeFactory.class, new KapuaMetatypeFactoryImpl());

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
        scriptSession(JobEntityManagerFactory.getInstance(), DROP_JOB_ENGINE_TABLES);
        scriptSession(JobEntityManagerFactory.getInstance(), DROP_TRIGGER_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    @When("^I configure$")
    public void setConfigurationValue(List<CucConfig> cucConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();

        for (CucConfig config : cucConfigs) {
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
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, name, Operator.STARTS_WITH));

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
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, name));

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
