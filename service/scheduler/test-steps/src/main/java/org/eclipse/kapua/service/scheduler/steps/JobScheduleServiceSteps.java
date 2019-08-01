/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.scheduler.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.qa.common.cucumber.CucTriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinition;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionAttributes;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionFactory;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerDefinitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ScenarioScoped
public class JobScheduleServiceSteps extends TestBase {

    private TriggerFactory triggerFactory;
    private TriggerService triggerService;
    private TriggerDefinitionFactory triggerDefinitionFactory;
    private TriggerDefinitionService triggerDefinitionService;


// ****************************************************************************************
// * Implementation of Gherkin steps used in JobService.feature scenarios.                *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

    private static final Logger LOGGER = LoggerFactory.getLogger(JobScheduleServiceSteps.class);

    // Default constructor
    @Inject
    public JobScheduleServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
        this.database = dbHelper;
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

        this.scenario = scenario;
        database.setup();
        stepData.clear();

        locator = KapuaLocator.getInstance();

        triggerFactory = locator.getFactory(TriggerFactory.class);
        triggerService = locator.getService(TriggerService.class);
        triggerDefinitionFactory = locator.getFactory(TriggerDefinitionFactory.class);
        triggerDefinitionService = locator.getService(TriggerDefinitionService.class);

        if (isUnitTest()) {
            // Create KapuaSession using KapuaSecurtiyUtils and kapua-sys user as logged in user.
            // All operations on database are performed using system user.
            // Only for unit tests. Integration tests assume that a real logon is performed.
            KapuaSession kapuaSession = new KapuaSession(null, SYS_SCOPE_ID, SYS_USER_ID);
            KapuaSecurityUtils.setSession(kapuaSession);
        }

        // Setup JAXB context
        XmlUtil.setContextProvider(new TestJAXBContextProvider());
    }

    @After
    public void afterScenario() {

        // ************************************************************************************
        // * Clean up the database                                                            *
        // ************************************************************************************
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
    // * The Cucumber test steps                                                          *
    // ************************************************************************************

    @And("^I try to create scheduler with name \"([^\"]*)\"$")
    public void iTryToCreateSchedulerWithName(String schedulerName) throws Exception {
        TriggerCreator triggerCreator = triggerFactory.newCreator(getCurrentScopeId());
        KapuaId triggerDefinitionId = (KapuaId) stepData.get("TriggerDefinitionId");
        triggerCreator.setName(schedulerName);
        triggerCreator.setStartsOn(new Date());
        triggerCreator.setTriggerDefinitionId(triggerDefinitionId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            primeException();
            stepData.remove("Trigger");
            Trigger trigger = triggerService.create(triggerCreator);
            stepData.put("Trigger", trigger);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I found trigger properties with name \"([^\"]*)\"$")
    public void iFoundTriggerPropertiesWithName(String triggerDefinitionName) throws Exception {
        primeException();

        try {
            TriggerDefinitionQuery triggerDefinitionQuery = triggerDefinitionFactory.newQuery(getCurrentScopeId());
            triggerDefinitionQuery.setPredicate(triggerDefinitionQuery.attributePredicate(TriggerDefinitionAttributes.NAME, triggerDefinitionName, AttributePredicate.Operator.EQUAL));
            TriggerDefinition triggerDefinition = triggerDefinitionService.query(triggerDefinitionQuery).getFirstItem();

            stepData.put("TriggerDefinition", triggerDefinition);
            stepData.put("TriggerDefinitionId", triggerDefinition.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^A regular trigger creator with the name \"([^\"]*)\" and following properties$")
    public void aRegularTriggerCreatorWithTheNameAndFollowingProperties(String triggerName, List<CucTriggerProperty> list) {
        TriggerCreator triggerCreator = triggerFactory.newCreator(getCurrentScopeId());
        KapuaId currentTriggerDefId = (KapuaId) stepData.get("TriggerDefinitionId");

        triggerCreator.setName(triggerName);
        triggerCreator.setTriggerDefinitionId(currentTriggerDefId);

        List<TriggerProperty> tmpPropList = new ArrayList<>();
        for(CucTriggerProperty prop : list) {
            tmpPropList.add(triggerFactory.newTriggerProperty(prop.getName(), prop.getType(), prop.getValue()));
        }
        triggerCreator.setTriggerProperties(tmpPropList);

        stepData.put("TriggerCreator", triggerCreator);
    }

    @And("^I try to create a new trigger entity from the existing creator$")
    public void iCreateANewTriggerEntityFromTheExistingCreator() throws Exception {
        TriggerCreator triggerCreator = (TriggerCreator) stepData.get("TriggerCreator");
        triggerCreator.setScopeId(getCurrentScopeId());
        triggerCreator.setStartsOn(new Date());

        primeException();
        try {
            stepData.remove("Trigger");
            stepData.remove("CurrentTriggerId");
            Trigger trigger = triggerService.create(triggerCreator);
            stepData.put("Trigger", trigger);
            stepData.put("CurrentTriggerId", trigger.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to edit trigger name \"([^\"]*)\"$")
    public void iTryToEditTriggerName(String newTriggerName) throws Exception {
        Trigger trigger = (Trigger) stepData.get("Trigger");
        trigger.setName(newTriggerName);
        try {
            primeException();
            Trigger newTrigger = triggerService.update(trigger);
            stepData.put("Trigger", newTrigger);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to delete last created trigger$")
    public void iTryToDeleteTrigger() throws Exception {
        Trigger trigger = (Trigger) stepData.get("Trigger");

        try {
            primeException();
            triggerService.delete(getCurrentScopeId(), trigger.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }
}

