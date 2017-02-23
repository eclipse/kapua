/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat - refactoring
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import static java.util.concurrent.TimeUnit.DAYS;

import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDatastoreException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMetric;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMetricDocumentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.KapuaInvalidTopicException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.KapuaTopic;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.LocalServicePlan;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsAssetDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsMessageDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsMetricDAO;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsTopicDAO;
import org.eclipse.kapua.service.datastore.internal.model.DataIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.MessageImpl;
import org.eclipse.kapua.service.datastore.internal.model.MessageListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricsIndexBy;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AssetInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MetricInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TopicInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.TopicMatchPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.model.AssetInfoListResult;
import org.eclipse.kapua.service.datastore.model.Message;
import org.eclipse.kapua.service.datastore.model.MessageCreator;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.TopicInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.concurrent.TimeUnit.DAYS;

@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService
{
    private static final long    serialVersionUID  = 4142282449826005424L;

    // @SuppressWarnings("unused")
    private static final Logger  logger            = LoggerFactory.getLogger(MessageStoreServiceImpl.class);

    private static final KapuaLocator locator = KapuaLocator.getInstance();

	private static final long DAY_SECS = DAYS.toSeconds(1);
	private static final long DAY_MILLIS = DAYS.toMillis(1);
	private static final long DATASTORE_TTL_DAYS = 30;
	private static final long DATASTORE_TTL_SECS = DATASTORE_TTL_DAYS * DAY_SECS;

    private final AccountService accountService;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;

    private static final Domain datastoreDomain = new DatastoreDomain();

    private final EsSchema       esSchema;
    private final int            maxTopicDepth;

    private final Object         metadataUpdateSync;

    public MessageStoreServiceImpl(AccountService accountService, AuthorizationService authorizationService,
                                      PermissionFactory permissionFactory, EsSchema esSchema,
                                      int maxTopicDepth, Object metadataUpdateSync) {

        super(MessageStoreService.class.getName(), datastoreDomain, DatastoreEntityManagerFactory.getInstance());
        this.accountService = accountService;
        this.authorizationService = authorizationService;
        this.permissionFactory = permissionFactory;
        this.esSchema = esSchema;
        this.maxTopicDepth = maxTopicDepth;
        this.metadataUpdateSync = metadataUpdateSync;
    }

    public MessageStoreServiceImpl() {
        this(locator.getService(AccountService.class), locator.getService(AuthorizationService.class),
                locator.getFactory(PermissionFactory.class), new EsSchema(),
                DatastoreSettings.getInstance().getInt(DatastoreSettingKey.CONFIG_TOPIC_MAX_DEPTH), new Object());
    }

    @Override
    public StorableId store(KapuaId scopeId, MessageCreator messageCreator)
            throws KapuaException {
        ArgumentValidator.notNull(messageCreator, "messageCreator");

        MessageImpl messageImpl = new MessageImpl();
        messageImpl.setPayload(messageCreator.getPayload());
        messageImpl.setReceivedOn(messageCreator.getReceivedOn());
        messageImpl.setTimestamp(messageCreator.getTimestamp());
        messageImpl.setTopic(messageCreator.getTopic());

        return this.store(scopeId, messageImpl);
    }

    private StorableId store(KapuaId scopeId, Message message)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(message, "message");

        //
        // Check Access
        // TODO temporary
        this.checkDataAccess(scopeId, Actions.write);

        if (message.getPayload() == null)
            throw new KapuaException(KapuaErrorCodes.ILLEGAL_ARGUMENT); // TODO find a better exception

        //
        // Store the message
        //
        // Check Storage Access
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_SECS;
        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            String msg = String.format("Storage not enabled for account %s, not storing message", scopeName);
            logger.debug(msg);
            throw KapuaException.internalError(msg);
        }

        // If eurotech set ttl equals to 1 month
        if (ttl < 0) {
            ttl = DATASTORE_TTL_SECS;
        }

        // MessageDAO: prepare the store parameters
        Date capturedOn = null;
        if (message.getPayload() != null) {
            capturedOn = message.getPayload().getCollectedOn();
        }

        long currentDate = KapuaDateUtils.getKapuaSysDate().getTime();

        long receivedOn = currentDate;
        if (message.getTimestamp() != null)
            receivedOn = message.getTimestamp().getTime();

        // Overwrite timestamp if necessary
        // Use the account service plan to determine whether we will give precede to the device time
        long indexedOn = currentDate;
        if (DataIndexBy.DEVICE_TIMESTAMP.equals(accountServicePlan.getDataIndexBy()) && capturedOn != null)
            indexedOn = capturedOn.getTime();

        // MessageDAO: query
        try {

            return this.storeMessage(scopeName,
                                     message,
                                     maxTopicDepth,
                                     indexedOn,
                                     receivedOn,
                                     ttl,
                                     accountServicePlan.getMetricsIndexBy());
        } catch (Exception e) { // TODO create e new datastore exception
            // TODO manage execeptions
            // CassandraUtils.handleException(e);
            // TODO Remove
            e.printStackTrace();
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.delete);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeName);
            return;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            EsMessageDAO.connection(EsClient.getcurrent())
                        .instance(everyIndex, EsSchema.MESSAGE_TYPE_NAME)
                        .deleteById(id.toString());
        } catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    @Override
    public Message find(KapuaId scopeId, StorableId id, MessageFetchStyle fetchStyle)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        ArgumentValidator.notNull(fetchStyle, "fetchStyle");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);

        throw KapuaException.internalError("Method not implemented.");
    }

    @Override
    public MessageListResult query(KapuaId scopeId, MessageQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeName);
            return new MessageListResultImpl();
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            MessageListResult result = null;
            result = EsMessageDAO.connection(EsClient.getcurrent())
                                 .instance(everyIndex, EsSchema.MESSAGE_TYPE_NAME)
                                 .query(query);

            return result;
        } catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    public long count(KapuaId scopeId, MessageQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeName);
            return 0;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            long result;
            result = EsMessageDAO.connection(EsClient.getcurrent())
                                 .instance(everyIndex, EsSchema.MESSAGE_TYPE_NAME)
                                 .count(query);

            return result;
        } catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    @Override
    public void delete(KapuaId scopeId, MessageQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.delete);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", scopeName);
            return;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            EsMessageDAO.connection(EsClient.getcurrent())
                        .instance(everyIndex, EsSchema.MESSAGE_TYPE_NAME)
                        .deleteByQuery(query);

            return;
        } catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        //
        // Check Access

        // TODO add enum for actions
        // Permission permission = permissionFactory.newPermission(DatastoreDomain.data, action, scopeId);
        // authorizationService.checkPermission(permission);
        //
        // Account account = accountService.find(scopeId);
        // return account.getName();
        if (false)
            throw KapuaException.internalError("Impossible exception");
    }

    private AccountInfo getAccountServicePlan(KapuaId scopeId)
            throws KapuaException {
        Account account = accountService.find(scopeId);
        return new AccountInfo(account, new LocalServicePlan(this.getConfigValues(account.getId())));
    }

    private StorableId storeMessage(String accountName,
                                    Message message,
                                    int maxTopicDepth,
                                    long indexedOn,
                                    long receivedOn,
                                    long ttl,
                                    MetricsIndexBy indexBy)
            throws IOException, ParseException, EsDatastoreException, KapuaInvalidTopicException {

        // Extract schema metadata
        EsSchema.Metadata schemaMetadata = this.esSchema.synch(accountName, indexedOn);

        Date indexedOnDt = new Date(indexedOn);
        Date receivedOnDt = new Date(receivedOn);

        StorableId messageId;
        if (message.getId() != null)
            messageId = message.getId();
        else {
            UUID uuid = UUID.randomUUID();
            messageId = new StorableIdImpl(uuid.toString());
        }

        // Parse document
        EsDocumentBuilder docBuilder = new EsDocumentBuilder();
        docBuilder.build(accountName, messageId, message, indexedOnDt, receivedOnDt);

        Map<String, EsMetric> esMetrics = docBuilder.getMessageMetrics();
        this.esSchema.updateMessageMappings(accountName, indexedOn, esMetrics);

        // TODO Investigate why update indivudual performs better than update bulk (!!!)
        this.updateIndividually(schemaMetadata, docBuilder);
        // this.updateBulk(schemaMetadata, docBuilder);

        return messageId;
    }

    private void updateIndividually(EsSchema.Metadata schemaMetadata, EsDocumentBuilder docBuilder)
            throws UnknownHostException, EsDatastoreException {

        String indexName = schemaMetadata.getPublicIndexName();
        String messageTypeName = schemaMetadata.getMessageTypeName();
        String kapuaIndexName = schemaMetadata.getPrivateIndexName();
        String topicTypeName = schemaMetadata.getTopicTypeName();
        String metricTypeName = schemaMetadata.getMetricTypeName();
        String assetTypeName = schemaMetadata.getAssetTypeName();

        // Save message (the big one)
        // TODO check response
        EsMessageDAO.connection(EsClient.getcurrent())
                    .instance(indexName, messageTypeName)
                    .upsert(docBuilder.getMessageId(), docBuilder.getMessage());

        // Save topic. Look up topic in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getTopicsCache().get(docBuilder.getTopicId())) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its update
            // then the others of the same type will find the cache updated and skip
            // the update.
            synchronized (this.metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getTopicsCache().get(docBuilder.getTopicId())) {
                    UpdateResponse response = null;
                    try {
                        response = EsTopicDAO.connection(EsClient.getcurrent())
                                             .instance(kapuaIndexName, topicTypeName)
                                             .upsert(docBuilder.getTopicId(), docBuilder.getTopicBuilder());
                        logger.debug(String.format("Upsert on topic succesfully executed [%s.%s, %s]", kapuaIndexName, topicTypeName, response.getId()));
                    } catch (DocumentAlreadyExistsException exc) {
                        logger.trace(String.format("Upsert failed because topic already exists [%s, %s]", docBuilder.getTopicId(), exc.getMessage()));
                    }
                    // Update cache if topic update is completed successfully
                    DatastoreCacheManager.getInstance().getTopicsCache().put(docBuilder.getTopicId(), true);
                }
            }
        }

        // Save topic metrics
        EsMetricDAO.connection(EsClient.getcurrent())
                   .instance(kapuaIndexName, metricTypeName);

        BulkRequest bulkRequest = new BulkRequest();
        List<EsMetricDocumentBuilder> esTopicMetrics = docBuilder.getTopicMetrics();
        if (esTopicMetrics != null) {
            for (EsMetricDocumentBuilder esTopicMetric : esTopicMetrics) {
                if (DatastoreCacheManager.getInstance().getMetricsCache().get(esTopicMetric.getId()))
                    continue;
                // this.esTopicMetricDAO.upsert(esTopicMetric);
                bulkRequest.add(EsMetricDAO.connection(EsClient.getcurrent())
                                           .instance(kapuaIndexName, metricTypeName)
                                           .getUpsertRequest(esTopicMetric));
            }
        }

        if (bulkRequest.numberOfActions() > 0) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its update
            // then the others of the same type will find the cache updated and skip
            // the update.
            synchronized (this.metadataUpdateSync) {
                BulkResponse response = EsMetricDAO.connection(EsClient.getcurrent())
                                                   .bulk(bulkRequest);

                BulkItemResponse[] itemResponses = response.getItems();
                if (itemResponses != null) {
                    for (BulkItemResponse bulkItemResponse : itemResponses) {
                        String topicMetricId = ((UpdateResponse) bulkItemResponse.getResponse()).getId();
                        if (bulkItemResponse.isFailed()) {
                            String failureMessage = bulkItemResponse.getFailureMessage();
                            logger.trace(String.format("Upsert failed because topic metric already exists [%s, %s]", topicMetricId, failureMessage));
                            continue;
                        }

                        logger.debug(String.format("Upsert on topic metric succesfully executed [%s.%s, %s]", kapuaIndexName, topicTypeName, topicMetricId));

                        if (DatastoreCacheManager.getInstance().getMetricsCache().get(topicMetricId))
                            continue;

                        // Update cache if topic metric update is completed successfully
                        DatastoreCacheManager.getInstance().getMetricsCache().put(topicMetricId, true);
                    }
                }
            }
        }

        // Save asset
        if (!DatastoreCacheManager.getInstance().getAssetsCache().get(docBuilder.getAssetId())) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its update
            // then the others of the same type will find the cache updated and skip
            // the update.
            synchronized (this.metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getAssetsCache().get(docBuilder.getAssetId())) {
                    UpdateResponse response = null;
                    try {
                        response = EsAssetDAO.connection(EsClient.getcurrent())
                                             .instance(kapuaIndexName, assetTypeName)
                                             .upsert(docBuilder.getAssetId(), docBuilder.getAssetBuilder());
                        logger.debug(String.format("Upsert on asset succesfully executed [%s.%s, %s]", kapuaIndexName, topicTypeName, response.getId()));
                    } catch (DocumentAlreadyExistsException exc) {
                        logger.trace(String.format("Upsert failed because asset already exists [%s, %s]", docBuilder.getAssetId(), exc.getMessage()));
                    }
                    // Update cache if asset update is completed successfully
                    DatastoreCacheManager.getInstance().getAssetsCache().put(docBuilder.getAssetId(), true);
                }
            }
        }
    }

    private void resetCache(String accountName, String topic)
            throws Exception {

        boolean isAnyAsset;
        boolean isAssetToDelete = false;
        String semTopic;

        if (topic != null) {

            // determine if we should delete an asset if topic = account/asset/#
            KapuaTopic kapuaTopic = new KapuaTopic(topic);
            isAnyAsset = kapuaTopic.isAnyAsset();
            semTopic = kapuaTopic.getSemanticTopic();

            if (semTopic.isEmpty() && !isAnyAsset)
                isAssetToDelete = true;
        } else {
            isAnyAsset = true;
            semTopic = "";
            isAssetToDelete = true;
        }

        // Find all topics
        String everyIndex = EsUtils.getAnyIndexName(accountName);

        int pageSize = 1000;
        int offset = 0;
        long totalHits = 1;

        MetricInfoQueryImpl metricQuery = new MetricInfoQueryImpl();
        metricQuery.setLimit(pageSize + 1);
        metricQuery.setOffset(offset);

        TopicMatchPredicateImpl topicPredicate = new TopicMatchPredicateImpl();
        topicPredicate.setExpression(topic);
        metricQuery.setPredicate(topicPredicate);

        // Remove metrics
        while (totalHits > 0) {
            MetricInfoListResult metrics = EsMetricDAO.connection(EsClient.getcurrent())
                                                      .instance(everyIndex, EsSchema.METRIC_TYPE_NAME)
                                                      .query(metricQuery);

            totalHits = metrics.size();
            LocalCache<String, Boolean> metricsCache = DatastoreCacheManager.getInstance().getMetricsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = metrics.get(i).getId().toString();
                if (metricsCache.get(id))
                    metricsCache.remove(id);
            }

            if (totalHits > pageSize)
                offset += (pageSize + 1);
        }

        logger.debug(String.format("Removed cached topic metrics for [%s]", topic));

        EsMetricDAO.connection(EsClient.getcurrent())
                   .instance(everyIndex, EsSchema.METRIC_TYPE_NAME)
                   .deleteByQuery(metricQuery);

        logger.debug(String.format("Removed topic metrics for [%s]", topic));
        //

        TopicInfoQueryImpl topicQuery = new TopicInfoQueryImpl();
        topicQuery.setLimit(pageSize + 1);
        topicQuery.setOffset(offset);

        topicPredicate = new TopicMatchPredicateImpl();
        topicPredicate.setExpression(topic);
        topicQuery.setPredicate(topicPredicate);

        // Remove topic
        offset = 0;
        totalHits = 1;
        while (totalHits > 0) {
            TopicInfoListResult topics = EsTopicDAO.connection(EsClient.getcurrent())
                                                   .instance(everyIndex, EsSchema.TOPIC_TYPE_NAME)
                                                   .query(topicQuery);

            totalHits = topics.size();
            LocalCache<String, Boolean> topicsCache = DatastoreCacheManager.getInstance().getTopicsCache();
            long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

            for (int i = 0; i < toBeProcessed; i++) {
                String id = topics.get(0).getId().toString();
                if (topicsCache.get(id))
                    topicsCache.remove(id);
            }
            if (totalHits > pageSize)
                offset += (pageSize + 1);
        }

        logger.debug(String.format("Removed cached topics for [%s]", topic));

        EsTopicDAO.connection(EsClient.getcurrent())
                  .instance(everyIndex, EsSchema.TOPIC_TYPE_NAME)
                  .deleteByQuery(topicQuery);

        logger.debug(String.format("Removed topics for [%s]", topic));
        //

        // Remove asset
        if (isAssetToDelete) {

            AssetInfoQueryImpl assetQuery = new AssetInfoQueryImpl();
            assetQuery.setLimit(pageSize + 1);
            assetQuery.setOffset(offset);

            topicPredicate = new TopicMatchPredicateImpl();
            topicPredicate.setExpression(topic);
            assetQuery.setPredicate(topicPredicate);

            offset = 0;
            totalHits = 1;
            while (totalHits > 0) {
                AssetInfoListResult assets = EsAssetDAO.connection(EsClient.getcurrent())
                                                       .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                                                       .query(assetQuery);

                totalHits = assets.size();
                LocalCache<String, Boolean> assetsCache = DatastoreCacheManager.getInstance().getAssetsCache();
                long toBeProcessed = totalHits > pageSize ? pageSize : totalHits;

                for (int i = 0; i < toBeProcessed; i++) {
                    String id = assets.get(i).getId().toString();
                    if (assetsCache.get(id))
                        assetsCache.remove(id);
                }
                if (totalHits > pageSize)
                    offset += (pageSize + 1);
            }

            logger.debug(String.format("Removed cached assets for [%s]", topic));

            EsAssetDAO.connection(EsClient.getcurrent())
                      .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                      .deleteByQuery(assetQuery);

            logger.debug(String.format("Removed assets for [%s]", topic));
        }
    }
}
