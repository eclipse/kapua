/*******************************************************************************
 * Copyright (c) 2019, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.endpoint.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalNullArgumentException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoAttributes;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointInfoService;

import com.google.inject.Singleton;

import javax.inject.Inject;

@Singleton
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

    private static final String ENDPOINT_INFO = "EndpointInfo";

    // Default constructor
    @Inject
    public EndpointServiceSteps(StepData stepData) {
        super(stepData);
    }

    @After(value="@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();
        endpointInfoService = locator.getService(EndpointInfoService.class);
        endpointInfoFactory = locator.getFactory(EndpointInfoFactory.class);
    }

    // ************************************************************************************
    // ************************************************************************************
    // * Definition of Cucumber scenario steps                                            *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Setup and tear-down steps                                                        *
    // ************************************************************************************

    @Before(value="@env_docker or @env_embedded_minimal or @env_none", order=10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
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
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            stepData.remove(ENDPOINT_INFO);
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put(ENDPOINT_INFO, endpointInfo);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("^I try to create endpoint with invalid symbols in schema$")
    public void iTryToCreateInvalidEndpoint() throws Exception {
        String invalidSymbols = "!\"#$%&'()=»Ç" +
                ">:;<-.,⁄@‹›€" +
                "*ı–°·‚_±Œ„‰" +
                "?“‘”’ÉØ∏{}|Æ" +
                "æÒ\uF8FFÔÓÌÏÎÍÅ«" +
                "◊Ñˆ¯Èˇ¿";
        for (int i = 0; i < invalidSymbols.length(); i++) {
            EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
            String schema = invalidSymbols + invalidSymbols.charAt(i);
            endpointInfoCreator.setSchema(schema);
            endpointInfoCreator.setDns("dns.com");
            endpointInfoCreator.setPort(2222);
            endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
            try {
                stepData.remove("EndpointInfo");
                EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
                stepData.put("EndpointInfo", endpointInfo);
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @And("^I create endpoint with domain name \"([^\"]*)\" and port (\\d+) without schema$")
    public void iCreateEndpointWithDnsAndPort(String dns, int port) throws Exception {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setDns(dns);
        endpointInfoCreator.setPort(port);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with schema \"([^\"]*)\" without domain name and port$")
    public void iCreateEndpointWithSchemaOnly(String schema) throws Exception {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setSchema(schema);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with NULL parameters$")
    public void iCreateEndpointWithNullParameters() throws Exception {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setSchema(null);
        endpointInfoCreator.setDns(null);
        endpointInfoCreator.setPort(0);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
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

            stepData.put(ENDPOINT_INFO, endpointInfo);
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

            stepData.put(ENDPOINT_INFO, endpointInfo);
            stepData.put("EndpointInfoId", endpointInfo.getId());
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I try to edit endpoint schema to \"([^\"]*)\"$")
    public void editEndpointSchema(String schema) throws Exception {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get(ENDPOINT_INFO);
        endpointInfo.setSchema(schema);

        primeException();
        try {
            EndpointInfo newEndpoint = endpointInfoService.update(endpointInfo);
            stepData.put(ENDPOINT_INFO, newEndpoint);
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
            EndpointInfo endpointInfo = (EndpointInfo) stepData.get(ENDPOINT_INFO);
            endpointInfoService.delete(SYS_SCOPE_ID, endpointInfo.getId());
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("^I delete endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void iDeleteEndpointWithSchemaDomainAndPort(String schema, String domainName, int port) throws Exception {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.DNS, domainName, AttributePredicate.Operator.EQUAL));
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.PORT, port, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();
            endpointInfoService.delete(getCurrentScopeId(), endpointInfo.getId());

            stepData.put("DeletedEndpointInfo", endpointInfo);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I search for all endpoints in current scopeId")
    public void iSearchForAllEndpoints() throws Exception {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            EndpointInfoListResult endpointInfo = endpointInfoService.query(endpointInfoQuery);
            stepData.put("NumberOfEndpoints", endpointInfo.getSize());
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("^I find (\\d+) endpoints")
    public void iFindEndpoint(int numberOfEndpoints) {
        int foundEndpoints = (int) stepData.get("NumberOfEndpoints");
        assertEquals(foundEndpoints, numberOfEndpoints);
    }

    @Then("^I found endpoint with schema \"([^\"]*)\"$")
    public void iFoundEndpoint(String endpointSchema) {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");

        assertEquals(endpointSchema, endpointInfo.getSchema());
    }

    @Then("^I found endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void iFoundEndpointWithSchemaDomainAndPort(String endpointSchema, String domainName, int port) {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");

        assertEquals(endpointSchema, endpointInfo.getSchema());
        assertEquals(domainName, endpointInfo.getDns());
        assertEquals(port, endpointInfo.getPort());
    }

    @Then("^I did not find endpoint with schema \"([^\"]*)\"$")
    public void notFoundEndpointWithSchema(String endpointSchema) {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");

        assertNotEquals(endpointSchema, endpointInfo.getSchema());
    }

    @When("^I delete all endpoints with schema \"([^\"]*)\"$")
    public void iDeleteEndpointsWithSchema(String schema) throws Throwable {
        primeException();

        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));

            EndpointInfoListResult endpointsToDelete = endpointInfoService.query(endpointInfoQuery);

            for (int i = 0; i < endpointsToDelete.getSize(); i++) {
                endpointInfoService.delete(getCurrentScopeId(), endpointsToDelete.getItem(i).getId());
            }

        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("^I edit last created endpoint Schema to \"([^\"]*)\"$")
    public void iEditEndpointSchema(String schema) throws Throwable {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");

        if (schema.equals("NULL")) {
            schema = null;
        }

        try {
            endpointInfo.setSchema(schema);
            endpointInfoService.update(endpointInfo);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Then("^I edit last created endpoint Domain Name to \"([^\"]*)\"$")
    public void iEditLastCreatedEndpointDomainNameTo(String domainName) throws Throwable {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");

        if (domainName.equals("NULL")) {
            domainName = null;
        }

        try {
            endpointInfo.setDns(domainName);
            endpointInfoService.update(endpointInfo);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Then("^I edit last created endpoint Port to (\\d+)$")
    public void iEditLastCreatedEndpointPort(int port) throws Throwable {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");
        try {
            endpointInfo.setPort(port);
            endpointInfoService.update(endpointInfo);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Then("^I edit port number to (\\d+) in endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\" and port (\\d+)$")
    public void iEditLastCreatedEndpointPortInEndpoint(int newPort, String schema, String domainName, int port) throws Throwable {
        try {
            EndpointInfoQuery endpointInfoQuery = endpointInfoFactory.newQuery(getCurrentScopeId());
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.SCHEMA, schema, AttributePredicate.Operator.EQUAL));
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.DNS, domainName, AttributePredicate.Operator.EQUAL));
            endpointInfoQuery.setPredicate(endpointInfoQuery.attributePredicate(EndpointInfoAttributes.PORT, port, AttributePredicate.Operator.EQUAL));
            EndpointInfo endpointInfo = endpointInfoService.query(endpointInfoQuery).getFirstItem();
            endpointInfo.setPort(newPort);
            endpointInfoService.update(endpointInfo);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("^I create endpoint with schema \"([^\"]*)\" and port (\\d+) without domain name$")
    public void iCreateEndpointWithSchemaAndPortWithoutDomain(String schema, int port ) throws Throwable {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setSchema(schema);
        endpointInfoCreator.setPort(port);
        endpointInfoCreator.setDns(null);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with domain name \"([^\"]*)\" without schema and port$")
    public void iCreateEndpointWithDomainWithoutSchemaAndPort(String domainName) throws Throwable{
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setDns(domainName);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with schema \"([^\"]*)\" and domain \"([^\"]*)\" without port$")
    public void iCreateEndpointWithSchemaAndDomainWithoutPort(String schema, String domainName) throws Throwable {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setDns(domainName);
        endpointInfoCreator.setSchema(schema);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with port (\\d+) without schema and domain name$")
    public void iCreateEndpointWithPortWithoutSchemaAndDomain(int port) throws Throwable{
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setPort(port);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @And("^I create endpoint with schema \"([^\"]*)\", domain \"([^\"]*)\", port (\\d+) and \"([^\"]*)\" secure field$")
    public void iCreateEndpointWithSchemaDomainPortAndSecureField(String schema, String domainName, int port, String secureField) throws Throwable {
        EndpointInfoCreator endpointInfoCreator = endpointInfoFactory.newCreator(getCurrentScopeId());
        endpointInfoCreator.setSchema(schema);
        endpointInfoCreator.setDns(domainName);
        endpointInfoCreator.setPort(port);
        endpointInfoCreator.setEndpointType(EndpointInfo.ENDPOINT_TYPE_RESOURCE);
        if (secureField.equals("ENABLED")) {
            endpointInfoCreator.setSecure(true);
        } else {
            endpointInfoCreator.setSecure(false);
        }

        try {
            EndpointInfo endpointInfo = endpointInfoService.create(endpointInfoCreator);
            stepData.put("EndpointInfo", endpointInfo);
        } catch (KapuaIllegalNullArgumentException ex) {
            verifyException(ex);
        }
    }

    @Then("^I edit last created endpoint Secure field to \"([^\"]*)\"$")
    public void iEditLastCreatedEndpointSecureField(String secureField) throws Throwable {
        EndpointInfo endpointInfo = (EndpointInfo) stepData.get("EndpointInfo");
        try {
            if (secureField.equals("ENABLED")) {
                endpointInfo.setSecure(true);
            } else {
                endpointInfo.setSecure(false);
            }
            endpointInfoService.update(endpointInfo);
        } catch (Exception e) {
            verifyException(e);
        }
    }
}


