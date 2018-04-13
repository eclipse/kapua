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
package org.eclipse.kapua.service.job.targets;

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
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;
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

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobStepService.feature scenarios.     *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobTargetsServiceTestSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKapuaSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_STEP_TABLES = "job_target_drop.sql";

    private static final KapuaId ROOT_ID = new KapuaEid(BigInteger.ONE);

    // Interstep scratchpads
    private CommonData commonData;
    private JobData jobData;
    private TargetData targetData;

    // Step service objects
    private JobTargetService targetService;
    private JobTargetFactory targetFactory;

    // Default constructor
    @Inject
    public JobTargetsServiceTestSteps(CommonData commonData, JobData jobData, TargetData targetData) {
        this.commonData = commonData;
        this.jobData = jobData;
        this.targetData = targetData;
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
        targetFactory = new JobTargetFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.targets.JobTargetFactory.class, targetFactory);
        targetService = new JobTargetServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.targets.JobTargetService.class, targetService);

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

    @Given("^A regular job target item$")
    public void createARegularTarget()
            throws Exception {

        targetData.targetCreator = prepareDefaultCreator();
        try {
            commonData.primeException();
            targetData.target = targetService.create(targetData.targetCreator);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I search for the last job target in the database$")
    public void findLastJobTarget()
            throws Exception {

        try {
            commonData.primeException();
            targetData.target = targetService.find(targetData.target.getScopeId(), targetData.target.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I delete the last job target in the database$")
    public void deleteLastJobTarget()
            throws Exception {

        try {
            commonData.primeException();
            targetService.delete(targetData.target.getScopeId(), targetData.target.getId());
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the job target target id$")
    public void updateJobTargetTargetId()
            throws Exception {

        targetData.targetCreator.setJobTargetId(commonData.getRandomId());
        targetData.target.setJobTargetId(targetData.targetCreator.getJobTargetId());

        try {
            commonData.primeException();
            targetData.target = targetService.update(targetData.target);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the job target step number to (\\d+)$")
    public void setTargetStepIndex(int i)
            throws Exception {

        try {
            commonData.primeException();
            targetData.target.setStepIndex(i);
            targetData.target = targetService.update(targetData.target);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the job target step status to \"(.+)\"$")
    public void setTargetStepStatus(String stat)
            throws Exception {

        try {
            commonData.primeException();
            targetData.target.setStatus(parseJobTargetStatusFromString(stat));
            targetData.target = targetService.update(targetData.target);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I update the job target step exception message to \"(.+)\"$")
    public void setTargetStepExceptionMessage(String text)
            throws Exception {

        Exception kex = new Exception(text);

        try {
            commonData.primeException();
            targetData.target.setException(kex);
            targetData.target = targetService.update(targetData.target);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the targets in the current scope$")
    public void countTargetsForJob()
            throws Exception {

        JobTargetQuery tmpQuery = targetFactory.newQuery(commonData.currentScopeId);

        try {
            commonData.primeException();
            commonData.itemCount = targetService.count(tmpQuery);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query the targets for the current job$")
    public void queryTargetsForJob()
            throws Exception {

        JobTargetQuery tmpQuery = targetFactory.newQuery(commonData.currentScopeId);
        tmpQuery.setPredicate(AttributePredicateImpl.attributeIsEqualTo("jobId", jobData.job.getId()));

        try {
            commonData.primeException();
            targetData.targetList = targetService.query(tmpQuery);
            commonData.itemCount = targetData.targetList.getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Then("^The target step index is indeed (\\d+)$")
    public void checkTargetStepIndex(int i) {

        assertEquals(String.format("The step index should be %d but is in fact %d.", i, targetData.target.getStepIndex()), i, targetData.target.getStepIndex());
    }

    @Then("^The target step exception message is indeed \"(.+)\"$")
    public void checkTargetStepExceptionMessage(String text) {

        assertEquals(text, targetData.target.getException().getMessage());
    }

    @Then("^The target step status is indeed \"(.+)\"$")
    public void checkTargetStepStatus(String stat) {

        assertEquals(parseJobTargetStatusFromString(stat), targetData.target.getStatus());
    }

    @Then("^The job target matches the creator$")
    public void checkJobTargetItemAgainstCreator() {

        assertEquals(targetData.targetCreator.getJobId(), targetData.target.getJobId());
        assertEquals(targetData.targetCreator.getJobTargetId(), targetData.target.getJobTargetId());
        assertEquals(targetData.targetCreator.getScopeId(), targetData.target.getScopeId());
    }

    @Then("^There is no such job target item in the database$")
    public void checkThatNoTargetWasFound() {
        assertNull("Unexpected job target item found!", targetData.target);
    }

    @When("^I test the sanity of the job target factory$")
    public void testTheJobTargetFactory() {

        assertNotNull(targetFactory.newCreator(ROOT_ID));
        assertNotNull(targetFactory.newEntity(ROOT_ID));
        assertNotNull(targetFactory.newListResult());
        assertNotNull(targetFactory.newQuery(ROOT_ID));
    }

// ************************************************************************************
// * Private helper functions                                                         *
// ************************************************************************************

    private JobTargetCreator prepareDefaultCreator() {

        JobTargetCreator tmpCr = targetFactory.newCreator(commonData.currentScopeId);
        tmpCr.setJobId(jobData.currentJobId);
        tmpCr.setJobTargetId(commonData.getRandomId());

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
}
