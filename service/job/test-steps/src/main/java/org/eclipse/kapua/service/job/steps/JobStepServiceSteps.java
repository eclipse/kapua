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
import org.eclipse.kapua.qa.common.cucumber.CucJobStepProperty;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.junit.Assert;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class JobStepServiceSteps extends JobServiceTestBase {

    // Job Step definition service objects
    private JobStepService jobStepService;
    private JobStepFactory jobStepFactory;

    @Inject
    public JobStepServiceSteps(StepData stepData) {
        super(stepData);
    }

    @Before(value = "@env_docker or @env_docker_base or @env_none", order = 10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @After(value = "@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();

        jobStepService = locator.getService(JobStepService.class);
        jobStepFactory = locator.getFactory(JobStepFactory.class);
    }

    @Given("A regular step creator with the name {string} and the following properties")
    public void prepareARegularStepCreatorWithPropertyList(String name, List<CucJobStepProperty> list) {
        JobStepCreator stepCreator;
        KapuaId currentStepDefId = (KapuaId) stepData.get(CURRENT_JOB_STEP_DEFINITION_ID);
        stepCreator = prepareDefaultJobStepCreator();
        stepCreator.setName(name);
        stepCreator.setJobStepDefinitionId(currentStepDefId);
        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for (CucJobStepProperty prop : list) {
            tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
        }
        stepCreator.setJobStepProperties(tmpPropLst);
        stepData.put(JOB_STEP_CREATOR, stepCreator);
    }

    @And("I create a regular step creator with the name {string} and properties")
    public void aRegularStepCreatorWithTheNameAndProperties(String name, List<CucJobStepProperty> tmpProperty) {
        JobStepCreator stepCreator;
        ArrayList<JobStepDefinition> jobStepDefinitions = (ArrayList<JobStepDefinition>) stepData.get(JOB_STEP_DEFINITIONS);
        ArrayList<JobStepCreator> stepCreators = new ArrayList<>();
        ArrayList<String> bundleIds = new ArrayList<>();
        String firstValue = tmpProperty.get(0).getValue();
        String[] values = firstValue.split(",");
        bundleIds.addAll(Arrays.asList(values));

        for (String bundleId : bundleIds) {
            for (JobStepDefinition stepDefinition : jobStepDefinitions) {
                stepCreator = prepareDefaultJobStepCreator();
                stepCreator.setName(name + stepCreators.size());
                stepCreator.setJobStepDefinitionId(stepDefinition.getId());
                List<JobStepProperty> tmpPropLst = new ArrayList<>();
                for (CucJobStepProperty prop : tmpProperty) {
                    if ((stepDefinition.getName().equals("Bundle Start") && prop.getName().equals("bundleId"))) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), bundleId));
                    } else if (stepDefinition.getName().equals("Bundle Stop") && prop.getName().equals("bundleId")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), bundleId));
                    } else if (stepDefinition.getName().equals("Command Execution") && prop.getName().equals("commandInput")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    } else if (stepDefinition.getName().equals("Configuration Put") && prop.getName().equals("configuration")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    } else if (stepDefinition.getName().equals("Asset Write") && prop.getName().equals("assets")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    } else if (stepDefinition.getName().equals("Package Download / Install") && prop.getName().equals("packageDownloadRequest")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    } else if (stepDefinition.getName().equals("Package Uninstall") && prop.getName().equals("packageUninstallRequest")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    } else if (prop.getName().equals("timeout")) {
                        tmpPropLst.add(jobStepFactory.newStepProperty(prop.getName(), prop.getType(), prop.getValue()));
                    }
                }
                stepCreator.setJobStepProperties(tmpPropLst);
                stepCreators.add(stepCreator);
                stepData.put(JOB_STEP_CREATOR, stepCreator);
                stepData.put("JobStepCreators", stepCreators);
            }
        }
    }

    @When("I create a new step entity from the existing creator")
    public void createAStepFromTheCreator() throws Exception {
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        JobStepCreator stepCreator = (JobStepCreator) stepData.get(JOB_STEP_CREATOR);
        stepCreator.setJobId(currentJobId);
        primeException();
        try {
            stepData.remove(JOB_STEP);
            stepData.remove(CURRENT_JOB_STEP_ID);
            JobStep step = jobStepService.create(stepCreator);
            stepData.put(JOB_STEP, step);
            stepData.put(CURRENT_JOB_STEP_ID, step.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I create a new step entities from the existing creator")
    public void iCreateANewStepEntitiesFromTheExistingCreator() throws Exception {
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        ArrayList<JobStepCreator> jobStepCreators = (ArrayList<JobStepCreator>) stepData.get("JobStepCreators");
        ArrayList<JobStep> jobSteps = new ArrayList<>();
        for (JobStepCreator jobStepCreator : jobStepCreators) {
            jobStepCreator.setJobId(currentJobId);
            primeException();
            try {
                stepData.remove(JOB_STEP);
                stepData.put("JobSteps", jobSteps);
                stepData.remove(CURRENT_JOB_STEP_ID);
                JobStep step = jobStepService.create(jobStepCreator);
                jobSteps.add(step);
                stepData.put(JOB_STEP, step);
                stepData.put("JobSteps", jobSteps);
                stepData.put(CURRENT_JOB_STEP_ID, step.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("I search the database for created job steps and I find {int}")
    public void searchJobSteps(int count) throws Exception {
        KapuaId currentJobId = (KapuaId) stepData.get(CURRENT_JOB_ID);
        primeException();
        try {
            JobStepQuery tmpQuery = jobStepFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(JobStepAttributes.JOB_ID, currentJobId, AttributePredicate.Operator.EQUAL));
            JobStepListResult jobStepListResult = jobStepService.query(tmpQuery);
            Assert.assertEquals(count, jobStepListResult.getSize());
        } catch (KapuaException ke) {
            verifyException(ke);
        }

    }

    @When("I change the step name to {string}")
    public void updateStepName(String name) throws Exception {
        JobStep step = (JobStep) stepData.get(JOB_STEP);
        step.setName(name);
        primeException();
        try {
            step = jobStepService.update(step);
            stepData.put(JOB_STEP, step);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I update the step with a new definition")
    public void updateStepDefinition() throws Exception {
        JobStep step = (JobStep) stepData.get(JOB_STEP);
        KapuaId currentStepDefId = (KapuaId) stepData.get(CURRENT_JOB_STEP_DEFINITION_ID);
        step.setJobStepDefinitionId(currentStepDefId);
        primeException();
        try {
            step = jobStepService.update(step);
            stepData.put(JOB_STEP, step);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for the last step in the database")
    public void findLastStep() throws Exception {
        JobStep step = (JobStep) stepData.get(JOB_STEP);
        primeException();
        try {
            JobStep newStep = jobStepService.find(step.getScopeId(), step.getId());
            stepData.put(JOB_STEP, newStep);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I query for a step with the name {string}")
    public void queryForNamedStep(String name) throws Exception {
        JobStepQuery tmpQuery = jobStepFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(JobStepAttributes.NAME, name, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove("JobStepList");
            JobStepListResult stepList = jobStepService.query(tmpQuery);
            stepData.put("JobStepList", stepList);
            stepData.updateCount(stepList.getSize());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I count the steps in the scope")
    public void countStepsInScope() throws Exception {
        updateCount(() -> (int) jobStepService.count(jobStepFactory.newQuery(getCurrentScopeId())));
    }

    @When("I delete the last step")
    public void deleteLastStep() throws Exception {
        JobStep step = (JobStep) stepData.get(JOB_STEP);
        primeException();
        try {
            jobStepService.delete(step.getScopeId(), step.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The step item matches the creator")
    public void checkStepItemAgainstCreator() {
        JobStep step = (JobStep) stepData.get(JOB_STEP);
        JobStepCreator stepCreator = (JobStepCreator) stepData.get(JOB_STEP_CREATOR);
        Assert.assertEquals(stepCreator.getJobId(), step.getJobId());
        Assert.assertEquals(stepCreator.getJobStepDefinitionId(), step.getJobStepDefinitionId());
        Assert.assertEquals(stepCreator.getName(), step.getName());
        Assert.assertEquals(stepCreator.getDescription(), step.getDescription());
        Assert.assertEquals(stepCreator.getStepIndex(), (Integer) step.getStepIndex());
        Assert.assertEquals(stepCreator.getStepProperties().size(), step.getStepProperties().size());
    }

    @Then("There is no such step item in the database")
    public void checkThatNoStepWasFound() {
        Assert.assertNull("Unexpected step item found!", stepData.get(JOB_STEP));
    }

    @When("I test the sanity of the step factory")
    public void testTheStepFactory() {
        Assert.assertNotNull(jobStepFactory.newCreator(SYS_SCOPE_ID));
        Assert.assertNotNull(jobStepFactory.newEntity(SYS_SCOPE_ID));
        Assert.assertNotNull(jobStepFactory.newListResult());
        Assert.assertNotNull(jobStepFactory.newQuery(SYS_SCOPE_ID));
        Assert.assertNotNull(jobStepFactory.newStepProperty("TestName", "TestType", "TestValue"));
    }

    //
    // Private methods
    //

    private JobStepCreator prepareDefaultJobStepCreator() {
        JobStepCreator tmpCr = jobStepFactory.newCreator(getCurrentScopeId());
        tmpCr.setName(String.format("StepName_%d", random.nextInt()));
        tmpCr.setDescription("StepDescription");
        return tmpCr;
    }

}
