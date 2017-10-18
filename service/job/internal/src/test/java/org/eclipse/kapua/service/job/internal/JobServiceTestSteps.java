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
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.job.common.CommonData;
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

    // Default constructor
    @Inject
    public JobServiceTestSteps(CommonData commonData, JobData jobData) {
        this.commonData = commonData;
        this.jobData = jobData;
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

    @Given("^A regular job creator with the name \"(.+)\"$")
    public void prepareARegularJobCreator(String name) {
        jobData.jobCreator = jobFactory.newCreator(commonData.currentScopeId);
        jobData.jobCreator.setName(name);
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
}
