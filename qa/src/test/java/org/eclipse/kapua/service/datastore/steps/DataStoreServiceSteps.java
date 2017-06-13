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
package org.eclipse.kapua.service.datastore.steps;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreJAXBContextProvider;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * Steps used in Datastore scenarios.
 */
@ScenarioScoped
public class DataStoreServiceSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(DataStoreServiceSteps.class);

    private AccountService accountService;

    private MessageStoreService messageStoreService;

    private ChannelInfoRegistryService channelInfoRegistryService;

    private MetricInfoRegistryService metricInfoRegistryService;

    private ClientInfoRegistryService clientInfoRegistryService;

    private StepData stepData;

    @Inject
    public DataStoreServiceSteps(StepData stepData, DBHelper dbHelper) {

        this.stepData = stepData;
    }

    @Before
    public void beforeScenario(Scenario scenario) throws Exception {

        // Get instance of services used in different scenarios
        KapuaLocator locator = KapuaLocator.getInstance();
        accountService = locator.getService(AccountService.class);
        messageStoreService = locator.getService(MessageStoreService.class);
        channelInfoRegistryService = locator.getService(ChannelInfoRegistryService.class);
        metricInfoRegistryService = locator.getService(MetricInfoRegistryService.class);
        clientInfoRegistryService = locator.getService(ClientInfoRegistryService.class);

        // JAXB Context
        XmlUtil.setContextProvider(new DatastoreJAXBContextProvider());

        // Precautions if index exists form previous tests.
        deleteAllIndices();
    }

    @After
    public void afterScenario() throws Exception {

        try {
            logger.info("Logging out in cleanup");
            SecurityUtils.getSubject().logout();
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    @Given("^Account for \"(.*)\"$")
    public void getAccountForName(String accountName) throws KapuaException {

        Account account = accountService.findByName(accountName);
        stepData.put("account", account);
    }

    @When("^I search for data message with id \"(.*)\"")
    public void messageStoreFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("account");
        DatastoreMessage message = messageStoreService.find(account.getId(), new StorableIdImpl(storableId), StorableFetchStyle.SOURCE_FULL);
        stepData.put("message", message);
    }

    @Then("^I don't find message$")
    public void dontFindMessage() {

        DatastoreMessage message = (DatastoreMessage) stepData.get("message");
        assertNull(message);
    }

    @When("^I search for channel info with id \"(.*)\"")
    public void channelInfoFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("account");
        ChannelInfo channelInfo = channelInfoRegistryService.find(account.getId(), new StorableIdImpl(storableId));
        stepData.put("channelInfo", channelInfo);
    }

    @Then("^I don't find channel info$")
    public void dontFindChannelInfo() {

        ChannelInfo channelInfo = (ChannelInfo) stepData.get("channelInfo");
        assertNull(channelInfo);
    }

    @When("^I search for metric info with id \"(.*)\"")
    public void metricInfoFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("account");
        MetricInfo metricInfo = metricInfoRegistryService.find(account.getId(), new StorableIdImpl(storableId));
        stepData.put("metricInfo", metricInfo);
    }

    @Then("^I don't find metric info$")
    public void dontFindMetricInfo() {

        MetricInfo metricInfo = (MetricInfo) stepData.get("metricInfo");
        assertNull(metricInfo);
    }

    @When("^I search for client info with id \"(.*)\"")
    public void clientInfoFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("account");
        ClientInfo clientInfo = clientInfoRegistryService.find(account.getId(), new StorableIdImpl(storableId));
        stepData.put("clientInfo", clientInfo);
    }

    @Then("^I don't find client info$")
    public void dontFindClientInfo() {

        ClientInfo clientInfo = (ClientInfo) stepData.get("clientInfo");
        assertNull(clientInfo);
    }

    @Given("^I create message query for current account with limit (\\d+)$")
    public void createMessageQueryForAccount(int limit) {

        Account account = (Account) stepData.get("account");
        MessageQuery messageQuery = DatastoreQueryFactory.createBaseMessageQuery(account.getId(), limit);
        stepData.put("messageQuery", messageQuery);
    }

    @When("^I query for data message$")
    public void queryForDataMessage() throws KapuaException {

        MessageQuery messageQuery = (MessageQuery) stepData.get("messageQuery");
        MessageListResult result = messageStoreService.query(messageQuery);
        stepData.put("messageListResult", result);
    }

    @Then("^I get empty message list result$")
    public void getEmptyMessageListResult() {

        MessageListResult result = (MessageListResult) stepData.get("messageListResult");
        assertTrue(result.isEmpty());
    }

    @When("^I count for data message$")
    public void countForDataMessage() throws KapuaException {

        MessageQuery messageQuery = (MessageQuery) stepData.get("messageQuery");
        long count = messageStoreService.count(messageQuery);
        stepData.put("messageCountResult", count);
    }

    @Then("^I get message count (\\d+)$")
    public void getDesiredMessageCountResult(int desiredCount) {

        long count = (long) stepData.get("messageCountResult");
        assertEquals(desiredCount, count);
    }

    @Given("^I create channel info query for current account with limit (\\d+)$")
    public void createChannelInofQueryForAccount(int limit) {

        Account account = (Account) stepData.get("account");
        ChannelInfoQuery channelInfoQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), limit);
        stepData.put("channelInfoQuery", channelInfoQuery);
    }

    @When("^I query for channel info$")
    public void queryForChannelInfo() throws KapuaException {

        ChannelInfoQuery cnannelInfoQuery = (ChannelInfoQuery) stepData.get("channelInfoQuery");
        ChannelInfoListResult result = channelInfoRegistryService.query(cnannelInfoQuery);
        stepData.put("channelInfoListResult", result);
    }

    @Then("^I get empty channel info list result$")
    public void getEmptyChannelInfoListResult() {

        ChannelInfoListResult result = (ChannelInfoListResult) stepData.get("channelInfoListResult");
        assertTrue(result.isEmpty());
    }

    @When("^I count for channel info$")
    public void countForChannelInfo() throws KapuaException {

        ChannelInfoQuery channelInfoQuery = (ChannelInfoQuery) stepData.get("channelInfoQuery");
        long count = channelInfoRegistryService.count(channelInfoQuery);
        stepData.put("channelInfoCountResult", count);
    }

    @Then("^I get channel info count (\\d+)$")
    public void getDesiredChannelInfoCountResult(int desiredCount) {

        long count = (long) stepData.get("channelInfoCountResult");
        assertEquals(desiredCount, count);
    }

    @Given("^I create metric info query for current account with limit (\\d+)$")
    public void createMetricInfoQueryForAccount(int limit) {

        Account account = (Account) stepData.get("account");
        MetricInfoQuery metricInfoQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), limit);
        stepData.put("metricInfoQuery", metricInfoQuery);
    }

    @When("^I query for metric info$")
    public void queryForMetricInfo() throws KapuaException {

        MetricInfoQuery metricInfoQuery = (MetricInfoQuery) stepData.get("metricInfoQuery");
        MetricInfoListResult result = metricInfoRegistryService.query(metricInfoQuery);
        stepData.put("metricInfoListResult", result);
    }

    @Then("^I get empty metric info list result$")
    public void getEmptyMetricInfoListResult() {

        MetricInfoListResult result = (MetricInfoListResult) stepData.get("metricInfoListResult");
        assertTrue(result.isEmpty());
    }

    @When("^I count for metric info$")
    public void countForMetricInfo() throws KapuaException {

        MetricInfoQuery metricInfoQuery = (MetricInfoQuery) stepData.get("metricInfoQuery");
        long count = metricInfoRegistryService.count(metricInfoQuery);
        stepData.put("metricInfoCountResult", count);
    }

    @Then("^I get metric info count (\\d+)$")
    public void getDesiredMetricInfoCountResult(int desiredCount) {

        long count = (long) stepData.get("metricInfoCountResult");
        assertEquals(desiredCount, count);
    }

    @Given("^I create client info query for current account with limit (\\d+)$")
    public void createClientInfoQueryForAccount(int limit) {

        Account account = (Account) stepData.get("account");
        ClientInfoQuery clientInfoQuery = DatastoreQueryFactory.createBaseClientInfoQuery(account.getId(), limit);
        stepData.put("clientInfoQuery", clientInfoQuery);
    }

    @When("^I query for client info$")
    public void queryForClientInfo() throws KapuaException {

        ClientInfoQuery clientInfoQuery = (ClientInfoQuery) stepData.get("clientInfoQuery");
        ClientInfoListResult result = clientInfoRegistryService.query(clientInfoQuery);
        stepData.put("clientInfoListResult", result);
    }

    @Then("^I get empty client info list result$")
    public void getEmptyClientInfoListResult() {

        ClientInfoListResult result = (ClientInfoListResult) stepData.get("clientInfoListResult");
        assertTrue(result.isEmpty());
    }

    @When("^I count for client info$")
    public void countForClientInfo() throws KapuaException {

        ClientInfoQuery clientInfoQuery = (ClientInfoQuery) stepData.get("clientInfoQuery");
        long count = clientInfoRegistryService.count(clientInfoQuery);
        stepData.put("clientInfoCountResult", count);
    }

    @Then("^I get client info count (\\d+)$")
    public void getDesiredClientInfoCountResult(int desiredCount) {

        long count = (long) stepData.get("clientInfoCountResult");
        assertEquals(desiredCount, count);
    }

    /**
     * Clean-up of indices between scenarios.
     *
     * @throws Exception
     */
    private void deleteAllIndices() throws Exception {

        DatastoreMediator.getInstance().deleteAllIndexes();
    }
}
