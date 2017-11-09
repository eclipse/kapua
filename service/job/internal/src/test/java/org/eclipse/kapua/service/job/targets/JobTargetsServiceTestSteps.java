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
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.common.CommonData;
import org.eclipse.kapua.service.job.internal.JobData;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.targets.internal.JobTargetFactoryImpl;
import org.eclipse.kapua.service.job.targets.internal.JobTargetServiceImpl;
import org.eclipse.kapua.service.liquibase.KapuaLiquibaseClient;
import org.eclipse.kapua.test.MockedLocator;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.security.acl.Permission;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

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
        AuthorizationService mockedAuthorization = mock(AuthorizationService.class);
        // TODO: Check why does this line needs an explicit cast!
        Mockito.doNothing().when(mockedAuthorization).checkPermission(
                (org.eclipse.kapua.service.authorization.permission.Permission) any(Permission.class));
        mockLocator.setMockedService(org.eclipse.kapua.service.authorization.AuthorizationService.class,
                mockedAuthorization);

        // Inject mocked Permission Factory
        PermissionFactory mockedPermissionFactory = mock(PermissionFactory.class);
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

}
