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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.util.ArrayList;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ChannelInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsConfigurationException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema.Metadata;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsChannelInfoDAO;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ChannelInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ChannelInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ChannelInfoQuery;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Channel information registry facade
 * 
 * @since 1.0
 *
 */
public class ChannelInfoRegistryFacade
{

    private static final Logger logger = LoggerFactory.getLogger(ChannelInfoRegistryFacade.class);

    private final ChannelInfoRegistryMediator mediator;
    private final ConfigurationProvider       configProvider;
    private final Object                      metadataUpdateSync;

    /**
     * Constructs the channel info registry facade
     * 
     * @param configProvider
     * @param mediator
     */
    public ChannelInfoRegistryFacade(ConfigurationProvider configProvider, ChannelInfoRegistryMediator mediator)
    {
        this.configProvider = configProvider;
        this.mediator = mediator;
        this.metadataUpdateSync = new Object();
    }

    /**
     * Update the channel information after a message store operation
     * 
     * @param scopeId
     * @param channelInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsDocumentBuilderException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     */
    public StorableId upstore(KapuaId scopeId, ChannelInfo channelInfo)
        throws KapuaIllegalArgumentException,
        EsDocumentBuilderException,
        EsClientUnavailableException,
        EsConfigurationException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(channelInfo, "channelInfoCreator");
        ArgumentValidator.notNull(channelInfo.getChannel(), "channelInfoCreator.getChannel");
        ArgumentValidator.notNull(channelInfo.getFirstPublishedMessageId(), "channelInfoCreator.messageId");
        ArgumentValidator.notNull(channelInfo.getFirstPublishedMessageTimestamp(), "channelInfoCreator.messageTimestamp");

        String channelInfoId = ChannelInfoXContentBuilder.getOrDeriveId(channelInfo.getId(), channelInfo);

        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getChannelsCache().get(channelInfoId)) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its
            // update then the others of the same type will find the cache
            // updated and skip the update.
            synchronized (this.metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getChannelsCache().get(channelInfoId)) {
                    UpdateResponse response = null;
                    try {
                        Metadata metadata = this.mediator.getMetadata(scopeId, channelInfo.getFirstPublishedMessageTimestamp().getTime());
                        String kapuaIndexName = metadata.getKapuaIndexName();

                        response = EsChannelInfoDAO.getInstance()
                                                   .index(metadata.getKapuaIndexName())
                                                   .upsert(channelInfo);

                        channelInfoId = response.getId();

                        logger.debug(String.format("Upsert on channel succesfully executed [%s.%s, %s]",
                                                   kapuaIndexName, EsSchema.CHANNEL_TYPE_NAME, channelInfoId));

                    }
                    catch (DocumentAlreadyExistsException exc) {
                        logger.trace(String.format("Upsert failed because channel already exists [%s, %s]",
                                                   channelInfoId, exc.getMessage()));
                    }
                    // Update cache if channel update is completed successfully
                    DatastoreCacheManager.getInstance().getChannelsCache().put(channelInfoId, true);
                }
            }
        }
        return new StorableIdImpl(channelInfoId);
    }

    /**
     * Delete channel information by identifier
     * 
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     */
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaIllegalArgumentException,
        EsClientUnavailableException,
        EsConfigurationException,
        EsQueryConversionException,
        EsObjectBuilderException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);

        ChannelInfo channelInfo = this.find(scopeId, id);

        this.mediator.onBeforeChannelInfoDelete(scopeId, channelInfo);

        EsChannelInfoDAO.getInstance()
                        .index(indexName)
                        .deleteById(id.toString());
    }

    /**
     * Find channel information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     */
    public ChannelInfo find(KapuaId scopeId, StorableId id)
        throws KapuaIllegalArgumentException,
        EsConfigurationException,
        EsClientUnavailableException,
        EsQueryConversionException,
        EsObjectBuilderException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        ChannelInfoQueryImpl q = new ChannelInfoQueryImpl();
        q.setLimit(1);

        ArrayList<StorableId> ids = new ArrayList<StorableId>();
        ids.add(id);

        AndPredicateImpl allPredicates = new AndPredicateImpl();
        allPredicates.addPredicate(new IdsPredicateImpl(EsSchema.CHANNEL_TYPE_NAME, ids));

        ChannelInfoListResult result = this.query(scopeId, q);
        if (result == null || result.size() == 0)
            return null;

        ChannelInfo channelInfo = result.get(0);
        return channelInfo;
    }

    /**
     * Find channels informations matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsClientUnavailableException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     */
    public ChannelInfoListResult query(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaIllegalArgumentException,
        EsConfigurationException,
        EsClientUnavailableException,
        EsQueryConversionException,
        EsObjectBuilderException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeId);
            return new ChannelInfoListResultImpl();
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);
        ChannelInfoListResult result = null;
        result = EsChannelInfoDAO.getInstance()
                                 .index(indexName)
                                 .query(query);

        return result;
    }

    /**
     * Get channels informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public long count(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaIllegalArgumentException,
        EsConfigurationException,
        EsQueryConversionException,
        EsClientUnavailableException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeId);
            return 0;
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);
        long result;
        result = EsChannelInfoDAO.getInstance()
                                 .index(indexName)
                                 .count(query);

        return result;
    }

    /**
     * Delete channels informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     * @throws EsObjectBuilderException
     */
    public void delete(KapuaId scopeId, ChannelInfoQuery query)
        throws KapuaIllegalArgumentException,
        EsQueryConversionException,
        EsClientUnavailableException,
        EsConfigurationException,
        EsObjectBuilderException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");

        //
        // Do the find
        MessageStoreConfiguration accountServicePlan = this.configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", scopeId);
            return;
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);

        ChannelInfoListResult channels = this.query(scopeId, query);

        // TODO Improve performances
        for (ChannelInfo channelInfo : channels)
            this.mediator.onBeforeChannelInfoDelete(scopeId, channelInfo);

        EsChannelInfoDAO.getInstance()
                        .index(indexName)
                        .deleteByQuery(query);
    }

}
