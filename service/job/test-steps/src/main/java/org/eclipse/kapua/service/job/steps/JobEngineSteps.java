/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.steps;

import com.google.inject.Singleton;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.service.job.Job;

import javax.inject.Inject;

@Singleton
public class JobEngineSteps extends JobServiceTestBase {

    private JobEngineService jobEngineService;
    private JobEngineFactory jobEngineFactory;

    @Inject
    public JobEngineSteps(StepData stepData) {
        super(stepData);
    }

    @Before(value = "@env_docker or @env_docker_base or @env_none", order = 10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();

        jobEngineService = locator.getService(JobEngineService.class);
        jobEngineFactory = locator.getFactory(JobEngineFactory.class);
    }

    @When("I start a job")
    public void startJob() throws Exception {
        primeException();
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        try {
            JobStartOptions jobStartOptions = jobEngineFactory.newJobStartOptions();
            jobStartOptions.setEnqueue(true);
            jobEngineService.startJob(getCurrentScopeId(), currentJobId, jobStartOptions);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @When("I restart a job")
    public void restartJob() throws Exception {
        primeException();
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        try {
            JobStartOptions jobStartOptions = jobEngineFactory.newJobStartOptions();
            jobStartOptions.setResetStepIndex(true);
            jobStartOptions.setFromStepIndex(0);
            jobStartOptions.setEnqueue(true);
            jobEngineService.startJob(getCurrentScopeId(), currentJobId, jobStartOptions);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @And("I stop the job")
    public void iStopTheJob() throws Exception {
        Job job = (Job) stepData.get("Job");
        try {
            primeException();
            jobEngineService.stopJob(getCurrentScopeId(), job.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }
}
