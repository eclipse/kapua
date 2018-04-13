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
package org.eclipse.kapua.service.datastore.internal;

import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.message.device.data.KapuaDataPayload;
import org.eclipse.kapua.message.internal.device.data.KapuaDataChannelImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataMessageImpl;
import org.eclipse.kapua.message.internal.device.data.KapuaDataPayloadImpl;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageStoreServiceSslTest extends AbstractMessageStoreServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageStoreServiceSslTest.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);
    private static final MessageStoreService MESSAGE_STORE_SERVICE = LOCATOR.getService(MessageStoreService.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

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
    // @Before
    // public void deleteAllIndices() throws Exception {
    // DatastoreMediator.getInstance().deleteAllIndexes();
    // }


    @Test(expected = DatastoreException.class)
    @Ignore
    public void connectNoSsl() throws InterruptedException, KapuaException, ParseException {
        // datastore.elasticsearch.ssl.enabled=false
        // datastore.elasticsearch.ssl.trust_server_certificate=false
        // datastore.elasticsearch.ssl.keystore_path=
        // datastore.elasticsearch.ssl.keystore_password=
        // datastore.elasticsearch.ssl.truststore_path=
        // datastore.elasticsearch.ssl.truststore_password=
        // datastore.elasticsearch.ssl.keystore_type=jks
        try {
            DatastoreClientFactory.getInstance();
            storeMessage("ssl_test/no_ssl");
            fail("ClientException should be thrown!");
        } catch (ClientException e) {
            // good
        } finally {
            DatastoreClientFactory.close();
        }
    }

    @Test
    @Ignore
    public void connectSsl() throws InterruptedException, KapuaException, ParseException {
        // datastore.elasticsearch.ssl.enabled=true
        // datastore.elasticsearch.ssl.trust_server_certificate=false
        // datastore.elasticsearch.ssl.keystore_path=
        // datastore.elasticsearch.ssl.keystore_password=
        // datastore.elasticsearch.ssl.truststore_path=
        // datastore.elasticsearch.ssl.truststore_password=
        // datastore.elasticsearch.ssl.keystore_type=jks
        try {
            DatastoreClientFactory.getInstance();
            storeMessage("ssl_test/ssl");
        } catch (ClientException e) {
            fail("No ClientException should be thrown!");
        } finally {
            DatastoreClientFactory.close();
        }
    }

    @Test
    @Ignore
    public void connectSslTrustServerNoTrustStoreSet() throws InterruptedException, KapuaException, ParseException {
        // datastore.elasticsearch.ssl.enabled=true
        // datastore.elasticsearch.ssl.trust_server_certificate=true
        // datastore.elasticsearch.ssl.keystore_path=
        // datastore.elasticsearch.ssl.keystore_password=
        // datastore.elasticsearch.ssl.truststore_path=
        // datastore.elasticsearch.ssl.truststore_password=
        // datastore.elasticsearch.ssl.keystore_type=jks
        try {
            DatastoreClientFactory.getInstance();
            storeMessage("ssl_test/ssl_trust_server_no_trust_store_set");
        } catch (ClientException e) {
            fail("No ClientException should be thrown!");
        } finally {
            DatastoreClientFactory.close();
        }
    }

    @Test
    @Ignore
    public void connectSslTrustServerTrustStoreSet() throws InterruptedException, KapuaException, ParseException {
        // datastore.elasticsearch.ssl.enabled=false
        // datastore.elasticsearch.ssl.trust_server_certificate=false
        // datastore.elasticsearch.ssl.keystore_path=
        // datastore.elasticsearch.ssl.keystore_password=
        // datastore.elasticsearch.ssl.truststore_path=some valid truststore path
        // datastore.elasticsearch.ssl.truststore_password=trust store password
        // datastore.elasticsearch.ssl.keystore_type=jks
        try {
            DatastoreClientFactory.getInstance();
            storeMessage("ssl_test/ssl_trust_server_default_trust_store_set");
        } catch (ClientException e) {
            fail("No ClientException should be thrown!");
        } finally {
            DatastoreClientFactory.close();
        }
    }

    @Test(expected = DatastoreException.class)
    @Ignore
    public void connectSslTrustServerSelfSignedTrustStore() throws InterruptedException, KapuaException, ParseException {
        // datastore.elasticsearch.ssl.enabled=false
        // datastore.elasticsearch.ssl.trust_server_certificate=false
        // datastore.elasticsearch.ssl.keystore_path=
        // datastore.elasticsearch.ssl.keystore_password=
        // datastore.elasticsearch.ssl.truststore_path=self signed trust store
        // datastore.elasticsearch.ssl.truststore_password=password
        // datastore.elasticsearch.ssl.keystore_type=jks
        try {
            DatastoreClientFactory.getInstance();
            storeMessage("ssl_test/ssl_trust_server_self_signed_tust");
        } catch (ClientException e) {
            fail("No ClientException should be thrown!");
        } finally {
            DatastoreClientFactory.close();
        }
    }

    private void storeMessage(String semanticTopic) throws InterruptedException, KapuaException, ParseException {
        Account account = getTestAccountCreator(adminScopeId);

        String clientId = String.format("device-%d", new Date().getTime());
        DeviceCreator deviceCreator = DEVICE_FACTORY.newCreator(account.getId(), clientId);
        Device device = DEVICE_REGISTRY_SERVICE.create(deviceCreator);

        // leave the message index by as default (DEVICE_TIMESTAMP)
        String stringPayload = "Hello delete by date message!";
        byte[] payload = stringPayload.getBytes();

        Instant currentInstant = KapuaDateUtils.getKapuaSysDate();
        Date date = Date.from(currentInstant);
        int messagesCount = 1;
        insertMessage(account, clientId, device.getId(), semanticTopic, payload, date);

        // do a first query count
        DatastoreMediator.getInstance().refreshAllIndexes();
        MessageQuery messageQuery = getBaseMessageQuery(KapuaEid.ONE, 10);
        setMessageQueryBaseCriteria(messageQuery, clientId, new DateRange(Date.from(currentInstant.minusSeconds(3600)), date));
        long count = MESSAGE_STORE_SERVICE.count(messageQuery);
        assertEquals(messagesCount, count);

    }

    private KapuaDataMessage insertMessage(Account account, String clientId, KapuaId deviceId, String semanticTopic, byte[] payload, Date sentOn) throws InterruptedException, KapuaException {
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

    // ===========================================================
    // ===========================================================
    // utility methods
    // ===========================================================
    // ===========================================================

    private List<StorableId> insertMessages(KapuaDataMessage... messages) throws InterruptedException, KapuaException {
        Objects.requireNonNull(messages);

        List<StorableId> storableIds = new ArrayList<>(messages.length);
        for (KapuaDataMessage message : messages) {
            storableIds.add(MESSAGE_STORE_SERVICE.store(message));
        }
        return storableIds;
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
        order.add(SortField.descending(MessageSchema.MESSAGE_TIMESTAMP));
        query.setSortFields(order);
        return query;
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
