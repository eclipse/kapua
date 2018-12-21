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
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionCreator;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.JobStepType;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionFactoryImpl;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepDefinitionServiceImpl;
import org.eclipse.kapua.test.MockedLocator;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
public class JobStepDefinitionServiceSteps extends TestBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobStepDefinitionServiceSteps.class);

    // Step definition service objects
    private JobStepDefinitionService jobStepDefinitionService;
    private JobStepDefinitionFactory jobStepDefinitionFactory;

    // Default constructor
    @Inject
    public JobStepDefinitionServiceSteps(StepData stepData, DBHelper dbHelper) {

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
                bind(JobStepDefinitionService.class).toInstance(new JobStepDefinitionServiceImpl());
                bind(JobStepDefinitionFactory.class).toInstance(new JobStepDefinitionFactoryImpl());
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
        jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
        jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);

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
    @Given("^A regular step definition creator with the name \"(.*)\"$")
    public void prepareARegularStepDefinitionCreator(String name) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionCreator.setName(name);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular definition creator with the name \"(.*)\" and (\\d+) properties$")
    public void prepareARegularStepDefinitionCreatorWithProperties(String name, Integer cnt) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property1", "Type1", null));
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property2", "Type2", null));
        tmpPropLst.add(jobStepDefinitionFactory.newStepProperty("Property3", "Type3", null));
        stepDefinitionCreator.setStepProperties(tmpPropLst);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular step definition creator with the name \"(.*)\" and the following properties$")
    public void prepareARegularStepDefinitionCreatorWithPropertyList(String name, List<CucStepProperty> list) {

        JobStepDefinitionCreator stepDefinitionCreator = prepareDefaultCreator();
        stepDefinitionCreator.setName(name);

        List<JobStepProperty> tmpPropLst = new ArrayList<>();
        for(CucStepProperty prop : list) {
            tmpPropLst.add(jobStepDefinitionFactory.newStepProperty(prop.getName(), prop.getType(), null));
        }
        stepDefinitionCreator.setStepProperties(tmpPropLst);

        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^A regular step definition with the name \"(.*)\" and the following properties$")
    public void createARegularStepDefinitionWithProperties(String name, List<CucStepProperty> list)
            throws Exception {

        prepareARegularStepDefinitionCreatorWithPropertyList(name, list);
        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            stepData.remove("CurrentJobStepDefinitionId");
            JobStepDefinition stepDefinition = jobStepDefinitionService.create(stepDefinitionCreator);
            stepData.put("JobStepDefinition", stepDefinition);
            stepData.put("CurrentJobStepDefinitionId", stepDefinition.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("^I set the step definition creator name to null$")
    public void setDefinitionCreatorNameToNull() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setName(null);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I set the step definition creator processor name to \"(.+)\"$")
    public void setDefinitionCreatorProcessorNameTo(String name) {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setProcessorName(name);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I set the step definition creator processor name to null$")
    public void setDefinitionCreatorProcessorNameToNull() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        stepDefinitionCreator.setProcessorName(null);
        stepData.put("JobStepDefinitionCreator", stepDefinitionCreator);
    }

    @Given("^I create (\\d+) step definition items$")
    public void createANumberOfStepDefinitions(Integer num)
            throws Exception {

        JobStepDefinitionCreator tmpCreator;

        primeException();
        try {
            for (int i = 0; i < num; i++) {
                tmpCreator = jobStepDefinitionFactory.newCreator(getCurrentScopeId());
                tmpCreator.setName(String.format("TestStepDefinitionNum%d", random.nextLong()));
                tmpCreator.setProcessorName("TestStepProcessor");
                tmpCreator.setStepType(JobStepType.TARGET);
                jobStepDefinitionService.create(tmpCreator);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I create a new step definition entity from the existing creator$")
    public void createAStepDefinitionFromTheCreator()
            throws Exception {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            stepData.remove("CurrentJobStepDefinitionId");
            JobStepDefinition stepDefinition = jobStepDefinitionService.create(stepDefinitionCreator);
            stepData.put("JobStepDefinition", stepDefinition);
            stepData.put("CurrentJobStepDefinitionId", stepDefinition.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I search for the step definition in the database$")
    public void findTheExistingStepDefinitionInTheDatabase()
            throws Exception {

        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        primeException();
        try {
            stepData.remove("JobStepDefinition");
            JobStepDefinition stepDefinition = jobStepDefinitionService.find(getCurrentScopeId(), currentStepDefId);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch(KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I count the step definition in the database$")
    public void countStepDefinitionInDatabase()
            throws Exception {

        JobStepDefinitionQuery tmpQuery = jobStepDefinitionFactory.newQuery(getCurrentScopeId());

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = jobStepDefinitionService.count(tmpQuery);
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I query for step definitions in scope (\\d+)$")
    public void countStepDefinitijonsInScope(Integer id)
            throws Exception {

        JobStepDefinitionQuery tmpQuery = jobStepDefinitionFactory.newQuery(getKapuaId(id));

        primeException();
        try {
            stepData.remove("Count");
            Long itemCount = (long) jobStepDefinitionService.query(tmpQuery).getSize();
            stepData.put("Count", itemCount);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the step definition$")
    public void deleteExistingStepDefinition()
            throws Exception{

        KapuaId currentStepDefId = (KapuaId) stepData.get("CurrentJobStepDefinitionId");

        primeException();
        try {
            jobStepDefinitionService.delete(getCurrentScopeId(), currentStepDefId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition name to \"(.+)\"$")
    public void changeExistingStepDefinitionName(String name)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setName(name);
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition processor name to \"(.+)\"$")
    public void changeExistingStepDefinitionProcessor(String name)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setProcessorName(name);
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I change the step definition type to \"(.+)\"$")
    public void changeExistingStepDefinitionType(String type)
            throws Exception {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        primeException();
        try {
            stepDefinition.setStepType(getTypeFromString(type));
            stepDefinition = jobStepDefinitionService.update(stepDefinition);
            stepData.put("JobStepDefinition", stepDefinition);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^The step definition entity matches the creator$")
    public void checkTheStepDefinitionAgainstTheCreator() {

        JobStepDefinitionCreator stepDefinitionCreator = (JobStepDefinitionCreator) stepData.get("JobStepDefinitionCreator");
        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");

        assertEquals("The step definition has the wrong name!", stepDefinitionCreator.getName(), stepDefinition.getName());
        assertEquals("The step definition has the wrong description!", stepDefinitionCreator.getDescription(), stepDefinition.getDescription());
        assertEquals("The step definition has the wrong reader name!", stepDefinitionCreator.getReaderName(), stepDefinition.getReaderName());
        assertEquals("The step definition has the wrong processor name!", stepDefinitionCreator.getProcessorName(), stepDefinition.getProcessorName());
        assertEquals("The step definition has the wrong writer name!", stepDefinitionCreator.getWriterName(), stepDefinition.getWriterName());
        assertEquals("The step definition has a wrong step type!", stepDefinitionCreator.getStepType(), stepDefinition.getStepType());
        assertNotNull("The step definition has no properties!", stepDefinition.getStepProperties());
        assertEquals("The step definition has a wrong number of properties!", stepDefinitionCreator.getStepProperties().size(), stepDefinition.getStepProperties().size());
        for(int i = 0; i < stepDefinitionCreator.getStepProperties().size(); i++) {
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getName(), stepDefinition.getStepProperties().get(i).getName());
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getPropertyType(), stepDefinition.getStepProperties().get(i).getPropertyType());
            assertEquals(stepDefinitionCreator.getStepProperties().get(i).getPropertyValue(), stepDefinition.getStepProperties().get(i).getPropertyValue());
        }
    }

    @Then("^There is no such step definition item in the database$")
    public void checkThatNoStepDefinitionWasFound() {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertNull("Unexpected step definition item was found!", stepDefinition);
    }

    @Then("^The step definition name is \"(.+)\"$")
    public void checkStepDefinitionName(String name) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition name does not match!", name, stepDefinition.getName());
    }

    @Then("^The step definition type is \"(.+)\"$")
    public void checkStepDefinitionType(String type) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition type does not match!", getTypeFromString(type), stepDefinition.getStepType());
    }

    @Then("^The step definition processor name is \"(.+)\"$")
    public void checkStepDefinitionProcessorName(String name) {

        JobStepDefinition stepDefinition = (JobStepDefinition) stepData.get("JobStepDefinition");
        assertEquals("The step definition processor name does not match!", name, stepDefinition.getProcessorName());
    }

    @When("^I test the sanity of the step definition factory$")
    public void testTheStepDefinitionFactory() {

        assertNotNull(jobStepDefinitionFactory.newCreator(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newEntity(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newListResult());
        assertNotNull(jobStepDefinitionFactory.newQuery(SYS_SCOPE_ID));
        assertNotNull(jobStepDefinitionFactory.newStepProperty("TestName", "TestType", "TestValue"));
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

        JobStepDefinitionCreator tmpCr = jobStepDefinitionFactory.newCreator(getCurrentScopeId());
        tmpCr.setName(String.format("DefinitionName_%d", random.nextInt()));
        tmpCr.setDescription("DefinitionDescription");
        tmpCr.setReaderName("DefinitionReader");
        tmpCr.setProcessorName("DefinitionProcessor");
        tmpCr.setWriterName("DefinitionWriter");
        tmpCr.setStepType(JobStepType.TARGET);

        return tmpCr;
    }

}
