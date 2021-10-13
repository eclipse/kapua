/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionAttributes;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.joda.time.DateTime;
import org.junit.Assert;

import javax.inject.Inject;

@Singleton
public class JobExecutionServiceSteps extends JobServiceTestBase {

    private JobExecutionService jobExecutionService;
    private JobExecutionFactory jobExecutionFactory;

    @Inject
    public JobExecutionServiceSteps(StepData stepData) {
        super(stepData);
    }

    @Before(value = "@env_docker or @env_docker_base or @env_none", order = 10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();

        jobExecutionService = locator.getService(JobExecutionService.class);
        jobExecutionFactory = locator.getFactory(JobExecutionFactory.class);
    }

    @Given("A regular job execution item")
    public void createARegularExecution() throws Exception {
        JobExecutionCreator executionCreator = prepareDefaultJobExecutionCreator();
        stepData.put("JobExecutionCreator", executionCreator);
        primeException();
        try {
            stepData.remove(JOB_EXECUTION);
            JobExecution execution = jobExecutionService.create(executionCreator);
            stepData.put(JOB_EXECUTION, execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I update the job id for the execution item")
    public void updateJobIdForExecution() throws Exception {
        Job job = (Job) stepData.get("Job");
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        execution.setJobId(job.getId());
        primeException();
        try {
            execution = jobExecutionService.update(execution);
            stepData.put(JOB_EXECUTION, execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I update the end time of the execution item")
    public void updateJobExecutionEndTime() throws Exception {
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        primeException();
        try {
            execution.setEndedOn(DateTime.now().toDate());
            execution = jobExecutionService.update(execution);
            stepData.put(JOB_EXECUTION, execution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for the last job execution in the database")
    public void findLastJobExecution() throws Exception {
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        primeException();
        try {
            stepData.remove("JobExecutionFound");
            JobExecution foundExecution = jobExecutionService.find(execution.getScopeId(), execution.getId());
            stepData.put("JobExecutionFound", foundExecution);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the last job execution in the database")
    public void deleteLastJobExecution() throws Exception {
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        primeException();
        try {
            jobExecutionService.delete(execution.getScopeId(), execution.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I count the execution items for the current job")
    public void countExecutionsForJob() throws Exception {
        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobExecutionAttributes.JOB_ID, job.getId(), AttributePredicate.Operator.EQUAL));
        updateCount(() -> (int) jobExecutionService.count(tmpQuery));
    }

    @Then("I query for the execution items for the current job")
    public void queryExecutionsForJobWithPackages() throws Exception {
        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobExecutionAttributes.JOB_ID, job.getId(), AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(JOB_EXECUTION_LIST);
            JobExecutionListResult resultList = jobExecutionService.query(tmpQuery);
            stepData.put(JOB_EXECUTION_LIST, resultList);
            stepData.updateCount(resultList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I query for the execution items for the current job and I count {int} or more")
    public void iQueryForTheExecutionItemsForTheCurrentJobAndICountOrMore(int numberOfExecutions) throws Exception {
        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobExecutionAttributes.JOB_ID, job.getId(), AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(JOB_EXECUTION_LIST);
            JobExecutionListResult resultList = jobExecutionService.query(tmpQuery);
            stepData.put(JOB_EXECUTION_LIST, resultList);
            stepData.updateCount(resultList.getSize());
            Assert.assertTrue(resultList.getSize() >= numberOfExecutions);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }


    @Then("I query for the execution items for the current job and I count {int}")
    public void queryExecutionsForJob(int num) throws Exception {
        Job job = (Job) stepData.get("Job");
        JobExecutionQuery tmpQuery = jobExecutionFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobExecutionAttributes.JOB_ID, job.getId(), AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(JOB_EXECUTION_LIST);
            JobExecutionListResult resultList = jobExecutionService.query(tmpQuery);
            stepData.put(JOB_EXECUTION_LIST, resultList);
            stepData.updateCount(resultList.getSize());
            Assert.assertEquals(num, resultList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I confirm the executed job is finished")
    public void confirmJobIsFinished() {
        JobExecutionListResult resultList = (JobExecutionListResult) stepData.get(JOB_EXECUTION_LIST);
        JobExecution jobExecution = resultList.getFirstItem();
        Assert.assertNotNull("Job execution end date cannot be null!", jobExecution.getEndedOn());
        Assert.assertNotNull("Job execution log cannot be null!", jobExecution.getLog());
    }

    @Then("The job execution matches the creator")
    public void checkJobExecutionItemAgainstCreator() {
        JobExecutionCreator executionCreator = (JobExecutionCreator) stepData.get("JobExecutionCreator");
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        Assert.assertEquals(executionCreator.getScopeId(), execution.getScopeId());
        Assert.assertEquals(executionCreator.getJobId(), execution.getJobId());
        Assert.assertEquals(executionCreator.getStartedOn(), execution.getStartedOn());
    }

    @Then("The job execution items match")
    public void checkJobExecutionItems() {
        JobExecution execution = (JobExecution) stepData.get(JOB_EXECUTION);
        JobExecution foundExecution = (JobExecution) stepData.get("JobExecutionFound");
        Assert.assertEquals(execution.getScopeId(), foundExecution.getScopeId());
        Assert.assertEquals(execution.getJobId(), foundExecution.getJobId());
        Assert.assertEquals(execution.getStartedOn(), foundExecution.getStartedOn());
        Assert.assertEquals(execution.getEndedOn(), foundExecution.getEndedOn());
    }

    @Then("There is no such job execution item in the database")
    public void checkThatNoExecutionWasFound() {
        Assert.assertNull("Unexpected job execution item found!", stepData.get("JobExecutionFound"));
    }

    @When("I test the sanity of the job execution factory")
    public void testTheJobExecutionFactory() {
        Assert.assertNotNull(jobExecutionFactory.newCreator(SYS_SCOPE_ID));
        Assert.assertNotNull(jobExecutionFactory.newEntity(SYS_SCOPE_ID));
        Assert.assertNotNull(jobExecutionFactory.newListResult());
        Assert.assertNotNull(jobExecutionFactory.newQuery(SYS_SCOPE_ID));
    }

    //
    // Private methods
    //

    private JobExecutionCreator prepareDefaultJobExecutionCreator() {
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        JobExecutionCreator tmpCr = jobExecutionFactory.newCreator(getCurrentScopeId());
        tmpCr.setJobId(currentJobId);
        tmpCr.setStartedOn(DateTime.now().toDate());
        return tmpCr;
    }
}
