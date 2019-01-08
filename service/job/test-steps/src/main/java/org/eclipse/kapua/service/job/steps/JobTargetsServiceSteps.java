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
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;
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
// * Implementation of Gherkin steps used in JobStepService.feature scenarios.     *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobTargetsServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTargetsServiceSteps.class);

    // Step service objects
    private JobTargetService jobTargetService;
    private JobTargetFactory jobTargetFactory;

    // Default constructor
    @Inject
    public JobTargetsServiceSteps(StepData stepData, DBHelper dbHelper) {

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
                bind(JobTargetService.class).toInstance(new JobTargetServiceImpl());
                bind(JobTargetFactory.class).toInstance(new JobTargetFactoryImpl());
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
        jobTargetService = locator.getService(JobTargetService.class);
        jobTargetFactory = locator.getFactory(JobTargetFactory.class);

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

    @When("^I configure the job target service$")
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

        JobTargetCreator targetCreator = prepareDefaultCreator();
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
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobAttributes.ENTITY_ID, job.getId()));

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
// * Private helper functions                                                         *
// ************************************************************************************

    private JobTargetCreator prepareDefaultCreator() {

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
}
