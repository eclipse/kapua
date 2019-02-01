/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucJobStepProperty;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionFactoryImpl;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionServiceImpl;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.internal.JobFactoryImpl;
import org.eclipse.kapua.service.job.internal.JobServiceImpl;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.JobStepType;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepFactoryImpl;
import org.eclipse.kapua.service.job.step.internal.JobStepServiceImpl;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;
import org.eclipse.kapua.qa.common.MockedLocator;
import org.joda.time.DateTime;
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
// * Implementation of Gherkin steps used in JobService.feature scenarios.                *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceSteps.class);

    // Job service objects
    private JobFactory jobFactory;
    private JobService jobService;

    // Job Step definition service objects
    private JobStepDefinitionService jobStepDefinitionService;
    private JobStepDefinitionFactory jobStepDefinitionFactory;

    // Job Step service objects
    private JobStepService jobStepService;
    private JobStepFactory jobStepFactory;

    // Job Target service objects
    private JobTargetService jobTargetService;
    private JobTargetFactory jobTargetFactory;

    // Job Execution service objects
    private JobExecutionService jobExecutionService;
    private JobExecutionFactory jobExecutionFactory;

    // Default constructor
    @Inject
    public JobServiceSteps(StepData stepData, DBHelper dbHelper) {

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

                bind(JobService.class).toInstance(new JobServiceImpl());
                bind(JobFactory.class).toInstance(new JobFactoryImpl());

                bind(JobStepDefinitionService.class).toInstance(new JobStepDefinitionServiceImpl());
                bind(JobStepDefinitionFactory.class).toInstance(new JobStepDefinitionFactoryImpl());

                bind(JobStepService.class).toInstance(new JobStepServiceImpl());
                bind(JobStepFactory.class).toInstance(new JobStepFactoryImpl());

                bind(JobTargetService.class).toInstance(new JobTargetServiceImpl());
                bind(JobTargetFactory.class).toInstance(new JobTargetFactoryImpl());

                bind(JobExecutionService.class).toInstance(new JobExecutionServiceImpl());
                bind(JobExecutionFactory.class).toInstance(new JobExecutionFactoryImpl());
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
        jobService = locator.getService(JobService.class);
        jobFactory = locator.getFactory(JobFactory.class);
        jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
        jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);
        jobStepService = locator.getService(JobStepService.class);
        jobStepFactory = locator.getFactory(JobStepFactory.class);
        jobTargetService = locator.getService(JobTargetService.class);
        jobTargetFactory = locator.getFactory(JobTargetFactory.class);
        jobExecutionService = locator.getService(JobExecutionService.class);
        jobExecutionFactory = locator.getFactory(JobExecutionFactory.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
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
    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Job service steps                                                              *
    // ************************************************************************************
    @When("^I configure the job service$")
    public void setJobServiceConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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

        primeException();
        try {
            jobService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular job creator with the name \"(.+)\"$")
    public void prepareARegularJobCreator(String name) {

        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName(name);
        jobCreator.setDescription("Test job");

        stepData.put("JobCreator", jobCreator);
    }

    @Given("^A job creator with a null name$")
    public void prepareAJobCreatorWithNullName() {

        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName(null);
        jobCreator.setDescription("Test job");

        stepData.put("JobCreator", jobCreator);
    }

    @Given("^A job creator with an empty name$")
    public void prepareAJobCreatorWithAnEmptyName() {

        JobCreator jobCreator = jobFactory.newCreator(getCurrentScopeId());
        jobCreator.setName("");
        jobCreator.setDescription("Test job");

        stepData.put("JobCreator", jobCreator);
    }

    @Given("^I create a job with the name \"(.+)\"$")
    public void createANamedJob(String name)
            throws Exception {

        prepareARegularJobCreator(name);
        JobCreator jobCreator = (JobCreator) stepData.get("JobCreator");

        primeException();
        try {
            stepData.remove("Job");
            stepData.remove("CurrentJobId");
            Job job = jobService.create(jobCreator);
            stepData.put("Job", job);
            stepData.put("CurrentJobId", job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) job items$")
    public void createANumberOfJobs(int num)
            throws Exception {

        primeException();
        try {
            for (int i = 0; i < num; i++) {
                JobCreator tmpCreator = jobFactory.newCreator(getCurrentScopeId());
                tmpCreator.setName(String.format("TestJobNum%d", i));
                tmpCreator.setDescription("TestJobDescription");
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I create (\\d+) job items with the name \"(.+)\"$")
    public void createANumberOfJobsWithName(int num, String name)
            throws Exception {

        JobCreator tmpCreator = jobFactory.newCreator(getCurrentScopeId());
        tmpCreator.setDescription("TestJobDescription");

        primeException();
        try {
            for (int i = 0; i < num; i++) {
                tmpCreator.setName(name + "_" + i);
                jobService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create a new job entity from the existing creator$")
    public void createJobFromCreator()
            throws Exception {

        JobCreator jobCreator = (JobCreator) stepData.get("JobCreator");

        primeException();
        try {
            stepData.remove("Job");
            stepData.remove("CurrentJobId");
            Job job = jobService.create(jobCreator);
            stepData.put("Job", job);
            stepData.put("CurrentJobId", job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the job name to \"(.+)\"$")
    public void updateExistingJobName(String newName)
            throws Exception {

        Job oldJob = (Job) stepData.get("Job");
        oldJob.setName(newName);

        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the job description to \"(.+)\"$")
    public void updateExistingJobDescription(String newDescription)
            throws Exception {

        Job oldJob = (Job) stepData.get("Job");
        oldJob.setDescription(newDescription);

        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the job XML definition to \"(.+)\"$")
    public void updateExistingJobXMLDefinition(String newDefinition)
            throws Exception {

        Job oldJob = (Job) stepData.get("Job");
        oldJob.setJobXmlDefinition(newDefinition);

        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I add the current step to the last job$")
    public void updateJobWithSteps()
            throws Exception {

        Job oldJob = (Job) stepData.get("Job");
        List<JobStep> tmpStepList = oldJob.getJobSteps();
        JobStep step = (JobStep) stepData.get("Step");

        tmpStepList.add(step);
        oldJob.setJobSteps(tmpStepList);

        primeException();
        try {
            stepData.remove("Job");
            Job newJob = jobService.update(oldJob);
            stepData.put("Job", newJob);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the job$")
    public void deleteJobFromDatabase()
            throws Exception {

        Job job = (Job) stepData.get("Job");

        primeException();
        try {
            jobService.delete(job.getScopeId(), job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the job in the database$")
    public void findJobInDatabase()
            throws Exception {

        KapuaId currentJobId = (KapuaId) stepData.get("CurrentJobId");

        primeException();
        try {
            stepData.remove("Job");
            Job job = jobService.find(getCurrentScopeId(), currentJobId);
            stepData.put("Job", job);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the jobs in the database$")
    public void countJobsInDatabase()
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove("Count");
            Long count = jobService.count(tmpQuery);
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for jobs in scope (\\d+)$")
    public void countJobsInScope(int id)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getKapuaId(id));

        primeException();
        try {
            stepData.remove("Count");
            JobListResult jobList = jobService.query(tmpQuery);
            Long count = (long) jobList.getSize();
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the jobs with the name starting with \"(.+)\"$")
    public void countJobsWithName(String name)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(new AttributePredicateImpl<>(JobAttributes.NAME, name, Operator.STARTS_WITH));

        primeException();
        try {
            stepData.remove("Count");
            JobListResult jobList = jobService.query(tmpQuery);
            Long count = (long) jobList.getSize();
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the job with the name \"(.+)\"$")
    public void queryForJobWithName(String name)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.NAME, name));

        primeException();
        try {
            stepData.remove("Job");
            Job job = jobService.query(tmpQuery).getFirstItem();
            stepData.put("Job", job);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The job entity matches the creator$")
    public void checkJobAgainstCreator() {

        Job job = (Job) stepData.get("Job");
        JobCreator jobCreator = (JobCreator) stepData.get("JobCreator");

        assertEquals("The job scope does not match the creator.", jobCreator.getScopeId(), job.getScopeId());
        assertEquals("The job name does not match the creator.", jobCreator.getName(), job.getName());
        assertEquals("The job description does not match the creator.", jobCreator.getDescription(), job.getDescription());
    }

    @Then("^The job has (\\d+) steps$")
    public void checkNumberOfJobSteps(int num) {

        Job job = (Job) stepData.get("Job");
        assertEquals("The job item has the wrong number of steps", num, job.getJobSteps().size());
    }

    @Then("^I find a job item in the database$")
    public void checkThatAJobWasFound() {

        assertNotNull("Unexpected null value for the job.", stepData.get("Job"));
    }

    @Then("^There is no such job item in the database$")
    public void checkThatNoJobWasFound() {

        assertNull("Unexpected job item was found!", stepData.get("Job"));
    }

    @Then("The job name is \"(.+)\"")
    public void checkJobItemName(String name) {

        Job job = (Job) stepData.get("Job");
        assertEquals("The job name does not match!", name, job.getName());
    }

    @Then("The job description is \"(.+)\"")
    public void checkJobItemDescription(String description) {

        Job job = (Job) stepData.get("Job");
        assertEquals("The job description does not match!", description, job.getDescription());
    }

    @Then("The job XML definition is \"(.+)\"")
    public void checkJobItemXMLDefinition(String definition) {

        Job job = (Job) stepData.get("Job");
        assertEquals("The job XML definition does not match!", definition, job.getJobXmlDefinition());
    }

    @When("^I test the sanity of the job factory$")
    public void testJobFactorySanity() {

        primeException();
        assertNotNull("The job factory returned a null creator!", jobFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull("The job factory returned a null job object!", jobFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull("The job factory returned a null job query!", jobFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull("The job factory returned a null job list result!", jobFactory.newListResult());
    }

    // ************************************************************************************
    // * Job Step Definition Test steps                                                              *
    // ************************************************************************************
    @Given("^A regular step definition creator with the name \"(.*)\"$")
    public void prepareARegularStepDefinitionCreator(String name) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultJobStepDefinitionCreator();
        stepDefinitionCreator.setName(name);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular definition creator with the name \"(.*)\" and (\\d+) properties$")
    public void prepareARegularStepDefinitionCreatorWithProperties(String name, Integer cnt) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultJobStepDefinitionCreator();
        stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property1", "Type1", null));
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property2", "Type2", null));
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property3", "Type3", null));
        stepDefinitionCreator.setStepProperties(tmpPropLst);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular step definition creator with the name \"(.*)\" and the following properties$")
    public void prepareARegularStepDefinitionCreatorWithPropertyList(String name, List<CucJobStepProperty> list) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultJobStepDefinitionCreator();
        stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for(CucJobStepProperty prop : list) {
            tmpPropLst.add(jobStepDefinitionFactory.newStepProperty(prop.getName(), prop.getType(), null));
        }
        stepDefinitionCreator.setStepProperties(tmpPropLst);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular step definition with the name \"(.*)\" and the following properties$")
    public void createARegularStepDefinitionWithProperties(String name, List<CucJobStepProperty> list)
            throws Exception {

        prepareARegularStepDefinitionCreatorWithPropertyList(name, list);
        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            stepData.remove("CurrentJobStepDefinitionId");
            JobStepDefinition stepDefinition = jobStepDefinitionService.create(stepDefinitionCreator);
            stepData.put("JobStepDefinition", stepDefinition);
            stepData.put("CurrentJobStepDefinitionId", stepDefinition.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I set the step definition creator name to null$")
    public void setDefinitionCreatorNameToNull() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setName(null);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I set the step definition creator processor name to \"(.+)\"$")
    public void setDefinitionCreatorProcessorNameTo(String name) {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setProcessorName(name);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I set the step definition creator processor name to null$")
    public void setDefinitionCreatorProcessorNameToNull() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setProcessorName(null);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I create (\\d+) step definition items$")
    public void createANumberOfStepDefinitions(Integer num)
            throws Exception {

        JobStepDefinitionCreator tmpCreator;

        primeException();
        try {
            for (int i = 0; i < num; i++) {
                tmpCreator = jobStepDefinitionFactory.newCreator(getCurrentScopeId());
                tmpCreator.setName(String.format("TestStepDefinitionNum%d", random.nextLong()));
                tmpCreator.setProcessorName("TestStepProcessor");
                tmpCreator.setStepType(JobStepType.TARGET);
                jobStepDefinitionService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create a new step definition entity from the existing creator$")
    public void createAStepDefinitionFromTheCreator()
            throws Exception {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            stepData.remove("CurrentJobStepDefinitionId");
            JobStepDefinition stepDefinition = jobStepDefinitionService.create(stepDefinitionCreator);
            stepData.put("JobStepDefinition", stepDefinition);
            stepData.put("CurrentJobStepDefinitionId", stepDefinition.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the step definition in the database$")
    public void findTheExistingStepDefinitionInTheDatabase()
            throws Exception {

        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            JobStepDefinition stepDefinition = jobStepDefinitionService.find(getCurrentScopeId(), currentStepDefId);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch(KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the step definition in the database$")
    public void countStepDefinitionInDatabase()
            throws Exception {

        JobStepDefinitionQuery tmpQuery = jobStepDefinitionFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = jobStepDefinitionService.count(tmpQuery);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for step definitions in scope (\\d+)$")
    public void countStepDefinitijonsInScope(Integer id)
            throws Exception {

        JobStepDefinitionQuery tmpQuery = jobStepDefinitionFactory.newQuery(getKapuaId(id));

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = (long) jobStepDefinitionService.query(tmpQuery).getSize();
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the step definition$")
    public void deleteExistingStepDefinition()
            throws Exception{

        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        primeException();
        try {
            jobStepDefinitionService.delete(getCurrentScopeId(), currentStepDefId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition name to \"(.+)\"$")
    public void changeExistingStepDefinitionName(String name)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setName(name);
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition processor name to \"(.+)\"$")
    public void changeExistingStepDefinitionProcessor(String name)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setProcessorName(name);
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition type to \"(.+)\"$")
    public void changeExistingStepDefinitionType(String type)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setStepType(getTypeFromString(type));
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The step definition entity matches the creator$")
    public void checkTheStepDefinitionAgainstTheCreator() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        assertEquals("The step definition has the wrong name!", stepDefinitionCreator.getName(), stepDefinition.getName());
        assertEquals("The step definition has the wrong description!", stepDefinitionCreator.getDescription(), stepDefinition.getDescription());
        assertEquals("The step definition has the wrong reader name!", stepDefinitionCreator.getReaderName(), stepDefinition.getReaderName());
        assertEquals("The step definition has the wrong processor name!", stepDefinitionCreator.getProcessorName(), stepDefinition.getProcessorName());
        assertEquals("The step definition has the wrong writer name!", stepDefinitionCreator.getWriterName(), stepDefinition.getWriterName());
        assertEquals("The step definition has a wrong step type!", stepDefinitionCreator.getStepType(), stepDefinition.getStepType());
        assertNotNull("The step definition has no properties!", stepDefinition.getStepProperties());
        assertEquals("The step definition has a wrong number of properties!", stepDefinitionCreator.getStepProperties().size(), stepDefinition.getStepProperties().size());
        for(int i = 0; i < stepDefinitionCreator.getStepProperties().size(); i++) {
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getName(), stepDefinition.getStepProperties().get(i).getName());
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getPropertyType(), stepDefinition.getStepProperties().get(i).getPropertyType());
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getPropertyValue(), stepDefinition.getStepProperties().get(i).getPropertyValue());
        }
    }

    @Then("^There is no such step definition item in the database$")
    public void checkThatNoStepDefinitionWasFound() {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertNull("Unexpected step definition item was found!", stepDefinition);
    }

    @Then("^The step definition name is \"(.+)\"$")
    public void checkStepDefinitionName(String name) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition name does not match!", name, stepDefinition.getName());
    }

    @Then("^The step definition type is \"(.+)\"$")
    public void checkStepDefinitionType(String type) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition type does not match!", getTypeFromString(type), stepDefinition.getStepType());
    }

    @Then("^The step definition processor name is \"(.+)\"$")
    public void checkStepDefinitionProcessorName(String name) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition processor name does not match!", name, stepDefinition.getProcessorName());
    }

    @When("^I test the sanity of the step definition factory$")
    public void testTheStepDefinitionFactory() {

        assertNotNull(jobStepDefinitionFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newListResult());
        assertNotNull(jobStepDefinitionFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newStepProperty("TestName", "TestType", "TestValue"));
    }


    // ************************************************************************************
    // * Job Step Service Test steps                                                      *
    // ************************************************************************************

    @When("^I configure the job step service$")
    public void setJobStepConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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
    public void prepareARegularStepCreatorWithPropertyList(String name, List<CucJobStepProperty> list) {

        JobStepCreator stepCreator;
        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        stepCreator = prepareDefaultJobStepCreator();
        stepCreator.setName(name);
        stepCreator.setJobStepDefinitionId(currentStepDefId);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for (CucJobStepProperty prop : list) {
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
        stepCreator.setJobId(currentJobId);

        primeException();
        try {
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
    // * Job Target Service Test steps                                                    *
    // ************************************************************************************

    @When("^I configure the job target service$")
    public void setJobTargetConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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

        primeException();
        try {
            jobTargetService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular job target item$")
    public void createARegularTarget()
            throws Exception {

        JobTargetCreator targetCreator = prepareDefaultJobTargetCreator();
        stepData.put("JobTargetCreator", targetCreator);

        primeException();
        try {
            stepData.remove("JobTarget");
            JobTarget target = jobTargetService.create(targetCreator);
            stepData.put("JobTarget", target);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last job target in the database$")
    public void findLastJobTarget()
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");

        primeException();
        try {
            stepData.remove("JobTarget");
            JobTarget targetFound = jobTargetService.find(target.getScopeId(), target.getId());
            stepData.put("JobTarget", targetFound);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last job target in the database$")
    public void deleteLastJobTarget()
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");

        primeException();
        try {
            jobTargetService.delete(target.getScopeId(), target.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the job target target id$")
    public void updateJobTargetTargetId()
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        JobTargetCreator targetCreator = (JobTargetCreator) stepData.get("JobTargetCreator");

        targetCreator.setJobTargetId(getKapuaId());
        stepData.put("JobTargetCreator", targetCreator);
        target.setJobTargetId(targetCreator.getJobTargetId());

        primeException();
        try {
            target = jobTargetService.update(target);
            stepData.put("JobTarget", target);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the job target step number to (\\d+)$")
    public void setTargetStepIndex(int i)
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        target.setStepIndex(i);

        primeException();
        try {
            target = jobTargetService.update(target);
            stepData.put("JobTarget", target);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the job target step status to \"(.+)\"$")
    public void setTargetStepStatus(String stat)
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        target.setStatus(parseJobTargetStatusFromString(stat));

        primeException();
        try {
            target = jobTargetService.update(target);
            stepData.put("JobTarget", target);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the job target step exception message to \"(.+)\"$")
    public void setTargetStepExceptionMessage(String text)
            throws Exception {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        Exception kex = new Exception(text);
        target.setException(kex);

        primeException();
        try {
            target = jobTargetService.update(target);
            stepData.put("JobTarget", target);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the targets in the current scope$")
    public void countTargetsForJob()
            throws Exception {

        JobTargetQuery tmpQuery = jobTargetFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = jobTargetService.count(tmpQuery);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query the targets for the current job$")
    public void queryTargetsForJob()
            throws Exception {

        Job job = (Job) stepData.get("Job");
        JobTargetQuery tmpQuery = jobTargetFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", job.getId()));

        primeException();
        try {
            stepData.remove("JobTargetList");
            stepData.remove("Count");
            JobTargetListResult targetList = jobTargetService.query(tmpQuery);
            stepData.put("JobTargetList", targetList);
            stepData.put("Count", targetList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The target step index is indeed (\\d+)$")
    public void checkTargetStepIndex(int i) {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        assertEquals(String.format("The step index should be %d but is in fact %d.", i, target.getStepIndex()), i, target.getStepIndex());
    }

    @Then("^The target step exception message is indeed \"(.+)\"$")
    public void checkTargetStepExceptionMessage(String text) {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        assertEquals(text, target.getException().getMessage());
    }

    @Then("^The target step status is indeed \"(.+)\"$")
    public void checkTargetStepStatus(String stat) {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        assertEquals(parseJobTargetStatusFromString(stat), target.getStatus());
    }

    @Then("^The job target matches the creator$")
    public void checkJobTargetItemAgainstCreator() {

        JobTarget target = (JobTarget) stepData.get("JobTarget");
        JobTargetCreator targetCreator = (JobTargetCreator) stepData.get("JobTargetCreator");

        assertEquals(targetCreator.getJobId(), target.getJobId());
        assertEquals(targetCreator.getJobTargetId(), target.getJobTargetId());
        assertEquals(targetCreator.getScopeId(), target.getScopeId());
    }

    @Then("^There is no such job target item in the database$")
    public void checkThatNoTargetWasFound() {
        assertNull("Unexpected job target item found!", stepData.get("JobTarget"));
    }

    @When("^I test the sanity of the job target factory$")
    public void testTheJobTargetFactory() {

        assertNotNull(jobTargetFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(jobTargetFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(jobTargetFactory.newListResult());
        assertNotNull(jobTargetFactory.newQuery(SYS_SCOPE_ID));
    }
    // ************************************************************************************
    // * Job Execution Service Test steps                                                 *
    // ************************************************************************************

    @When("^I configure the job execution service$")
    public void setJobExecutionConfigurationValue(List<CucConfig> cucConfigs) throws Exception {

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
            jobExecutionService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular job execution item$")
    public void createARegularExecution() throws Exception {

        JobExecutionCreator executionCreator = prepareDefaultJobExecutionCreator();
        stepData.put("JobExecutionCreator", executionCreator);

        primeException();
        try {
            stepData.remove("JobExecution");
            JobExecution execution = jobExecutionService.create(executionCreator);
            stepData.put("JobExecution", execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the job id for the execution item$")
    public void updateJobIdForExecution()
            throws Exception {

        Job job = (Job) stepData.get("Job");
        JobExecution execution = (JobExecution) stepData.get("JobExecution");
        execution.setJobId(job.getId());

        primeException();
        try {
            execution = jobExecutionService.update(execution);
            stepData.put("JobExecution", execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I update the end time of the execution item$")
    public void updateJobExecutionEndTime()
            throws Exception {

        JobExecution execution = (JobExecution) stepData.get("JobExecution");

        primeException();
        try {
            execution.setEndedOn(DateTime.now().toDate());
            execution = jobExecutionService.update(execution);
            stepData.put("JobExecution", execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the last job execution in the database$")
    public void findLastJobExecution()
            throws Exception {

        JobExecution execution = (JobExecution) stepData.get("JobExecution");

        primeException();
        try {
            stepData.remove("JobExecutionFound");
            JobExecution foundExecution = jobExecutionService.find(execution.getScopeId(), execution.getId());
            stepData.put("JobExecutionFound", foundExecution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the last job execution in the database$")
    public void deleteLastJobExecution()
            throws Exception {

        JobExecution execution = (JobExecution) stepData.get("JobExecution");

        primeException();
        try {
            jobExecutionService.delete(execution.getScopeId(), execution.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the execution items for the current job$")
    public void countExecutionsForJob()
            throws Exception {

        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", job.getId()));

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = jobExecutionService.count(tmpQuery);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the execution items for the current job$")
    public void queryExecutionsForJob()
            throws Exception {

        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", job.getId()));

        primeException();
        try {
            stepData.remove("JobExecutionList");
            stepData.remove("Count");
            JobExecutionListResult resultList = jobExecutionService.query(tmpQuery);
            Long itemCount = (long) resultList.getSize();
            stepData.put("JobExecutionList", resultList);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

//    @When("^I update the job target target id$")
//    public void updateJobTargetTargetId() throws Exception {
//
//        targetData.targetCreator.setJobTargetId(commonData.getRandomId());
//        targetData.target.setJobTargetId(targetData.targetCreator.getJobTargetId());
//
//        try {
//            primeException();
//            targetData.target = targetService.update(targetData.target);
//        } catch (KapuaException ex) {
//            verifyException(ex);
//        }
//    }
//
//    @When("^I query the targets for the current job$")
//    public void queryTargetsForJob()
//            throws Exception {
//
//        JobTargetQuery tmpQuery = targetFactory.newQuery(commonData.currentScopeId);
//        tmpQuery.setPredicate(attributeIsEqualTo("jobId", jobData.job.getId()));
//
//        try {
//            primeException();
//            targetData.targetList = targetService.query(tmpQuery);
//            commonData.itemCount = targetData.targetList.getSize();
//        } catch (KapuaException ex) {
//            verifyException(ex);
//        }
//    }

    @Then("^The job execution matches the creator$")
    public void checkJobExecutionItemAgainstCreator() {

        JobExecutionCreator executionCreator = (JobExecutionCreator) stepData.get("JobExecutionCreator");
        JobExecution execution = (JobExecution) stepData.get("JobExecution");

        assertEquals(executionCreator.getScopeId(), execution.getScopeId());
        assertEquals(executionCreator.getJobId(), execution.getJobId());
        assertEquals(executionCreator.getStartedOn(), execution.getStartedOn());
    }

    @Then("^The job execution items match$")
    public void checkJobExecutionItems() {

        JobExecution execution = (JobExecution) stepData.get("JobExecution");
        JobExecution foundExecution = (JobExecution) stepData.get("JobExecutionFound");

        assertEquals(execution.getScopeId(), foundExecution.getScopeId());
        assertEquals(execution.getJobId(), foundExecution.getJobId());
        assertEquals(execution.getStartedOn(), foundExecution.getStartedOn());
        assertEquals(execution.getEndedOn(), foundExecution.getEndedOn());
    }

    @Then("^There is no such job execution item in the database$")
    public void checkThatNoExecutionWasFound() {
        assertNull("Unexpected job execution item found!", stepData.get("JobExecutionFound"));
    }

    @When("^I test the sanity of the job execution factory$")
    public void testTheJobExecutionFactory() {

        assertNotNull(jobExecutionFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(jobExecutionFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(jobExecutionFactory.newListResult());
        assertNotNull(jobExecutionFactory.newQuery(SYS_SCOPE_ID));
    }

    // ************************************************************************************
    // * Private helper functions                                                         *
    // ************************************************************************************
    private JobStepType getTypeFromString(String type) {

        if (type.trim().toUpperCase().equals("TARGET")) {
            return JobStepType.TARGET;
        } else {
            return JobStepType.GENERIC;
        }
    }

    private JobStepDefinitionCreator prepareDefaultJobStepDefinitionCreator() {

        JobStepDefinitionCreator tmpCr = jobStepDefinitionFactory.newCreator(getCurrentScopeId());
        tmpCr.setName(String.format("DefinitionName_%d", random.nextInt()));
        tmpCr.setDescription("DefinitionDescription");
        tmpCr.setReaderName("DefinitionReader");
        tmpCr.setProcessorName("DefinitionProcessor");
        tmpCr.setWriterName("DefinitionWriter");
        tmpCr.setStepType(JobStepType.TARGET);

        return tmpCr;
    }

    private JobStepCreator prepareDefaultJobStepCreator() {

        JobStepCreator tmpCr = jobStepFactory.newCreator(getCurrentScopeId());
        tmpCr.setName(String.format("StepName_%d", random.nextInt()));
        tmpCr.setDescription("StepDescription");

        return tmpCr;
    }

    private JobTargetCreator prepareDefaultJobTargetCreator() {

        KapuaId currentJobId = (KapuaId) stepData.get("CurrentJobId");
        JobTargetCreator tmpCr = jobTargetFactory.newCreator(getCurrentScopeId());

        tmpCr.setJobId(currentJobId);
        tmpCr.setJobTargetId(getKapuaId());

        return tmpCr;
    }

    private JobTargetStatus parseJobTargetStatusFromString(String stat) {

        switch (stat.toUpperCase().trim()) {
            case "PROCESS_AWAITING": return JobTargetStatus.PROCESS_AWAITING;
            case "PROCESS_FAILED": return JobTargetStatus.PROCESS_FAILED;
            case "PROCESS_OK": return JobTargetStatus.PROCESS_OK;
            default: return JobTargetStatus.PROCESS_FAILED;
        }
    }

    private JobExecutionCreator prepareDefaultJobExecutionCreator() {

        KapuaId currentJobId = (KapuaId) stepData.get("CurrentJobId");
        JobExecutionCreator tmpCr = jobExecutionFactory.newCreator(getCurrentScopeId());

        tmpCr.setJobId(currentJobId);
        tmpCr.setStartedOn(DateTime.now().toDate());

        return tmpCr;
    }
}
