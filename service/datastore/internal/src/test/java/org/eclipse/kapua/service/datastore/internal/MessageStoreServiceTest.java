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

import static java.util.Objects.requireNonNull;
import static org.eclipse.kapua.service.datastore.model.query.SortField.ascending;
import static org.eclipse.kapua.service.datastore.model.query.SortField.descending;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
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
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.embedded.EsEmbeddedEngine;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreChannel;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreUtils;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.mediator.MetricInfoField;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MetricsIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageStoreServiceTest extends AbstractMessageStoreServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageStoreServiceTest.class);
    private static final long PUBLISH_DATE_TEST_CHECK_TIME_WINDOW = 1000l;
    private final static String DATA_TTL_KEY = "dataTTL";

    @SuppressWarnings("unused")
    // this unused instance is just added to initialize the embedded node as first step before executing any action on datastore side.
    // otherwise, if the datastore service is initialized before the embedded es node startup, the transport connector is not able to be initialized (since it tries to connect to the node)
    // if the embedded node is initialized in @Before method of this class, the initialization happens after this is loaded by the classloader so the datastore service initialization, at that point,
    // is already done!
    private static EsEmbeddedEngine esEmbeddedEngine;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);
    private static final MessageStoreService MESSAGE_STORE_SERVICE = LOCATOR.getService(MessageStoreService.class);
    private static final DatastoreObjectFactory DATASTORE_OBJECT_FACTORY = LOCATOR.getFactory(DatastoreObjectFactory.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);
    // for the registries the locator should return the Impls that provide the deletes method. But I prefer to explicitly cast the service instance to the impl when I call the deletes
    private static final ChannelInfoRegistryService CHANNEL_INFO_REGISTRY_SERVICE = LOCATOR.getService(ChannelInfoRegistryService.class);
    private static final MetricInfoRegistryService METRIC_INFO_REGISTRY_SERVICE = LOCATOR.getService(MetricInfoRegistryService.class);
    private static final ClientInfoRegistryService CLIENT_INFO_REGISTRY_SERVICE = LOCATOR.getService(ClientInfoRegistryService.class);
    private static DatastoreClient datastoreClient;

    static {
        try {
            datastoreClient = DatastoreClientFactory.getInstance();
        } catch (Exception e) {
            logger.error("Error getting data store client!", e);
        }
    }

    @BeforeClass
    public static void startEmbeddedEngine() throws Exception {
        esEmbeddedEngine = new EsEmbeddedEngine();
        Thread.sleep(5000);
    }

    /**
     * This method deletes all indices of the current ES instance
     * <p>
     * The method deletes all indices and resets the Kapua internal singleton state.
     * This is required to ensure that each unit test, as it currently expects, starts with an empty ES setup
     * </p>
     * 
     * @throws Exception
     *             any case anything goes wrong
     */
    @Before
    public void deleteAllIndices() throws Exception {
        DatastoreMediator.getInstance().deleteAllIndexes();
    }

    @AfterClass
    public static void stopEsEmbeddedEngine() throws Exception {
        if (esEmbeddedEngine != null) {
            esEmbeddedEngine.close();
            Thread.sleep(5000);
            esEmbeddedEngine = null;
        }
    }

    @Test
    public void deleteByDate() throws InterruptedException, KapuaException, ParseException {
        Account account = getTestAccountCreator(adminScopeId);
        String[] semanticTopic = new String[] {
                "delete/by/date/test"
        };
        Map<String, Object> configValues = MESSAGE_STORE_SERVICE.getConfigValues(account.getId());
        int dataTtl = (int)configValues.get(DATA_TTL_KEY);
        try {
            //update ttl
            configValues.put(DATA_TTL_KEY, 2*365);
            MESSAGE_STORE_SERVICE.setConfigValues(account.getId(), null, configValues);
            String clientId = String.format("device-%d", new Date().getTime());
            DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
            Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

            // leave the message index by as default (DEVICE_TIMESTAMP)
            String stringPayload = "Hello delete by date message!";
            byte[] payload = stringPayload.getBytes();

            Date startDate = KapuaDateUtils.parseDate("2016-11-01T00:00:00.000Z");
            Date endDate = KapuaDateUtils.parseDate("2017-03-31T00:00:00.000Z");
            Instant startInstant = startDate.toInstant();
            Instant endInstant = endDate.toInstant();
            Date sentOn = startDate;
            int messagesCount = 0;
            while (sentOn.before(endDate)) {
                insertMessage(account, clientId, device.getId(), semanticTopic[0], payload, sentOn);
                messagesCount++;
                startInstant = startInstant.plus(1, ChronoUnit.DAYS);
                sentOn = Date.from(startInstant);
            }

            // insert 2 messages/day for the window
            Date startDate1 = KapuaDateUtils.parseDate("2016-12-01T00:00:00.000Z");
            Date endDate1 = KapuaDateUtils.parseDate("2016-12-31T00:00:00.000Z");
            Instant startInstant1 = startDate1.toInstant();
            Instant endInstant1 = endDate1.toInstant();
            sentOn = startDate1;
            while (!sentOn.after(endDate1)) {
                sentOn = Date.from(startInstant1.plus(600, ChronoUnit.SECONDS));
                insertMessage(account, clientId, device.getId(), semanticTopic[0], payload, sentOn);
                messagesCount++;
                startInstant1 = startInstant1.plus(1, ChronoUnit.DAYS);
                sentOn = Date.from(startInstant1);
            }

            // insert 3 messages/day for the window
            Date startDate2 = KapuaDateUtils.parseDate("2017-02-01T00:00:00.000Z");
            Date endDate2 = KapuaDateUtils.parseDate("2017-02-28T00:00:00.000Z");
            Instant startInstant2 = startDate2.toInstant();
            Instant endInstant2 = endDate2.toInstant();
            sentOn = startDate2;
            while (!sentOn.after(endDate2)) {
                sentOn = Date.from(startInstant2.plus(600, ChronoUnit.SECONDS));
                insertMessage(account, clientId, device.getId(), semanticTopic[0], payload, sentOn);
                sentOn = Date.from(startInstant2.plus(2 * 600, ChronoUnit.SECONDS));
                insertMessage(account, clientId, device.getId(), semanticTopic[0], payload, sentOn);
                messagesCount += 2;
                startInstant2 = startInstant2.plus(1, ChronoUnit.DAYS);
                sentOn = Date.from(startInstant2);
            }

            // do a first query count
            DatastoreMediator.getInstance().refreshAllIndexes();
            MessageQuery messageQuery = getBaseMessageQuery(KapuaEid.ONE, 1000);
            setMessageQueryBaseCriteria(messageQuery, clientId, new DateRange(startDate, endDate));
            long count = MESSAGE_STORE_SERVICE.count(messageQuery);
            assertEquals(messagesCount, count);

            // delete by month window
            messagesCount -= 56;
            String[] indexes = DatastoreUtils.convertToDataIndexes(KapuaEid.ONE, KapuaDateUtils.parseDate("2016-12-01T00:00:00.000Z").toInstant(),
                    KapuaDateUtils.parseDate("2016-12-31T00:00:00.000Z").toInstant());
            datastoreClient.deleteIndexes(indexes);
            DatastoreMediator.getInstance().refreshAllIndexes();
            count = MESSAGE_STORE_SERVICE.count(messageQuery);
            assertEquals(messagesCount, count);

            // delete by month window
            messagesCount -= 28;
            indexes = DatastoreUtils.convertToDataIndexes(KapuaEid.ONE, KapuaDateUtils.parseDate("2017-01-01T00:00:00.000Z").toInstant(), KapuaDateUtils.parseDate("2017-01-31T00:00:00.000Z").toInstant());
            datastoreClient.deleteIndexes(indexes);
            DatastoreMediator.getInstance().refreshAllIndexes();
            count = MESSAGE_STORE_SERVICE.count(messageQuery);
            assertEquals(messagesCount, count);

            // delete by month window
            messagesCount -= 63;
            indexes = DatastoreUtils.convertToDataIndexes(KapuaEid.ONE, KapuaDateUtils.parseDate("2017-02-01T00:00:00.000Z").toInstant(), KapuaDateUtils.parseDate("2017-02-31T00:00:00.000Z").toInstant());
            datastoreClient.deleteIndexes(indexes);
            DatastoreMediator.getInstance().refreshAllIndexes();
            count = MESSAGE_STORE_SERVICE.count(messageQuery);
            assertEquals(messagesCount, count);

            // do a new query
            messagesCount = 0;
            indexes = DatastoreUtils.convertToDataIndexes(KapuaEid.ONE, startDate.toInstant().minus(7, ChronoUnit.DAYS), endDate.toInstant().plus(7, ChronoUnit.DAYS));
            datastoreClient.deleteIndexes(indexes);
            DatastoreMediator.getInstance().refreshAllIndexes();
            MESSAGE_STORE_SERVICE.count(messageQuery);
            count = MESSAGE_STORE_SERVICE.count(messageQuery);
            assertEquals(messagesCount, count);
        }
        finally {
            //restore ttl
            configValues.put(DATA_TTL_KEY, dataTtl);
            MESSAGE_STORE_SERVICE.setConfigValues(account.getId(), null, configValues);
        }
    }

    // private void printMessages(MessageQuery messageQuery) throws KapuaException {
    // MessageListResult result = MESSAGE_STORE_SERVICE.query(messageQuery);
    // List<DatastoreMessage> list = result.getItems();
    // for (DatastoreMessage message : list) {
    // logger.info("Date: {}", message.getSentOn());
    // }
    // }

    // private void logMessagesByMonth(String clientId) throws ParseException, KapuaException {
    // MessageQuery messageQuery1 = getBaseMessageQuery(KapuaEid.ONE, 1000);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2016-11-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-05-31T00:00:00.000Z")));
    // long count0 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2016-11-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2016-11-30T00:00:00.000Z")));
    // long count1 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2016-12-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2016-12-31T00:00:00.000Z")));
    // long count2 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2017-01-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-01-31T00:00:00.000Z")));
    // long count3 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2017-02-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-02-28T00:00:00.000Z")));
    // long count4 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2017-03-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-03-31T00:00:00.000Z")));
    // long count5 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2017-04-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-04-30T00:00:00.000Z")));
    // long count6 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // setMessageQueryBaseCriteria(messageQuery1, clientId, new DateRange(KapuaDateUtils.parseDate("2017-05-01T00:00:00.000Z"), KapuaDateUtils.parseDate("2017-05-31T00:00:00.000Z")));
    // long count7 = MESSAGE_STORE_SERVICE.count(messageQuery1);
    // logger.info("{} - {} - {} - {} - {} - {} - {} - {}", new Object[] { count0, count1, count2, count3, count4, count5, count6, count7 });
    // }

    private KapuaDataMessage insertMessage(Account account, String clientId, KapuaId deviceId, String semanticTopic, byte[] payload, Date sentOn) throws InterruptedException {
        KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();
        Map<String, Object> metrics = new HashMap<String, Object>();
        metrics.put("float", new Float((float) 0.01));
        messagePayload.setMetrics(metrics);
        messagePayload.setBody(payload);
        Date receivedOn = Date.from(KapuaDateUtils.getKapuaSysDate());
        Date capturedOn = sentOn;
        KapuaDataMessage message = createMessage(clientId, account.getId(), deviceId, receivedOn, capturedOn, sentOn);
        setChannel(message, semanticTopic);
        updatePayload(message, messagePayload);
        List<StorableId> messageStoredIds = insertMessages(message);
        return message;
    }

    @Test
    public void deleteById() throws KapuaException, ParseException, InterruptedException {
        Account account = getTestAccountCreator(adminScopeId);
        String[] semanticTopic = new String[] {
                "delete/by/id/test"
        };

        KapuaDataMessage message = null;
        String clientId = String.format("device-%d", new Date().getTime());
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        byte[] randomPayload = new byte[128];
        random.nextBytes(randomPayload);
        String stringPayload = "Hello delete by id message!";
        byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

        KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();

        Map<String, Object> metrics = new HashMap<String, Object>();
        metrics.put("float", new Float((float) 0.01));
        metrics.put("integer", new Integer(1));
        metrics.put("double", (double) 0.01);
        metrics.put("long", (long) (10000000000000l));
        metrics.put("string_value", Integer.toString(1000));
        messagePayload.setMetrics(metrics);

        messagePayload.setBody(payload);
        Date receivedOn = Date.from(KapuaDateUtils.getKapuaSysDate());
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = receivedOn;
        message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message, semanticTopic[0]);
        updatePayload(message, messagePayload);
        KapuaPosition messagePosition = getPosition(10.00d, 12d, 1.123d, 2d, 0001d, 1000, 1d, 44, new Date());
        message.setPosition(messagePosition);
        List<StorableId> messageStoredIds = null;
        DatastoreMessage storedMessage = null;
        try {
            messageStoredIds = insertMessages(message);

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            storedMessage = MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
            fullMessageCheck(account, storedMessage, message, messageStoredIds.get(0), storedMessage.getDatastoreId(), storedMessage.getTimestamp());

            // delete the message
            MESSAGE_STORE_SERVICE.delete(account.getId(), messageStoredIds.get(0));

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertNull(MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL));

            // check for the mappings

            // channel
            ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
            ChannelInfoListResult channelInfo = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
            assertEquals("Wrong channel info size", 1, channelInfo.getSize());
            // delete the channel info registry
            ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(account.getId(), channelInfo.getFirstItem().getId());
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(CHANNEL_INFO_REGISTRY_SERVICE.count(channelInfoQuery), 0);

            // metric
            MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
            MetricInfoListResult metricInfoList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
            assertEquals("Wrong metric info size", 5, metricInfoList.getSize());
            // delete the metric info registry
            for (MetricInfo metricInfo : metricInfoList.getItems()) {
                ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(account.getId(), metricInfo.getId());
            }
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(METRIC_INFO_REGISTRY_SERVICE.count(metricInfoQuery), 0);

            // client
            ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 10);
            ClientInfoListResult clientInfo = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
            assertEquals("Wrong client info size", 1, clientInfo.getSize());
            // delete the client info registry
            ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(account.getId(), clientInfo.getFirstItem().getId());
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(CLIENT_INFO_REGISTRY_SERVICE.count(clientInfoQuery), 0);
        } catch (KapuaException e) {
            logger.error("Exception: ", e.getMessage(), e);
        }
    }

    @Test
    public void deleteByQuery() throws KapuaException, ParseException, InterruptedException {
        Account account = getTestAccountCreator(adminScopeId);
        String[] semanticTopic = new String[] {
                "delete/by/query/test/1",
                "delete/by/query/test/2",
                "delete/by/query/test/3"
        };
        long clientIdSuffix = new Date().getTime();
        KapuaDataMessage[] messages = new KapuaDataMessage[semanticTopic.length];
        for (int i = 0; i < semanticTopic.length; i++) {
            String clientId = String.format("device-%d", clientIdSuffix + 1000 * i);
            KapuaDataMessage message = null;
            DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
            Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

            // leave the message index by as default (DEVICE_TIMESTAMP)
            byte[] randomPayload = new byte[128];
            random.nextBytes(randomPayload);
            String stringPayload = "Hello delete by query message!";
            byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

            KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();

            Map<String, Object> metrics = new HashMap<String, Object>();
            metrics.put("float", new Float((float) 0.01));
            metrics.put("integer", new Integer(1));
            metrics.put("double", (double) 0.01);
            metrics.put("long", (long) (10000000000000l));
            metrics.put("string_value", Integer.toString(1000));
            messagePayload.setMetrics(metrics);

            messagePayload.setBody(payload);
            Date receivedOn = Date.from(KapuaDateUtils.getKapuaSysDate());
            Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
            Date capturedOn = receivedOn;
            message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message, semanticTopic[i]);
            updatePayload(message, messagePayload);
            KapuaPosition messagePosition = getPosition(10.00d, 12d, 1.123d, 2d, 0001d, 1000, 1d, 44, new Date());
            message.setPosition(messagePosition);
            messages[i] = message;
        }
        List<StorableId> messageStoredIds = null;
        DatastoreMessage storedMessage = null;
        try {
            messageStoredIds = insertMessages(messages);

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            storedMessage = MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
            fullMessageCheck(account, storedMessage, messages[0], messageStoredIds.get(0), storedMessage.getDatastoreId(), storedMessage.getTimestamp());

            // delete the message
            MESSAGE_STORE_SERVICE.delete(account.getId(), messageStoredIds.get(0));

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertNull(MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL));

            // check for the mappings

            // channel
            ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
            ChannelInfoListResult channelInfo = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
            assertEquals("Wrong channel info size", semanticTopic.length, channelInfo.getSize());
            // delete the channel info registry
            ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(account.getId(), channelInfo.getFirstItem().getId());
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(CHANNEL_INFO_REGISTRY_SERVICE.count(channelInfoQuery), semanticTopic.length - 1);

            // metric
            MetricInfoQuery metricInfoQueryFull = getBaseMetricInfoQuery(account.getId());
            MetricInfoListResult metricInfoListFull = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQueryFull);
            assertEquals("Wrong metric info size", 5 * semanticTopic.length, metricInfoListFull.getSize());
            MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
            setMetricInfoQueryBaseCriteria(metricInfoQuery, semanticTopic[0]);
            MetricInfoListResult metricInfoList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
            assertEquals("Wrong metric info size", 5, metricInfoList.getSize());
            // delete the metric info registry
            for (MetricInfo metricInfo : metricInfoList.getItems()) {
                ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(account.getId(), metricInfo.getId());
            }
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(5 * (semanticTopic.length - 1), METRIC_INFO_REGISTRY_SERVICE.count(metricInfoQueryFull));
            assertEquals(0, METRIC_INFO_REGISTRY_SERVICE.count(metricInfoQuery));

            // client
            ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 10);
            ClientInfoListResult clientInfo = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
            assertEquals("Wrong client info size", semanticTopic.length, clientInfo.getSize());
            // delete the client info registry
            ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(account.getId(), clientInfo.getFirstItem().getId());
            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            assertEquals(semanticTopic.length - 1, CLIENT_INFO_REGISTRY_SERVICE.count(clientInfoQuery));
        } catch (KapuaException e) {
            logger.error("Exception: ", e.getMessage(), e);
        }
    }

    @Test
    public void checkMappingForSameMetricNameAndDifferentType() throws KapuaException, ParseException, InterruptedException {
        Account account = getTestAccountCreator(adminScopeId);
        String[] semanticTopic = new String[] {
                "same/metric/name/different/type/1",
                "same/metric/name/different/type/2"
        };
        long clientIdSuffix = new Date().getTime();
        KapuaDataMessage[] messages = new KapuaDataMessage[semanticTopic.length];
        Instant capturedOnDate = KapuaDateUtils.getKapuaSysDate();
        for (int i = 0; i < semanticTopic.length; i++) {
            String clientId = String.format("device-%d", clientIdSuffix);
            KapuaDataMessage message = null;
            DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
            Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

            // leave the message index by as default (DEVICE_TIMESTAMP)
            byte[] randomPayload = new byte[128];
            random.nextBytes(randomPayload);
            String stringPayload = "Hello delete by query message!";
            byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

            KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();
            messagePayload.setMetrics(getMetrics(message, i));
            messagePayload.setBody(payload);
            Date receivedOn = Date.from(capturedOnDate.plusMillis(i * 1000));
            Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
            Date capturedOn = receivedOn;
            message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message, semanticTopic[i]);
            updatePayload(message, messagePayload);
            KapuaPosition messagePosition = getPosition(10.00d, 12d, 1.123d, 2d, 0001d, 1000, 1d, 44, new Date());
            message.setPosition(messagePosition);
            messages[i] = message;
        }
        List<StorableId> messageStoredIds = null;
        try {
            messageStoredIds = insertMessages(messages);

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            List<OrderConstraint<?>> sort = new ArrayList<>();
            sort.add(orderConstraint(ascending(MessageSchema.MESSAGE_TIMESTAMP), Date.class));
            MessageQuery messageQuery = getMessageOrderedQuery(account.getId(), 100, sort);

            Thread.sleep(2*PUBLISH_DATE_TEST_CHECK_TIME_WINDOW); // wait for messages because of TTL filter
            MessageListResult messageList = MESSAGE_STORE_SERVICE.query(messageQuery);
            checkMessagesCount(messageList, semanticTopic.length);
            for (int i = 0; i < messageList.getSize(); i++) {
                 DatastoreMessage message = messageList.getItems().get(i);
                KapuaDataMessage messageToCompare = messages[i];
                checkMessageId(message, messageStoredIds.get(i));
                 checkTopic(message, messageToCompare.getChannel().toString());
                 checkMessageBody(message, messageToCompare.getPayload().getBody());
                 checkMetricsSize(message, messageToCompare.getPayload().getMetrics().size());
                 checkMetrics(message, messageToCompare.getPayload().getMetrics());
                 checkPosition(message, messageToCompare.getPosition());
             }
        } catch (KapuaException e) {
            logger.error("Exception: ", e.getMessage(), e);
        }
    }

    @Test
    public void queryBeforeSchema() throws KapuaException {
        Account account = getTestAccountCreator(adminScopeId);
        assertNull(MESSAGE_STORE_SERVICE.find(account.getId(), new StorableIdImpl("fake-id"), StorableFetchStyle.SOURCE_FULL));
        assertNull(CHANNEL_INFO_REGISTRY_SERVICE.find(account.getId(), new StorableIdImpl("fake-id")));
        assertNull(METRIC_INFO_REGISTRY_SERVICE.find(account.getId(), new StorableIdImpl("fake-id")));
        assertNull(CLIENT_INFO_REGISTRY_SERVICE.find(account.getId(), new StorableIdImpl("fake-id")));

        MessageQuery messageQuery = getBaseMessageQuery(account.getId(), 100);
        assertTrue(MESSAGE_STORE_SERVICE.query(messageQuery).isEmpty());
        assertEquals(MESSAGE_STORE_SERVICE.count(messageQuery), 0);
        MESSAGE_STORE_SERVICE.delete(messageQuery);
        MESSAGE_STORE_SERVICE.delete(account.getId(), new StorableIdImpl("fake-id"));

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        assertTrue(CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery).isEmpty());
        assertEquals(CHANNEL_INFO_REGISTRY_SERVICE.count(channelInfoQuery), 0);
        ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(channelInfoQuery);
        ((ChannelInfoRegistryServiceImpl) CHANNEL_INFO_REGISTRY_SERVICE).delete(account.getId(), new StorableIdImpl("fake-id"));

        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        assertTrue(METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery).isEmpty());
        assertEquals(METRIC_INFO_REGISTRY_SERVICE.count(metricInfoQuery), 0);
        ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(metricInfoQuery);
        ((MetricInfoRegistryServiceImpl) METRIC_INFO_REGISTRY_SERVICE).delete(account.getId(), new StorableIdImpl("fake-id"));

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 10);
        assertTrue(CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery).isEmpty());
        assertEquals(CLIENT_INFO_REGISTRY_SERVICE.count(clientInfoQuery), 0);
        ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(clientInfoQuery);
        ((ClientInfoRegistryServiceImpl) CLIENT_INFO_REGISTRY_SERVICE).delete(account.getId(), new StorableIdImpl("fake-id"));
    }

    @Test
    /**
     * This test checks the coherence of the registry cache for the metrics info (so if, once the cache is erased, after a new metric insert the firstMessageId and firstMessageOn contain the previous
     * value)
     * 
     * @throws Exception
     */
    public void checkRegistryCache() throws Exception {
        Account account = getTestAccountCreator(adminScopeId);
        String[] semanticTopic = new String[] {
                "registry/cache/check"
        };

        KapuaDataMessage message = null;
        String clientId = String.format("device-%d", new Date().getTime());
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        byte[] randomPayload = new byte[128];
        random.nextBytes(randomPayload);
        String stringPayload = "Hello first message on!";
        byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

        KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();

        Map<String, Object> metrics = new HashMap<String, Object>();
        metrics.put("float", new Float((float) 0.01));
        metrics.put("integer", new Integer(1));
        metrics.put("double", (double) 0.01);
        metrics.put("long", (long) (10000000000000l));
        metrics.put("string_value", Integer.toString(1000));
        messagePayload.setMetrics(metrics);

        messagePayload.setBody(payload);
        Date receivedOn = Date.from(KapuaDateUtils.getKapuaSysDate());
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = receivedOn;
        message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message, semanticTopic[0]);
        updatePayload(message, messagePayload);
        KapuaPosition messagePosition = getPosition(10.00d, 12d, 1.123d, 2d, 0001d, 1000, 1d, 44, new Date());
        message.setPosition(messagePosition);
        List<StorableId> messageStoredIds = null;
        DatastoreMessage firstMessage = null;
        DatastoreMessage secondMessage = null;
        try {
            messageStoredIds = insertMessages(message);

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            firstMessage = MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
            fullMessageCheck(account, firstMessage, message, messageStoredIds.get(0), firstMessage.getDatastoreId(), firstMessage.getTimestamp());
        } catch (KapuaException e) {
            logger.error("Exception: ", e.getMessage(), e);
        }
        // reset the cache
        DatastoreCacheManager.getInstance().getChannelsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getClientsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetricsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetadataCache().invalidateAll();
        receivedOn.setTime(receivedOn.getTime() + 5000);
        sentOn.setTime(sentOn.getTime() + 5000);
        // do a new insert
        try {
            messageStoredIds = insertMessages(message);

            // Refresh indices before querying
            DatastoreMediator.getInstance().refreshAllIndexes();
            secondMessage = MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
            fullMessageCheck(account, secondMessage, message, messageStoredIds.get(0), firstMessage.getDatastoreId(), firstMessage.getTimestamp());
        } catch (KapuaException e) {
            logger.error("Exception: ", e.getMessage(), e);
        }
    }

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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        String metricDate = new String("01/04/2017 03:00:00");
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        DateFormat dfBrussels = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfBrussels.setTimeZone(TimeZone.getTimeZone("Europe/Brussels"));
        DateFormat dfLA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfLA.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        DateFormat dfHK = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfHK.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
        DateFormat dfSydney = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dfSydney.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
        Date dateBrussels = dfBrussels.parse(metricDate);
        Date dateLA = dfLA.parse(metricDate);
        Date dateHK = dfHK.parse(metricDate);
        Date dateSydney = dfSydney.parse(metricDate);

        for (int i = 0; i < 12; i++) {
            byte[] randomPayload = new byte[128];
            random.nextBytes(randomPayload);
            String stringPayload = "Hello World" + (i + 1) + " \n\n\t\n\tHelloWord2";
            byte[] payload = ArrayUtils.addAll(randomPayload, stringPayload.getBytes());

            KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();

            Map<String, Object> metrics = new HashMap<>();
            metrics.put("float_int", Float.valueOf(i + 1));
            metrics.put("float_float", Float.valueOf((i + 1) * 0.01f));
            metrics.put("integer_value", Integer.valueOf(i + 1));
            metrics.put("double_int", (double) (i + 1));
            metrics.put("double_float", (i + 1) * 0.01);
            metrics.put("long_long", 10000000000000l * (i + 1));
            metrics.put("long_int_1", (long) (1000 * (i + 1)));
            metrics.put("long_int_2", (long) (i + 1));
            metrics.put("string_value", Integer.toString((i + 1) * 1000));
            metrics.put("date_value_brussels", dateBrussels);
            metrics.put("date_value_ls", dateLA);
            metrics.put("date_value_hk", dateHK);
            metrics.put("date_value_sydney", dateSydney);
            messagePayload.setMetrics(metrics);

            messagePayload.setBody(payload);
            Date receivedOn = new Date();
            Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
            Date capturedOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2016").getTime());
            message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message, semanticTopic[i % semanticTopic.length]);
            updatePayload(message, messagePayload);
            KapuaPosition messagePosition = getPosition(10.00d * (i + 1), 12d * (i + 1), 1.123d * (i + 1), 2d * (i + 1), 0001d * (i + 1), 1000 * (i + 1), 1d * (i + 1), 44 * (i + 1), new Date());
            message.setPosition(messagePosition);
            List<StorableId> messageStoredIds = null;
            try {
                messageStoredIds = insertMessages(message);

                // Refresh indices before querying
                DatastoreMediator.getInstance().refreshAllIndexes();

                // start queries

                DatastoreMessage messageQueried = MESSAGE_STORE_SERVICE.find(account.getId(), messageStoredIds.get(0), StorableFetchStyle.SOURCE_FULL);
                checkMessageId(messageQueried, messageStoredIds.get(0));
                checkTopic(messageQueried, semanticTopic[i % semanticTopic.length]);
                checkMessageBody(messageQueried, message.getPayload().getBody());
                checkMetricsSize(messageQueried, metrics.size());
                checkMetrics(messageQueried, metrics);
                checkPosition(messageQueried, messagePosition);
                checkMessageDate(messageQueried, new Range<>("timestamp", capturedOn), new Range<>("sentOn", sentOn), new Range<>("capturedOn", capturedOn),
                        new Range<>("receivedOn", receivedOn));
            } catch (KapuaException e) {
                logger.error("Exception: ", e.getMessage(), e);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId1);
        Device device1 = DEVICE_REGISTRY_SERVICE.create(deviceCreator);
        DeviceCreator deviceCreator2 = DEVICE_FACTORY.newCreator(account.getId(), clientId2);
        Device device2 = DEVICE_REGISTRY_SERVICE.create(deviceCreator2);
        int messagesCount = 100;
        Date sentOn1 = new Date();
        Date sentOn2 = new Date(sentOn1.getTime() + 5000);
        Date capturedOn1 = new Date(new Date().getTime() + 1000);
        Date capturedOn2 = new Date(capturedOn1.getTime() + 1000);
        String clientId = null;
        Device device = null;

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);

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
            if (i < messagesCount / 4 - 1 || i > messagesCount / 2 - 1 && i < messagesCount * 3 / 4 - 1) {
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
            message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message, semanticTopic[i % semanticTopic.length]);
            insertMessages(message);
        }

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        List<OrderConstraint<?>> sort = new ArrayList<>();
        sort.add(orderConstraint(descending(MessageSchema.MESSAGE_SENT_ON), Date.class));
        sort.add(orderConstraint(ascending(MessageSchema.MESSAGE_TIMESTAMP), Date.class));
        sort.add(orderConstraint(descending(MessageSchema.MESSAGE_CLIENT_ID), String.class));
        MessageQuery messageQuery = getMessageOrderedQuery(account.getId(), messagesCount + 1, sort);
        setMessageQueryBaseCriteria(messageQuery, new DateRange(capturedOn1, capturedOn2));

        Thread.sleep(5000); // wait for messages because of TTL filter
        MessageListResult messageList = MESSAGE_STORE_SERVICE.query(messageQuery);
        checkMessagesCount(messageList, messagesCount);
        checkMessagesDateBound(messageList, new Date(capturedOn1.getTime()), new Date(capturedOn2.getTime()));
        for (DatastoreMessage messageStored : messageList.getItems()) {
            logger.debug("message sentOn: '" + messageStored.getSentOn() + "' - capturedOn: '" + messageStored.getCapturedOn() + "' clientId: '" + messageStored.getClientId() + "'");
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String topicSemanticPart = "testStoreWithNullPayload/testStoreWithNullPayload/" + Calendar.getInstance().getTimeInMillis();
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message, topicSemanticPart);
        updatePayload(message, null);
        message.setReceivedOn(messageTime);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        List<StorableId> messageStoredIds = insertMessages(message);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        MessageQuery messageQuery = getBaseMessageQuery(account.getId(), 100);
        setMessageQueryBaseCriteria(messageQuery, null);

        MessageListResult result = MESSAGE_STORE_SERVICE.query(messageQuery);
        DatastoreMessage messageQueried = checkMessagesCount(result, 1);
        checkMessageId(messageQueried, messageStoredIds.get(0));
        checkMessageBody(messageQueried, null);
        checkMetricsSize(messageQueried, 0);
        checkPosition(messageQueried, null);
        checkMessageDate(messageQueried, new Range<>("timestamp", capturedOn), new Range<>("sentOn", sentOn), new Range<>("capturedOn", capturedOn),
                new Range<>("receivedOn", messageTime));
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String topicSemanticPart = "testStoreWithNullPayload/testStoreWithNullPayload/" + Calendar.getInstance().getTimeInMillis();
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message, topicSemanticPart);
        updatePayload(message, null);
        message.setReceivedOn(messageTime);

        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.SERVER_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        messageTime = new Date();
        message.setReceivedOn(messageTime);
        List<StorableId> messageStoredIds = insertMessages(message);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        MessageQuery messageQuery = getBaseMessageQuery(account.getId(), 100);
        setMessageQueryBaseCriteria(messageQuery, null);

        MessageListResult result = MESSAGE_STORE_SERVICE.query(messageQuery);
        DatastoreMessage messageQueried = checkMessagesCount(result, 1);
        checkMessageId(messageQueried, messageStoredIds.get(0));
        checkTopic(messageQueried, topicSemanticPart);
        checkMessageBody(messageQueried, null);
        checkMetricsSize(messageQueried, 0);
        checkPosition(messageQueried, null);
        checkMessageDate(messageQueried,
                new Range<>("timestamp", messageTime, new Date(messageTime.getTime() + 10000)),
                new Range<>("sentOn", sentOn),
                new Range<>("capturedOn", capturedOn),
                new Range<>("receivedOn", messageTime));
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "ci_client_by_account_client1", "ci_client_by_account_client2", "ci_client_by_account_client3", "ci_client_by_account_client4" };
        String[] semanticTopic = new String[] { "ci_client_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[2], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[0]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message4, semanticTopic[0]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = createMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message5, semanticTopic[0]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = createMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message6, semanticTopic[0]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4, message5, message6);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, null);

        ChannelInfoListResult channelList = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "ci_client_by_pd_by_account_client1", "ci_client_by_pd_by_account_client2" };
        String[] semanticTopic = new String[] { "ci_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        setChannel(message3, semanticTopic[0]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        setChannel(message4, semanticTopic[0]);
        message4.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, new DateRange(messageTime, capturedOnThirdMessage));

        Thread.sleep(2*PUBLISH_DATE_TEST_CHECK_TIME_WINDOW); // wait for messages because of TTL filter
        ChannelInfoListResult channelList = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "ci_topic_by_account_client1", "ci_topic_by_account_client2" };
        String[] semanticTopic = new String[] { "ci_topic_by_account/1/2/3", "ci_topic_by_account/1/2/4", "ci_topic_by_account/1/2/5", "ci_topic_by_account/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[1]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[2]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message4, semanticTopic[3]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message5, semanticTopic[0]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message6, semanticTopic[1]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4, message5, message6);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, null);

        ChannelInfoListResult channelList = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "ci_topic_by_client_client1" };
        String[] semanticTopic = new String[] { "ci_topic_by_client/1/2/3", "ci_topic_by_client/1/2/4", "ci_topic_by_client/1/2/5", "ci_topic_by_client/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[1]);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[2]);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message4, semanticTopic[3]);
        message4.setReceivedOn(messageTime);
        KapuaDataMessage message5 = createMessage(clientIds[0] + "_NO", account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message5, semanticTopic[2]);
        message5.setReceivedOn(messageTime);
        KapuaDataMessage message6 = createMessage(clientIds[0] + "_NO", account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message6, semanticTopic[3]);
        message6.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4, message5, message6);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, clientIds[0], null);

        ChannelInfoListResult channelList = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_account_client1", "mi_client_by_account_client2" };
        String[] metrics = new String[] { "mi_client_by_account_metric1", "mi_client_by_account_metric2", "mi_client_by_account_metric3", "mi_client_by_account_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getMetrics().put(metrics[0], Double.valueOf(123));
        message1.getPayload().getMetrics().put(metrics[1], Integer.valueOf(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getMetrics().put(metrics[2], String.valueOf("123"));
        message2.getPayload().getMetrics().put(metrics[3], Boolean.valueOf(true));
        message2.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries
        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        // setMetricInfoQueryBaseCriteria(metricInfoQuery, null);

        MetricInfoListResult metricList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_pd_by_account_client1", "mi_client_by_pd_by_account_client2" };
        String[] metrics = new String[] { "mi_client_by_pd_by_account_metric1", "mi_client_by_pd_by_account_metric2", "mi_client_by_pd_by_account_metric3", "mi_client_by_pd_by_account_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getMetrics().put(metrics[0], Double.valueOf(123));
        message1.getPayload().getMetrics().put(metrics[1], Integer.valueOf(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getMetrics().put(metrics[2], "123");
        message2.getPayload().getMetrics().put(metrics[3], Boolean.TRUE);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        setChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.getPayload().getMetrics().put(metrics[2], "123");
        message3.getPayload().getMetrics().put(metrics[3], Boolean.TRUE);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        setChannel(message4, semanticTopic[0]);
        initMetrics(message4);
        message4.getPayload().getMetrics().put(metrics[2], "123");
        message4.getPayload().getMetrics().put(metrics[3], Boolean.TRUE);
        message4.setReceivedOn(messageTime);

        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        // Store messages
        insertMessages(message1, message2, message3, message4);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries
        Thread.sleep(2*PUBLISH_DATE_TEST_CHECK_TIME_WINDOW); // wait for messages because of TTL filter
        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, new DateRange(capturedOn, capturedOnThirdMessage));

        MetricInfoListResult metricList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "mi_client_by_client_client1", "mi_client_by_client_client2" };
        String[] metrics = new String[] { "mi_client_by_client_metric1", "mi_client_by_client_metric2", "mi_client_by_client_metric3", "mi_client_by_client_metric4" };
        String[] semanticTopic = new String[] { "mi_client_by_client/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.getPayload().getMetrics().put(metrics[0], Double.valueOf(123));
        message1.getPayload().getMetrics().put(metrics[1], Integer.valueOf(123));
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.getPayload().getMetrics().put(metrics[2], "123");
        message2.getPayload().getMetrics().put(metrics[3], Boolean.TRUE);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.getPayload().getMetrics().put(metrics[2], Double.valueOf(123));
        message3.getPayload().getMetrics().put(metrics[3], Integer.valueOf(123));
        message3.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3);

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, clientIds[0], null, null);

        MetricInfoListResult metricList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
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

        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientIds[0]);
        Device device1 = DEVICE_REGISTRY_SERVICE.create(deviceCreator);
        DeviceCreator deviceCreator2 = DEVICE_FACTORY.newCreator(account.getId(), clientIds[1]);
        Device device2 = DEVICE_REGISTRY_SERVICE.create(deviceCreator2);
        int messagesCount = 100;
        Date sentOn1 = new Date();
        Date sentOn2 = new Date(sentOn1.getTime() + 5000);
        Date capturedOn1 = new Date(new Date().getTime() + 1000);
        Date capturedOn2 = new Date(capturedOn1.getTime() + 1000);
        String clientId = null;
        Device device = null;

        // leave the message index by as default (DEVICE_TIMESTAMP)
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);

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
            if (i < messagesCount / 4 - 1 || i > messagesCount / 2 - 1 && i < messagesCount * 3 / 4 - 1) {
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
            KapuaDataMessage message = createMessage(clientId, account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message, semanticTopic[i % semanticTopic.length]);
            // insert metrics
            initMetrics(message);
            message.getPayload().getMetrics().put(metrics[0], metricsValuesDate[i % metricsValuesDate.length]);
            message.getPayload().getMetrics().put(metrics[1], metricsValuesString[i % metricsValuesString.length]);
            message.getPayload().getMetrics().put(metrics[2], metricsValuesInt[i % metricsValuesInt.length]);
            message.getPayload().getMetrics().put(metrics[3], metricsValuesFloat[i % metricsValuesFloat.length]);
            message.getPayload().getMetrics().put(metrics[4], metricsValuesBoolean[i % metricsValuesBoolean.length]);
            message.getPayload().getMetrics().put(metrics[5], metricsValuesDouble[i % metricsValuesDouble.length]);
            insertMessages(message);
        }

        // Refresh indices before querying
        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        List<OrderConstraint<?>> sort = new ArrayList<>();
        sort.add(orderConstraint(ascending(MetricInfoSchema.METRIC_MTR_NAME_FULL), String.class));

        MetricInfoQuery metricInfoQuery = getMetricInfoOrderedQuery(account.getId(), (6 + 1) * messagesCount, sort);
        setMetricInfoQueryBaseCriteria(metricInfoQuery, new DateRange(capturedOn1, capturedOn2));

        MetricInfoListResult metricList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
        checkMetricInfoClientIdsAndMetricNames(metricList, metrics.length * semanticTopic.length, new String[] { clientIds[0], clientIds[1] }, new String[] { metrics[0], metrics[1], metrics[2],
                metrics[3], metrics[4], metrics[5] });
        checkMetricDateBound(metricList, new Date(capturedOn1.getTime()), new Date(capturedOn2.getTime()));

        for (MetricInfo metricInfo : metricList.getItems()) {
            logger.debug("metric client id: '" + metricInfo.getClientId() + "' - channel: '" + metricInfo.getChannel() + "' metric name: '" + metricInfo.getName()
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "clii_client_by_account_client1", "clii_client_by_account_client2" };
        String[] semanticTopic = new String[] { "clii_client_by_account/1/2/3", "clii_client_by_account/1/2/4", "clii_client_by_account/1/2/5", "clii_client_by_account/1/2/6" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[1]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[2]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message4, semanticTopic[3]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4);

        // Refresh indices before querying

        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 100);
        setClientInfoQueryBaseCriteria(clientInfoQuery, null);

        ClientInfoListResult clientList = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "clii_client_by_pd_by_account_client1", "clii_client_by_pd_by_account_client2" };
        String[] semanticTopic = new String[] { "clii_client_by_pd_by_account/1/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date capturedOnSecondMessage = new Date(capturedOn.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date capturedOnThirdMessage = new Date(capturedOnSecondMessage.getTime() + PUBLISH_DATE_TEST_CHECK_TIME_WINDOW);
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[0]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnSecondMessage, sentOn);
        setChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOnThirdMessage, sentOn);
        setChannel(message4, semanticTopic[0]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4);

        // Refresh indices before querying

        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 100);
        setClientInfoQueryBaseCriteria(clientInfoQuery, new DateRange(capturedOn, capturedOnThirdMessage));

        Thread.sleep(2*PUBLISH_DATE_TEST_CHECK_TIME_WINDOW); // wait for messages because of TTL filter
        ClientInfoListResult clientList = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "clii_by_client_client1", "clii_by_client_client2", "clii_by_client_client3", "clii_by_client_client4" };
        String[] semanticTopic = new String[] { "clii_by_client/1/2/3", "clii_by_client/1/2/4" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message1, semanticTopic[0]);
        initMetrics(message1);
        message1.setReceivedOn(messageTime);
        KapuaDataMessage message2 = createMessage(clientIds[1], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message2, semanticTopic[1]);
        initMetrics(message2);
        message2.setReceivedOn(messageTime);
        KapuaDataMessage message3 = createMessage(clientIds[2], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message3, semanticTopic[0]);
        initMetrics(message3);
        message3.setReceivedOn(messageTime);
        KapuaDataMessage message4 = createMessage(clientIds[3], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
        setChannel(message4, semanticTopic[1]);
        initMetrics(message4);
        message4.setReceivedOn(messageTime);
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        insertMessages(message1, message2, message3, message4);

        // Refresh indices before querying

        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 100);
        setClientInfoQueryBaseCriteria(clientInfoQuery, clientIds[0], null);

        ClientInfoListResult clientList = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
        checkClientInfo(clientList, 1, new String[] { clientIds[0] });
    }

    @Test
    public void testTopicsByAccount()
            throws Exception {
        Account account = createAccount(null, null);
        Date messageTime = new Date();
        String clientId = String.format("device-%d", messageTime.getTime());
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        String[] clientIds = new String[] { "tba_client1" };
        String[] semanticTopic = new String[] { "tba_1/1/1/1", "tba_1/1/1/2", "tba_1/1/1/3", "tba_1/1/2/1", "tba_1/1/2/2", "tba_1/1/2/3",
                "tba_1/2/1/1", "tba_1/2/1/2", "tba_1/2/1/3", "tba_1/2/2/1", "tba_1/2/2/2", "tba_1/2/2/3",
                "tba_2/1/1/1", "tba_2/1/1/2", "tba_2/1/1/3", "tba_2/1/2/1", "tba_2/1/2/2", "tba_2/1/2/3",
                "tba_2/2/1/1", "tba_2/2/1/2", "tba_2/2/1/3", "tba_2/2/2/1", "tba_2/2/2/2", "tba_2/2/2/3" };
        Date sentOn = new Date(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2015").getTime());
        Date capturedOn = new Date();
        Date receivedOn = new Date();
        updateConfiguration(MESSAGE_STORE_SERVICE, account.getId(), account.getScopeId(), DataIndexBy.DEVICE_TIMESTAMP, MetricsIndexBy.TIMESTAMP, 30, true);
        for (String semanticTopicTmp : semanticTopic) {
            KapuaDataMessage message1 = createMessage(clientIds[0], account.getId(), device.getId(), receivedOn, capturedOn, sentOn);
            setChannel(message1, semanticTopicTmp);
            message1.setReceivedOn(messageTime);
            insertMessages(message1);
        }
        // Refresh indices before querying

        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        ChannelInfoListResult channelList = doChannelInfoQuery(account, clientIds[0], "1/#");
        checkChannelInfoClientIdsAndTopics(channelList, 0, null, null);

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_1/#");
        checkChannelInfoClientIdsAndTopics(channelList, 12, clientIds, Arrays.copyOfRange(semanticTopic, 0, 12));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_2/#");
        checkChannelInfoClientIdsAndTopics(channelList, 12, clientIds, Arrays.copyOfRange(semanticTopic, 12, 24));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_1/1/#");
        checkChannelInfoClientIdsAndTopics(channelList, 6, clientIds, Arrays.copyOfRange(semanticTopic, 0, 6));

        channelList = doChannelInfoQuery(account, clientIds[0], "tba_2/1/1/#");
        checkChannelInfoClientIdsAndTopics(channelList, 3, clientIds, Arrays.copyOfRange(semanticTopic, 12, 15));

    }

    // ===========================================================
    // ===========================================================
    // utility methods
    // ===========================================================
    // ===========================================================

    private List<StorableId> insertMessages(KapuaDataMessage... messages) throws InterruptedException {
        requireNonNull(messages);

        List<StorableId> storableIds = new ArrayList<>(messages.length);
        for (KapuaDataMessage message : messages) {
            try {
                storableIds.add(MESSAGE_STORE_SERVICE.store(message));
            } catch (Exception e) {
                logger.error("Message insert exception!", e);
                fail("Store messages should have succeded");
            }
        }
        return storableIds;
    }

    /**
     * This method should create a new account for the test (temp implementation that return always the default kapua-sys account)
     *
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
     * @param capturedOn
     * @param sentOn
     * @return
     */
    private KapuaDataMessage createMessage(String clientId, KapuaId scopeId, KapuaId deviceId,
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
    private void setChannel(KapuaDataMessage message, String semanticPart) {
        final KapuaDataChannelImpl channel = new KapuaDataChannelImpl();
        channel.setSemanticParts(new ArrayList<>(Arrays.asList(semanticPart.split("/"))));

        message.setChannel(channel);
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

    private Map<String, Object> getMetrics(KapuaDataMessage message, int profile) {
        Map<String, Object> metrics = new HashMap<String, Object>();
        for (int i = 0; i < 6; i++) {
            metrics.put("metric_" + i, getMetricValue(profile + i));
        }
        return metrics;
    }

    private Object getMetricValue(int number) {
        int choiche = number % 6;
        switch (choiche) {
        case 0:
            return new Double(1 * Math.E);
        case 1:
            return new Float(2);
        case 2:
            return new Integer(3);
        case 3:
            return new Long(4);
        case 4:
            return new String("string_value");
        default:
            return new Date();
        }
    }

    //
    // Utility methods to help to to create message queries
    //

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Message schema
     *
     * @return
     */
    private MessageQuery getBaseMessageQuery(KapuaId scopeId, int limit) {
        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(MessageSchema.MESSAGE_TIMESTAMP));
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
        List<SortField> order = new ArrayList<>();
        order.add(descending(ChannelInfoSchema.CHANNEL_TIMESTAMP));
        query.setSortFields(order);
        return query;
    }

    private ChannelInfoListResult doChannelInfoQuery(Account account, String clientId, String channelFilter) throws KapuaException {
        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        channelInfoQuery.setLimit(100);
        DatastoreChannel datastoreChannel = new DatastoreChannel(channelFilter);
        setChannelInfoQueryChannelPredicateCriteria(channelInfoQuery, clientId, datastoreChannel, null);
        return CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
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
        query.setLimit(100);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(MetricInfoSchema.METRIC_MTR_TIMESTAMP));
        query.setSortFields(order);
        return query;
    }

    /**
     * Creates a new query setting the default base parameters (fetch style, sort, limit, offset, ...) for the Client Info schema
     *
     * @return
     */
    private ClientInfoQuery getBaseClientInfoQuery(KapuaId scopeId, int limit) {
        ClientInfoQuery query = new ClientInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(10);
        query.setOffset(0);
        List<SortField> order = new ArrayList<>();
        order.add(descending(ClientInfoSchema.CLIENT_TIMESTAMP));
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
    private MessageQuery getMessageOrderedQuery(KapuaId scopeId, int limit, List<OrderConstraint<?>> order) {
        MessageQuery query = new MessageQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        query.setSortFields(order.stream().map(OrderConstraint::getField).collect(Collectors.toList()));
        return query;
    }

    /**
     * Set the query account and message timestamp filter
     *
     * @param messageQuery
     * @param dateRange
     */
    private void setMessageQueryBaseCriteria(MessageQuery messageQuery, DateRange dateRange) {
        setMessageQueryBaseCriteria(messageQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param messageQuery
     * @param clientId
     * @param dateRange
     */
    private void setMessageQueryBaseCriteria(MessageQuery messageQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MessageField.CLIENT_ID, clientId);
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
     * @param dateRange
     */
    private void setChannelInfoQueryBaseCriteria(ChannelInfoQuery channelInfoQuery, DateRange dateRange) {
        setChannelInfoQueryBaseCriteria(channelInfoQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param channelInfoQuery
     * @param clientId
     * @param dateRange
     */
    private void setChannelInfoQueryBaseCriteria(ChannelInfoQuery channelInfoQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ChannelInfoField.CLIENT_ID, clientId);
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
     */
    private void setChannelInfoQueryChannelPredicateCriteria(ChannelInfoQuery channelInfoQuery, String clientId, DatastoreChannel channelPredicate, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ChannelInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(ChannelInfoField.TIMESTAMP, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        ChannelMatchPredicate channelMatchPredicate = new ChannelMatchPredicateImpl(channelPredicate.getChannelCleaned());
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
    private MetricInfoQuery getMetricInfoOrderedQuery(KapuaId scopeId, int limit, List<OrderConstraint<?>> order) {
        MetricInfoQuery query = new MetricInfoQueryImpl(scopeId);
        query.setAskTotalCount(true);
        query.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        query.setLimit(limit);
        query.setOffset(0);
        query.setSortFields(order.stream().map(OrderConstraint::getField).collect(Collectors.toList()));
        return query;
    }

    /**
     * Set the query account and message timestamp id filter
     *
     * @param metricInfoQuery
     * @param dateRange
     */
    private void setMetricInfoQueryBaseCriteria(MetricInfoQuery metricInfoQuery, DateRange dateRange) {
        setMetricInfoQueryBaseCriteria(metricInfoQuery, null, null, dateRange);
    }

    /**
     * Set the query account and message timestamp id filter
     *
     * @param metricInfoQuery
     */
    private void setMetricInfoQueryBaseCriteria(MetricInfoQuery metricInfoQuery, String channel) {
        setMetricInfoQueryBaseCriteria(metricInfoQuery, null, channel, null);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param metricInfoQuery
     * @param clientId
     * @param dateRange
     */
    private void setMetricInfoQueryBaseCriteria(MetricInfoQuery metricInfoQuery, String clientId, String channel, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MetricInfoField.CLIENT_ID, clientId);
            andPredicate.getPredicates().add(clientIdPredicate);
        }
        if (dateRange != null) {
            RangePredicate timestampPredicate = new RangePredicateImpl(MetricInfoField.TIMESTAMP_FULL, dateRange.getLowerBound(), dateRange.getUpperBound());
            andPredicate.getPredicates().add(timestampPredicate);
        }
        if (channel != null) {
            TermPredicate channelPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(MetricInfoField.CHANNEL, channel);
            andPredicate.getPredicates().add(channelPredicate);
        }
        metricInfoQuery.setPredicate(andPredicate);
    }

    /**
     * Set the query account and message timestamp filter
     *
     * @param clientInfoQuery
     * @param dateRange
     */
    private void setClientInfoQueryBaseCriteria(ClientInfoQuery clientInfoQuery, DateRange dateRange) {
        setClientInfoQueryBaseCriteria(clientInfoQuery, null, dateRange);
    }

    /**
     * Set the query account, message timestamp and client id filter
     *
     * @param clientInfoQuery
     * @param clientId
     * @param dateRange
     */
    private void setClientInfoQueryBaseCriteria(ClientInfoQuery clientInfoQuery, String clientId, DateRange dateRange) {
        AndPredicate andPredicate = new AndPredicateImpl();
        if (!StringUtils.isEmpty(clientId)) {
            TermPredicate clientIdPredicate = STORABLE_PREDICATE_FACTORY.newTermPredicate(ClientInfoField.CLIENT_ID, clientId);
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
            assertEquals("Query result has a wrong size!", messagesCount, result.getTotalCount().intValue());
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
            assertNotNull("Message id is null", message.getDatastoreId());
            assertEquals("Message id doesn't match", storableId.toString(), message.getDatastoreId().toString());
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
        assertNotNull("Message prorperties are null!", message.getPayload().getMetrics());
    }

    /**
     * Check if the queried message has the correct metrics size
     *
     * @param message
     * @param metricsSize
     */
    private void checkMetricsSize(DatastoreMessage message, int metricsSize) {
        if (metricsSize < 0) {
            assertNull("Message metrics is not null!", message.getPayload().getMetrics());
        } else {
            assertNotNull("Message metrics shouldn't be null!", message.getPayload().getMetrics());
            assertEquals("Message metrics size doesn't match!", metricsSize, message.getPayload().getMetrics().size());
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
        Map<String, Object> messageProperties = message.getPayload().getMetrics();
        Iterator<String> metricsKeys = metrics.keySet().iterator();
        while (metricsKeys.hasNext()) {
            String key = metricsKeys.next();
            logger.debug("metric retrieved type '{}' - value '{}' - original metric type '{}' - value '{}' ",
                    metrics.get(key) != null ? metrics.get(key).getClass() : "null", metrics.get(key),
                    messageProperties.get(key) != null ? messageProperties.get(key).getClass() : "null", messageProperties.get(key));
            assertEquals(String.format("Metric type is different for metric [%s]!", key),
                    metrics.get(key) != null ? metrics.get(key).getClass() : "null",
                    messageProperties.get(key) != null ? messageProperties.get(key).getClass() : "null");
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
     */
    private void checkChannelInfoClientIdsAndTopics(ChannelInfoListResult result, int clientInfoCount, String[] clientIds, String[] topics) {
        checkChannelInfoCount(result, clientInfoCount);
        Set<String> allClientId = new HashSet<>();
        Set<String> allTopics = new HashSet<>();
        for (ChannelInfo channelInfo : result.getItems()) {
            allClientId.add(channelInfo.getClientId());
            allTopics.add(channelInfo.getName());
        }
        assertEquals("Wrong client ids size!", clientIds != null ? clientIds.length : 0, allClientId.size());
        assertEquals("Wrong topics size!", topics != null ? topics.length : 0, allTopics.size());
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
     */
    private void checkMetricInfoClientIdsAndMetricNames(MetricInfoListResult result, int metricInfoCount, String[] clientIds, String[] metrics) {
        checkMetricInfoCount(result, metricInfoCount);
        Set<String> allClientId = new HashSet<>();
        Set<String> allMetrics = new HashSet<>();
        for (MetricInfo metricInfo : result.getItems()) {
            allClientId.add(metricInfo.getClientId());
            allMetrics.add(metricInfo.getName());
        }
        assertEquals("Wrong client ids size!", clientIds != null ? clientIds.length : 0, allClientId.size());
        assertEquals("Wrong metrics size!", metrics != null ? metrics.length : 0, allMetrics.size());
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
        Set<String> allClientId = new HashSet<>();
        for (ClientInfo clientInfo : result.getItems()) {
            allClientId.add(clientInfo.getClientId());
        }
        assertEquals("Wrong client ids size!", clientIds != null ? clientIds.length : 0, allClientId.size());
        if (clientIds != null) {
            for (String clientIdFound : clientIds) {
                assertTrue(String.format("Cannot find the client [%s] in the client ids list!", clientIdFound), allClientId.contains(clientIdFound));
            }
        }
    }

    private void checkMetricDateBound(MetricInfoListResult result, Date startDate, Date endDate) {
        for (MetricInfo metric : result.getItems()) {
            if (metric.getLastMessageOn() == null) {
                logger.debug(String.format("Metric '[%s]' lastMessageOn is NULL", metric.getName()));
                continue;
            } else {
                assertTrue(String.format("Metric '[%s]' date is after upper bound", metric.getName()), !metric.getLastMessageOn().after(endDate));
                assertTrue(String.format("Metric '[%s]' date is before lower bound", metric.getName()), !metric.getLastMessageOn().before(startDate));
            }
        }
    }

    private void checkMessagesDateBound(MessageListResult result, Date startDate, Date endDate) {
        for (DatastoreMessage message : result.getItems()) {
            assertTrue(String.format("Message '[%s]' date is after upper bound", message.getDatastoreId()), !message.getTimestamp().after(endDate));
            assertTrue(String.format("Message '[%s]' date is before lower bound", message.getDatastoreId()), !message.getTimestamp().before(startDate));
        }
    }

    private void fullMessageCheck(Account account, DatastoreMessage messageToCheck, KapuaDataMessage correctMessage, StorableId correctId, StorableId firstPublishedMessage, Date firstPublishedOn)
            throws KapuaException {
        checkMessageId(messageToCheck, correctId);
        checkTopic(messageToCheck, correctMessage.getChannel().toString());
        checkMessageBody(messageToCheck, correctMessage.getPayload().getBody());
        checkMetricsSize(messageToCheck, correctMessage.getPayload().getMetrics().size());
        checkMetrics(messageToCheck, correctMessage.getPayload().getMetrics());
        checkPosition(messageToCheck, correctMessage.getPosition());
        checkMessageDate(messageToCheck, new Range<Date>("timestamp", correctMessage.getCapturedOn()), new Range<Date>("sentOn", correctMessage.getSentOn()),
                new Range<Date>("capturedOn", correctMessage.getCapturedOn()),
                new Range<Date>("receivedOn", correctMessage.getReceivedOn()));
        checkChannelFirstMessageIdAndDate(account, correctMessage.getClientId(), firstPublishedMessage, firstPublishedOn);
        checkClientFirstMessageIdAndDate(account, correctMessage.getClientId(), firstPublishedMessage, firstPublishedOn);
        checkMetricFirstMessageIdAndDate(account, correctMessage.getClientId(), firstPublishedMessage, firstPublishedOn);
    }

    private void checkChannelFirstMessageIdAndDate(Account account, String clientId, StorableId firstPublishedMessage, Date firstPublishedOn) throws KapuaException {
        ChannelInfoQuery channelInfoQuery = getBaseChannelInfoQuery(account.getId());
        setChannelInfoQueryBaseCriteria(channelInfoQuery, clientId, null);
        ChannelInfoListResult channelInfoList = CHANNEL_INFO_REGISTRY_SERVICE.query(channelInfoQuery);
        assertNotNull("Cannot find the channel info registry!", channelInfoList);
        assertNotNull("Cannot find the channel info registry!", channelInfoList.getFirstItem());
        assertEquals("Wrong channel info message id!", channelInfoList.getFirstItem().getFirstMessageId(), firstPublishedMessage);
        assertEquals("Wrong channel info message on!", channelInfoList.getFirstItem().getFirstMessageOn(), firstPublishedOn);
    }

    private void checkClientFirstMessageIdAndDate(Account account, String clientId, StorableId firstPublishedMessage, Date firstPublishedOn) throws KapuaException {
        ClientInfoQuery clientInfoQuery = getBaseClientInfoQuery(account.getId(), 100);
        setClientInfoQueryBaseCriteria(clientInfoQuery, clientId, null);
        ClientInfoListResult clientInfoList = CLIENT_INFO_REGISTRY_SERVICE.query(clientInfoQuery);
        assertNotNull("Cannot find the client info registry!", clientInfoList);
        assertNotNull("Cannot find the client info registry!", clientInfoList.getFirstItem());
        assertEquals("Wrong client info message id!", clientInfoList.getFirstItem().getFirstMessageId(), firstPublishedMessage);
        assertEquals("Wrong client info message on!", clientInfoList.getFirstItem().getFirstMessageOn(), firstPublishedOn);
    }

    private void checkMetricFirstMessageIdAndDate(Account account, String clientId, StorableId firstPublishedMessage, Date firstPublishedOn) throws KapuaException {
        MetricInfoQuery metricInfoQuery = getBaseMetricInfoQuery(account.getId());
        setMetricInfoQueryBaseCriteria(metricInfoQuery, clientId, null, null);
        MetricInfoListResult metricInfoList = METRIC_INFO_REGISTRY_SERVICE.query(metricInfoQuery);
        assertNotNull("Cannot find the metric info registry!", metricInfoList);
        assertNotNull("Cannot find the metric info registry!", metricInfoList.getFirstItem());
        assertEquals("Wrong metric info message id!", metricInfoList.getFirstItem().getFirstMessageId(), firstPublishedMessage);
        assertEquals("Wrong metric info message on!", metricInfoList.getFirstItem().getFirstMessageOn(), firstPublishedOn);
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

        private final Date lowerBound;
        private final Date upperBound;

        public DateRange(Date lowerBound, Date upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
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
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        KapuaDataMessageImpl message = new KapuaDataMessageImpl();
        KapuaDataChannelImpl channel = new KapuaDataChannelImpl();
        KapuaDataPayloadImpl messagePayload = new KapuaDataPayloadImpl();
        KapuaPositionImpl messagePosition = new KapuaPositionImpl();
        Map<String, Object> metrics = new HashMap<>();

        channel.setClientId(device.getClientId());
        channel.setSemanticParts(Arrays.asList("APP01"));

        message.setScopeId(account.getId());
        message.setDeviceId(device.getId());
        message.setCapturedOn(now);
        message.setReceivedOn(now);
        message.setChannel(channel);

        metrics.put("distance", 1L);
        metrics.put("label", "goofy");
        messagePayload.setMetrics(metrics);

        messagePosition.setAltitude(1.0);
        messagePosition.setTimestamp(now);
        message.setPosition(messagePosition);

        messagePayload.setMetrics(metrics);
        message.setPayload(messagePayload);
        message.setClientId(clientId);

        StorableId messageId = MESSAGE_STORE_SERVICE.store(message);
        //
        // A non empty message id must be returned
        assertNotNull(messageId);
        assertTrue(!messageId.toString().isEmpty());

        // Refresh indices before querying

        DatastoreMediator.getInstance().refreshAllIndexes();

        // start queries

        //
        // Retrieve the message from its id
        DatastoreMessage retrievedMessage = MESSAGE_STORE_SERVICE.find(account.getId(), messageId, StorableFetchStyle.SOURCE_FULL);

        //
        // The returned message must be not null and values must coincide
        assertNotNull(retrievedMessage);
        assertTrue(messageId.equals(retrievedMessage.getDatastoreId()));
        assertTrue(account.getId().equals(retrievedMessage.getScopeId()));
        assertTrue(device.getId().equals(retrievedMessage.getDeviceId()));
        assertTrue(device.getClientId().equals(retrievedMessage.getClientId()));

        TermPredicate equalsMessageId = STORABLE_PREDICATE_FACTORY.newTermPredicate(ClientInfoField.MESSAGE_ID, messageId);

        ClientInfoQuery clientInfoQuery = DATASTORE_OBJECT_FACTORY.newClientInfoQuery(account.getId());
        clientInfoQuery.setOffset(0);
        clientInfoQuery.setLimit(1);
        clientInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        clientInfoQuery.setPredicate(equalsMessageId);

        ClientInfoRegistryService clientInfoRegistry = KapuaLocator.getInstance().getService(ClientInfoRegistryService.class);
        ClientInfoListResult clientInfos = clientInfoRegistry.query(clientInfoQuery);

        assertNotNull(clientInfos);
        assertEquals("Wrong client info registry size", 1, clientInfos.getSize());

        ClientInfo clientInfo = clientInfos.getItem(0);

        assertNotNull(clientInfo);
        assertTrue(messageId.equals(clientInfo.getFirstMessageId()));

        // There must be a channel info entry in the registry
        equalsMessageId = STORABLE_PREDICATE_FACTORY.newTermPredicate(ChannelInfoField.MESSAGE_ID, messageId);

        ChannelInfoQuery channelInfoQuery = DATASTORE_OBJECT_FACTORY.newChannelInfoQuery(account.getId());
        channelInfoQuery.setOffset(0);
        channelInfoQuery.setLimit(1);
        channelInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        channelInfoQuery.setPredicate(equalsMessageId);

        ChannelInfoRegistryService channelInfoRegistry = KapuaLocator.getInstance().getService(ChannelInfoRegistryService.class);
        ChannelInfoListResult channelInfos = channelInfoRegistry.query(channelInfoQuery);

        assertNotNull(channelInfos);
        assertEquals("Wrong client info registry size", 1, channelInfos.getSize());

        ChannelInfo channelInfo = channelInfos.getItem(0);

        assertNotNull(channelInfo);
        assertTrue(messageId.equals(channelInfo.getFirstMessageId()));

        // There must be two metric info entries in the registry
        equalsMessageId = STORABLE_PREDICATE_FACTORY.newTermPredicate(MetricInfoField.MESSAGE_ID_FULL, messageId);

        MetricInfoQuery metricInfoQuery = DATASTORE_OBJECT_FACTORY.newMetricInfoQuery(account.getId());
        metricInfoQuery.setOffset(0);
        metricInfoQuery.setLimit(2);
        metricInfoQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        metricInfoQuery.setPredicate(equalsMessageId);

        MetricInfoRegistryService metricInfoRegistry = KapuaLocator.getInstance().getService(MetricInfoRegistryService.class);
        MetricInfoListResult metricInfos = metricInfoRegistry.query(metricInfoQuery);

        assertNotNull(metricInfos);
        assertEquals("Wrong client info registry size", 2, metricInfos.getSize());

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
