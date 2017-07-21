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

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.DatastoreClient;
import org.eclipse.kapua.service.datastore.client.ClientErrorCodes;
import org.eclipse.kapua.service.datastore.client.ClientErrorMessages;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.client.model.TypeDescriptor;
import org.eclipse.kapua.service.datastore.client.model.UpdateRequest;
import org.eclipse.kapua.service.datastore.client.model.UpdateResponse;
import org.eclipse.kapua.service.datastore.internal.client.DatastoreClientFactory;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoField;
import org.eclipse.kapua.service.datastore.internal.mediator.ClientInfoRegistryMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageStoreConfiguration;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ClientInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.schema.ClientInfoSchema;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client information registry facade
 * 
 * @since 1.0.0
 */
public class ClientInfoRegistryFacade {

    private static final Logger logger = LoggerFactory.getLogger(ClientInfoRegistryFacade.class);

    private final ClientInfoRegistryMediator mediator;
    private final ConfigurationProvider configProvider;
    private final Object metadataUpdateSync = new Object();
    private DatastoreClient client;

    /**
     * Constructs the client info registry facade
     * 
     * @param configProvider
     * @param mediator
     * @throws ClientUnavailableException
     * 
     * @since 1.0.0
     */
    public ClientInfoRegistryFacade(ConfigurationProvider configProvider, ClientInfoRegistryMediator mediator) throws ClientUnavailableException {
        this.configProvider = configProvider;
        this.mediator = mediator;
        client = DatastoreClientFactory.getInstance();
    }

    /**
     * Update the client information after a message store operation
     * 
     * @param clientInfo
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     *
     * @since 1.0.0
     */
    public StorableId upstore(ClientInfo clientInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException, ClientException {
        ArgumentValidator.notNull(clientInfo, "clientInfo");
        ArgumentValidator.notNull(clientInfo.getScopeId(), "clientInfo.scopeId");
        ArgumentValidator.notNull(clientInfo.getFirstMessageId(), "clientInfo.firstPublishedMessageId");
        ArgumentValidator.notNull(clientInfo.getFirstMessageOn(), "clientInfo.firstPublishedMessageTimestamp");

        String clientInfoId = ClientInfoField.getOrDeriveId(clientInfo.getId(), clientInfo);
        StorableId storableId = new StorableIdImpl(clientInfoId);

        UpdateResponse response = null;
        // Store channel. Look up channel in the cache, and cache it if it doesn't exist
        if (!DatastoreCacheManager.getInstance().getClientsCache().get(clientInfo.getClientId())) {
            // The code is safe even without the synchronized block
            // Synchronize in order to let the first thread complete its update
            // then the others of the same type will find the cache updated and
            // skip the update.
            synchronized (metadataUpdateSync) {
                if (!DatastoreCacheManager.getInstance().getClientsCache().get(clientInfo.getClientId())) {
                    // fix #REPLACE_ISSUE_NUMBER
                    ClientInfo storedField = find(clientInfo.getScopeId(), storableId);
                    if (storedField == null) {
                        Metadata metadata = mediator.getMetadata(clientInfo.getScopeId(), clientInfo.getFirstMessageOn().getTime());
                        String kapuaIndexName = metadata.getRegistryIndexName();

                        UpdateRequest request = new UpdateRequest(clientInfo.getId().toString(), new TypeDescriptor(kapuaIndexName, ClientInfoSchema.CLIENT_TYPE_NAME), clientInfo);
                        response = client.upsert(request);

                        if (!clientInfoId.equals(response.getId())) {
                            // this condition shouldn't happens
                            throw new ClientException(ClientErrorCodes.ACTION_ERROR, String.format(ClientErrorMessages.CRUD_INTERNAL_ERROR, "ClientInfoRegistry - upstore"));
                        }
                        logger.debug(String.format("Upsert on asset succesfully executed [%s.%s, %s]", kapuaIndexName,
                                ClientInfoSchema.CLIENT_TYPE_NAME, response.getId()));
                        // Update cache if asset update is completed successfully
                    }
                    DatastoreCacheManager.getInstance().getClientsCache().put(clientInfo.getClientId(), true);
                }
            }
        }
        return storableId;
    }

    /**
     * Delete client information by identifier.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the client info registry entry by id without checking the consistency of the others registries or the message store.</b>
     * 
     * @param scopeId
     * @param id
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws ClientException
     */
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            ConfigurationException, ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(scopeId);
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeId);
            return;
        }

        String indexName = SchemaUtil.getKapuaIndexName(scopeId);
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ClientInfoSchema.CLIENT_TYPE_NAME);
        client.delete(typeDescriptor, id.toString());
    }

    /**
     * Find client information by identifier
     * 
     * @param scopeId
     * @param id
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public ClientInfo find(KapuaId scopeId, StorableId id)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        ClientInfoQueryImpl idsQuery = new ClientInfoQueryImpl(scopeId);
        idsQuery.setLimit(1);

        IdsPredicateImpl idsPredicate = new IdsPredicateImpl(ClientInfoSchema.CLIENT_TYPE_NAME);
        idsPredicate.addValue(id);
        idsQuery.setPredicate(idsPredicate);

        ClientInfoListResult result = query(idsQuery);
        return result.getFirstItem();
    }

    /**
     * Find clients informations matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public ClientInfoListResult query(ClientInfoQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return new ClientInfoListResultImpl();
        }

        String indexName = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ClientInfoSchema.CLIENT_TYPE_NAME);
        return new ClientInfoListResultImpl(client.query(typeDescriptor, query, ClientInfo.class));
    }

    /**
     * Get clients informations count matching the given query
     * 
     * @param query
     * @return
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public long count(ClientInfoQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", query.getScopeId());
            return 0;
        }

        String dataIndexName = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(dataIndexName, ClientInfoSchema.CLIENT_TYPE_NAME);
        return client.count(typeDescriptor, query);
    }

    /**
     * Delete clients informations count matching the given query.<br>
     * <b>Be careful using this function since it doesn't guarantee the datastore consistency.<br>
     * It just deletes the client info registry entries that matching the query without checking the consistency of the others registries or the message store.</b>
     * 
     * @param query
     * @throws KapuaIllegalArgumentException
     * @throws ConfigurationException
     * @throws QueryMappingException
     * @throws ClientException
     */
    public void delete(ClientInfoQuery query)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        MessageStoreConfiguration accountServicePlan = configProvider.getConfiguration(query.getScopeId());
        long ttl = accountServicePlan.getDataTimeToLiveMilliseconds();

        if (!accountServicePlan.getDataStorageEnabled() || ttl == MessageStoreConfiguration.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", query.getScopeId());
            return;
        }

        String indexName = SchemaUtil.getKapuaIndexName(query.getScopeId());
        TypeDescriptor typeDescriptor = new TypeDescriptor(indexName, ClientInfoSchema.CLIENT_TYPE_NAME);
        client.deleteByQuery(typeDescriptor, query);
    }

}
