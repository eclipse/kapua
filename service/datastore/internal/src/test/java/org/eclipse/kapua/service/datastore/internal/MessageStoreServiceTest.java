/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaChannel;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.KapuaPositionImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageField;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MetricsIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.SortFieldImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
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
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageStoreServiceTest extends AbstractMessageStoreServiceTest {

    private static final Logger s_logger = LoggerFactory.getLogger(MessageStoreServiceTest.class);
    private static final long QUERY_TIME_WINDOW = 2000l;
    private static final long PUBLISH_DATE_TEST_CHECK_TIME_WINDOW = 1000l;
    private static final long INDEX_TIME_ESTIMATE_SECONDS = 2; // Trail and error value depends on system where tests are run

    private static final DeviceRegistryService deviceRegistryService = KapuaLocator.getInstance().getService(DeviceRegistryService.class);
    private static final DeviceFactory deviceFactory = KapuaLocator.getInstance().getFactory(DeviceFactory.class);
    private static final MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);
    private static final DatastoreObjectFactory datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);
    private static final ChannelInfoRegistryService channelInfoRegistryService = KapuaLocator.getInstance().getService(ChannelInfoRegistryService.class);
    private static final MetricInfoRegistryService metricInfoRegistryService = KapuaLocator.getInstance().getService(MetricInfoRegistryService.class);
    private static final ClientInfoRegistryService clientInfoRegistryService = KapuaLocator.getInstance().getService(ClientInfoRegistryService.class);

    private Duration elasticsearchRefreshTime = Duration.ofSeconds((DatastoreSettings.getInstance().getLong(DatastoreSettingKey.ELASTICSEARCH_IDX_REFRESH_INTERVAL) + INDEX_TIME_ESTIMATE_SECONDS) );

    @Test
    /**
     * Store few messages with few metrics, position and body (partially randomly generated) and check if the stored message (retrieved by id) has all the fields correctly set
     *
     * @throws Exception
     */
    public void testMessageStore()
            throws Exception {
        Account account = getTestAccountCreator(adminScopeId);
        Random random = new Random();

        String[] semanticTopic = new String[] {
                "bus/route/one",
                "bus/route/one",
                "bus/route/two/a",
                "bus/route/two/b",
                "tram/route/one",
                "car/one"
        };

        KapuaDataMessage message = null;
        String clientId = String.format("device-%d", new Date().getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);

        for (int i = 0; i < 12; i++) {
            byte[] randomPayload = new byte[128];
            random.nextBytes(randomPayload);
            String stringPayload = "Hello World" + (i + 1) + " \n\n\t\n\tHelloWord2";
            byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

            KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();

            Map<String, Object> metrics = new HashMap<String, Object>();
            metrics.put("float_int", new Float((float) (i + 1)));
            metrics.put("float_float", new Float((float) (i + 1) * 0.01));
            metrics.put("integer_value", new Integer((i + 1)));
            metrics.put("double_int", (double) (i + 1));
            metrics.put("double_float", (double) (i + 1) * 0.01);
            metrics.put("long_long", (long) (10000000000000l * (i + 1)));
            metrics.put("long_int_1", (long) (1000 * (i + 1)));
            metrics.put("long_int_2", (long) (i + 1));
            metrics.put("string_value", Integer.toString((i + 1) * 1000));
            messagePayload.setProperties(metrics);

            messagePayload.setBody(payload);
            Date receivedOn = new Date();
            Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
            Date capturedOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2016").getTime());
            message = getMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            updateChannel(message, semanticTopic[i % semanticTopic.length]);
            updatePayload(message, messagePayload);
            KapuaPosition messagePosition = getPosition(10.00d * (i + 1), 12d * (i + 1), 1.123d * (i + 1), 2d * (i + 1), 0001d * (i + 1), 1000 * (i + 1), 1d * (i + 1), 44 * (i + 1), new Date());
            message.setPosition(messagePosition);
            List<StorableId> messageStoredIds = null;
            try {
                messageStoredIds = insertMessages(true, message);

                DatastoreMessage messageQueried = messageStoreService.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
                checkMessageId(messageQueried, messageStoredIds.get(0));
                checkTopic(messageQueried, semanticTopic[i % semanticTopic.length]);
                checkMessageBody(messageQueried, message.getPayload().getBody());
                checkMetricsSize(messageQueried, metrics.size());
                checkMetrics(messageQueried, metrics);
                checkPosition(messageQueried, messagePosition);
                checkMessageDate(messageQueried, new Range<Date>("timestamp", capturedOn), new Range<Date>("sentOn", sentOn), new Range<Date>("capturedOn", capturedOn),
                        new Range<Date>("receivedOn", receivedOn));
            } catch (KapuaException e) {
                s_logger.error("Exception: ", e.getMessage(), e);
            }
        }
    }

    /**
     * Test the correctness of the query filtering order (3 fields: date descending, date ascending, string descending)
     *
     * @throws Exception
     */
    @Test
    public void testMessageOrderingMixedTest()
            throws Exception {
        Account account = getTestAccountCreator(adminScopeId);

        String[] semanticTopic = new String[] {
                "bus/route/one",
                "bus/route/one",
                "bus/route/two/a",
                "bus/route/two/b",
                "tram/route/one",
                "car/one"
        };

        KapuaDataMessage message = null;
        String clientId1 = String.format("device-%d", new Date().getTime());
        Thread.sleep(100);
        String clientId2 = String.format("device-%d", new Date().getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId1);
        Device device1 = deviceRegistryService.create(deviceCreator);
        DeviceCreator deviceCreator2 = deviceFactory.newCreator(account.getId(), clientId2);
        Device device2 = deviceRegistryService.create(deviceCreator2);
        int messagesCount = 100;
        Date sentOn1 = new Date();
        Date sentOn2 = new Date(sentOn1.getTime() + 5000);
        Date capturedOn1 = new Date(new Date().getTime() + 1000);
        Date capturedOn2 = new Date(capturedOn1.getTime() + 1000);
        String clientId = null;
        Device device = null;

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);

        for (int i = 0; i < messagesCount; i++) {
            clientId = clientId1;
            device = device1;
            Date receivedOn = new Date();
            Date sentOn = null;
            if (i < messagesCount / 2) {
                sentOn = sentOn1;
            } else {
                sentOn = sentOn2;
            }
            Date capturedOn = null;
            if (i < messagesCount / 4 - 1 || (i > messagesCount / 2 - 1 && i < messagesCount * 3 / 4 - 1)) {
                capturedOn = capturedOn1;
                if (i % 2 == 0) {
                    clientId = clientId2;
                    device = device2;
                }
            } else {
                capturedOn = capturedOn2;
                if (i % 2 == 0) {
                    clientId = clientId2;
                    device = device2;
                }
            }
            message = getMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            updateChannel(message, semanticTopic[i % semanticTopic.length]);
            insertMessages(false, message);
        }
        waitEsRefresh();
        List<SortField> sort = new ArrayList<SortField>();
        SortField sortSentOn = new SortFieldImpl();
        sortSentOn.setField(EsSchema.MESSAGE_SENT_ON);
        sortSentOn.setSortDirection(SortDirection.DESC);
        sort.add(sortSentOn);
        SortField sortTimestamp = new SortFieldImpl();
        sortTimestamp.setField(EsSchema.MESSAGE_TIMESTAMP);
        sortTimestamp.setSortDirection(SortDirection.ASC);
        sort.add(sortTimestamp);
        SortField sortClientId = new SortFieldImpl();
        sortClientId.setField(EsSchema.MESSAGE_CLIENT_ID);
        sortClientId.setSortDirection(SortDirection.DESC);
        sort.add(sortClientId);
        MessageQuery messageQuery = getMessageOrderedQuery(account.getId(), messagesCount + 1, sort);
        setMessageQueryBaseCriteria(messageQuery, new DateRange(capturedOn1, capturedOn2));

        MessageListResult messageList = messageStoreService.query(messageQuery);
        checkMessagesCount(messageList, messagesCount);
        checkMessagesDateBound(messageList, new Date(capturedOn1.getTime()), new Date(capturedOn2.getTime()));
        for (DatastoreMessage messageStored : messageList.getItems()) {
            s_logger.debug("message sentOn: '" + messageStored.getSentOn() + "' - capturedOn: '" + messageStored.getCapturedOn() + "' clientId: '" + messageStored.getClientId() + "'");
        }
        checkListOrder(messageList, sort);
    }

    @Test
    /**
     * Test the correctness of the storage process with a basic message (no metrics, payload and position) indexing message date by device timestamp (as default)
     *
     * @throws Exception
     */
    public void testMessageStoreWithDeviceTimestampIndexingAndNullPayload()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String topicSemanticPart = "testStoreWithNullPayload/testStoreWithNullPayload/" + Calendar.getInstance().getTimeInMillis();
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message = getMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message, topicSemanticPart);
        updatePayload(message, null);
        message.setReceivedOn(messageTime);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        List<StorableId> messageStoredIds = insertMessages(true, message);

        MessageQuery messageQuery = getBaseMessageQuery(account.getId());
        setMessageQueryBaseCriteria(messageQuery, new DateRange(capturedOn));

        MessageListResult result = messageStoreService.query(messageQuery);
        DatastoreMessage messageQueried = checkMessagesCount(result, 1);
        checkMessageId(messageQueried, messageStoredIds.get(0));
        checkMessageBody(messageQueried, null);
        checkMetricsSize(messageQueried, 0);
        checkPosition(messageQueried, null);
        checkMessageDate(messageQueried, new Range<Date>("timestamp", capturedOn), new Range<Date>("sentOn", sentOn), new Range<Date>("capturedOn", capturedOn),
                new Range<Date>("receivedOn", messageTime));
    }

    @Test
    /**
     * Test the correctness of the storage process with a basic message (no metrics, payload and position) indexing message date by server timestamp
     *
     * @throws Exception
     */
    public void testMessageStoreWithServerTimestampIndexingAndNullPayload()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String topicSemanticPart = "testStoreWithNullPayload/testStoreWithNullPayload/" + Calendar.getInstance().getTimeInMillis();
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message = getMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message, topicSemanticPart);
        updatePayload(message, null);
        message.setReceivedOn(messageTime);

        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.SERVER_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        messageTime = new Date();
        message.setReceivedOn(messageTime);
        List<StorableId> messageStoredIds = insertMessages(true, message);

        Date timestampLowerBound = new Date(messageTime.getTime() - QUERY_TIME_WINDOW);
        Date timestampUpperBound = new Date(messageTime.getTime() + QUERY_TIME_WINDOW);
        DateRange dateRange = new DateRange(timestampLowerBound, timestampUpperBound);
        MessageQuery messageQuery = getBaseMessageQuery(account.getId());
        setMessageQueryBaseCriteria(messageQuery, dateRange);

        MessageListResult result = messageStoreService.query(messageQuery);
        DatastoreMessage messageQueried = checkMessagesCount(result, 1);
        checkMessageId(messageQueried, messageStoredIds.get(0));
        checkTopic(messageQueried, topicSemanticPart);
        checkMessageBody(messageQueried, null);
        checkMetricsSize(messageQueried, 0);
        checkPosition(messageQueried, null);
        checkMessageDate(messageQueried, new Range<Date>("timestamp", dateRange.getLowerBound(), dateRange.getUpperBound()), new Range<Date>("sentOn", sentOn),
                new Range<Date>("capturedOn", capturedOn),
                new Range<Date>("receivedOn", messageTime));
    }

    @Test
    /**
     * Check the correctness of the client ids info stored in the channel info data by retrieving the channel info by account.
     *
     * @throws Exception
     */
    public void testChannelInfoFindClientIdByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "ci_client_by_account_client1", "ci_client_by_account_client2", "ci_client_by_account_client3", "ci_client_by_account_client4" };
        String[] semanticTopic = new String[] { "ci_client_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[2], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[0]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message4, semanticTopic[0]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = getMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message5, semanticTopic[0]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = getMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message6, semanticTopic[0]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4, message5, message6);

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, new DateRange(messageTime));

        ChannelInfoListResult channelList = channelInfoRegistryService.query(channelInfoQuery);
        checkChannelInfoClientIdsAndTopics(channelList, 4, clientIds, semanticTopic);
    }

    @Test
    /**
     * Check the correctness of the channel info last publish date stored by retrieving the channel info by client id.
     *
     * This test is failing because Elastichsearch caching code should be improved.
     *
     * @throws Exception
     */
    public void testChannelInfoFindClientIdByPublishDateByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "ci_client_by_pd_by_account_client1", "ci_client_by_pd_by_account_client2" };
        String[] semanticTopic = new String[] { "ci_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        updateChannel(message3, semanticTopic[0]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        updateChannel(message4, semanticTopic[0]);
        message4.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4);

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, new DateRange(messageTime, capturedOnThirdMessage));

        ChannelInfoListResult channelList = channelInfoRegistryService.query(channelInfoQuery);
        checkChannelInfoClientIdsAndTopics(channelList, 2, clientIds, semanticTopic);

        // check the channel info date
        for (ChannelInfo channelInfo : channelList.getItems()) {
            if (clientIds[0].equals(channelInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[0]), capturedOn, channelInfo.getLastMessageOn());
            } else if (clientIds[1].equals(channelInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[1]), capturedOnThirdMessage, channelInfo.getLastMessageOn());
            }
            assertEquals(String.format("Wrong first publish date for the client id [%s]", channelInfo.getClientId()), capturedOn, channelInfo.getFirstMessageOn());
        }
    }

    @Test
    /**
     * Check the correctness of the topic info stored in the channel info data by retrieving the channel info by account.
     *
     * @throws Exception
     */
    public void testChannelInfoFindTopicByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "ci_topic_by_account_client1", "ci_topic_by_account_client2" };
        String[] semanticTopic = new String[] { "ci_topic_by_account/1/2/3", "ci_topic_by_account/1/2/4", "ci_topic_by_account/1/2/5", "ci_topic_by_account/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[1]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[2]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message4, semanticTopic[3]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message5, semanticTopic[0]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message6, semanticTopic[1]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4, message5, message6);

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, new DateRange(messageTime));

        ChannelInfoListResult channelList = channelInfoRegistryService.query(channelInfoQuery);
        checkChannelInfoClientIdsAndTopics(channelList, 6, clientIds, semanticTopic);
    }

    @Test
    /**
     * Check the correctness of the topic info stored in the channel info data by retrieving the channel info by client id.
     *
     * @throws Exception
     */
    public void testChannelInfoFindTopicByClientId()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "ci_topic_by_client_client1" };
        String[] semanticTopic = new String[] { "ci_topic_by_client/1/2/3", "ci_topic_by_client/1/2/4", "ci_topic_by_client/1/2/5", "ci_topic_by_client/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[1]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[2]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message4, semanticTopic[3]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = getMessage(clientIds[0] + "_NO", account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message5, semanticTopic[2]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = getMessage(clientIds[0] + "_NO", account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message6, semanticTopic[3]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4, message5, message6);

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, clientIds[0], new DateRange(messageTime));

        ChannelInfoListResult channelList = channelInfoRegistryService.query(channelInfoQuery);
        checkChannelInfoClientIdsAndTopics(channelList, 4, clientIds, semanticTopic);
    }

    @Test
    /**
     * Check the correctness of the metric info data stored by retrieving the metrics information by account.
     *
     * @throws Exception
     */
    public void testMetricsInfoFindClientByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_account_client1", "mi_client_by_account_client2" };
        String[] metrics = new String[] { "mi_client_by_account_metric1", "mi_client_by_account_metric2", "mi_client_by_account_metric3", "mi_client_by_account_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getProperties().put(metrics[0], new Double(123));
        message1.getPayload().getProperties().put(metrics[1], new Integer(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getProperties().put(metrics[2], new String("123"));
        message2.getPayload().getProperties().put(metrics[3], new Boolean(true));
        message2.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2);

        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, new DateRange(capturedOn));

        MetricInfoListResult metricList = metricInfoRegistryService.query(metricInfoQuery);
        checkMetricInfoClientIdsAndMetricNames(metricList, 4, clientIds, metrics);
    }

    @Test
    /**
     * Check the correctness of the metric info last publish date stored by retrieving the metric info by account.
     *
     * This test is failing because Elastichsearch caching code should be improved.
     *
     * @throws Exception
     */
    public void testMetricsInfoFindClientByPublishDateByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_pd_by_account_client1", "mi_client_by_pd_by_account_client2" };
        String[] metrics = new String[] { "mi_client_by_pd_by_account_metric1", "mi_client_by_pd_by_account_metric2", "mi_client_by_pd_by_account_metric3", "mi_client_by_pd_by_account_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getProperties().put(metrics[0], new Double(123));
        message1.getPayload().getProperties().put(metrics[1], new Integer(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getProperties().put(metrics[2], new String("123"));
        message2.getPayload().getProperties().put(metrics[3], new Boolean(true));
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        updateChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.getPayload().getProperties().put(metrics[2], new String("123"));
        message3.getPayload().getProperties().put(metrics[3], new Boolean(true));
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        updateChannel(message4, semanticTopic[0]);
        initMetrics(message4);
        message4.getPayload().getProperties().put(metrics[2], new String("123"));
        message4.getPayload().getProperties().put(metrics[3], new Boolean(true));
        message4.setReceivedOn(messageTime);

        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        // Store messages
        insertMessages(true, message1, message2, message3, message4);

        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, new DateRange(capturedOn, capturedOnThirdMessage));

        MetricInfoListResult metricList = metricInfoRegistryService.query(metricInfoQuery);
        checkMetricInfoClientIdsAndMetricNames(metricList, 4, clientIds, metrics);
        // check the metric info date
        for (MetricInfo metricInfo : metricList.getItems()) {
            if (clientIds[0].equals(metricInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[0]), capturedOn, metricInfo.getLastMessageOn());
            } else if (clientIds[1].equals(metricInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[1]), capturedOnThirdMessage, metricInfo.getLastMessageOn());
            }
            if (metrics[0].equals(metricInfo.getName())) {
                assertEquals(String.format("Wrong last publish date for the metric [%s]", metrics[0]), capturedOn, metricInfo.getLastMessageOn());
            } else if (metrics[1].equals(metricInfo.getName())) {
                assertEquals(String.format("Wrong last publish date for the metric [%s]", metrics[1]), capturedOn, metricInfo.getLastMessageOn());
            } else if (metrics[2].equals(metricInfo.getName())) {
                assertEquals(String.format("Wrong last publish date for the metric [%s]", metrics[2]), capturedOnThirdMessage, metricInfo.getLastMessageOn());
            } else if (metrics[3].equals(metricInfo.getName())) {
                assertEquals(String.format("Wrong last publish date for the metric [%s]", metrics[3]), capturedOnThirdMessage, metricInfo.getLastMessageOn());
            }
            assertEquals(String.format("Wrong first publish date for the client id [%s]", metricInfo.getClientId()), capturedOn, metricInfo.getFirstMessageOn());
        }
    }

    @Test
    /**
     * Check the correctness of the metric info data stored by retrieving the metrics information by client id.
     *
     * @throws Exception
     */
    public void testMetricsInfoByClientId()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_client_client1", "mi_client_by_client_client2" };
        String[] metrics = new String[] { "mi_client_by_client_metric1", "mi_client_by_client_metric2", "mi_client_by_client_metric3", "mi_client_by_client_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_client/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getProperties().put(metrics[0], new Double(123));
        message1.getPayload().getProperties().put(metrics[1], new Integer(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getProperties().put(metrics[2], new String("123"));
        message2.getPayload().getProperties().put(metrics[3], new Boolean(true));
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.getPayload().getProperties().put(metrics[2], new Double(123));
        message3.getPayload().getProperties().put(metrics[3], new Integer(123));
        message3.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3);

        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, clientIds[0], new DateRange(capturedOn));

        MetricInfoListResult metricList = metricInfoRegistryService.query(metricInfoQuery);
        checkMetricInfoClientIdsAndMetricNames(metricList, 4, new String[] { clientIds[0] }, metrics);
    }

    /**
     * Test the correctness of the query filtering order (3 fields: date descending, date ascending, string descending) for the metrics
     *
     * @throws Exception
     */
    @Test
    public void testMetricOrderingMixedTest()
            throws Exception {
        Account account = getTestAccountCreator(adminScopeId);

        String[] semanticTopic = new String[] {
                "bus/route/one",
                "bus/route/one",
                "bus/route/two/a",
                "bus/route/two/b",
                "tram/route/one",
                "car/one"
        };
        String[] metrics = new String[] { "m_order_metric1", "m_order_metric2", "m_order_metric3", "m_order_metric4", "m_order_metric5", "m_order_metric6" };
        String[] clientIds = new String[] { String.format("device-%d", new Date().getTime()), String.format("device-%d", new Date().getTime() + 100) };
        String[] metricsValuesString = new String[] { "string_metric_1", "string_metric_2", "string_metric_3", "string_metric_4", "string_metric_5", "string_metric_6" };
        Date[] metricsValuesDate = new Date[] { new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:01 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:02 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:03 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:04 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:05 01/01/2017").getTime()),
                new Date(new SimpleDateFormat("hh:MM:ss dd/MM/yyyy").parse("10:10:06 01/01/2017").getTime()) };
        int[] metricsValuesInt = new int[] { 10, 20, 30, 40, 50, 60 };
        float[] metricsValuesFloat = new float[] { 0.002f, 10.12f, 20.22f, 33.33f, 44.44f, 55.66f };
        double[] metricsValuesDouble = new double[] { 1.002d, 11.12d, 21.22d, 34.33d, 45.44d, 56.66d };
        boolean[] metricsValuesBoolean = new boolean[] { true, true, false, true, false, false };

        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientIds[0]);
        Device device1 = deviceRegistryService.create(deviceCreator);
        DeviceCreator deviceCreator2 = deviceFactory.newCreator(account.getId(), clientIds[1]);
        Device device2 = deviceRegistryService.create(deviceCreator2);
        int messagesCount = 100;
        Date sentOn1 = new Date();
        Date sentOn2 = new Date(sentOn1.getTime() + 5000);
        Date capturedOn1 = new Date(new Date().getTime() + 1000);
        Date capturedOn2 = new Date(capturedOn1.getTime() + 1000);
        String clientId = null;
        Device device = null;

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);

        for (int i = 0; i < messagesCount; i++) {
            clientId = clientIds[0];
            device = device1;
            Date receivedOn = new Date();
            Date sentOn = null;
            if (i < messagesCount / 2) {
                sentOn = sentOn1;
            } else {
                sentOn = sentOn2;
            }
            Date capturedOn = null;
            if (i < messagesCount / 4 - 1 || (i > messagesCount / 2 - 1 && i < messagesCount * 3 / 4 - 1)) {
                capturedOn = capturedOn1;
                if (i % 2 == 0) {
                    clientId = clientIds[1];
                    device = device2;
                }
            } else {
                capturedOn = capturedOn2;
                if (i % 2 == 0) {
                    clientId = clientIds[1];
                    device = device2;
                }
            }
            KapuaDataMessage message = getMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            updateChannel(message, semanticTopic[i % semanticTopic.length]);
            // insert metrics
            initMetrics(message);
            message.getPayload().getProperties().put(metrics[0], metricsValuesDate[i % metricsValuesDate.length]);
            message.getPayload().getProperties().put(metrics[1], metricsValuesString[i % metricsValuesString.length]);
            message.getPayload().getProperties().put(metrics[2], metricsValuesInt[i % metricsValuesInt.length]);
            message.getPayload().getProperties().put(metrics[3], metricsValuesFloat[i % metricsValuesFloat.length]);
            message.getPayload().getProperties().put(metrics[4], metricsValuesBoolean[i % metricsValuesBoolean.length]);
            message.getPayload().getProperties().put(metrics[5], metricsValuesDouble[i % metricsValuesDouble.length]);
            insertMessages(false, message);
        }
        waitEsRefresh();

        List<SortField> sort = new ArrayList<SortField>();
        SortField sortMetricName = new SortFieldImpl();
        sortMetricName.setField(EsSchema.METRIC_MTR_NAME_FULL);
        sortMetricName.setSortDirection(SortDirection.ASC);
        sort.add(sortMetricName);

        MetricInfoQuery metricInfoQuery = getMetricInfoOrderedQuery(account.getId(), (6 + 1) * messagesCount, sort);
        setMetricInfoQueryBaseCriteria(metricInfoQuery, new DateRange(capturedOn1, capturedOn2));

        MetricInfoListResult metricList = metricInfoRegistryService.query(metricInfoQuery);
        checkMetricInfoClientIdsAndMetricNames(metricList, metrics.length * semanticTopic.length, new String[] { clientIds[0], clientIds[1] }, new String[] { metrics[0], metrics[1], metrics[2],
                metrics[3], metrics[4], metrics[5] });
        checkMetricDateBound(metricList, new Date(capturedOn1.getTime()), new Date(capturedOn2.getTime()));

        for (MetricInfo metricInfo : metricList.getItems()) {
            s_logger.debug("metric client id: '" + metricInfo.getClientId() + "' - channel: '" + metricInfo.getChannel() + "' metric name: '" + metricInfo.getName()
                    + "' metric type: '" + metricInfo.getMetricType() + "'");
        }
        checkListOrder(metricList, sort);
    }

    @Test
    /**
     * Check the correctness of the client info data stored by retrieving the client information by account.
     *
     * @throws Exception
     */
    public void testClientInfoFindClientIdByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "clii_client_by_account_client1", "clii_client_by_account_client2" };
        String[] semanticTopic = new String[] { "clii_client_by_account/1/2/3", "clii_client_by_account/1/2/4", "clii_client_by_account/1/2/5", "clii_client_by_account/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[1]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[2]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message4, semanticTopic[3]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4);

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId());
        setClientInfoQueryBaseCriteria(clientInfoQuery, new DateRange(capturedOn));

        ClientInfoListResult clientList = clientInfoRegistryService.query(clientInfoQuery);
        checkClientInfo(clientList, 2, clientIds);
    }

    @Test
    /**
     * Check the correctness of the client info data stored by retrieving the client information by account.
     *
     * @throws Exception
     */
    public void testClientInfoFindClientIdByPublishDateByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "clii_client_by_pd_by_account_client1", "clii_client_by_pd_by_account_client2" };
        String[] semanticTopic = new String[] { "clii_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        updateChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        updateChannel(message4, semanticTopic[0]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4);

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId());
        setClientInfoQueryBaseCriteria(clientInfoQuery, new DateRange(capturedOn, capturedOnThirdMessage));

        ClientInfoListResult clientList = clientInfoRegistryService.query(clientInfoQuery);
        checkClientInfo(clientList, 2, clientIds);
        for (ClientInfo clientInfo : clientList.getItems()) {
            if (clientIds[0].equals(clientInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[0]), capturedOn, clientInfo.getLastMessageOn());
            } else if (clientIds[1].equals(clientInfo.getClientId())) {
                assertEquals(String.format("Wrong last publish date for the client id [%s]", clientIds[1]), capturedOnThirdMessage, clientInfo.getLastMessageOn());
            }
            assertEquals(String.format("Wrong first publish date for the client id [%s]", clientInfo.getClientId()), capturedOn, clientInfo.getFirstMessageOn());
        }
    }

    @Test
    /**
     * Check the correctness of the client info data stored by retrieving the client information by account.
     *
     * @throws Exception
     */
    public void testClientInfoByClientId()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "clii_by_client_client1", "clii_by_client_client2", "clii_by_client_client3", "clii_by_client_client4" };
        String[] semanticTopic = new String[] { "clii_by_client/1/2/3", "clii_by_client/1/2/4" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = getMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message2, semanticTopic[1]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = getMessage(clientIds[2], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = getMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        updateChannel(message4, semanticTopic[1]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(true, message1, message2, message3, message4);

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId());
        setClientInfoQueryBaseCriteria(clientInfoQuery, clientIds[0], new DateRange(capturedOn));

        ClientInfoListResult clientList = clientInfoRegistryService.query(clientInfoQuery);
        checkClientInfo(clientList, 1, new String[] { clientIds[0] });
    }

    @Test
    public void testTopicsByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        String[] clientIds = new String[] { "tba_client1" };
        String[] semanticTopic = new String[] { "tba_1/1/1/1", "tba_1/1/1/2", "tba_1/1/1/3", "tba_1/1/2/1", "tba_1/1/2/2", "tba_1/1/2/3",
                "tba_1/2/1/1", "tba_1/2/1/2", "tba_1/2/1/3", "tba_1/2/2/1", "tba_1/2/2/2", "tba_1/2/2/3",
                "tba_2/1/1/1", "tba_2/1/1/2", "tba_2/1/1/3", "tba_2/1/2/1", "tba_2/1/2/2", "tba_2/1/2/3",
                "tba_2/2/1/1", "tba_2/2/1/2", "tba_2/2/1/3", "tba_2/2/2/1", "tba_2/2/2/2", "tba_2/2/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        updateConfiguration(messageStoreService, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        for (String semanticTopicTmp : semanticTopic) {
            KapuaDataMessage message1 = getMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            updateChannel(message1, semanticTopicTmp);
            message1.setReceivedOn(messageTime);
            insertMessages(false, message1);
        }
        waitEsRefresh();

        ChannelInfoListResult channelList = doChannelInfoQuery(account, clientIds[0], "1/#", messageTime);
        checkChannelInfoClientIdsAndTopics(channelList, 0, null, null);

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_1/#", messageTime);
        checkChannelInfoClientIdsAndTopics(channelList, 12, clientIds, Arrays.copyOfRange(semanticTopic, 0, 12));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_2/#", messageTime);
        checkChannelInfoClientIdsAndTopics(channelList, 12, clientIds, Arrays.copyOfRange(semanticTopic, 12, 24));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_1/1/#", messageTime);
        checkChannelInfoClientIdsAndTopics(channelList, 6, clientIds, Arrays.copyOfRange(semanticTopic, 0, 6));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_2/1/1/#", messageTime);
        checkChannelInfoClientIdsAndTopics(channelList, 3, clientIds, Arrays.copyOfRange(semanticTopic, 12, 15));

    }

    // ===========================================================
    // ===========================================================
    // utility methods
    // ===========================================================
    // ===========================================================

    private List<StorableId> insertMessages(boolean waitForElasticsearchRefreshTime, KapuaDataMessage... messages) throws InterruptedException {
        List<StorableId> storableIds = new ArrayList<StorableId>();
        for (KapuaDataMessage message : messages) {
            try {
                storableIds.add(messageStoreService.store(message));
            } catch (Exception e) {
                s_logger.error("Message insert exception!", e);
                fail("Store messages should have succeded");
            }
        }
        if (waitForElasticsearchRefreshTime) {
            waitEsRefresh();
        }
        return storableIds;
    }

    private void waitEsRefresh() throws InterruptedException {
        // Wait ES indexes to be refreshed
        Thread.sleep(elasticsearchRefreshTime.toMillis());
    }

    /**
     * This method should create a new account for the test (temp implementation that return always the default kapua-sys account)
     *
     * @param accountName
     * @param password
     * @return
     * @throws KapuaException
     */
    private Account createAccount(KapuaId scopeId, String password) throws KapuaException {
        return getTestAccountCreator(adminScopeId);
    }

    /**
     * Creates a new KapuaMessage setting the provided parameters
     *
     * @param clientId
     * @param scopeId
     * @param deviceId
     * @param receiedOn
     * @param capturedOn
     * @param sentOn
     * @return
     */
    private KapuaDataMessage getMessage(String clientId, KapuaId scopeId, KapuaId deviceId,
            Date receivedOn, Date capturedOn, Date sentOn) {
        KapuaDataMessage message = new KapuaDataMessageImpl();
        message.setReceivedOn(receivedOn);
        message.setCapturedOn(capturedOn);
        message.setSentOn(sentOn);
        message.setChannel(new KapuaDataChannelImpl());
        message.setClientId(clientId);
        message.setDeviceId(deviceId);
        message.setScopeId(scopeId);
        return message;
    }

    /**
     * Update the KapuaMessage channel with the provided semantic part
     *
     * @param message
     * @param semanticPart
     */
    private void updateChannel(KapuaDataMessage message, String semanticPart) {
        message.setChannel(new KapuaDataChannelImpl());
        message.getChannel().setSemanticParts(new ArrayList<String>(Arrays.asList(semanticPart.split("/"))));
    }

    /**
     * Update the KapuaMessage payload with the provided payload
     *
     * @param message
     * @param messagePayload
     */
    private void updatePayload(KapuaDataMessage message, KapuaDataPayload messagePayload) {
        message.setPayload(messagePayload);
    }

    private void initMetrics(KapuaDataMessage message) {
        if (message.getPayload() == null) {
            message.setPayload(new KapuaDataPayloadImpl());
        }
    }

    /**
     * Return a new KapuaPosition instance and set all the provided position informations
     *
     * @param altitude
     * @param heading
     * @param latitude
     * @param longitude
     * @param precision
     * @param satellites
     * @param speed
     * @param status
     * @param timestamp
     * @return
     */
    private KapuaPosition getPosition(Double altitude, Double heading, Double latitude, Double longitude, Double precision, Integer satellites, Double speed, Integer status, Date timestamp) {
        KapuaPosition messagePosition = new KapuaPositionImpl();
        messagePosition.setAltitude(altitude);
        messagePosition.setHeading(heading);
        messagePosition.setLatitude(latitude);
        messagePosition.setLongitude(longitude);
        messagePosition.setPrecision(precision);
        messagePosition.setSatellites(satellites);
        messagePosition.setSpeed(speed);
        messagePosition.setStatus(status);
        messagePosition.setTimestamp(timestamp);
        return messagePosition;
    }

    //
    // Utility methods to help to to create message queries
    //

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Message schema
     *
     * @return
     */
    private MessageQuery getBaseMessageQuery(KapuaId scopeId) {
        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(10);
        query.setOffset(0);
        List<SortField> order = new ArrayList<SortField>();
        SortField sf = new SortFieldImpl();
        sf.setField(EsSchema.MESSAGE_TIMESTAMP);
        sf.setSortDirection(SortDirection.DESC);
        order.add(sf);
        query.setSortFields(order);
        return query;
    }

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Channel Info schema
     *
     * @return
     */
    private ChannelInfoQuery getBaseChannelInfoQuery(KapuaId scopeId) {
        ChannelInfoQuery query = new ChannelInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(10);
        query.setOffset(0);
        List<SortField> order = new ArrayList<SortField>();
        SortField sf = new SortFieldImpl();
        sf.setField(EsSchema.MESSAGE_TIMESTAMP);
        sf.setSortDirection(SortDirection.DESC);
        order.add(sf);
        query.setSortFields(order);
        return query;
    }

    private ChannelInfoListResult doChannelInfoQuery(Account account, String clientId, String channelFilter, Date queryDate) throws KapuaException {
        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        channelInfoQuery.setLimit(100);
        setChannelInfoQueryChannelPredicateCriteria(channelInfoQuery, clientId, channelFilter, new DateRange(queryDate));
        return channelInfoRegistryService.query(channelInfoQuery);
    }

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Metric Info schema
     *
     * @return
     */
    private MetricInfoQuery getBaseMetricInfoQuery(KapuaId scopeId) {
        MetricInfoQuery query = new MetricInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(10);
        query.setOffset(0);
        List<SortField> order = new ArrayList<SortField>();
        SortField sf = new SortFieldImpl();
        sf.setField(EsSchema.MESSAGE_TIMESTAMP);
        sf.setSortDirection(SortDirection.DESC);
        order.add(sf);
        query.setSortFields(order);
        return query;
    }

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Metric Info schema
     *
     * @return
     */
    private ClientInfoQuery getBaseClientInfoQuery(KapuaId scopeId) {
        ClientInfoQuery query = new ClientInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(10);
        query.setOffset(0);
        List<SortField> order = new ArrayList<SortField>();
        SortField sf = new SortFieldImpl();
        sf.setField(EsSchema.MESSAGE_TIMESTAMP);
        sf.setSortDirection(SortDirection.DESC);
        order.add(sf);
        query.setSortFields(order);
        return query;
    }

    /**
     * Get the ordered query (adding the sort fields list provided and the result limit count)
     *
     * @param limit
     * @param order
     * @return
     */
    private MessageQuery getMessageOrderedQuery(KapuaId scopeId, int limit, List<SortField> order) {
        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        query.setSortFields(order);
        return query;
    }

    /**
     * Set the query account and message timestamp filter
     *
     * @param messageQuery
     * @param accountName
     * @param dateRange
     */
    private void setMessageQueryBaseCriteria(MessageQuery messageQuery, DateRange dateRange) {
        setMessageQueryBaseCriteria(messageQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param messageQuery
     * @param accountName
     * @param clientId
     * @param dateRange
     */
    private void setMessageQueryBaseCriteria(MessageQuery messageQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientPredicate = datastoreObjectFactory.newTermPredicate(MessageField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(MessageField.TIMESTAMP, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        messageQuery.setPredicate(andPredicate);
    }

    /**
     * Set the query account and message timestamp filter
     *
     * @param channelInfoQuery
     * @param accountName
     * @param dateRange
     */
    private void setChannelInfoQueryBaseCriteria(ChannelInfoQuery channelInfoQuery, DateRange dateRange) {
        setChannelInfoQueryBaseCriteria(channelInfoQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param channelInfoQuery
     * @param accountName
     * @param clientId
     * @param dateRange
     */
    private void setChannelInfoQueryBaseCriteria(ChannelInfoQuery channelInfoQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientPredicate = datastoreObjectFactory.newTermPredicate(ChannelInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        channelInfoQuery.setPredicate(andPredicate);
    }

    /**
     *
     * @param channelInfoQuery
     * @param channelPredicate
     * @param dateLowerBound
     * @param dateUpperBound
     */
    private void setChannelInfoQueryChannelPredicateCriteria(ChannelInfoQuery channelInfoQuery, String clientId, String channelPredicate, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(ChannelInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        ChannelMatchPredicate channelMatchPredicate = new ChannelMatchPredicateImpl(channelPredicate);
        andPredicate.getPredicates().add(channelMatchPredicate);
        channelInfoQuery.setPredicate(andPredicate);
    }

    /**
     * Get the ordered query (adding the sort fields list provided and the result limit count)
     *
     * @param limit
     * @param order
     * @return
     */
    private MetricInfoQuery getMetricInfoOrderedQuery(KapuaId scopeId, int limit, List<SortField> order) {
        MetricInfoQuery query = new MetricInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        query.setSortFields(order);
        return query;
    }

    /**
     * Set the query account and message timestamp id filter
     *
     * @param metricInfoQuery
     * @param accountName
     * @param dateRange
     */
    private void setMetricInfoQueryBaseCriteria(MetricInfoQuery metricInfoQuery, DateRange dateRange) {
        setMetricInfoQueryBaseCriteria(metricInfoQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param metricInfoQuery
     * @param accountName
     * @param clientId
     * @param dateRange
     */
    private void setMetricInfoQueryBaseCriteria(MetricInfoQuery metricInfoQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(MetricInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(MetricInfoField.TIMESTAMP_FULL, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        metricInfoQuery.setPredicate(andPredicate);
    }

    /**
     * Set the query account and message timestamp filter
     *
     * @param clientInfoQuery
     * @param accountName
     * @param dateRange
     */
    private void setClientInfoQueryBaseCriteria(ClientInfoQuery clientInfoQuery, DateRange dateRange) {
        setClientInfoQueryBaseCriteria(clientInfoQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param clientInfoQuery
     * @param accountName
     * @param clientId
     * @param dateRange
     */
    private void setClientInfoQueryBaseCriteria(ClientInfoQuery clientInfoQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(ClientInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(ClientInfoField.TIMESTAMP, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        clientInfoQuery.setPredicate(andPredicate);
    }

    //
    // Utility methods to help to check the message result
    //
    /**
     * Check if in the result set has the expected messages count and return the first (if any)
     *
     * @param result
     * @return
     */
    private DatastoreMessage checkMessagesCount(MessageListResult result, int messagesCount) {
        DatastoreMessage messageQueried = null;
        if (messagesCount > 0) {
            assertNotNull("No result found!", result);
            assertNotNull("No result found!", result.getTotalCount());
            assertEquals("Result message has a wrong size!", messagesCount, result.getTotalCount().intValue());
            messageQueried = result.getFirstItem();
            assertNotNull("Result message is null!", messageQueried);
        } else {
            assertTrue("No result should be found!", result == null || result.getTotalCount() == null || result.getTotalCount() <= 0);
        }

        return messageQueried;
    }

    /**
     * Check if the queried message has the correct message id
     *
     * @param message
     * @param storableId
     */
    private void checkMessageId(DatastoreMessage message, StorableId storableId) {
        if (storableId != null) {
            assertNotNull("Message is null", message);
            assertNotNull("Message id doesn't match", message.getId());
            assertEquals("Message id doesn't match", storableId.toString(), message.getId().toString());
        }
    }

    /**
     * Check if the queried message has the correct semantic part (the same topic part and the same length)
     *
     * @param message
     * @param topicSemanticPart
     */
    private void checkTopic(DatastoreMessage message, String topicSemanticPart) {
        KapuaChannel channel = message.getChannel();
        assertNotNull("Null message channel!", channel);
        List<String> semanticParts = channel.getSemanticParts();
        assertNotNull("Null topic semantic part!", semanticParts);
        String[] topicSemanticPartTokenized = topicSemanticPart.split("/");
        assertEquals("Wrong semantic topic stored!", topicSemanticPartTokenized.length, semanticParts.size());
        int i = 0;
        for (String tmp : topicSemanticPartTokenized) {
            assertEquals(String.format("Wrong [%s] sematic part!", i), tmp, semanticParts.get(i++));
        }
    }

    /**
     * Check if the queried message has the correct dates (indexOn, sentOn, receivedOn, capturedOn). The dates can be checked also for a range.
     *
     * @param message
     * @param index
     * @param sentOn
     * @param capturedOn
     * @param receivedOn
     */
    private void checkMessageDate(DatastoreMessage message, Range<Date> index, Range<Date> sentOn, Range<Date> capturedOn, Range<Date> receivedOn) {
        assertNotNull("Message timestamp is null!", message.getTimestamp());
        index.checkValue(message.getTimestamp());
        sentOn.checkValue(message.getSentOn());
        capturedOn.checkValue(message.getCapturedOn());
        receivedOn.checkValue(message.getReceivedOn());
        assertNotNull("Message payload is null!", message.getPayload());
        assertNotNull("Message prorperties are null!", message.getPayload().getProperties());
    }

    /**
     * Check if the queried message has the correct metrics size
     *
     * @param message
     * @param metricsSize
     */
    private void checkMetricsSize(DatastoreMessage message, int metricsSize) {
        if (metricsSize < 0) {
            assertNull("Message metrics is not null!", message.getPayload().getProperties());
        } else {
            assertNotNull("Message metrics shouldn't be null!", message.getPayload().getProperties());
            assertEquals("Message metrics size doesn't match!", metricsSize, message.getPayload().getProperties().size());
        }
    }

    /**
     * Check if the queried message has the correct body (length and also content should be the same)
     *
     * @param message
     * @param body
     */
    private void checkMessageBody(DatastoreMessage message, byte[] body) {
        if (body == null) {
            assertNull("Message body is not null!", message.getPayload().getBody());
        } else {
            assertNotNull("Message body shouldn't be null!", message.getPayload().getBody());
            assertEquals("Message body size doesn't match!", body.length, message.getPayload().getBody().length);
            assertArrayEquals("Message body differs from the original!", body, message.getPayload().getBody());
        }
    }

    /**
     * Check if the queried message has the correct metrics (metrics count and same keys/values)
     *
     * @param message
     * @param metrics
     */
    private void checkMetrics(DatastoreMessage message, Map<String, Object> metrics) {
        // assuming metrics size is already checked by the checkMetricsSize
        Map<String, Object> messageProperties = message.getPayload().getProperties();
        Iterator<String> metricsKeys = metrics.keySet().iterator();
        while (metricsKeys.hasNext()) {
            String key = metricsKeys.next();
            assertEquals(String.format("Metric [%s] differs!", key), metrics.get(key), messageProperties.get(key));
        }
    }

    /**
     * Check if the queried message has the correct position
     *
     * @param message
     * @param position
     */
    private void checkPosition(DatastoreMessage message, KapuaPosition position) {
        if (position == null) {
            assertNull("Message position is not null!", message.getPosition());
        } else {
            assertNotNull("Message position shouldn't be null!", message.getPosition());
            KapuaPosition messagePosition = message.getPosition();
            assertEquals("Altitude position differs from the original!", messagePosition.getAltitude(), position.getAltitude());
            assertEquals("Heading position differs from the original!", messagePosition.getHeading(), position.getHeading());
            assertEquals("Latitude position differs from the original!", messagePosition.getLatitude(), position.getLatitude());
            assertEquals("Longitude position differs from the original!", messagePosition.getLongitude(), position.getLongitude());
            assertEquals("Precision position differs from the original!", messagePosition.getPrecision(), position.getPrecision());
            assertEquals("Satellites position differs from the original!", messagePosition.getSatellites(), position.getSatellites());
            assertEquals("Speed position differs from the original!", messagePosition.getSpeed(), position.getSpeed());
            assertEquals("Status position differs from the original!", messagePosition.getStatus(), position.getStatus());
            assertEquals("Timestamp position differs from the original!", messagePosition.getTimestamp(), position.getTimestamp());
        }
    }

    /**
     * Check if in the result set has the expected channel info count and return the first (if any)
     *
     * @param result
     * @return
     */
    private ChannelInfo checkChannelInfoCount(ChannelInfoListResult result, int clientInfoCount) {
        ChannelInfo channelInfoQueried = null;
        if (clientInfoCount > 0) {
            assertNotNull("No result found!", result);
            assertNotNull("No result found!", result.getTotalCount());
            assertEquals("Result channel info list has a wrong size!", clientInfoCount, result.getTotalCount().intValue());
            channelInfoQueried = result.getFirstItem();
            assertNotNull("Result channel info list is null!", channelInfoQueried);
        } else {
            assertTrue("No result should be found!", result == null || result.getTotalCount() == null || result.getTotalCount() <= 0);

        }
        return channelInfoQueried;
    }

    /**
     * Check if in the result set has the expected channel info client ids
     *
     * @param result
     * @return
     */
    private void checkChannelInfoClientIdsAndTopics(ChannelInfoListResult result, int clientInfoCount, String[] clientIds, String[] topics) {
        checkChannelInfoCount(result, clientInfoCount);
        Set<String> allClientId = new HashSet<String>();
        Set<String> allTopics = new HashSet<String>();
        for (ChannelInfo channelInfo : result.getItems()) {
            allClientId.add(channelInfo.getClientId());
            allTopics.add(channelInfo.getName());
        }
        assertEquals("Wrong client ids size!", (clientIds != null ? clientIds.length : 0), allClientId.size());
        assertEquals("Wrong topics size!", (topics != null ? topics.length : 0), allTopics.size());
        if (clientIds != null) {
            for (String clientIdFound : clientIds) {
                assertTrue(String.format("Cannot find the client [%s] in the client ids list!", clientIdFound), allClientId.contains(clientIdFound));
            }
        }
        if (topics != null) {
            for (String topicFound : topics) {
                assertTrue(String.format("Cannot find the topic [%s] in the topics list!", topicFound), allTopics.contains(topicFound));
            }
        }
    }

    /**
     * Check if in the result set has the expected metric info count and return the first (if any)
     *
     * @param result
     * @return
     */
    private MetricInfo checkMetricInfoCount(MetricInfoListResult result, int metricInfoCount) {
        MetricInfo metricInfoQueried = null;
        if (metricInfoCount > 0) {
            assertNotNull("No result found!", result);
            assertNotNull("No result found!", result.getTotalCount());
            assertEquals("Result metric info list has a wrong size!", metricInfoCount, result.getTotalCount().intValue());
            metricInfoQueried = result.getFirstItem();
            assertNotNull("Result metric info list is null!", metricInfoQueried);
        } else {
            assertTrue("No result should be found!", result == null || result.getTotalCount() == null || result.getTotalCount() <= 0);

        }
        return metricInfoQueried;
    }

    /**
     * Check if in the result set has the expected metric info client ids
     *
     * @param result
     * @return
     */
    private void checkMetricInfoClientIdsAndMetricNames(MetricInfoListResult result, int metricInfoCount, String[] clientIds, String[] metrics) {
        checkMetricInfoCount(result, metricInfoCount);
        Set<String> allClientId = new HashSet<String>();
        Set<String> allMetrics = new HashSet<String>();
        for (MetricInfo metricInfo : result.getItems()) {
            allClientId.add(metricInfo.getClientId());
            allMetrics.add(metricInfo.getName());
        }
        assertEquals("Wrong client ids size!", (clientIds != null ? clientIds.length : 0), allClientId.size());
        assertEquals("Wrong metrics size!", (metrics != null ? metrics.length : 0), allMetrics.size());
        if (clientIds != null) {
            for (String clientIdFound : clientIds) {
                assertTrue(String.format("Cannot find the client [%s] in the client ids list!", clientIdFound), allClientId.contains(clientIdFound));
            }
        }
        if (metrics != null) {
            for (String metric : metrics) {
                assertTrue(String.format("Cannot find the metric [%s] in the metrics list!", metric), allMetrics.contains(metric));
            }
        }
    }

    /**
     * Check if in the result set has the expected metric info count and return the first (if any)
     *
     * @param result
     * @return
     */
    private ClientInfo checkClientInfoCount(ClientInfoListResult result, int clientInfoCount) {
        ClientInfo clientInfoQueried = null;
        if (clientInfoCount > 0) {
            assertNotNull("No result found!", result);
            assertNotNull("No result found!", result.getTotalCount());
            assertEquals("Result client id list has a wrong size!", clientInfoCount, result.getTotalCount().intValue());
            clientInfoQueried = result.getItem(0);
            assertNotNull("Result client id list is null!", clientInfoQueried);
        } else {
            assertTrue("No result should be found!", result == null || result.getTotalCount() == null || result.getTotalCount() <= 0);

        }
        return clientInfoQueried;
    }

    /**
     * Check if in the result set has the expected client ids
     *
     * @param result
     * @param clientInfoCount
     * @param clientIds
     */
    private void checkClientInfo(ClientInfoListResult result, int clientInfoCount, String[] clientIds) {
        checkClientInfoCount(result, clientInfoCount);
        Set<String> allClientId = new HashSet<String>();
        for (ClientInfo clientInfo : result.getItems()) {
            allClientId.add(clientInfo.getClientId());
        }
        assertEquals("Wrong client ids size!", (clientIds != null ? clientIds.length : 0), allClientId.size());
        if (clientIds != null) {
            for (String clientIdFound : clientIds) {
                assertTrue(String.format("Cannot find the client [%s] in the client ids list!", clientIdFound), allClientId.contains(clientIdFound));
            }
        }
    }

    private void checkMetricDateBound(MetricInfoListResult result, Date startDate, Date endDate) {
        // TODO
    }

    private void checkMessagesDateBound(MessageListResult result, Date startDate, Date endDate) {
        // TODO
    }

    /**
     * Check if the message result list is correctly ordered by the provided criteria (list of fields and ordering)
     *
     * @param result
     * @param sortFieldList
     * @param cleanComposedFieldName
     *            takes only the field part after the last dot (useful for clean up the composed field name)
     */
    @SuppressWarnings("rawtypes")
    private void checkListOrder(StorableListResult<?> result, List<SortField> sortFieldList) {
        Object previousItem = null;
        for (Object item : result.getItems()) {
            for (SortField field : sortFieldList) {
                if (previousItem != null) {

                    Comparable currentValue = getValue(item, field.getField());
                    Comparable previousValue = getValue(previousItem, field.getField());

                    if (!currentValue.equals(previousValue)) {
                        checkNextValueCoherence(field, currentValue, previousValue);
                        // proceed with next message
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void checkNextValueCoherence(SortField field, Comparable currentValue, Comparable previousValue) {
        if (SortDirection.ASC.equals(field.getSortDirection())) {
            assertTrue(String.format("The field [%s] is not correctly ordered as [%s]!", field.getField(), field.getSortDirection()), currentValue.compareTo(previousValue) > 0);
        } else {
            assertTrue(String.format("The field [%s] is not correctly ordered as [%s]!", field.getField(), field.getSortDirection()), currentValue.compareTo(previousValue) < 0);
        }
    }

    /**
     * Return the value of the field name provided (assuming that this value is a Comparable)
     *
     * @param message
     * @param field
     * @param cleanComposedFieldName
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Comparable getValue(Object object, String field) {
        try {
            Class objetcClass = object.getClass();
            String getterFieldName = getFieldName(field, true);
            Method getMethod = getMethod(objetcClass, getterFieldName, "get");
            if (getMethod != null) {
                return (Comparable) getMethod.invoke(object, new Object[0]);
            }
            getMethod = getMethod(objetcClass, getterFieldName, "is");
            if (getMethod != null) {
                return (Comparable) getMethod.invoke(object, new Object[0]);
            }
            // else try by field access
            String fieldName = getFieldName(field, false);
            Field objField = getField(objetcClass, fieldName);
            if (objField != null) {
                return (Comparable) objField.get(object);
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
     * @param cleanComposedFieldName
     * @return
     */
    private String getFieldName(String field, boolean capitalizeFirstLetter) {
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

    private String[] cleanupFieldName(String field) {
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Method getMethod(Class objetcClass, String field, String prefix) {
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
     * @param objetcClass
     * @param field
     * @param prefix
     * @return
     */
    @SuppressWarnings({ "rawtypes" })
    private Field getField(Class objetcClass, String field) {
        Field objField = null;
        do {
            try {
                objField = objetcClass.getDeclaredField(field);
                objField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                objetcClass = objetcClass.getSuperclass();
            }
        } while (objField == null && objetcClass != null);

        return objField;
    }

    //
    // Configuration utility
    //
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
    private void updateConfiguration(MessageStoreService messageStoreService, KapuaId scopeId, KapuaId parentId, DataIndexBy dataIndexBy, MetricsIndexBy metricsIndexBy, int dataTTL, boolean storageEnabled)
            throws KapuaException {
        Map<String, Object> config = messageStoreService.getConfigValues(scopeId);
        if (config == null) {
            config = new HashMap<String, Object>();
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

    /**
     * Utility class to check the correctness value (exact value or range depending on the constructor used)
     *
     * @param <O>
     */
    private class Range<O extends Comparable<O>> {

        private String field;
        private O min;
        private O max;

        private Range(String field, O min, O max) {
            this(field, min);
            this.max = max;
        }

        private Range(String field, O exactValue) {
            assertNotNull("The lower bound or the exact value to compare cannot be null!", exactValue);
            this.field = field;
            this.min = exactValue;
        }

        public void checkValue(O value) {
            if (max == null) {
                assertEquals("Expected value for " + field + " doesn't match!", min, value);
            } else {
                assertTrue("Expected value for " + field + " doesn't match the lower bound", min.compareTo(value) <= 0);
                assertTrue("Expected value for " + field + " doesn't match the upper bound", max.compareTo(value) >= 0);
            }
        }

    }

    private class DateRange {

        private Date lowerBound;
        private Date upperBound;

        public DateRange(Date bound) {
            this(bound, bound);
        }

        public DateRange(Date lowerBound, Date upperBound) {
            this.lowerBound = new Date(lowerBound.getTime() - QUERY_TIME_WINDOW);
            this.upperBound = new Date(upperBound.getTime() + QUERY_TIME_WINDOW);
        }

        public Date getLowerBound() {
            return lowerBound;
        }

        public Date getUpperBound() {
            return upperBound;
        }

    }

    @Test
    /**
     * Base test, may be removed since we should already have tests that covers also this one (to be check!)
     *
     * @throws Exception
     */
    public void testStore()
            throws Exception {
        Account account = getTestAccountCreator(adminScopeId);
        Date now = new Date();

        String clientId = String.format("device-%d", now.getTime());
        DeviceCreator deviceCreator = deviceFactory.newCreator(account.getId(), clientId);
        Device device = deviceRegistryService.create(deviceCreator);

        KapuaDataMessageImpl message = new KapuaDataMessageImpl();
        KapuaDataChannelImpl channel = new KapuaDataChannelImpl();
        KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();
        KapuaPositionImpl messagePosition = new KapuaPositionImpl();
        Map<String, Object> metrics = new HashMap<String, Object>();

        channel.setClientId(device.getClientId());
        channel.setSemanticParts(Arrays.asList("APP01"));

        message.setScopeId(account.getId());
        message.setDeviceId(device.getId());
        message.setCapturedOn(now);
        message.setReceivedOn(now);
        message.setChannel(channel);

        metrics.put("distance", 1L);
        metrics.put("label", "pippo");
        messagePayload.setProperties(metrics);

        messagePosition.setAltitude(1.0);
        messagePosition.setTimestamp(now);
        message.setPosition(messagePosition);

        messagePayload.setProperties(metrics);
        message.setPayload(messagePayload);
        message.setClientId(clientId);

        StorableId messageId = messageStoreService.store(message);

        //
        // A non empty message id must be returned
        assertNotNull(messageId);
        assertTrue(!messageId.toString().isEmpty());

        //
        // Wait ES indexes to be refreshed
        Thread.sleep(elasticsearchRefreshTime.toMillis());

        //
        // Retrieve the message from its id
        DatastoreMessage retrievedMessage = messageStoreService.find(account.getId(), messageId, StorableFetchStyle.SOURCE_FULL);

        //
        // The returned message must be not null and values must coincide
        assertNotNull(retrievedMessage);
        assertTrue(messageId.equals(retrievedMessage.getDatastoreId()));
        assertTrue(account.getId().equals(retrievedMessage.getScopeId()));
        assertTrue(device.getId().equals(retrievedMessage.getDeviceId()));
        assertTrue(device.getClientId().equals(retrievedMessage.getClientId()));

        TermPredicate equalsMessageId = datastoreObjectFactory.newTermPredicate(ClientInfoField.MESSAGE_ID, messageId);

        ClientInfoQuery clientInfoQuery = datastoreObjectFactory.newClientInfoQuery(account.getId());
        clientInfoQuery.setOffset(0);
        clientInfoQuery.setLimit(1);
        clientInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        clientInfoQuery.setPredicate(equalsMessageId);

        ClientInfoRegistryService clientInfoRegistry = KapuaLocator.getInstance().getService(ClientInfoRegistryService.class);
        ClientInfoListResult clientInfos = clientInfoRegistry.query(clientInfoQuery);

        assertNotNull(clientInfos);
        assertTrue(clientInfos.getSize() == 1);

        ClientInfo clientInfo = clientInfos.getItem(0);

        assertNotNull(clientInfo);
        assertTrue(messageId.equals(clientInfo.getFirstMessageId()));

        // There must be a channel info entry in the registry
        equalsMessageId = datastoreObjectFactory.newTermPredicate(ChannelInfoField.MESSAGE_ID, messageId);

        ChannelInfoQuery channelInfoQuery = datastoreObjectFactory.newChannelInfoQuery(account.getId());
        channelInfoQuery.setOffset(0);
        channelInfoQuery.setLimit(1);
        channelInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        channelInfoQuery.setPredicate(equalsMessageId);

        ChannelInfoRegistryService channelInfoRegistry = KapuaLocator.getInstance().getService(ChannelInfoRegistryService.class);
        ChannelInfoListResult channelInfos = channelInfoRegistry.query(channelInfoQuery);

        assertNotNull(channelInfos);
        assertTrue(channelInfos.getSize() == 1);

        ChannelInfo channelInfo = channelInfos.getItem(0);

        assertNotNull(channelInfo);
        assertTrue(messageId.equals(channelInfo.getFirstMessageId()));

        // There must be two metric info entries in the registry
        equalsMessageId = datastoreObjectFactory.newTermPredicate(MetricInfoField.MESSAGE_ID_FULL, messageId);

        MetricInfoQuery metricInfoQuery = datastoreObjectFactory.newMetricInfoQuery(account.getId());
        metricInfoQuery.setOffset(0);
        metricInfoQuery.setLimit(2);
        metricInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        metricInfoQuery.setPredicate(equalsMessageId);

        MetricInfoRegistryService metricInfoRegistry = KapuaLocator.getInstance().getService(MetricInfoRegistryService.class);
        MetricInfoListResult metricInfos = metricInfoRegistry.query(metricInfoQuery);

        assertNotNull(metricInfos);
        assertTrue(metricInfos.getSize() == 2);

        MetricInfo metricInfo = metricInfos.getItem(0);

        assertNotNull(metricInfo);
        assertTrue(messageId.equals(metricInfo.getFirstMessageId()));

        metricInfo = metricInfos.getItem(1);

        assertNotNull(metricInfo);
        assertTrue(messageId.equals(metricInfo.getFirstMessageId()));
    }

    /**
     * Return a new account created just for the test.<br>
     * <b>WARNING!!!!!!! Current implementation is not compliance with that since it is a temporary implementation that returns the default kapua-sys account</b>
     *
     * @param scopeId
     * @return
     * @throws KapuaException
     */
    private Account getTestAccountCreator(KapuaId scopeId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        Account account = locator.getService(AccountService.class).findByName("kapua-sys");
        return account;
    }

}
