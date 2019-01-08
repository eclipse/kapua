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
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobAttributes;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionFactoryImpl;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionServiceImpl;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.test.MockedLocator;
import org.joda.time.DateTime;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobExecutionService.feature scenarios.       *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobExecutionServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutionServiceSteps.class);

    // Step service objects
    private JobExecutionService jobExecutionService;
    private JobExecutionFactory jobExecutionFactory;

    // Default constructor
    @Inject
    public JobExecutionServiceSteps(StepData stepData, DBHelper dbHelper) {

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

    @When("^I configure the job execution service$")
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
            jobExecutionService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("^A regular job execution item$")
    public void createARegularExecution() throws Exception {

        JobExecutionCreator executionCreator = prepareDefaultCreator();
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
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.ENTITY_ID, job.getId()));

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
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.ENTITY_ID, job.getId()));

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

    private JobExecutionCreator prepareDefaultCreator() {

        KapuaId currentJobId = (KapuaId) stepData.get("CurrentJobId");
        JobExecutionCreator tmpCr = jobExecutionFactory.newCreator(getCurrentScopeId());

        tmpCr.setJobId(currentJobId);
        tmpCr.setStartedOn(DateTime.now().toDate());

        return tmpCr;
    }
}
