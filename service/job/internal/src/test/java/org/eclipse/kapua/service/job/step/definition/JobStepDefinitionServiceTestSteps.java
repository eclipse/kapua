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
package org.eclipse.kapua.service.job.step.definition;

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
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.JobJAXBContextProvider;
import org.eclipse.kapua.service.job.common.CommonData;
import org.eclipse.kapua.service.job.step.CucStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
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
import java.util.ArrayList;
import java.util.List;

// ****************************************************************************************
// * Implementation of Gherkin steps used in JobStepDefinitionService.feature scenarios.  *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

@ScenarioScoped
public class JobStepDefinitionServiceTestSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(JobStepDefinitionServiceTestSteps.class);

    private static final String DEFAULT_COMMONS_PATH = "../../../commons";
    private static final String DROP_STEP_DEFINITION_TABLES = "job_step_definition_drop.sql";

    private static final KapuaId ROOT_ID = new KapuaEid(BigInteger.ONE);

    // Interstep scratchpads
    CommonData commonData;
    StepDefinitionData stepDefinitionData;

    // Step definition service objects
    private JobStepDefinitionService stepDefinitionService;
    private JobStepDefinitionFactory stepDefinitionFactory;

    // Default constructor
    @Inject
    public JobStepDefinitionServiceTestSteps(CommonData commonData, StepDefinitionData stepDefinitionData) {
        this.commonData = commonData;
        this.stepDefinitionData = stepDefinitionData;
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
        stepDefinitionFactory = new JobStepDefinitionFactoryImpl();
        mockLocator.setMockedFactory(org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory.class, stepDefinitionFactory);
        stepDefinitionService = new JobStepDefinitionServiceImpl();
        mockLocator.setMockedService(org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService.class, stepDefinitionService);

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
        scriptSession(JobEntityManagerFactory.getInstance(), DROP_STEP_DEFINITION_TABLES);
        KapuaConfigurableServiceSchemaUtils.dropSchemaObjects(DEFAULT_COMMONS_PATH);
        KapuaSecurityUtils.clearSession();
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************
    @Given("^A regular step definition creator with the name \"(.*)\"$")
    public void prepareARegularStepDefinitionCreator(String name) {

        stepDefinitionData.stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionData.stepDefinitionCreator.setName(name);
    }

    @Given("^A regular definition creator with the name \"(.*)\" and (\\d+) properties$")
    public void prepareARegularStepDefinitionCreatorWithProperties(String name, int cnt) {

        stepDefinitionData.stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionData.stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        tmpPropLst.add(stepDefinitionFactory.newStepProperty("Property1", "Type1", null));
        tmpPropLst.add(stepDefinitionFactory.newStepProperty("Property2", "Type2", null));
        tmpPropLst.add(stepDefinitionFactory.newStepProperty("Property3", "Type3", null));

        stepDefinitionData.stepDefinitionCreator.setStepProperties(tmpPropLst);
    }

    @Given("^A regular step definition creator with the name \"(.*)\" and the following properties$")
    public void prepareARegularStepDefinitionCreatorWithPropertyList(String name, List<CucStepProperty> list) {

        stepDefinitionData.stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionData.stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for(CucStepProperty prop : list) {
            tmpPropLst.add(stepDefinitionFactory.newStepProperty(prop.getName(), prop.getType(), null));
        }

        stepDefinitionData.stepDefinitionCreator.setStepProperties(tmpPropLst);
    }

    @Given("^A regular step definition with the name \"(.*)\" and the following properties$")
    public void createARegularStepDefinitionWithProperties(String name, List<CucStepProperty> list)
            throws Exception {

        prepareARegularStepDefinitionCreatorWithPropertyList(name, list);

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition = stepDefinitionService.create(stepDefinitionData.stepDefinitionCreator);
            stepDefinitionData.currentStepDefinitionId = stepDefinitionData.stepDefinition.getId();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Given("^I set the step definition creator name to null$")
    public void setDefinitionCreatorNameToNull() {
        stepDefinitionData.stepDefinitionCreator.setName(null);
    }

    @Given("^I set the step definition creator processor name to \"(.+)\"$")
    public void setDefinitionCreatorProcessorNameTo(String name) {
        stepDefinitionData.stepDefinitionCreator.setProcessorName(name);
    }

    @Given("^I set the step definition creator processor name to null$")
    public void setDefinitionCreatorProcessorNameToNull() {
        stepDefinitionData.stepDefinitionCreator.setProcessorName(null);
    }

    @Given("^I create (\\d+) step definition items$")
    public void createANumberOfStepDefinitions(int num)
            throws Exception {

        JobStepDefinitionCreator tmpCreator;
        try {
            commonData.primeException();
            for (int i = 0; i < num; i++) {
                tmpCreator = stepDefinitionFactory.newCreator(commonData.currentScopeId);
                tmpCreator.setName(String.format("TestStepDefinitionNum%d", random.nextLong()));
                tmpCreator.setProcessorName("TestStepProcessor");
                tmpCreator.setStepType(JobStepType.TARGET);
                stepDefinitionService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I create a new step definition entity from the existing creator$")
    public void createAStepDefinitionFromTheCreator()
            throws Exception {

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition = stepDefinitionService.create(stepDefinitionData.stepDefinitionCreator);
            stepDefinitionData.currentStepDefinitionId = stepDefinitionData.stepDefinition.getId();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I search for the step definition in the database$")
    public void findTheExistingStepDefinitionInTheDatabase()
            throws Exception {

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition = stepDefinitionService.find(commonData.currentScopeId, stepDefinitionData.currentStepDefinitionId);
        } catch(KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I count the step definition in the database$")
    public void countStepDefinitionInDatabase()
            throws Exception {

        JobStepDefinitionQuery tmpQuery = stepDefinitionFactory.newQuery(commonData.currentScopeId);

        try {
            commonData.primeException();
            commonData.itemCount = stepDefinitionService.count(tmpQuery);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I query for step definitions in scope (\\d+)$")
    public void countStepDefinitijonsInScope(int id)
            throws Exception {

        JobStepDefinitionQuery tmpQuery = stepDefinitionFactory.newQuery(new KapuaEid(BigInteger.valueOf(id)));
        try {
            commonData.primeException();
            commonData.itemCount = stepDefinitionService.query(tmpQuery).getSize();
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I delete the step definition$")
    public void deleteExistingStepDefinition()
            throws Exception{

        try {
            commonData.primeException();
            stepDefinitionService.delete(commonData.currentScopeId, stepDefinitionData.currentStepDefinitionId);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the step definition name to \"(.+)\"$")
    public void changeExistingStepDefinitionName(String name)
            throws Exception {

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition.setName(name);
            stepDefinitionData.stepDefinition = stepDefinitionService.update(stepDefinitionData.stepDefinition);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the step definition processor name to \"(.+)\"$")
    public void changeExistingStepDefinitionProcessor(String name)
            throws Exception {

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition.setProcessorName(name);
            stepDefinitionData.stepDefinition = stepDefinitionService.update(stepDefinitionData.stepDefinition);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @When("^I change the step definition type to \"(.+)\"$")
    public void changeExistingStepDefinitionType(String type)
            throws Exception {

        try {
            commonData.primeException();
            stepDefinitionData.stepDefinition.setStepType(getTypeFromString(type));
            stepDefinitionData.stepDefinition = stepDefinitionService.update(stepDefinitionData.stepDefinition);
        } catch (KapuaException ex) {
            commonData.verifyException(ex);
        }
    }

    @Then("^The step definition entity matches the creator$")
    public void checkTheStepDefinitionAgainstTheCreator() {

        assertEquals("The step definition has the wrong name!", stepDefinitionData.stepDefinitionCreator.getName(), stepDefinitionData.stepDefinition.getName());
        assertEquals("The step definition has the wrong description!", stepDefinitionData.stepDefinitionCreator.getDescription(), stepDefinitionData.stepDefinition.getDescription());
        assertEquals("The step definition has the wrong reader name!", stepDefinitionData.stepDefinitionCreator.getReaderName(), stepDefinitionData.stepDefinition.getReaderName());
        assertEquals("The step definition has the wrong processor name!", stepDefinitionData.stepDefinitionCreator.getProcessorName(), stepDefinitionData.stepDefinition.getProcessorName());
        assertEquals("The step definition has the wrong writer name!", stepDefinitionData.stepDefinitionCreator.getWriterName(), stepDefinitionData.stepDefinition.getWriterName());
        assertEquals("The step definition has a wrong step type!", stepDefinitionData.stepDefinitionCreator.getStepType(), stepDefinitionData.stepDefinition.getStepType());
        assertNotNull("The step definition has no properties!", stepDefinitionData.stepDefinition.getStepProperties());
        assertEquals("The step definition has a wrong number of properties!", stepDefinitionData.stepDefinitionCreator.getStepProperties().size(), stepDefinitionData.stepDefinition.getStepProperties().size());
        for(int i = 0; i < stepDefinitionData.stepDefinitionCreator.getStepProperties().size(); i++) {
            assertEquals(stepDefinitionData.stepDefinitionCreator.getStepProperties().get(i).getName(), stepDefinitionData.stepDefinition.getStepProperties().get(i).getName());
            assertEquals(stepDefinitionData.stepDefinitionCreator.getStepProperties().get(i).getPropertyType(), stepDefinitionData.stepDefinition.getStepProperties().get(i).getPropertyType());
            assertEquals(stepDefinitionData.stepDefinitionCreator.getStepProperties().get(i).getPropertyValue(), stepDefinitionData.stepDefinition.getStepProperties().get(i).getPropertyValue());
        }
    }

    @Then("^There is no such step definition item in the database$")
    public void checkThatNoStepDefinitionWasFound() {
        assertNull("Unexpected step definition item was found!", stepDefinitionData.stepDefinition);
    }

    @Then("^The step definition name is \"(.+)\"$")
    public void checkStepDefinitionName(String name) {
        assertEquals("The step definition name does not match!", name, stepDefinitionData.stepDefinition.getName());
    }

    @Then("^The step definition type is \"(.+)\"$")
    public void checkStepDefinitionType(String type) {
        assertEquals("The step definition type does not match!", getTypeFromString(type), stepDefinitionData.stepDefinition.getStepType());
    }

    @Then("^The step definition processor name is \"(.+)\"$")
    public void checkStepDefinitionProcessorName(String name) {
        assertEquals("The step definition processor name does not match!", name, stepDefinitionData.stepDefinition.getProcessorName());
    }

    @When("^I test the sanity of the step definition factory$")
    public void testTheStepDefinitionFactory() {

        assertNotNull(stepDefinitionFactory.newCreator(ROOT_ID));
        assertNotNull(stepDefinitionFactory.newEntity(ROOT_ID));
        assertNotNull(stepDefinitionFactory.newListResult());
        assertNotNull(stepDefinitionFactory.newQuery(ROOT_ID));
        assertNotNull(stepDefinitionFactory.newStepProperty("TestName", "TestType", "TestValue"));
    }

    // ************************************************************************************
    // * Private helper functions                                                         *
    // ************************************************************************************
    private JobStepType getTypeFromString(String type) {

        if (type.trim().toUpperCase().equals("TARGET")) {
            return JobStepType.TARGET;
        } else {
            return JobStepType.GENERIC;
        }
    }

    private JobStepDefinitionCreator prepareDefaultCreator() {

        JobStepDefinitionCreator tmpCr = stepDefinitionFactory.newCreator(commonData.currentScopeId);
        tmpCr.setName(String.format("DefinitionName_%d", random.nextInt()));
        tmpCr.setDescription("DefinitionDescription");
        tmpCr.setReaderName("DefinitionReader");
        tmpCr.setProcessorName("DefinitionProcessor");
        tmpCr.setWriterName("DefinitionWriter");
        tmpCr.setStepType(JobStepType.TARGET);

        return tmpCr;
    }

}
