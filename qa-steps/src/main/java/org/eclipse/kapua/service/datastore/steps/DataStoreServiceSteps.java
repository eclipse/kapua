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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.steps;

import static org.eclipse.kapua.service.datastore.model.query.SortField.ascending;
import static org.eclipse.kapua.service.datastore.model.query.SortField.descending;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.SecurityUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
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
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.ChannelInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.ClientInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.MetricInfoRegistryServiceProxy;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreChannel;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MetricsIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TermPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.StorableListResult;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.ChannelMatchPredicate;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.user.steps.TestConfig;
import org.eclipse.kapua.test.steps.AbstractKapuaSteps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.guice.ScenarioScoped;

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
        storablePredicateFactory = locator.getFactory(StorablePredicateFactory.class);
        channelInfoRegistryService = locator.getService(ChannelInfoRegistryService.class);
        channelInfoRegistryServiceProxy = new ChannelInfoRegistryServiceProxy();
        metricInfoRegistryService = locator.getService(MetricInfoRegistryService.class);
        metricInfoRegistryServiceProxy = new MetricInfoRegistryServiceProxy();
        clientInfoRegistryService = locator.getService(ClientInfoRegistryService.class);
        clientInfoRegistryServiceProxy = new ClientInfoRegistryServiceProxy();

        // JAXB Context
        XmlUtil.setContextProvider(new DatastoreJAXBContextProvider());
    }

    @After
    public void afterScenario() throws Exception {

        try {
            deleteAllIndices();
            logger.info("Logging out in cleanup");
            SecurityUtils.getSubject().logout();
            KapuaSecurityUtils.clearSession();
        } catch (Exception e) {
            logger.error("Failed to log out in @After", e);
        }
    }

    @When("^I configure datastore service$")
    public void setDatastoreServiceConfig(List<TestConfig> testConfigs)
            throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId;
        KapuaId scopeId;
        Account tmpAccount = (Account) stepData.get("LastAccount");

        if (tmpAccount != null) {
            accId = tmpAccount.getId();
            scopeId = tmpAccount.getScopeId();
        } else {
            accId = new KapuaEid(BigInteger.ONE);
            scopeId = new KapuaEid(BigInteger.ONE);
        }

        for (TestConfig config : testConfigs) {
            config.addConfigToMap(valueMap);
        }

        messageStoreService.setConfigValues(accId, scopeId, valueMap);
    }

    @Given("^All indices are deleted$")
    public void deleteIndices() throws Exception {

        deleteAllIndices();
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

    @Given("^I prepare a random message with delta for sent (-?\\d+), captured (-?\\d+) and received (-?\\d+), save it as \"(.*)\"$")
    public void prepareAndRememberARandomMessageWithDeltas(int dSent, int dCaptured, int dReceived, String msgKey) throws Exception {

        KapuaDataMessage tmpMessage = createTestMessageWithTimeDelta(((Account) stepData.get("LastAccount")).getId(),
                ((Device) stepData.get("LastDevice")).getId(),
                ((Device) stepData.get("LastDevice")).getClientId(),
                null,
                dSent, dCaptured,dReceived);
        stepData.put(msgKey, tmpMessage);
    }

    @Given("^I prepare a random message and save it as \"(.*)\"$")
    public void prepareAndRememberARandomMessage(String msgKey) throws Exception {

        KapuaDataMessage tmpMessage = createTestMessage(((Account) stepData.get("LastAccount")).getId(),
                ((Device) stepData.get("LastDevice")).getId(),
                ((Device) stepData.get("LastDevice")).getClientId(),
                null, null);
        stepData.put(msgKey, tmpMessage);
    }

    @Given("^I prepare a random message with null payload and save it as \"(.*)\"$")
    public void prepareRandomMessageWithNullPayload(String msgKey) throws Exception {

        KapuaDataMessage tmpMessage = createTestMessage(
                ((Account) stepData.get("LastAccount")).getId(),
                ((Device) stepData.get("LastDevice")).getId(),
                ((Device) stepData.get("LastDevice")).getClientId(),
                null, null);
        tmpMessage.setPayload(null);
        stepData.put(msgKey, tmpMessage);
    }

    @Given("^I prepare a number of messages with the following details and remember the list as \"(.*)\"$")
    public void prepareAListOfMessagesWithDifferentTopics(String listKey, List<TestTopic> topics) throws Exception {

        List<KapuaDataMessage> tmpList = new ArrayList<>();
        KapuaDataMessage tmpMsg = null;

        for (TestTopic tmpTopic : topics) {
            tmpMsg = createTestMessage(
                    ((Account) stepData.get("LastAccount")).getId(),
                    ((Device) stepData.get("LastDevice")).getId(),
                    tmpTopic.getClientId(),
                    tmpTopic.getTopic(),
                    tmpTopic.getCaptured());
            tmpList.add(tmpMsg);
        }

        stepData.put(listKey, tmpList);
    }

    @And("^I set the following metrics to the message (\\d+) from the list \"(.+)\"$")
    public void appendMetricsToSelectedMessage(int idx, String lstKey, List<TestMetric> metLst) throws Exception {

        List<KapuaDataMessage> tmpMsgLst = (List<KapuaDataMessage>) stepData.get(lstKey);
        KapuaDataMessage tmpMsg = tmpMsgLst.get(idx);

        tmpMsg.setPayload(new KapuaDataPayloadImpl());

        for (TestMetric tmpMet : metLst) {
            switch (tmpMet.getType().trim().toLowerCase()) {
            case "string": {
                tmpMsg.getPayload().getMetrics().put(tmpMet.getMetric(), tmpMet.getValue());
                break;
            }
            case "int": {
                tmpMsg.getPayload().getMetrics().put(tmpMet.getMetric(), Integer.valueOf(tmpMet.getValue()));
                break;
            }
            case "double": {
                tmpMsg.getPayload().getMetrics().put(tmpMet.getMetric(), Double.valueOf(tmpMet.getValue()));
                break;
            }
            case "bool": {
                tmpMsg.getPayload().getMetrics().put(tmpMet.getMetric(), Boolean.valueOf(tmpMet.getValue().trim().toUpperCase()));
                break;
            }
            default: {
                fail(String.format("Unknown metric type [%s]", tmpMet.getType()));
                break;
            }
            }
        }
    }

    @Given("^I prepare (\\d+) random messages and remember the list as \"(.*)\"$")
    public void prepareAndRememberANumberOfRandomMessages(int number, String lstKey) throws Exception {

        KapuaDataMessage tmpMsg;
        List<KapuaDataMessage> msgList = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            tmpMsg = createTestMessage(((Account) stepData.get("LastAccount")).getId(),
                    ((Device) stepData.get("LastDevice")).getId(),
                    ((Device) stepData.get("LastDevice")).getClientId(),
                    null, null);
            msgList.add(tmpMsg);
        }

        stepData.put(lstKey, msgList);
    }

    @Given("^I prepare (\\d+) randomly ordered messages and remember the list as \"(.*)\"$")
    public void prepareUnorderedRandomMessagesWithTopics(int number, String lstKey, List<TestTopic> topics) throws Exception {

        Account tmpAccount = (Account) stepData.get("LastAccount");
        KapuaDataMessage tmpMsg;
        List<KapuaDataMessage> tmpList = new ArrayList<>();
        String clientId1 = "test-device-1";
        Date sentOn1 = new Date(new Date().getTime() - 10000);
        Date capturedOn1 = new Date(new Date().getTime() - 500);
        DeviceCreator tmpDevCr1 = deviceFactory.newCreator(tmpAccount.getId(), clientId1);
        Device device1 = deviceRegistryService.create(tmpDevCr1);
        String clientId2 = "test-device-2";
        Date sentOn2 = new Date(sentOn1.getTime() + 1000);
        Date capturedOn2 = new Date(new Date().getTime());
        DeviceCreator tmpDevCr2 = deviceFactory.newCreator(tmpAccount.getId(), clientId2);
        Device device2 = deviceRegistryService.create(tmpDevCr2);
        Device tmpDev;
        Date sentOn, capturedOn;

        String[] metrics = new String[] {
                "m_order_metric1",
                "m_order_metric2",
                "m_order_metric3",
                "m_order_metric4",
                "m_order_metric5",
                "m_order_metric6" };
        String[] metricsValuesString = new String[] {
                "string_metric_1",
                "string_metric_2",
                "string_metric_3",
                "string_metric_4",
                "string_metric_5",
                "string_metric_6" };
        Date[] metricsValuesDate = new Date[] {
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:01 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:02 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:03 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:04 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:05 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:06 01/01/2017").getTime()) };
        int[] metricsValuesInt = new int[] { 10, 20, 30, 40, 50, 60 };
        float[] metricsValuesFloat = new float[] { 0.002f, 10.12f, 20.22f, 33.33f, 44.44f, 55.66f };
        double[] metricsValuesDouble = new double[] { 1.002d, 11.12d, 21.22d, 34.33d, 45.44d, 56.66d };
        boolean[] metricsValuesBoolean = new boolean[] { true, true, false, true, false, false };

        for (int i = 0; i < number; i++) {
            if (i < number / 4) {
                sentOn = sentOn1;
                capturedOn = capturedOn1;
            } else if (i < number / 2) {
                sentOn = sentOn1;
                capturedOn = capturedOn2;
            } else if (i < number * 3 / 4) {
                sentOn = sentOn2;
                capturedOn = capturedOn1;
            } else {
                sentOn = sentOn2;
                capturedOn = capturedOn2;
            }
            if (i % 2 == 0) {
                tmpDev = device1;
            } else {
                tmpDev = device2;
            }

            Date receivedOn = new Date();
            TestTopic tmpTopic = topics.get(i % topics.size());
            tmpMsg = createTestMessage(
                    tmpAccount.getId(),
                    tmpDev.getId(),
                    tmpDev.getClientId(),
                    tmpTopic.getTopic(),
                    null);
            tmpMsg.setCapturedOn(capturedOn);
            tmpMsg.setSentOn(sentOn);
            tmpMsg.setReceivedOn(receivedOn);
            tmpMsg.getPayload().getMetrics().put(metrics[0], metricsValuesDate[i % metricsValuesDate.length]);
            tmpMsg.getPayload().getMetrics().put(metrics[1], metricsValuesString[i % metricsValuesString.length]);
            tmpMsg.getPayload().getMetrics().put(metrics[2], metricsValuesInt[i % metricsValuesInt.length]);
            tmpMsg.getPayload().getMetrics().put(metrics[3], metricsValuesFloat[i % metricsValuesFloat.length]);
            tmpMsg.getPayload().getMetrics().put(metrics[4], metricsValuesBoolean[i % metricsValuesBoolean.length]);
            tmpMsg.getPayload().getMetrics().put(metrics[5], metricsValuesDouble[i % metricsValuesDouble.length]);
            tmpList.add(tmpMsg);
        }

        stepData.put(lstKey, tmpList);
    }

    @Given("^I store the message \"(.*)\" and remember its ID as \"(.*)\"$")
    public void insertRandomMessageIntoDatastore(String msgKey, String idKey) throws KapuaException {

        KapuaDataMessage tmpMessage = (KapuaDataMessage) stepData.get(msgKey);
        StorableId storeId = insertMessageInStore(tmpMessage);
        stepData.put(idKey, storeId);
    }

    @Given("^I store the message \"(.*)\" with the server time and remember its ID as \"(.*)\"$")
    public void insertRandomMessageIntoDatastoreWithCurrentTimestamp(String msgKey, String idKey) throws KapuaException {

        KapuaDataMessage tmpMessage = (KapuaDataMessage) stepData.get(msgKey);
        tmpMessage.setReceivedOn(new Date());
        StorableId storeId = insertMessageInStore(tmpMessage);
        stepData.put(idKey, storeId);
    }

    @SuppressWarnings("unchecked")
    @Given("^I store the messages from list \"(.*)\" and remember the IDs as \"(.*)\"$")
    public void insertRandomMessagesIntoDatastore(String msgListKey, String idListKey) throws KapuaException {

        List<KapuaDataMessage> tmpMsgList = (List<KapuaDataMessage>) stepData.get(msgListKey);
        List<StorableId> tmpIdList = insertMessagesInStore(tmpMsgList);
        stepData.put(idListKey, tmpIdList);
    }

    @Given("^I store the messages from list \"(.*)\" with the server time and remember the IDs as \"(.*)\"$")
    public void insertRandomMessagesIntoDatastoreWithCurrentTimestamps(String msgListKey, String idListKey) throws KapuaException {

        List<KapuaDataMessage> tmpMsgList = (List<KapuaDataMessage>) stepData.get(msgListKey);
        StorableId tmpId = null;
        List<StorableId> tmpList = new ArrayList<>();

        for (KapuaDataMessage tmpMsg : tmpMsgList) {
            tmpMsg.setReceivedOn(new Date());
            tmpId = insertMessageInStore(tmpMsg);
            tmpList.add(tmpId);
        }

        stepData.put(idListKey, tmpList);
    }

    @Given("^I set the database to device timestamp indexing$")
    public void setDatabaseToDeviceTimestampIndexing() throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(),
                DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
    }

    @Given("^I set the database to server timestamp indexing$")
    public void setDatabaseToServerTimestampIndexing() throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(),
                DataIndexBy.SERVER_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
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

    @SuppressWarnings("unchecked")
    @When("^I pick the ID number (\\d+) from the list \"(.*)\" and remember it as \"(.*)\"$")
    public void pickAMessageIdFromAList(int index, String lstKey, String idKey) {

        List<StorableId> msgIds = (List<StorableId>) stepData.get(lstKey);
        StorableId tmpId = msgIds.get(index);
        stepData.put(idKey, tmpId);
    }

    @When("^I pick the ID of the channel number (\\d+) in the list \"(.*)\" and remember it as \"(.*)\"$")
    public void pickAChannelIdFromAList(int index, String lstKey, String idKey) {

        ChannelInfoListResult tmpLst = (ChannelInfoListResult) stepData.get(lstKey);
        StorableId tmpId = tmpLst.getItem(index).getId();
        stepData.put(idKey, tmpId);
    }

    @When("^I pick message number (\\d+) from the list \"(.+)\" and remember it as \"(.+)\"$")
    public void pickADatastoreMessageFromList(int index, String lstKey, String msgKey) {

        MessageListResult tmpList = (MessageListResult) stepData.get(lstKey);
        DatastoreMessage tmpMsg = tmpList.getItem(index);
        stepData.put(msgKey, tmpMsg);
    }

    @When("^I search for the last inserted message$")
    public void findLastInsertedMessage() throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId msgId = (StorableId) stepData.get("LastStoredMessageId");
        DatastoreMessage tmpMsg = messageStoreService.find(account.getId(), msgId, StorableFetchStyle.SOURCE_FULL);
        stepData.put("LastMessageInDatastore", tmpMsg);
    }

    @When("^I search for the last inserted message and save it as \"(.*)\"$")
    public void findLastInsertedMessage(String keyName) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId msgId = (StorableId) stepData.get("LastStoredMessageId");
        DatastoreMessage tmpMsg = messageStoreService.find(account.getId(), msgId, StorableFetchStyle.SOURCE_FULL);
        stepData.put(keyName, tmpMsg);
    }

    @When("^I search for a data message with ID \"(.*)\" and remember it as \"(.*)\"")
    public void searchForMessageInDatastore(String idKey, String msgKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId tmpId = (StorableId) stepData.get(idKey);
        DatastoreMessage tmpMsg = messageStoreService.find(account.getId(), tmpId, StorableFetchStyle.SOURCE_FULL);
        stepData.put(msgKey, tmpMsg);
    }

    @SuppressWarnings("unchecked")
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

    @When("^I perform a default query for the account messages and store the results as \"(.+)\"$")
    public void performADefaultMessageQuery(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MessageQuery query = DatastoreQueryFactory.createBaseMessageQuery(account.getId(), 100);
        MessageListResult msgList = messageStoreService.query(query);
        stepData.put(lstKey, msgList);
    }

    @When("^I perform an ordered query for messages and store the results as \"(.*)\"$")
    public void performAnOrderedMessageQuery(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MessageQuery query = getBaseMessageQuery(account.getId(), 100, getDefaultListOrdering());
        MessageListResult msgList = messageStoreService.query(query);
        stepData.put(lstKey, msgList);
    }

    @When("^I perform an ordered query for messages with delta for date from (-?\\d+) to (-?\\d+) and store the results as \"(.*)\"$")
    public void performAnOrderedMessageQueryWithDelta(int dFrom, int dTo, String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MessageQuery query = getBaseMessageQuery(account.getId(), 100, getDefaultListOrdering());

        Date now = new Date();
        RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP,
                new Date(now.getTime() + dFrom), new Date(now.getTime() + dTo));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        query.setPredicate(andPredicate);

        MessageListResult msgList = messageStoreService.query(query);
        stepData.put(lstKey, msgList);
    }

    @When("^I count the current account messages and store the count as \"(.*)\"$")
    public void countAccountMessages(String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MessageQuery msgQuery = DatastoreQueryFactory.createBaseMessageQuery(account.getId(), 100);

        long messageCount = messageStoreService.count(msgQuery);
        stepData.put(countKey, messageCount);
    }

    @When("^I count the current account messages with delta for date from (-?\\d+) to (-?\\d+) and store the count as \"(.*)\"$")
    public void countAccountMessagesWitDelta(int dFrom, int dTo, String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MessageQuery msgQuery = DatastoreQueryFactory.createBaseMessageQuery(account.getId(), 100);

        Date now = new Date();
        RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP,
                new Date(now.getTime() + dFrom), new Date(now.getTime() + dTo));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        msgQuery.setPredicate(andPredicate);

        long messageCount = messageStoreService.count(msgQuery);
        stepData.put(countKey, messageCount);
    }

    @When("^I query for the current account channel(?:|s) (?:|again )and store (?:it|them) as \"(.*)\"$")
    public void queryForAccountChannelInfo(String chnKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoQuery tmpQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);

        ChannelInfoListResult tmpList = channelInfoRegistryService.query(tmpQuery);
        stepData.put(chnKey, tmpList);
    }

    @When("^I query for the current account channels in the date range \"(.+)\" to \"(.+)\" and store them as \"(.+)\"$")
    public void queryForAccountChannelInfoByDateRange(String start, String end, String lstKey) throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoQuery tmpQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);
        RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP,
                KapuaDateUtils.parseDate(start), KapuaDateUtils.parseDate(end));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        tmpQuery.setPredicate(andPredicate);

        ChannelInfoListResult tmpList = channelInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @When("^I query for the current account channels with the filter \"(.+)\" and store them as \"(.+)\"$")
    public void queryForAccountChannelInfoWithFiltering(String filter, String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        DatastoreChannel datastoreChannel = new DatastoreChannel(filter);
        ChannelInfoQuery tmpQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);
        ChannelMatchPredicate channelMatchPredicate = new ChannelMatchPredicateImpl(datastoreChannel.getChannelCleaned());
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(channelMatchPredicate);
        tmpQuery.setPredicate(andPredicate);

        ChannelInfoListResult tmpList = channelInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @Then("^The channel list \"(.+)\" contains the following topics$")
    public void checkThattheChannelInfoContainsTheSpecifiedTopics(String lstKey, List<TestTopic> topics) {

        ChannelInfoListResult tmpList = (ChannelInfoListResult) stepData.get(lstKey);
        Set<String> infoTopics = new HashSet<>();

        assertEquals("Wrong number of topics found!", tmpList.getSize(), topics.size());

        for (ChannelInfo tmpInfo : tmpList.getItems()) {
            infoTopics.add(tmpInfo.getName());
        }
        for (TestTopic tmpTop : topics) {
            assertTrue(String.format("The topic [%s] was not found!", tmpTop.getTopic()), infoTopics.contains(tmpTop.getTopic()));
        }
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

    @Then("^Client \"(.+)\" first published on a channel in the list \"(.+)\" on \"(.+)\"$")
    public void checkFirstPublishDateOnChannel(String clientId, String lstKey, String date) throws Exception {

        ChannelInfoListResult chnList = (ChannelInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (ChannelInfo tmpInfo : chnList.getItems()) {
            if (tmpInfo.getClientId().equals(clientId)) {
                assertEquals(tmpCaptured, tmpInfo.getFirstMessageOn());
                return;
            }
        }
        fail(String.format("No channel matches the client id [%s]", clientId));
    }

    @Then("^Client \"(.+)\" last published on a channel in the list \"(.+)\" on \"(.+)\"$")
    public void checkLastPublishDateOnChannel(String clientId, String lstKey, String date) throws Exception {

        ChannelInfoListResult chnList = (ChannelInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (ChannelInfo tmpInfo : chnList.getItems()) {
            if (tmpInfo.getClientId().equals(clientId)) {
                assertEquals(tmpCaptured, tmpInfo.getLastMessageOn());
                return;
            }
        }
        fail(String.format("No channel matches the client id [%s]", clientId));
    }

    @When("^I delete all channels from the list \"(.*)\"$")
    public void deleteAllChannelsFromList(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoListResult tmpList = (ChannelInfoListResult) stepData.get(lstKey);

        for (ChannelInfo tmpItem : tmpList.getItems()) {
            channelInfoRegistryServiceProxy.delete(account.getId(), tmpItem.getId());
        }
    }

    @When("^I delete the channel with the ID \"(.*)\"$")
    public void deleteChannelWithId(String idKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        StorableId tmpId = (StorableId) stepData.get(idKey);

        channelInfoRegistryServiceProxy.delete(account.getId(), tmpId);
    }

    @When("^I query for the current account metrics (?:|again )and store (?:it|them) as \"(.*)\"$")
    public void queryForAccountMetrics(String metricKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);

        stepData.put("metricInfoQuery", tmpQuery);
        MetricInfoListResult tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(metricKey, tmpList);
    }

    @When("^I query for the current account metrics in the date range \"(.+)\" to \"(.+)\" and store them as \"(.+)\"$")
    public void queryForAccountMetricsInfoByDateRange(String start, String end, String lstKey) throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);
        RangePredicate timestampPredicate = new RangePredicateImpl(MetricInfoField.TIMESTAMP_FULL,
                KapuaDateUtils.parseDate(start), KapuaDateUtils.parseDate(end));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        tmpQuery.setPredicate(andPredicate);

        stepData.put("metricInfoQuery", tmpQuery);
        MetricInfoListResult tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @When("^I perform an ordered query for the current account metrics in the date range \"(.+)\" to \"(.+)\" and store them as \"(.+)\"$")
    public void queryForAccountMetricsInfoByDateRangeOrdered(String start, String end, String lstKey) throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);
        RangePredicate timestampPredicate = new RangePredicateImpl(MetricInfoField.TIMESTAMP_FULL,
                KapuaDateUtils.parseDate(start), KapuaDateUtils.parseDate(end));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        tmpQuery.setPredicate(andPredicate);
        tmpQuery.setSortFields(getNamedMetricOrdering().stream().map(OrderConstraint::getField).collect(Collectors.toList()));

        stepData.put("metricInfoQuery", tmpQuery);
        MetricInfoListResult tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @When("^I perform an ordered query for metrics and store the results as \"(.*)\"$")
    public void performAnOrderedMetricQuery(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery query = getBaseMetricQuery(account.getId(), 100, getNamedMetricOrdering());
        MetricInfoListResult metList = metricInfoRegistryService.query(query);
        stepData.put(lstKey, metList);
    }

    @When("^I count the current account metrics and store the count as \"(.*)\"$")
    public void countAccountMetrics(String countKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);

        stepData.put("metricInfoQuery", tmpQuery);
        stepData.remove(countKey);
        long metricCount = metricInfoRegistryService.count(tmpQuery);
        stepData.put(countKey, metricCount);
    }

    @When("^I query for the metrics in topic \"(.*)\" and store them as \"(.*)\"$")
    public void queryForTopicMetrics(String topic, String lstKey) throws KapuaException {

        stepData.remove("metricInfoQuery");
        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);
        AndPredicate andPredicate = new AndPredicateImpl();
        TermPredicate tmpPred = new TermPredicateImpl(MetricInfoField.CHANNEL, topic);
        MetricInfoListResult tmpList = null;

        andPredicate.getPredicates().add(tmpPred);
        tmpQuery.setPredicate(andPredicate);
        stepData.put("metricInfoQuery", tmpQuery);
        stepData.remove(lstKey);
        tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @When("^I query for the metrics from client \"(.*)\" and store them as \"(.*)\"$")
    public void queryForClientMetrics(String clientId, String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(account.getId(), 100);
        AndPredicate andPredicate = new AndPredicateImpl();
        TermPredicate tmpPred = new TermPredicateImpl(MetricInfoField.CLIENT_ID, clientId);
        MetricInfoListResult tmpList = null;

        andPredicate.getPredicates().add(tmpPred);
        tmpQuery.setPredicate(andPredicate);
        stepData.put("metricInfoQuery", tmpQuery);
        tmpList = metricInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @Then("^There (?:is|are) exactly (\\d+) metric(?:|s) in the list \"(.*)\"$")
    public void checkNumberOfQueriedMetrics(int cnt, String lstKey) {

        MetricInfoListResult tmpResults = (MetricInfoListResult) stepData.get(lstKey);
        assertEquals(cnt, tmpResults.getSize());
    }

    @Then("^Client \"(.+)\" first published a metric in the list \"(.+)\" on \"(.+)\"$")
    public void checkFirstPublishDateOfClientMetric(String clientId, String lstKey, String date) throws Exception {

        MetricInfoListResult metList = (MetricInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (MetricInfo tmpMet : metList.getItems()) {
            if (tmpMet.getClientId().equals(clientId)) {
                assertEquals(tmpMet.getFirstMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("No metric matches the client id [%s]", clientId));
    }

    @Then("^Client \"(.+)\" last published a metric in the list \"(.+)\" on \"(.+)\"$")
    public void checkLastPublishDateOfClientMetric(String clientId, String lstKey, String date) throws Exception {

        MetricInfoListResult metList = (MetricInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (MetricInfo tmpMet : metList.getItems()) {
            if (tmpMet.getClientId().equals(clientId)) {
                assertEquals(tmpMet.getLastMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("No metric matches the client id [%s]", clientId));
    }

    @Then("^The metric \"(.+)\" was first published in the list \"(.+)\" on \"(.+)\"$")
    public void checkFirstPublishDateOfMetricInList(String metric, String lstKey, String date) throws Exception {

        MetricInfoListResult metList = (MetricInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (MetricInfo tmpMet : metList.getItems()) {
            if (tmpMet.getName().equals(metric)) {
                assertEquals(tmpMet.getFirstMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("There is no metric [%s]", metric));
    }

    @Then("^The metric \"(.+)\" was last published in the list \"(.+)\" on \"(.+)\"$")
    public void checkLastPublishDateOfMetricInList(String metric, String lstKey, String date) throws Exception {

        MetricInfoListResult metList = (MetricInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (MetricInfo tmpMet : metList.getItems()) {
            if (tmpMet.getName().equals(metric)) {
                assertEquals(tmpMet.getLastMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("There is no metric [%s]", metric));
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

    @When("^I query for current account clients in the date range \"(.+)\" to \"(.+)\" and store them as \"(.+)\"$")
    public void queryForAccountClientInfoByDateRange(String start, String end, String lstKey) throws Exception {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoQuery tmpQuery = DatastoreQueryFactory.createBaseClientInfoQuery(account.getId(), 100);
        RangePredicate timestampPredicate = new RangePredicateImpl(ClientInfoField.TIMESTAMP,
                KapuaDateUtils.parseDate(start), KapuaDateUtils.parseDate(end));
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(timestampPredicate);
        tmpQuery.setPredicate(andPredicate);

        ClientInfoListResult tmpList = clientInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
    }

    @When("^I query for the current account client with the Id \"(.+)\" and store it as \"(.+)\"$")
    public void queryForAccountClientInfoByClientId(String clientId, String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoQuery tmpQuery = DatastoreQueryFactory.createBaseClientInfoQuery(account.getId(), 100);
        TermPredicate clientIdPredicate = storablePredicateFactory.newTermPredicate(MetricInfoField.CLIENT_ID, clientId);
        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(clientIdPredicate);
        tmpQuery.setPredicate(andPredicate);

        ClientInfoListResult tmpList = clientInfoRegistryService.query(tmpQuery);
        stepData.put(lstKey, tmpList);
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

    @Then("^There (?:is|are) exactly (\\d+) message(?:|s) in the list \"(.*)\"$")
    public void checkNumberOfQueriedMessages(int cnt, String lstKey) {

        assertEquals(cnt, ((MessageListResult) stepData.get(lstKey)).getSize());
    }

    @Then("^Client \"(.+)\" first message in the list \"(.+)\" is on \"(.+)\"$")
    public void checkFirstPublishDateForClient(String clientId, String lstKey, String date) throws Exception {

        ClientInfoListResult cliList = (ClientInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (ClientInfo tmpInfo : cliList.getItems()) {
            if (tmpInfo.getClientId().equals(clientId)) {
                assertEquals(tmpInfo.getFirstMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("No client info item matches the client id [%s]", clientId));
    }

    @Then("^Client \"(.+)\" last message in the list \"(.+)\" is on \"(.+)\"$")
    public void checkLastPublishDateForClient(String clientId, String lstKey, String date) throws Exception {

        ClientInfoListResult cliList = (ClientInfoListResult) stepData.get(lstKey);
        Date tmpCaptured = KapuaDateUtils.parseDate(date);

        for (ClientInfo tmpInfo : cliList.getItems()) {
            if (tmpInfo.getClientId().equals(clientId)) {
                assertEquals(tmpInfo.getLastMessageOn(), tmpCaptured);
                return;
            }
        }
        fail(String.format("No client info item matches the client id [%s]", clientId));
    }

    @When("^I delete all clients from the list \"(.*)\"$")
    public void deleteAllClientsFromList(String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoListResult tmpList = (ClientInfoListResult) stepData.get(lstKey);

        for (ClientInfo tmpItem : tmpList.getItems()) {
            clientInfoRegistryServiceProxy.delete(account.getId(), tmpItem.getId());
        }
    }

    @When("^I delete client number (\\d+) from the list \"(.*)\"$")
    public void deleteClientFromList(int index, String lstKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ClientInfoListResult tmpList = (ClientInfoListResult) stepData.get(lstKey);

        clientInfoRegistryServiceProxy.delete(account.getId(), tmpList.getItem(index).getId());
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
        if (origMsg.getPayload() != null) {
            isMetricForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        }
    }

    @SuppressWarnings("unchecked")
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
        if ((origMsg.getPayload() != null) || (foundMsg.getPayload() != null)) {
            isMetricForFirstMessageInStoreOK(foundMsg.getDatastoreId(), foundMsg.getTimestamp());
        }
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

    @SuppressWarnings("unchecked")
    @Then("^The channel info items \"(.+)\" match the prepared messages in \"(.+)\"$")
    public void checkThatChannelInfoMatchesTheMessageData(String channelKey, String msgKey) {

        ChannelInfoListResult tmpChn = (ChannelInfoListResult) stepData.get(channelKey);
        List<KapuaDataMessage> tmpMsgs = (List<KapuaDataMessage>) stepData.get(msgKey);

        checkChannelInfoAgainstPreparedMessages(tmpChn, tmpMsgs);
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

    @Then("^The metric info items \"(.+)\" match the prepared messages in \"(.+)\"$")
    public void checkThatMetricInfoMatchesTheMessageData(String metricKey, String msgKey) {

        MetricInfoListResult tmpMet = (MetricInfoListResult) stepData.get(metricKey);
        List<KapuaDataMessage> tmpMsgs = (List<KapuaDataMessage>) stepData.get(msgKey);

        checkMetricInfoAgainstPreparedMessages(tmpMet, tmpMsgs);
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

    @Then("^The client info items \"(.+)\" match the prepared messages in \"(.+)\"$")
    public void checkThatClientInfoMatchesTheMessageData(String infoKey, String msgKey) {

        ClientInfoListResult tmpList = (ClientInfoListResult) stepData.get(infoKey);
        List<KapuaDataMessage> tmpMsgs = (List<KapuaDataMessage>) stepData.get(msgKey);

        checkClientInfoAgainstPreparedMessages(tmpList, tmpMsgs);
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

    @When("^I delete the messages based on the last query$")
    public void deleteMessagesByQuery() throws KapuaException {

        MessageQuery messageQuery = (MessageQuery) stepData.get("messageQuery");
        messageStoreService.delete(messageQuery);
    }

    @When("^I delete the the message with the ID \"(.+)\" from the current account$")
    public void deleteMessageWithId(String id) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        messageStoreService.delete(account.getId(), new StorableIdImpl(id));
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

    @Given("^I query for the channel info of the client \"(.+)\" and store the result as \"(.+)\"$")
    public void createChannelInofQueryForClientId(String clientId, String resKey) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        ChannelInfoQuery channelInfoQuery = DatastoreQueryFactory.createBaseChannelInfoQuery(account.getId(), 100);
        AndPredicate andPredicate = new AndPredicateImpl();
        TermPredicate clientPredicate = storablePredicateFactory.newTermPredicate(ChannelInfoField.CLIENT_ID, clientId);
        andPredicate.getPredicates().add(clientPredicate);
        channelInfoQuery.setPredicate(andPredicate);

        ChannelInfoListResult infoRes = channelInfoRegistryService.query(channelInfoQuery);

        stepData.put(resKey, infoRes);
    }

    @When("^I query for channel info$")
    public void queryForChannelInfo() throws KapuaException {

        ChannelInfoQuery cnannelInfoQuery = (ChannelInfoQuery) stepData.get("channelInfoQuery");
        ChannelInfoListResult result = channelInfoRegistryService.query(cnannelInfoQuery);
        stepData.put("channelInfoListResult", result);
    }

    @When("^I delete the channel info data based on the last query$")
    public void deleteChannelInfoByQuery() throws KapuaException {

        ChannelInfoQuery tmpQuery = (ChannelInfoQuery) stepData.get("channelInfoQuery");
        channelInfoRegistryServiceProxy.delete(tmpQuery);
    }

    @When("^I delete the the channel info data with the ID \"(.+)\" from the current account$")
    public void deleteChannelInfoWithId(String id) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        channelInfoRegistryServiceProxy.delete(account.getId(), new StorableIdImpl(id));
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

    @When("^I delete the metric info data based on the last query$")
    public void deleteMetricsInfoByQuery() throws KapuaException {

        MetricInfoQuery tmpQuery = (MetricInfoQuery) stepData.get("metricInfoQuery");
        metricInfoRegistryServiceProxy.delete(tmpQuery);
    }

    @When("^I delete the the metric info data with the ID \"(.+)\" from the current account$")
    public void deleteMetricsInfoWithId(String id) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        metricInfoRegistryServiceProxy.delete(account.getId(), new StorableIdImpl(id));
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

    @When("^I delete the client info data based on the last query$")
    public void deleteClientInfoByQuery() throws KapuaException {

        ClientInfoQuery tmpQuery = (ClientInfoQuery) stepData.get("clientInfoQuery");
        clientInfoRegistryServiceProxy.delete(tmpQuery);
    }

    @When("^I delete the the client info data with the ID \"(.+)\" from the current account$")
    public void deleteClientInfoWithId(String id) throws KapuaException {

        Account account = (Account) stepData.get("LastAccount");
        clientInfoRegistryServiceProxy.delete(account.getId(), new StorableIdImpl(id));
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

    @Then("^The messages in the list \"(.*)\" are stored in the default order$")
    public void checkListForDefaultMessageOrdering(String lstKey) {

        MessageListResult msgLst = (MessageListResult) stepData.get(lstKey);
        checkListOrder(msgLst, getDefaultListOrdering());
    }

    @Then("^The metrics in the list \"(.*)\" are ordered by name$")
    public void checkListForNamedMetricOrdering(String lstKey) {

        MetricInfoListResult metLst = (MetricInfoListResult) stepData.get(lstKey);
        checkListOrder(metLst, getNamedMetricOrdering());
    }

    // Private helper functions

    private KapuaDataPayload createRandomTestPayload() throws Exception {

        KapuaDataPayloadImpl tmpTestPayload = new KapuaDataPayloadImpl();

        byte[] randomPayload = new byte[128];
        random.nextBytes(randomPayload);
        String stringPayload = "Hello first message on!";
        byte[] tmpByteArrayPayload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());
        tmpTestPayload.setBody(tmpByteArrayPayload);

        DateFormat dfBr = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfBr.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        Date dateBr = dfBr.parse("01/04/2017 03:00:00");
        DateFormat dfLA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfLA.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date dateLA = dfLA.parse("01/04/2017 03:00:00");
        DateFormat dfHK = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfHK.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
        Date dateHK = dfHK.parse("01/04/2017 03:00:00");
        DateFormat dfSyd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfSyd.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        Date dateSyd = dfSyd.parse("01/04/2017 03:00:00");

        Map<String, Object> tmpMetrics = new HashMap<String, Object>();
        tmpMetrics.put("float", random.nextFloat() * 100);
        tmpMetrics.put("float_int", Float.valueOf(random.nextInt()));
        tmpMetrics.put("double", random.nextDouble() * 100);
        tmpMetrics.put("double_int", Float.valueOf(random.nextInt()));
        tmpMetrics.put("integer", random.nextInt());
        tmpMetrics.put("long", random.nextLong());
        tmpMetrics.put("long_int", (long)random.nextInt());
        tmpMetrics.put("string_value", Integer.toString(random.nextInt()));
        tmpMetrics.put("date_value_brussels", dateBr);
        tmpMetrics.put("date_value_la", dateLA);
        tmpMetrics.put("date_value_hk", dateHK);
        tmpMetrics.put("date_value_sydney", dateSyd);
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

    private KapuaDataMessage createTestMessage(KapuaId scopeId, KapuaId deviceId, String clientId, String topic, String captured)
            throws Exception {

        String tmpTopic = (topic != null) ? topic : "default/test/topic";
        String tmpClientId = (clientId != null) ? clientId : ((Device) stepData.get("LastDevice")).getClientId();
        KapuaDataMessageImpl tmpMessage = new KapuaDataMessageImpl();

        Date tmpRecDate = new Date();
        Calendar tmpCal = Calendar.getInstance();
        tmpCal.add(Calendar.MINUTE, -10);
        Date tmpSentDate = tmpCal.getTime();

        Date tmpCaptured = tmpRecDate;
        if (captured != null) {
            tmpCaptured = KapuaDateUtils.parseDate(captured);
        }

        tmpMessage.setReceivedOn(tmpRecDate);
        tmpMessage.setSentOn(tmpSentDate);
        tmpMessage.setCapturedOn(tmpCaptured);
        tmpMessage.setClientId(tmpClientId);
        tmpMessage.setDeviceId(deviceId);
        tmpMessage.setScopeId(scopeId);

        final KapuaDataChannelImpl tmpChannel = new KapuaDataChannelImpl();
        tmpChannel.setSemanticParts(new ArrayList<>(Arrays.asList(tmpTopic.split("/"))));
        tmpMessage.setChannel(tmpChannel);

        tmpMessage.setPayload(createRandomTestPayload());
        tmpMessage.setPosition(createRandomTestPosition(tmpSentDate));

        return tmpMessage;
    }

    private KapuaDataMessage createTestMessageWithTimeDelta(KapuaId scopeId, KapuaId deviceId, String clientId, String topic,
            long dSentMill, long dCapturedMill, long dReceivedMill)
            throws Exception {

        String tmpTopic = (topic != null) ? topic : "default/test/topic";
        String tmpClientId = (clientId != null) ? clientId : ((Device) stepData.get("LastDevice")).getClientId();
        KapuaDataMessageImpl tmpMessage = new KapuaDataMessageImpl();

        Date now = new Date();
        Date capturedOn = new Date(now.getTime() + dCapturedMill);
        Date sentOn = new Date(now.getTime() + dSentMill);
        Date receivedOn = new Date(now.getTime() + dReceivedMill);

        tmpMessage.setReceivedOn(receivedOn);
        tmpMessage.setSentOn(sentOn);
        tmpMessage.setCapturedOn(capturedOn);
        tmpMessage.setClientId(tmpClientId);
        tmpMessage.setDeviceId(deviceId);
        tmpMessage.setScopeId(scopeId);

        final KapuaDataChannelImpl tmpChannel = new KapuaDataChannelImpl();
        tmpChannel.setSemanticParts(new ArrayList<>(Arrays.asList(tmpTopic.split("/"))));
        tmpMessage.setChannel(tmpChannel);

        tmpMessage.setPayload(createRandomTestPayload());
        tmpMessage.setPosition(createRandomTestPosition(sentOn));

        return tmpMessage;
    }

    private StorableId insertMessageInStore(KapuaDataMessage message) throws KapuaException {
        return messageStoreService.store(message);
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
        assertNotEquals("Cannot find the metric info registry!", metricInfoList.getSize(), 0);
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

    // create a basic message query with default parameters. Also set the requested ordering.
    private MessageQuery getBaseMessageQuery(KapuaId scopeId, int limit, List<OrderConstraint<?>> order) {

        MessageQuery tmpQuery = DatastoreQueryFactory.createBaseMessageQuery(scopeId, limit,
                order.stream().map(OrderConstraint::getField).collect(Collectors.toList()));

        return tmpQuery;
    }

    // create a basic metric query with default parameters. Also set the requested ordering.
    private MetricInfoQuery getBaseMetricQuery(KapuaId scopeId, int limit, List<OrderConstraint<?>> order) {

        MetricInfoQuery tmpQuery = DatastoreQueryFactory.createBaseMetricInfoQuery(scopeId, limit);
        tmpQuery.setSortFields(order.stream().map(OrderConstraint::getField).collect(Collectors.toList()));

        return tmpQuery;
    }

    // Check whether the message that was inserted into the message store matches the originally prepared message
    private boolean checkThatTheInsertedMessageMatchesTheOriginal(KapuaDataMessage origMsg, DatastoreMessage foundMsg) throws KapuaException {

        assertTrue(areSemanticPartsEqual(origMsg.getChannel().getSemanticParts(), foundMsg.getChannel().getSemanticParts()));
        if (origMsg.getPayload() != null) {
            assertArrayEquals(origMsg.getPayload().getBody(), foundMsg.getPayload().getBody());
            assertTrue(areMetricsEqual(origMsg.getPayload().getMetrics(), foundMsg.getPayload().getMetrics()));
        }
        assertTrue(arePositionsEqual(origMsg.getPosition(), foundMsg.getPosition()));
        assertTrue(foundMsg.getTimestamp().compareTo(origMsg.getReceivedOn()) >= 0);
        assertTrue(foundMsg.getTimestamp().compareTo(new Date(origMsg.getReceivedOn().getTime() + 10000)) <= 0);
        assertEquals(origMsg.getCapturedOn(), foundMsg.getCapturedOn());
        assertEquals(origMsg.getSentOn(), foundMsg.getSentOn());
        assertEquals(origMsg.getReceivedOn(), foundMsg.getReceivedOn());

        return true;
    }

    // Check that two datastore messages match
    private boolean checkThatDatastoreMessagesMatch(DatastoreMessage firstMsg, DatastoreMessage secondMsg) throws KapuaException {

        assertEquals(firstMsg.getDatastoreId().toString(), secondMsg.getDatastoreId().toString());
        assertTrue(areSemanticPartsEqual(firstMsg.getChannel().getSemanticParts(), secondMsg.getChannel().getSemanticParts()));
        if ((firstMsg.getPayload() != null) || (secondMsg.getPayload() != null)) {
            assertArrayEquals(firstMsg.getPayload().getBody(), secondMsg.getPayload().getBody());
            assertTrue(areMetricsEqual(firstMsg.getPayload().getMetrics(), secondMsg.getPayload().getMetrics()));
        }
        assertTrue(arePositionsEqual(firstMsg.getPosition(), secondMsg.getPosition()));
        assertEquals(firstMsg.getTimestamp(), secondMsg.getTimestamp());
        assertEquals(firstMsg.getCapturedOn(), secondMsg.getCapturedOn());
        assertEquals(firstMsg.getSentOn(), secondMsg.getSentOn());
        assertEquals(firstMsg.getReceivedOn(), secondMsg.getReceivedOn());

        return true;
    }

    // Check whether the supplied channel info list contains all the client ids and topic names that were
    // defined by the messages
    private void checkChannelInfoAgainstPreparedMessages(ChannelInfoListResult chnInfo, List<KapuaDataMessage> msgLst) {

        Set<String> msgTopics = new HashSet<>();
        Set<String> msgClients = new HashSet<>();
        Set<String> infoTopics = new HashSet<>();
        Set<String> infoClients = new HashSet<>();

        assertNotNull("No channel info data!", chnInfo);
        assertNotNull("No messages to compare to!", msgLst);

        for (KapuaDataMessage tmpMsg : msgLst) {
            msgClients.add(tmpMsg.getClientId());
            msgTopics.add(tmpMsg.getChannel().toString());
        }
        for (ChannelInfo tmpInfo : chnInfo.getItems()) {
            infoClients.add(tmpInfo.getClientId());
            infoTopics.add(tmpInfo.getName());
        }

        assertEquals("The number of clients does not match!", msgClients.size(), infoClients.size());
        assertEquals("The number of topics does not match!", msgTopics.size(), infoTopics.size());

        for (String tmpTopic : msgTopics) {
            assertTrue(String.format("The topic [%s] is not found in the info list!", tmpTopic), infoTopics.contains(tmpTopic));
        }

        for (String tmpClient : msgClients) {
            assertTrue(String.format("The client id [%s] is not found in the info list!", tmpClient), infoClients.contains(tmpClient));
        }
    }

    // Check whether the supplied metric info list contains all the client ids and metric names that were
    // defined by the messages
    private void checkMetricInfoAgainstPreparedMessages(MetricInfoListResult metInfo, List<KapuaDataMessage> msgLst) {

        Set<String> msgMetrics = new HashSet<>();
        Set<String> msgClients = new HashSet<>();
        Set<String> infoMetrics = new HashSet<>();
        Set<String> infoClients = new HashSet<>();

        assertNotNull("No channel info data!", metInfo);
        assertNotNull("No messages to compare to!", msgLst);

        for (KapuaDataMessage tmpMsg : msgLst) {
            msgClients.add(tmpMsg.getClientId());
            for (String tmpMet : tmpMsg.getPayload().getMetrics().keySet()) {
                msgMetrics.add(tmpMet);
            }
        }
        for (MetricInfo tmpMet : metInfo.getItems()) {
            infoClients.add(tmpMet.getClientId());
            infoMetrics.add(tmpMet.getName());
        }

        assertEquals("The number of clients does not match!", msgClients.size(), infoClients.size());
        assertEquals("The number of topics does not match!", msgMetrics.size(), infoMetrics.size());

        for (String tmpMetric : msgMetrics) {
            assertTrue(String.format("The topic [%s] is not found in the info list!", tmpMetric), infoMetrics.contains(tmpMetric));
        }

        for (String tmpClient : msgClients) {
            assertTrue(String.format("The client id [%s] is not found in the info list!", tmpClient), infoClients.contains(tmpClient));
        }
    }

    // Check whether the supplied metric info list contains all the client ids and metric names that were
    // defined by the messages
    private void checkClientInfoAgainstPreparedMessages(ClientInfoListResult cliInfo, List<KapuaDataMessage> msgLst) {

        Set<String> msgClients = new HashSet<>();
        Set<String> infoClients = new HashSet<>();

        assertNotNull("No client info data!", cliInfo);
        assertNotNull("No messages to compare to!", msgLst);

        for (KapuaDataMessage tmpMsg : msgLst) {
            msgClients.add(tmpMsg.getClientId());
        }
        for (ClientInfo tmpClient : cliInfo.getItems()) {
            infoClients.add(tmpClient.getClientId());
        }

        assertEquals("The number of clients does not match!", msgClients.size(), infoClients.size());

        for (String tmpClient : msgClients) {
            assertTrue(String.format("The client id [%s] is not found in the info list!", tmpClient), infoClients.contains(tmpClient));
        }
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

    private List<OrderConstraint<?>> getDefaultListOrdering() {

        List<OrderConstraint<?>> sort = new ArrayList<>();

        sort.add(orderConstraint(descending(MessageSchema.MESSAGE_SENT_ON), Date.class));
        sort.add(orderConstraint(ascending(MessageSchema.MESSAGE_TIMESTAMP), Date.class));
        sort.add(orderConstraint(descending(MessageSchema.MESSAGE_CLIENT_ID), String.class));

        List<SortField> order = new ArrayList<>();
        order.add(descending(MessageSchema.MESSAGE_TIMESTAMP));

        return sort;
    }

    private List<OrderConstraint<?>> getNamedMetricOrdering() {

        List<OrderConstraint<?>> sort = new ArrayList<>();

        sort.add(orderConstraint(ascending(MetricInfoSchema.METRIC_MTR_NAME_FULL), String.class));

        return sort;
    }

    private static class OrderConstraint<T extends Comparable<T>> {

        private final SortField field;
        private final Function<Object, T> valueFunction;

        public OrderConstraint(final SortField field, final Function<Object, T> valueFunction) {
            this.field = field;
            this.valueFunction = valueFunction;
        }

        public SortField getField() {
            return field;
        }

        public boolean validate(final Object previousItem, final Object currentItem) {
            final T v1 = valueFunction.apply(previousItem);
            final T v2 = valueFunction.apply(currentItem);

            if (!v2.equals(v1)) {
                checkNextValueCoherence(field, v2, v1);
                return true;
            }
            return false;
        }
    }

    private static <T extends Comparable<T>> OrderConstraint<T> orderConstraint(SortField field, Class<T> clazz) {

        return new OrderConstraint<>(field, fieldFunction(field, clazz));
    }

    private static <T extends Comparable<T>> Function<Object, T> fieldFunction(SortField field, Class<T> clazz) {

        return item -> getValue(item, field.getField(), clazz);
    }

    /**
     * Check if the message result list is correctly ordered by the provided criteria (list of fields and ordering)
     *
     * @param result
     * @param sortFieldList
     */
    private static void checkListOrder(StorableListResult<?> result, List<OrderConstraint<?>> sortFieldList) {

        Object previousItem = null;
        for (Object item : result.getItems()) {
            for (OrderConstraint<?> field : sortFieldList) {
                if (previousItem != null) {
                    if (field.validate(previousItem, item)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            previousItem = item;
        }
    }

    /**
     * Check if the next value (it must be not equals, so the equals condition must be checked before calling this method) is coherent with its ordering criteria
     *
     * @param field
     * @param currentValue
     * @param previousValue
     */
    private static <T extends Comparable<T>> void checkNextValueCoherence(final SortField field, final T currentValue, final T previousValue) {

        if (SortDirection.ASC.equals(field.getSortDirection())) {
            assertTrue(String.format("The field [%s] is not correctly ordered as [%s] (%s -> %s)!", field.getField(), field.getSortDirection(), currentValue, previousValue),
                    currentValue.compareTo(previousValue) > 0);
        } else {
            assertTrue(String.format("The field [%s] is not correctly ordered as [%s] (%s -> %s)!", field.getField(), field.getSortDirection(), currentValue, previousValue),
                    currentValue.compareTo(previousValue) < 0);
        }
    }

    /**
     * Return the value of the field name provided (assuming that this value is a Comparable)
     *
     * @param field
     * @return
     */
    private static <T> T getValue(Object object, String field, Class<T> clazz) {

        try {
            Class<?> objetcClass = object.getClass();
            String getterFieldName = getFieldName(field, true);
            Method getMethod = getMethod(objetcClass, getterFieldName, "get");
            if (getMethod != null) {
                return clazz.cast(getMethod.invoke(object, new Object[0]));
            }
            getMethod = getMethod(objetcClass, getterFieldName, "is");
            if (getMethod != null) {
                return clazz.cast(getMethod.invoke(object, new Object[0]));
            }
            // else try by field access
            String fieldName = getFieldName(field, false);
            Field objField = getField(objetcClass, fieldName);
            if (objField != null) {
                return clazz.cast(objField.get(object));
            } else {
                throw new IllegalArgumentException(String.format("Cannot find getter for field [%s] or field [%s] or the field value is not a Comparable value!", field, field));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Cannot find getter for field [%s] or field [%s] or the field value is not a Comparable value!", field, field));
        }
    }

    /**
     * Return the suffix field name to compose (in a different method) the getter name.
     * It removes the _ and append the remaining part capitalizing the first letter (if capitalizeFirstLetter = true)
     *
     * @param field
     * @return
     */
    private static String getFieldName(String field, boolean capitalizeFirstLetter) {

        String str[] = cleanupFieldName(field);
        String fieldName = null;
        if (capitalizeFirstLetter) {
            fieldName = str[0].substring(0, 1).toUpperCase() + str[0].substring(1);
        } else {
            fieldName = str[0];
        }
        for (int i = 1; i < str.length; i++) {
            fieldName += str[i].substring(0, 1).toUpperCase() + str[i].substring(1);
        }
        return fieldName;
    }

    private static String[] cleanupFieldName(String field) {

        int lastDot = field.lastIndexOf('.');
        if (lastDot > -1) {
            field = field.substring(lastDot + 1, field.length());
        }
        String str[] = field.split("_");
        if (str == null || str.length <= 0) {
            throw new IllegalArgumentException(String.format("Invalid field name [%s]", field));
        }
        return str;
    }

    /**
     * Return the method combining the prefix and the field name provided
     *
     * @param objetcClass
     * @param field
     * @param prefix
     * @return
     */
    private static Method getMethod(Class<?> objetcClass, String field, String prefix) {

        String fieldName = prefix + field.substring(0, 1).toUpperCase() + field.substring(1);

        Method objMethod = null;
        do {
            try {
                objMethod = objetcClass.getMethod(fieldName, new Class[0]);
            } catch (NoSuchMethodException e) {
                objetcClass = objetcClass.getSuperclass();
            }
        } while (objMethod == null && objetcClass != null);

        return objMethod;
    }

    /**
     * Return the field combining the prefix and the field name provided
     *
     * @param objectClass
     * @param field
     * @return
     */
    private static Field getField(Class<?> objectClass, final String field) {

        Field objField = null;
        do {
            try {
                objField = objectClass.getDeclaredField(field);
                objField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                objectClass = objectClass.getSuperclass();
            }
        } while (objField == null && objectClass != null);

        return objField;
    }

    /**
     * Update the store service configuration with the provided values
     *
     * @param messageStoreService
     * @param scopeId
     * @param dataIndexBy
     * @param metricsIndexBy
     * @param dataTTL
     * @param storageEnabled
     * @throws KapuaException
     */
    private void updateConfiguration(MessageStoreService messageStoreService, KapuaId scopeId, KapuaId parentId, DataIndexBy dataIndexBy, MetricsIndexBy metricsIndexBy, int dataTTL,
            boolean storageEnabled)
            throws KapuaException {

        Map<String, Object> config = messageStoreService.getConfigValues(scopeId);
        if (config == null) {
            config = new HashMap<>();
        }
        if (dataIndexBy != null) {
            config.put(MessageStoreConfiguration.CONFIGURATION_DATA_INDEX_BY_KEY, dataIndexBy.name());
        }
        if (metricsIndexBy != null) {
            config.put(MessageStoreConfiguration.CONFIGURATION_METRICS_INDEX_BY_KEY, metricsIndexBy.name());
        }
        config.put(MessageStoreConfiguration.CONFIGURATION_DATA_TTL_KEY, dataTTL);
        config.put(MessageStoreConfiguration.CONFIGURATION_DATA_STORAGE_ENABLED_KEY, storageEnabled);
        messageStoreService.setConfigValues(scopeId, parentId, config);
    }
}
