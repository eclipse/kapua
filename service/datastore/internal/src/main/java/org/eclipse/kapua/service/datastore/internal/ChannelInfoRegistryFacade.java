/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ChannelInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.schema.ChannelInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.elasticsearch.client.model.TypeDescriptor;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateRequest;
import org.eclipse.kapua.service.elasticsearch.client.model.UpdateResponse;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;
import org.eclipse.kapua.service.storable.model.query.predicate.IdsPredicate;
import org.eclipse.kapua.service.storable.model.query.predicate.StorablePredicateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel information registry facade
 *
 * @since 1.0.0
 */
public class ChannelInfoRegistryFacade extends AbstractRegistryFacade {

    private static final Logger LOG = LoggerFactory.getLogger(ChannelInfoRegistryFacade.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final StorableIdFactory STORABLE_ID_FACTORY = LOCATOR.getFactory(StorableIdFactory.class);
    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);

    private final ChannelInfoRegistryMediator mediator;
    private final Object metadataUpdateSync = new Object();

    private static final String QUERY = "query";
    private static final String QUERY_SCOPE_ID = "query.scopeId";

    /**
     * Constructs the channel info registry facade
     *
     * @param configProvider
     * @param mediator
     * @since 1.0.0
     */
    public ChannelInfoRegistryFacade(ConfigurationProvider configProvider, ChannelInfoRegistryMediator mediator) {
        super(configProvider);

        this.mediator = mediator;
    }

    /**
     * Update the channel information after a message store operation
     *
     * @param channelInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public StorableId upstore(ChannelInfo channelInfo) throws KapuaIllegalArgumentException, ConfigurationException, ClientException, MappingException {
        ArgumentValidator.notNull(channelInfo, "channelInfo");
        ArgumentValidator.notNull(channelInfo.getScopeId(), "channelInfo.scopeId");
        ArgumentValidator.notNull(channelInfo.getName(), "channelInfo.name");
        ArgumentValidator.notNull(channelInfo.getFirstMessageId(), "channelInfo.messageId");
        ArgumentValidator.notNull(channelInfo.getFirstMessageOn(), "channelInfo.messageTimestamp");

        String channelInfoId = ChannelInfoField.getOrDeriveId(channelInfo.getId(), channelInfo);
        StorableId storableId = STORABLE_ID_FACTORY.newStorableId(channelInfoId);

        UpdateResponse response;
        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getChannelsCache().get(channelInfoId)) {
            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its
            // update then the others of the same type will find the cache
            // updated and skip the update.
            synchronized (metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getChannelsCache().get(channelInfoId)) {
                    ChannelInfo storedField = find(channelInfo.getScopeId(), storableId);
                    if (storedField == null) {
                        Metadata metadata = mediator.getMetadata(channelInfo.getScopeId(), channelInfo.getFirstMessageOn().getTime());
                        String registryIndexName = metadata.getChannelRegistryIndexName();

                        UpdateRequest request = new UpdateRequest(channelInfo.getId().toString(), new TypeDescriptor(metadata.getChannelRegistryIndexName(), ChannelInfoSchema.CHANNEL_TYPE_NAME), channelInfo);
                        response = getElasticsearchClient().upsert(request);

                        LOG.debug("Upsert on channel successfully executed [{}.{}, {} - {}]", registryIndexName, ChannelInfoSchema.CHANNEL_TYPE_NAME, channelInfoId, response.getId());
                    }
                    // Update cache if channel update is completed successfully
                    DatastoreCacheManager.getInstance().getChannelsCache().put(channelInfoId, true);
                }
            }
        }
        return storableId;
    }

    /**
     * Delete channel information by identifier.
     *
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the channel info registry entry by id without checking the consistency of the others registries or the message store.</b>
     *
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public void delete(KapuaId scopeId, StorableId id) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        if (!isDatastoreServiceEnabled(scopeId)) {
            LOG.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        String indexName = SchemaUtil.getChannelIndexName(scopeId);
        ChannelInfo channelInfo = find(scopeId, id);
        if (channelInfo != null) {
            mediator.onBeforeChannelInfoDelete(channelInfo);

            TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
            getElasticsearchClient().delete(typeDescriptor, id.toString());
        }
    }

    /**
     * Find channel information by identifier
     *
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public ChannelInfo find(KapuaId scopeId, StorableId id) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        ChannelInfoQueryImpl idsQuery = new ChannelInfoQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicate idsPredicate = STORABLE_PREDICATE_FACTORY.newIdsPredicate(ChannelInfoSchema.CHANNEL_TYPE_NAME);
        idsPredicate.addId(id);
        idsQuery.setPredicate(idsPredicate);

        ChannelInfoListResult result = query(idsQuery);
        return result.getFirstItem();
    }

    /**
     * Find channels informations matching the given query
     *
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public ChannelInfoListResult query(ChannelInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return new ChannelInfoListResultImpl();
        }

        String indexName = SchemaUtil.getChannelIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
        ChannelInfoListResult result = new ChannelInfoListResultImpl(getElasticsearchClient().query(typeDescriptor, query, ChannelInfo.class));
        setLimitExceed(query, result);
        return result;
    }

    /**
     * Get channels informations count matching the given query
     *
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public long count(ChannelInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String indexName = SchemaUtil.getChannelIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
        return getElasticsearchClient().count(typeDescriptor, query);
    }

    /**
     * Delete channels informations count matching the given query.
     *
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the channel info registry entries that matching the query without checking the consistency of the others registries or the message store.</b>
     *
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    void delete(ChannelInfoQuery query) throws KapuaIllegalArgumentException, ConfigurationException, ClientException {
        ArgumentValidator.notNull(query, QUERY);
        ArgumentValidator.notNull(query.getScopeId(), QUERY_SCOPE_ID);

        if (!isDatastoreServiceEnabled(query.getScopeId())) {
            LOG.debug("Storage not enabled for account {}, skipping delete", query.getScopeId());
            return;
        }

        String indexName = SchemaUtil.getChannelIndexName(query.getScopeId());
        ChannelInfoListResult channels = query(query);

        for (ChannelInfo channelInfo : channels.getItems()) {
            mediator.onBeforeChannelInfoDelete(channelInfo);
        }

        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ChannelInfoSchema.CHANNEL_TYPE_NAME);
        getElasticsearchClient().deleteByQuery(typeDescriptor, query);
    }
}
