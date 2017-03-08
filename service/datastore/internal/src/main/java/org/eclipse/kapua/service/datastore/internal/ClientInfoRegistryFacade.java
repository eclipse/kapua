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
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.ClientInfoXContentBuilder;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsConfigurationException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsDocumentBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsObjectBuilderException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsQueryConversionException;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema.Metadata;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsClientInfoDAO;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.engine.DocumentAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client information registry facade
 * 
 * @since 1.0
 *
 */
public class ClientInfoRegistryFacade
{
    private static final Logger logger = LoggerFactory.getLogger(ClientInfoRegistryFacade.class);

    private final ClientInfoRegistryMediator mediator;
    private final ConfigurationProvider      configProvider;
    private final Object                     metadataUpdateSync;

    /**
     * Constructs the client info registry facade
     * 
     * @param configProvider
     * @param mediator
     */
    public ClientInfoRegistryFacade(ConfigurationProvider configProvider, ClientInfoRegistryMediator mediator)
    {
        this.configProvider = configProvider;
        this.mediator = mediator;
        this.metadataUpdateSync = new Object();
    }

    /**
     * Update the client information after a message store operation
     * 
     * @param scopeId
     * @param clientInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsDocumentBuilderException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     */
    public StorableId upstore(KapuaId scopeId, ClientInfo clientInfo)
        throws KapuaIllegalArgumentException,
        EsDocumentBuilderException,
        EsClientUnavailableException,
        EsConfigurationException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(clientInfo, "clientInfoCreator");
        ArgumentValidator.notNull(clientInfo.getFirstPublishedMessageId(), "clientInfoCreator.firstPublishedMessageId");
        ArgumentValidator.notNull(clientInfo.getFirstPublishedMessageTimestamp(), "clientInfoCreator.firstPublishedMessageTimestamp");

        ClientInfoXContentBuilder docBuilder = new ClientInfoXContentBuilder();
        docBuilder.build(clientInfo);

        // Save client
        if (!DatastoreCacheManager.getInstance().getClientsCache().get(docBuilder.getClientId())) {

            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its update
            // then the others of the same type will find the cache updated and
            // skip the update.
            synchronized (this.metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getClientsCache().get(docBuilder.getClientId())) {
                    UpdateResponse response = null;
                    try {
                        Metadata metadata = this.mediator.getMetadata(scopeId, clientInfo.getFirstPublishedMessageTimestamp().getTime());
                        String kapuaIndexName = metadata.getKapuaIndexName();

                        response = EsClientInfoDAO.getInstance().index(kapuaIndexName)
                                                  .upsert(docBuilder.getClientId(), docBuilder.getClientBuilder());
                        logger.debug(String.format("Upsert on asset succesfully executed [%s.%s, %s]", kapuaIndexName,
                                                   EsSchema.CHANNEL_TYPE_NAME, response.getId()));
                    }
                    catch (DocumentAlreadyExistsException exc) {
                        logger.trace(String.format("Upsert failed because asset already exists [%s, %s]",
                                                   docBuilder.getClientId(), exc.getMessage()));
                    }
                    // Update cache if asset update is completed successfully
                    DatastoreCacheManager.getInstance().getClientsCache().put(docBuilder.getClientId(), true);
                }
            }
        }

        return new StorableIdImpl(docBuilder.getClientId());
    }

    /**
     * Delete client information by identifier
     * 
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     */
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaIllegalArgumentException,
        EsClientUnavailableException,
        EsConfigurationException
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

        String indexName = EsSchema.getDataIndexName(scopeId);
        EsClientInfoDAO.getInstance()
                       .index(indexName)
                       .deleteById(id.toString());
    }

    /**
     * Find client information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsClientUnavailableException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsObjectBuilderException
     */
    public ClientInfo find(KapuaId scopeId, StorableId id)
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

        ClientInfoQueryImpl q = new ClientInfoQueryImpl();
        q.setLimit(1);

        ArrayList<StorableId> ids = new ArrayList<StorableId>();
        ids.add(id);

        AndPredicateImpl allPredicates = new AndPredicateImpl();
        allPredicates.addPredicate(new IdsPredicateImpl(EsSchema.CLIENT_TYPE_NAME, ids));

        ClientInfoListResult result = this.query(scopeId, q);
        if (result == null || result.size() == 0)
            return null;

        ClientInfo assetInfo = result.get(0);
        return assetInfo;
    }

    /**
     * Find clients informations matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     * @throws EsObjectBuilderException
     */
    public ClientInfoListResult query(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaIllegalArgumentException,
        EsConfigurationException,
        EsQueryConversionException,
        EsClientUnavailableException,
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
            return new ClientInfoListResultImpl();
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);
        ClientInfoListResult result = null;
        result = EsClientInfoDAO.getInstance()
                                .index(indexName)
                                .query(query);

        return result;
    }

    /**
     * Get clients informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public long count(KapuaId scopeId, ClientInfoQuery query)
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

        String dataIndexName = EsSchema.getKapuaIndexName(scopeId);
        long result;
        result = EsClientInfoDAO.getInstance()
                                .index(dataIndexName)
                                .count(query);

        return result;
    }

    /**
     * Delete clients informations count matching the given query
     * 
     * @param scopeId
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws EsConfigurationException
     * @throws EsQueryConversionException
     * @throws EsClientUnavailableException
     */
    public void delete(KapuaId scopeId, ClientInfoQuery query)
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
            logger.debug("Storage not enabled for account {}, skipping delete", scopeId);
            return;
        }

        String indexName = EsSchema.getKapuaIndexName(scopeId);
        EsClientInfoDAO.getInstance()
                       .index(indexName)
                       .deleteByQuery(query);

        return;
    }

}
