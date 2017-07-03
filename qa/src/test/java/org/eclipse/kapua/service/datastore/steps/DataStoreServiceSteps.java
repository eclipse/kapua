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
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.steps.DBHelper;
import org.eclipse.kapua.service.StepData;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreJAXBContextProvider;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.internal.ChannelInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.ClientInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.MetricInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Steps used in Datastore scenarios.
 */
@ScenarioScoped
public class DataStoreServiceSteps extends AbstractKapuaSteps {

    private static final Logger logger = LoggerFactory.getLogger(DataStoreServiceSteps.class);

    private AccountService accountService;

    private DeviceRegistryService deviceRegistryService;

    private DeviceFactory deviceFactory;

    private MessageStoreService messageStoreService;

    private DatastoreObjectFactory datastoreObjectFactory;

    private StorablePredicateFactory storablePredicateFactory;

    private ChannelInfoRegistryService channelInfoRegistryService;
    private ChannelInfoRegistryServiceProxy channelInfoRegistryServiceProxy;

    private MetricInfoRegistryService metricInfoRegistryService;
    private MetricInfoRegistryServiceProxy metricInfoRegistryServiceProxy;

    private ClientInfoRegistryService clientInfoRegistryService;
    private ClientInfoRegistryServiceProxy clientInfoRegistryServiceProxy;

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
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceFactory = locator.getFactory(DeviceFactory.class);
        messageStoreService = locator.getService(MessageStoreService.class);
        datastoreObjectFactory = locator.getFactory(DatastoreObjectFactory.class);
        storablePredicateFactory = locator.getFactory(StorablePredicateFactory.class);
        channelInfoRegistryService = locator.getService(ChannelInfoRegistryService.class);
        channelInfoRegistryServiceProxy = new ChannelInfoRegistryServiceProxy();
        metricInfoRegistryService = locator.getService(MetricInfoRegistryService.class);
        metricInfoRegistryServiceProxy = new MetricInfoRegistryServiceProxy();
        clientInfoRegistryService = locator.getService(ClientInfoRegistryService.class);
        clientInfoRegistryServiceProxy = new ClientInfoRegistryServiceProxy();

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
        stepData.put("LastAccount", account);
    }

    @Given("^The device \"(.*)\"$")
    public void createDeviceWithName(String clientId) throws KapuaException {

        Account tmpAcc = (Account) stepData.get("LastAccount");
        DeviceCreator tmpDevCr = deviceFactory.newCreator(tmpAcc.getId(), clientId);
        Device tmpDev = deviceRegistryService.create(tmpDevCr);
        stepData.put("LastDevice", tmpDev);
    }

    @Given("^I prepare a random message and save it as \"(.*)\"$")
    public void prepareAndRememberARandomMessage(String msgKey) throws KapuaException {

        KapuaDataMessage tmpMessage = createTestMessage(((Account) stepData.get("LastAccount")).getId(),
                ((Device) stepData.get("LastDevice")).getId(),
                ((Device) stepData.get("LastDevice")).getClientId());
        stepData.put(msgKey, tmpMessage);
    }

    @Given("^I prepare (\\d+) random messages and remember the list as \"(.*)\"$")
    public void prepareAndRememberANumberOfRandomMessages(int number, String lstKey) {

        KapuaDataMessage tmpMsg;
        List<KapuaDataMessage> msgList = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            tmpMsg = createTestMessage(((Account) stepData.get("LastAccount")).getId(),
                    ((Device) stepData.get("LastDevice")).getId(),
                    ((Device) stepData.get("LastDevice")).getClientId());
            msgList.add(tmpMsg);
        }

        stepData.put(lstKey, msgList);
    }

    @Given("^I store the message \"(.*)\" and remember its ID as \"(.*)\"$")
    public void insertRandomMessageIntoDatastore(String msgKey, String idKey) throws KapuaException {

        KapuaDataMessage tmpMessage = (KapuaDataMessage) stepData.get(msgKey);
        StorableId storeId = insertMessageInStore(tmpMessage);
        stepData.put(idKey, storeId);
        // Wait for the database to index the newly inserted records
        waitABit(5000);
    }

    @Given("^I store the messages from list \"(.*)\" and remember the IDs as \"(.*)\"$")
    public void insertRandomMessagesIntoDatastore(String msgListKey, String idListKey) throws KapuaException {

        List<KapuaDataMessage> tmpMsgList = (List<KapuaDataMessage>) stepData.get(msgListKey);
        List<StorableId> tmpIdList = insertMessagesInStore(tmpMsgList);
        stepData.put(idListKey, tmpIdList);
        // Wait for the database to index the newly inserted records
        waitABit(5000);
    }

    @When("^I clear all the database caches$")
    public void clearDatabaseCaches() {

        clearAllCaches();
    }

    @When("^I refresh all database indices$")
    public void refreshDatabaseIndices() throws Exception {

        refreshAllIndices();
    }

    @When("^I delete the datastore message with ID \"(.*)\"$")
    public void deleteDatastoreMessage(String idKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId msgId = (StorableId) stepData.get(idKey);
        messageStoreService.delete(account.getId(), msgId);
    }

    @When("^I search for a data message with ID \"(.*)\" and remember it as \"(.*)\"")
    public void searchForMessageInDatastore(String idKey, String msgKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId tmpId = (StorableId) stepData.get(idKey);
        DatastoreMessage tmpMsg = messageStoreService.find(account.getId(), tmpId, StorableFetchStyle.SOURCE_FULL);
        stepData.put(msgKey, tmpMsg);
    }

    @When("^I search for messages with IDs from the list \"(.*)\" and store them in the list \"(.*)\"$")
    public void searchForMessagesInTheDatastore(String idListKey, String msgListKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        List<StorableId> msgIdLst = (List<StorableId>) stepData.get(idListKey);
        List<DatastoreMessage> tmpMsgLst = new ArrayList<DatastoreMessage>();
        DatastoreMessage tmpMsg;

        for (StorableId tmpId : msgIdLst) {
            tmpMsg = messageStoreService.find(account.getId(), tmpId, StorableFetchStyle.SOURCE_FULL);
            tmpMsgLst.add(tmpMsg);
        }

        stepData.put(msgListKey, tmpMsgLst);
    }

    @When("^I query for the current account channel(?:|s) (?:|again )and store (?:it|them) as \"(.*)\"$")
    public void queryForAccountChannels(String chnKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoQuery tmpQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);

        ChannelInfoListResult tmpList = channelInfoRegistryService.query(tmpQuery);
        stepData.put(chnKey, tmpList);
    }

    @When("^I count the current account channels and store the count as \"(.*)\"$")
    public void countAccountChannels(String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoQuery tmpQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);

        long channelCount = channelInfoRegistryService.count(tmpQuery);
        stepData.put(countKey, channelCount);
    }

    @Then("^There (?:is|are) exactly (\\d+) channel(?:|s) in the list \"(.*)\"$")
    public void checkNumberOfQueriedChannels(int cnt, String lstKey) {

        assertEquals(cnt, ((ChannelInfoListResult) stepData.get(lstKey)).getSize());
    }

    @When("^I delete all channels from the list \"(.*)\"$")
    public void deleteAllChannelsFromList(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoListResult tmpList = (ChannelInfoListResult) stepData.get(lstKey);

        for (ChannelInfo tmpItem : tmpList.getItems()) {
            channelInfoRegistryServiceProxy.delete(account.getId(), tmpItem.getId());
        }
    }

    @When("^I query for the current account metrics (?:|again )and store (?:it|them) as \"(.*)\"$")
    public void queryForAccountMetrics(String metricKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);

        MetricInfoListResult tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(metricKey, tmpList);
    }

    @When("^I count the current account metrics and store the count as \"(.*)\"$")
    public void countAccountMetrics(String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);

        long metricCount = metricInfoRegistryService.count(tmpQuery);
        stepData.put(countKey, metricCount);
    }

    @Then("^There (?:is|are) exactly (\\d+) metric(?:|s) in the list \"(.*)\"$")
    public void checkNumberOfQueriedMetrics(int cnt, String lstKey) {

        assertEquals(cnt, ((MetricInfoListResult) stepData.get(lstKey)).getSize());
    }

    @When("^I delete all metrics from the list \"(.*)\"$")
    public void deleteAllMetricsFromList(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoListResult tmpList = (MetricInfoListResult) stepData.get(lstKey);

        for (MetricInfo tmpItem : tmpList.getItems()) {
            metricInfoRegistryServiceProxy.delete(account.getId(), tmpItem.getId());
        }
    }

    @When("^I query for the current account client(?:|s) (?:|again )and store (?:it|them) as \"(.*)\"$")
    public void queryForAccountClientInfo(String clientKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoQuery tmpQuery = DatastoreQueryFactory.createBaseClientInfoQuery(account.getId(), 100);

        ClientInfoListResult tmpList = clientInfoRegistryService.query(tmpQuery);
        stepData.put(clientKey, tmpList);
    }

    @When("^I count the current account clients and store the count as \"(.*)\"$")
    public void countAccountClients(String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoQuery tmpQuery = DatastoreQueryFactory.createBaseClientInfoQuery(account.getId(), 100);

        long clientCount = clientInfoRegistryService.count(tmpQuery);
        stepData.put(countKey, clientCount);
    }

    @Then("^There (?:is|are) exactly (\\d+) client(?:|s) in the list \"(.*)\"$")
    public void checkNumberOfQueriedClients(int cnt, String lstKey) {

        assertEquals(cnt, ((ClientInfoListResult) stepData.get(lstKey)).getSize());
    }

    @When("^I delete all clients from the list \"(.*)\"$")
    public void deleteAllClientsFromList(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoListResult tmpList = (ClientInfoListResult) stepData.get(lstKey);

        for (ClientInfo tmpItem : tmpList.getItems()) {
            clientInfoRegistryServiceProxy.delete(account.getId(), tmpItem.getId());
        }
    }

    @When("^I search for data message with id \"(.*)\"")
    public void messageStoreFind(String storeId) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        DatastoreMessage tmpMsg = messageStoreService.find(account.getId(), new StorableIdImpl(storeId), StorableFetchStyle.SOURCE_FULL);
        stepData.put("message", tmpMsg);
    }

    @Then("^I don't find message$")
    public void dontFindMessage() {

        DatastoreMessage message = (DatastoreMessage) stepData.get("message");
        assertNull(message);
    }

    @Then("^Message \"(.*)\" is null$")
    public void checkThatTheMessageIsActuallyNull(String msgKey) {

        assertNull(stepData.get(msgKey));
    }

    @Then("^The datastore message \"(.*)\" matches the prepared message \"(.*)\"$")
    public void checkThatTheStoredMessageMatchesTheOriginal(String datastoreMsgKey, String originalMsgKey) throws KapuaException {

        KapuaDataMessage origMsg = (KapuaDataMessage) stepData.get(originalMsgKey);
        DatastoreMessage foundMsg = (DatastoreMessage) stepData.get(datastoreMsgKey);

        checkThatTheInsertedMessageMatchesTheOriginal(origMsg, foundMsg);

        isChannelForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        isClientForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        isMetricForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
    }

    @Then("^The datastore messages in list \"(.*)\" matches the prepared messages in list \"(.*)\"$")
    public void checkThatTheStoredMessagesMatchTheOriginals(String datastoreMsgLstKey, String originalMsgLstKey) throws KapuaException {

        List<KapuaDataMessage> origMsgs = (List<KapuaDataMessage>) stepData.get(originalMsgLstKey);
        List<DatastoreMessage> dataMsg = (List<DatastoreMessage>) stepData.get(datastoreMsgLstKey);

        for (int i = 0; i < origMsgs.size(); i++) {
            checkThatTheInsertedMessageMatchesTheOriginal(origMsgs.get(i), dataMsg.get(i));
        }
    }

    @Then("^The datastore messages \"(.*)\" and \"(.*)\" match$")
    public void checkSavedMessages(String firstMsg, String secondMsg) throws KapuaException {

        DatastoreMessage origMsg = (DatastoreMessage) stepData.get(firstMsg);
        DatastoreMessage foundMsg = (DatastoreMessage) stepData.get(secondMsg);

        checkThatDatastoreMessagesMatch(origMsg, foundMsg);

        isChannelForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        isClientForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        isMetricForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
    }

    @When("^I search for channel info with id \"(.*)\"")
    public void channelInfoFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
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

        Account account = (Account) stepData.get("LastAccount");
        MetricInfo tmpInfo = metricInfoRegistryService.find(account.getId(), new StorableIdImpl(storableId));
        stepData.put("metricInfo", tmpInfo);
    }

    @Then("^I don't find metric info$")
    public void dontFindMetricInfo() {

        MetricInfo metricInfo = (MetricInfo) stepData.get("metricInfo");
        assertNull(metricInfo);
    }

    @When("^I search for client info with id \"(.*)\"")
    public void clientInfoFind(String storableId) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfo tmpInfo = clientInfoRegistryService.find(account.getId(), new StorableIdImpl(storableId));
        stepData.put("clientInfo", tmpInfo);
    }

    @Then("^I don't find client info$")
    public void dontFindClientInfo() {

        ClientInfo clientInfo = (ClientInfo) stepData.get("clientInfo");
        assertNull(clientInfo);
    }

    @Given("^I create message query for current account with limit (\\d+)$")
    public void createMessageQueryForAccount(int limit) {

        Account account = (Account) stepData.get("LastAccount");
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

        Account account = (Account) stepData.get("LastAccount");
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

        Account account = (Account) stepData.get("LastAccount");
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

        Account account = (Account) stepData.get("LastAccount");
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

    @Then("^The value of \"(.*)\" is exactly (\\d+)$")
    public void checkValueOfIntegerItem(String valKey, long value) {
        assertEquals(value, (long) stepData.get(valKey));
    }

    // Private helper functions

    private KapuaDataPayload createRandomTestPayload() {

        KapuaDataPayloadImpl tmpTestPayload = new KapuaDataPayloadImpl();

        byte[] randomPayload = new byte[128];
        random.nextBytes(randomPayload);
        String stringPayload = "Hello first message on!";
        byte[] tmpByteArrayPayload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());
        tmpTestPayload.setBody(tmpByteArrayPayload);

        Map<String, Object> tmpMetrics = new HashMap<String, Object>();
        tmpMetrics.put("float", random.nextFloat() * 100);
        tmpMetrics.put("integer", random.nextInt());
        tmpMetrics.put("double", random.nextDouble() * 100);
        tmpMetrics.put("long", random.nextLong());
        tmpMetrics.put("string_value", Integer.toString(random.nextInt()));
        tmpTestPayload.setMetrics(tmpMetrics);

        return tmpTestPayload;
    }

    private KapuaPosition createRandomTestPosition(Date timeStamp) {

        KapuaPositionImpl tmpPosition = new KapuaPositionImpl();
        tmpPosition.setAltitude(20000.0 * random.nextDouble()); // 0 .. 20000
        tmpPosition.setHeading(360.0 * random.nextDouble()); // 0 .. 360
        tmpPosition.setLatitude(180.0 * (random.nextDouble() - 0.5)); // -90 .. 90
        tmpPosition.setLongitude(360.0 * (random.nextDouble() - 0.5)); // -180 .. 180
        tmpPosition.setPrecision(100.0 * random.nextDouble()); // 0 .. 100
        tmpPosition.setSatellites(random.nextInt(30)); // 0 .. 30
        tmpPosition.setSpeed(500.0 * random.nextDouble()); // 0 .. 500
        tmpPosition.setStatus(random.nextInt(10)); // 0 .. 10
        tmpPosition.setTimestamp(timeStamp);

        return tmpPosition;
    }

    private KapuaDataMessage createTestMessage(KapuaId scopeId, KapuaId deviceId, String clientId) {

        final String tmpSemanticTopic = "registry/cache/check";
        KapuaDataMessage tmpMessage = createTestMessageWithTopic(scopeId, deviceId, clientId, tmpSemanticTopic);

        return tmpMessage;
    }

    private KapuaDataMessage createTestMessageWithTopic(KapuaId scopeId, KapuaId deviceId, String clientId, String topic) {

        KapuaDataMessageImpl tmpMessage = new KapuaDataMessageImpl();

        Date tmpRecDate = new Date();
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.add(Calendar.MINUTE, -10);
        Date tmpSentDate = tmpCal.getTime();

        tmpMessage.setReceivedOn(tmpRecDate);
        tmpMessage.setCapturedOn(tmpRecDate);
        tmpMessage.setSentOn(tmpSentDate);
        tmpMessage.setClientId(clientId);
        tmpMessage.setDeviceId(deviceId);
        tmpMessage.setScopeId(scopeId);

        final KapuaDataChannelImpl tmpChannel = new KapuaDataChannelImpl();
        tmpChannel.setSemanticParts(new ArrayList<>(Arrays.asList(topic.split("/"))));
        tmpMessage.setChannel(tmpChannel);

        tmpMessage.setPayload(createRandomTestPayload());
        tmpMessage.setPosition(createRandomTestPosition(tmpSentDate));

        return tmpMessage;
    }

    private StorableId insertMessageInStore(KapuaDataMessage message) throws KapuaException {

        InsertResponse tmpResponse = messageStoreService.store(message);
        StorableId tmpId = new StorableIdImpl(tmpResponse.getId());
        return tmpId;
    }

    private List<StorableId> insertMessagesInStore(List<KapuaDataMessage> messages) throws KapuaException {

        StorableId tmpId = null;
        List<StorableId> tmpList = new ArrayList<StorableId>();

        for (KapuaDataMessage tmpMsg : messages) {
            tmpId = insertMessageInStore(tmpMsg);
            tmpList.add(tmpId);
        }

        return tmpList;
    }

    private boolean areSemanticPartsEqual(List<String> parts1, List<String> parts2) {

        if ((parts1 == null) && (parts2 == null)) {
            return true;
        }
        if ((parts1 != null) && (parts2 != null)) {
            assertEquals(parts1.size(), parts2.size());
            for (int i = 0; i < parts1.size(); i++) {
                assertEquals(parts1.get(i), parts2.get(i));
            }
        }
        return true;
    }

    private boolean areMetricsEqual(Map<String, Object> properties1, Map<String, Object> properties2) {

        assertEquals(properties1.size(), properties2.size());

        if (properties1.size() > 0) {
            for (String key : properties1.keySet()) {
                assertTrue(properties2.containsKey(key));
                assertEquals(properties1.get(key), properties2.get(key));
            }
        }
        return true;
    }

    private void isChannelForFirstMessageInStoreOK(StorableId msgId, Date storedOn) throws KapuaException {

        KapuaId tmpAccId = ((Account) stepData.get("LastAccount")).getId();
        String tmpClId = ((Device) stepData.get("LastDevice")).getClientId();
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(new TermPredicateImpl(ChannelInfoField.CLIENT_ID, tmpClId));
        ChannelInfoQuery channelInfoQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(tmpAccId, 100);
        channelInfoQuery.setPredicate(andPredicate);

        ChannelInfoListResult channelInfoList = channelInfoRegistryService.query(channelInfoQuery);

        assertNotNull("Cannot find the channel info registry!", channelInfoList);
        assertNotEquals("Cannot find the channel info registry!", channelInfoList.getSize(), 0);
        assertNotNull("Cannot find the channel info registry!", channelInfoList.getFirstItem());
        assertEquals("Wrong channel info message id!", channelInfoList.getFirstItem().getFirstMessageId(), msgId);
        assertEquals("Wrong channel info message on!", channelInfoList.getFirstItem().getFirstMessageOn(), storedOn);
    }

    private void isClientForFirstMessageInStoreOK(StorableId msgId, Date storedOn) throws KapuaException {

        KapuaId tmpAccId = ((Account) stepData.get("LastAccount")).getId();
        String tmpClId = ((Device) stepData.get("LastDevice")).getClientId();
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(new TermPredicateImpl(ClientInfoField.CLIENT_ID, tmpClId));
        ClientInfoQuery clientInfoQuery = DatastoreQueryFactory.createBaseClientInfoQuery(tmpAccId, 100);
        clientInfoQuery.setPredicate(andPredicate);

        ClientInfoListResult clientInfoList = clientInfoRegistryService.query(clientInfoQuery);

        assertNotNull("Cannot find the client info registry!", clientInfoList);
        assertNotEquals("Cannot find the client info registry!", clientInfoList.getSize(), 0);
        assertNotNull("Cannot find the client info registry!", clientInfoList.getFirstItem());
        assertEquals("Wrong client info message id!", clientInfoList.getFirstItem().getFirstMessageId(), msgId);
        assertEquals("Wrong client info message on!", clientInfoList.getFirstItem().getFirstMessageOn(), storedOn);
    }

    private void isMetricForFirstMessageInStoreOK(StorableId msgId, Date storedOn) throws KapuaException {

        KapuaId tmpAccId = ((Account) stepData.get("LastAccount")).getId();
        String tmpClId = ((Device) stepData.get("LastDevice")).getClientId();
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(new TermPredicateImpl(MetricInfoField.CLIENT_ID, tmpClId));
        MetricInfoQuery metricInfoQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(tmpAccId, 100);
        metricInfoQuery.setPredicate(andPredicate);

        MetricInfoListResult metricInfoList = metricInfoRegistryService.query(metricInfoQuery);

        assertNotNull("Cannot find the metric info registry!", metricInfoList);
        assertNotEquals("Cannot find the client info registry!", metricInfoList.getSize(), 0);
        assertNotNull("Cannot find the metric info registry!", metricInfoList.getFirstItem());
        assertEquals("Wrong metric info message id!", metricInfoList.getFirstItem().getFirstMessageId(), msgId);
        assertEquals("Wrong metric info message on!", metricInfoList.getFirstItem().getFirstMessageOn(), storedOn);
    }

    private boolean arePositionsEqual(KapuaPosition position1, KapuaPosition position2) {

        assertEquals(position1.getAltitude(), position2.getAltitude());
        assertEquals(position1.getHeading(), position2.getHeading());
        assertEquals(position1.getLatitude(), position2.getLatitude());
        assertEquals(position1.getLongitude(), position2.getLongitude());
        assertEquals(position1.getPrecision(), position2.getPrecision());
        assertEquals(position1.getSatellites(), position2.getSatellites());
        assertEquals(position1.getSpeed(), position2.getSpeed());
        assertEquals(position1.getStatus(), position2.getStatus());
        assertEquals(position1.getTimestamp(), position2.getTimestamp());
        return true;
    }

    // Check whether the message that was inserted into the message store matches the originally prepared message
    private boolean checkThatTheInsertedMessageMatchesTheOriginal(KapuaDataMessage origMsg, DatastoreMessage foundMsg) throws KapuaException {

        assertTrue(areSemanticPartsEqual(origMsg.getChannel().getSemanticParts(), foundMsg.getChannel().getSemanticParts()));
        assertArrayEquals(origMsg.getPayload().getBody(), foundMsg.getPayload().getBody());
        assertTrue(areMetricsEqual(origMsg.getPayload().getMetrics(), foundMsg.getPayload().getMetrics()));
        assertTrue(arePositionsEqual(origMsg.getPosition(), foundMsg.getPosition()));
        assertEquals(origMsg.getCapturedOn(), foundMsg.getCapturedOn());
        assertEquals(origMsg.getSentOn(), foundMsg.getSentOn());
        assertEquals(origMsg.getReceivedOn(), foundMsg.getReceivedOn());

        return true;
    }

    // Check that two datastore messages match
    private boolean checkThatDatastoreMessagesMatch(DatastoreMessage firstMsg, DatastoreMessage secondMsg) throws KapuaException {

        assertEquals(firstMsg.getDatastoreId().toString(), secondMsg.getDatastoreId().toString());
        assertTrue(areSemanticPartsEqual(firstMsg.getChannel().getSemanticParts(), secondMsg.getChannel().getSemanticParts()));
        assertArrayEquals(firstMsg.getPayload().getBody(), secondMsg.getPayload().getBody());
        assertTrue(areMetricsEqual(firstMsg.getPayload().getMetrics(), secondMsg.getPayload().getMetrics()));
        assertTrue(arePositionsEqual(firstMsg.getPosition(), secondMsg.getPosition()));
        assertEquals(firstMsg.getCapturedOn(), secondMsg.getCapturedOn());
        assertEquals(firstMsg.getSentOn(), secondMsg.getSentOn());
        assertEquals(firstMsg.getReceivedOn(), secondMsg.getReceivedOn());

        return true;
    }

    /**
     * Clean-up of indices between scenarios.
     *
     * @throws Exception
     */
    private void deleteAllIndices() throws Exception {

        DatastoreMediator.getInstance().deleteAllIndexes();
    }

    private void refreshAllIndices() throws Exception {

        DatastoreMediator.getInstance().refreshAllIndexes();
    }

    private void clearAllCaches() {

        DatastoreCacheManager.getInstance().getChannelsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getClientsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetricsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetadataCache().invalidateAll();
    }

    private void waitABit(int millis) {

        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
        }
    }
}
