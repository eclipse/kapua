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
import org.eclipse.kapua.qa.common.CucConfig;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.internal.JobFactoryImpl;
import org.eclipse.kapua.service.job.internal.JobServiceImpl;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Job service steps                                                              *
    // ************************************************************************************

    @When("^I configure the job service$")
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
        stepData.remove("Job");
        stepData.remove("CurrentJobId");

        primeException();
        try {
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

        KapuaId currentScopeId = getCurrentScopeId();
        JobCreator tmpCreator;

        primeException();
        try {
            for (int i = 0; i < num; i++) {
                tmpCreator = jobFactory.newCreator(currentScopeId);
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

        stepData.remove("Job");
        stepData.remove("CurrentJobId");

        primeException();
        try {
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

        stepData.remove("Job");

        primeException();
        try {
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

        stepData.remove("Job");

        primeException();
        try {
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

        stepData.remove("Job");

        primeException();
        try {
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

        stepData.remove("Job");

        primeException();
        try {
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
        KapuaId currentScopeId = getCurrentScopeId();

        stepData.remove("Job");

        primeException();
        try {
            Job job = jobService.find(currentScopeId, currentJobId);
            stepData.put("Job", job);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the jobs in the database$")
    public void countJobsInDatabase()
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());

        stepData.remove("Count");

        primeException();
        try {
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

        stepData.remove("Count");

        primeException();
        try {
            Long count = Long.valueOf(jobService.query(tmpQuery).getSize());
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

        stepData.remove("Count");

        primeException();
        try {
            Long count = Long.valueOf(jobService.query(tmpQuery).getSize());
            stepData.put("Count", count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for the job with the name \"(.+)\"$")
    public void queryForJobWithName(String name)
            throws Exception {

        JobQuery tmpQuery = jobFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo(JobAttributes.NAME, name));

        stepData.remove("Job");

        primeException();
        try {
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
}
