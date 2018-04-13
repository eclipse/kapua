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
package org.eclipse.kapua.service.job.execution;

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
import org.eclipse.kapua.service.job.execution.internal.JobExecutionFactoryImpl;
import org.eclipse.kapua.service.job.execution.internal.JobExecutionServiceImpl;
import org.eclipse.kapua.service.job.internal.JobData;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.joda.time.DateTime;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.acl.Permission;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobExecutionService.feature scenarios.       *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobExecutionServiceTestSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKapuaSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_STEP_TABLES = "job_execution_drop.sql";

    private static final KapuaId ROOT_ID = new KapuaEid(BigInteger.ONE);

    // Interstep scratchpads
    private CommonData commonData;
    private JobData jobData;
    private ExecutionData executionData;

    // Step service objects
    private JobExecutionService executionService;
    private JobExecutionFactory executionFactory;

    // Default constructor
    @Inject
    public JobExecutionServiceTestSteps(CommonData commonData, JobData jobData, ExecutionData executionData) {
        this.commonData = commonData;
        this.jobData = jobData;
        this.executionData = executionData;
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
        executionFactory = new JobExecutionFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.execution.JobExecutionFactory.class, executionFactory);
        executionService = new JobExecutionServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.execution.JobExecutionService.class, executionService);

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

    @Given("^A mock test step$")
    public void mockTestStep() {

    }

    @Then("^Nothing happens$")
    public void checkThatNothingHappened() {

        assertTrue(true);
    }

    @Given("^A regular job execution item$")
    public void createARegularExecution() throws Exception {

        executionData.executionCreator = prepareDefaultCreator();
        try {
            commonData.primeException();
            executionData.execution = executionService.create(executionData.executionCreator);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the job id for the execution item$")
    public void updateJobIdForExecution()
            throws Exception {

        try {
            commonData.primeException();
            executionData.execution.setJobId(jobData.job.getId());
            executionData.execution = executionService.update(executionData.execution);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the end time of the execution item$")
    public void updateJobExecutionEndTime()
            throws Exception {

        try {
            commonData.primeException();
            executionData.execution.setEndedOn(DateTime.now().toDate());
            executionData.execution = executionService.update(executionData.execution);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I search for the last job execution in the database$")
    public void findLastJobExecution()
            throws Exception {

        try {
            commonData.primeException();
            executionData.foundExecution = executionService.find(executionData.execution.getScopeId(), executionData.execution.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I delete the last job execution in the database$")
    public void deleteLastJobExecution()
            throws Exception {

        try {
            commonData.primeException();
            executionService.delete(executionData.execution.getScopeId(), executionData.execution.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the execution items for the current job$")
    public void countExecutionsForJob()
            throws Exception {

        JobExecutionQuery tmpQuery = executionFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", jobData.job.getId()));

        try {
            commonData.primeException();
            commonData.itemCount = executionService.count(tmpQuery);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query for the execution items for the current job$")
    public void queryExecutionsForJob()
            throws Exception {

        JobExecutionQuery tmpQuery = executionFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", jobData.job.getId()));

        try {
            commonData.primeException();
            executionData.resultList = executionService.query(tmpQuery);
            commonData.itemCount = executionData.resultList.getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

//    @When("^I update the job target target id$")
//    public void updateJobTargetTargetId() throws Exception {
//
//        targetData.targetCreator.setJobTargetId(commonData.getRandomId());
//        targetData.target.setJobTargetId(targetData.targetCreator.getJobTargetId());
//
//        try {
//            commonData.primeException();
//            targetData.target = targetService.update(targetData.target);
//        } catch (KapuaException ex) {
//            commonData.verifyException(ex);
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
//            commonData.primeException();
//            targetData.targetList = targetService.query(tmpQuery);
//            commonData.itemCount = targetData.targetList.getSize();
//        } catch (KapuaException ex) {
//            commonData.verifyException(ex);
//        }
//    }

    @Then("^The job execution matches the creator$")
    public void checkJobExecutionItemAgainstCreator() {

        assertEquals(executionData.executionCreator.getScopeId(), executionData.execution.getScopeId());
        assertEquals(executionData.executionCreator.getJobId(), executionData.execution.getJobId());
        assertEquals(executionData.executionCreator.getStartedOn(), executionData.execution.getStartedOn());
    }

    @Then("^The job execution items match match$")
    public void checkJobExecutionItems() {

        assertEquals(executionData.execution.getScopeId(), executionData.foundExecution.getScopeId());
        assertEquals(executionData.execution.getJobId(), executionData.foundExecution.getJobId());
        assertEquals(executionData.execution.getStartedOn(), executionData.foundExecution.getStartedOn());
        assertEquals(executionData.execution.getEndedOn(), executionData.foundExecution.getEndedOn());
    }

    @Then("^There is no such job execution item in the database$")
    public void checkThatNoExecutionWasFound() {
        assertNull("Unexpected job execution item found!", executionData.foundExecution);
    }

    @When("^I test the sanity of the job execution factory$")
    public void testTheJobExecutionFactory() {

        assertNotNull(executionFactory.newCreator(ROOT_ID));
        assertNotNull(executionFactory.newEntity(ROOT_ID));
        assertNotNull(executionFactory.newListResult());
        assertNotNull(executionFactory.newQuery(ROOT_ID));
    }

// ************************************************************************************
// * Private helper functions                                                         *
// ************************************************************************************

    private JobExecutionCreator prepareDefaultCreator() {

        JobExecutionCreator tmpCr = executionFactory.newCreator(commonData.currentScopeId);
        tmpCr.setJobId(jobData.currentJobId);
        tmpCr.setStartedOn(DateTime.now().toDate());

        return tmpCr;
    }
}
