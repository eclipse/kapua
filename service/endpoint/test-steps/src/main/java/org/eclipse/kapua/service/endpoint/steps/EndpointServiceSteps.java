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
package org.eclipse.kapua.service.endpoint.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestJAXBContextProvider;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


@ScenarioScoped
public class EndpointServiceSteps extends TestBase {

    private EndpointInfoService endpointInfoService;
    private EndpointInfoFactory endpointInfoFactory;


// ****************************************************************************************
// * Implementation of Gherkin steps used in JobService.feature scenarios.                *
// *                                                                                      *
// * MockedLocator is used for Location Service. Mockito is used to mock other            *
// * services that the Account services dependent on. Dependent services are:             *
// * - Authorization Service                                                              *
// ****************************************************************************************

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointServiceSteps.class);

    // Default constructor
    @Inject
    public EndpointServiceSteps(StepData stepData, DBHelper dbHelper) {

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
        endpointInfoService = locator.getService(EndpointInfoService.class);
        endpointInfoFactory = locator.getFactory(EndpointInfoFactory.class);

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

    @And("^I create endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void iCreateEndpointWithSchemaDnsAndPort(String schema, String dns, int port) throws Exception {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setSchema(schema);
        endpointInfoCreator.setDns(dns);
        endpointInfoCreator.setPort(port);
        try {
            stepData.remove("EndpointInfo");
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("^I delete the endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void iDeleteEndpointWithSchema(String schema, String domain, int port) throws Throwable {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();
            assertEquals(schema, endpointInfo.getSchema());
            assertEquals(domain, endpointInfo.getDns());
            assertEquals(port, endpointInfo.getPort());

            endpointInfoService.delete(SYS_SCOPE_ID, endpointInfo.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to find endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void foundEndpointBySchemaDomainPort(String schema, String domain, int port) throws Exception {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();
            assertEquals(schema, endpointInfo.getSchema());
            assertEquals(domain, endpointInfo.getDns());
            assertEquals(port, endpointInfo.getPort());

            stepData.put("EndpointInfo", endpointInfo);
            stepData.put("EndpointInfoId", endpointInfo.getId());
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I try to find endpoint with schema \"([^\"]*)\"$")
    public void foundEndpointBySchema(String schema) throws Exception {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();
            assertEquals(schema, endpointInfo.getSchema());

            stepData.put("EndpointInfo", endpointInfo);
            stepData.put("EndpointInfoId", endpointInfo.getId());
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I try to edit endpoint schema to \"([^\"]*)\"$")
    public void editEndpointSchema(String schema) throws Exception {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");
        endpointInfo.setSchema(schema);

        primeException();
        try {
            EndpointInfo newEndpoint = endpointInfoService.update(endpointInfo);
            stepData.put("EndpointInfo", newEndpoint);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I delete endpoint with schema \"([^\"]*)\"$")
    public void iDeleteEndpointWithSchema(String schema) throws Exception {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();

            endpointInfoService.delete(SYS_SCOPE_ID, endpointInfo.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I delete the last created endpoint$")
    public void iDeleteTheLastCreatedEndpoint() throws Exception {

        try {
            EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");
            endpointInfoService.delete(SYS_SCOPE_ID, endpointInfo.getId());
        } catch (Exception e) {
            verifyException(e);
        }
    }
}


