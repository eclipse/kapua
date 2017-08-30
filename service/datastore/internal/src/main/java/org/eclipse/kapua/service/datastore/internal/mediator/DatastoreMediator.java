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
package org.eclipse.kapua.service.datastore.internal.mediator;

import java.util.Map;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.client.ClientException;
import org.eclipse.kapua.service.datastore.client.QueryMappingException;
import org.eclipse.kapua.service.datastore.internal.ChannelInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.ClientInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.MessageStoreFacade;
import org.eclipse.kapua.service.datastore.internal.MetricInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.StorableIdImpl;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.Schema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;

/**
 * Datastore mediator definition
 *
 * @since 1.0.0
 */
public class DatastoreMediator implements MessageStoreMediator,
        ClientInfoRegistryMediator,
        ChannelInfoRegistryMediator,
        MetricInfoRegistryMediator {

    private final static DatastoreMediator INSTANCE;

    private final Schema esSchema;

    private MessageStoreFacade messageStoreFacade;
    private ClientInfoRegistryFacade clientInfoStoreFacade;
    private ChannelInfoRegistryFacade channelInfoStoreFacade;
    private MetricInfoRegistryFacade metricInfoStoreFacade;

    static {
        INSTANCE = new DatastoreMediator();
    }

    private DatastoreMediator() {
        esSchema = new Schema();
    }

    /**
     * Get the {@link DatastoreMediator} instance (singleton)
     *
     * @return
     * @since 1.0.0
     */
    public static DatastoreMediator getInstance() {
        return INSTANCE;
    }

    /**
     * Set the message store facade
     *
     * @param messageStoreFacade
     * @since 1.0.0
     */
    public void setMessageStoreFacade(MessageStoreFacade messageStoreFacade) {
        this.messageStoreFacade = messageStoreFacade;
    }

    /**
     * Set the client info facade
     *
     * @param clientInfoStoreFacade
     * @since 1.0.0
     */
    public void setClientInfoStoreFacade(ClientInfoRegistryFacade clientInfoStoreFacade) {
        this.clientInfoStoreFacade = clientInfoStoreFacade;
    }

    /**
     * Set the channel info facade
     *
     * @param channelInfoStoreFacade
     * @since 1.0.0
     */
    public void setChannelInfoStoreFacade(ChannelInfoRegistryFacade channelInfoStoreFacade) {
        this.channelInfoStoreFacade = channelInfoStoreFacade;
    }

    /**
     * Set the metric info facade
     *
     * @param metricInfoStoreFacade
     * @since 1.0.0
     */
    public void setMetricInfoStoreFacade(MetricInfoRegistryFacade metricInfoStoreFacade) {
        this.metricInfoStoreFacade = metricInfoStoreFacade;
    }

    /*
     * 
     * Message Store Mediator methods
     */
    @Override
    public Metadata getMetadata(KapuaId scopeId, long indexedOn) throws ClientException {
        return esSchema.synch(scopeId, indexedOn);
    }

    @Override
    public void onUpdatedMappings(KapuaId scopeId, long indexedOn, Map<String, Metric> metrics) throws ClientException {
        esSchema.updateMessageMappings(scopeId, indexedOn, metrics);
    }

    @Override
    public void onAfterMessageStore(MessageInfo messageInfo,
            DatastoreMessage message)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        // convert semantic channel to String
        String semanticChannel = message.getChannel() != null ? message.getChannel().toString() : "";

        ClientInfoImpl clientInfo = new ClientInfoImpl(message.getScopeId());
        clientInfo.setClientId(message.getClientId());
        clientInfo.setId(new StorableIdImpl(ClientInfoField.getOrDeriveId(null, message.getScopeId(), message.getClientId())));
        clientInfo.setFirstMessageId(message.getDatastoreId());
        clientInfo.setFirstMessageOn(message.getTimestamp());
        clientInfoStoreFacade.upstore(clientInfo);

        ChannelInfoImpl channelInfo = new ChannelInfoImpl(message.getScopeId());
        channelInfo.setClientId(message.getClientId());
        channelInfo.setName(semanticChannel);
        channelInfo.setFirstMessageId(message.getDatastoreId());
        channelInfo.setFirstMessageOn(message.getTimestamp());
        channelInfo.setId(new StorableIdImpl(ChannelInfoField.getOrDeriveId(null, channelInfo)));
        clientInfo.setFirstMessageId(message.getDatastoreId());
        clientInfo.setFirstMessageOn(message.getTimestamp());
        channelInfoStoreFacade.upstore(channelInfo);

        KapuaPayload payload = message.getPayload();
        if (payload == null) {
            return;
        }

        Map<String, Object> metrics = payload.getMetrics();
        if (metrics == null) {
            return;
        }

        int i = 0;
        MetricInfoImpl[] messageMetrics = new MetricInfoImpl[metrics.size()];
        for (Map.Entry<String, Object> entry : metrics.entrySet()) {
            MetricInfoImpl metricInfo = new MetricInfoImpl(message.getScopeId());
            metricInfo.setClientId(message.getClientId());
            metricInfo.setChannel(semanticChannel);
            metricInfo.setName(entry.getKey());
            metricInfo.setMetricType(entry.getValue().getClass());
            metricInfo.setId(new StorableIdImpl(MetricInfoField.getOrDeriveId(null, metricInfo)));
            metricInfo.setFirstMessageId(message.getDatastoreId());
            metricInfo.setFirstMessageOn(message.getTimestamp());
            messageMetrics[i++] = metricInfo;
        }

        metricInfoStoreFacade.upstore(messageMetrics);
    }

    /*
     * 
     * ClientInfo Store Mediator methods
     */
    @Override
    public void onAfterClientInfoDelete(KapuaId scopeId, ClientInfo clientInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            ClientException {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    /*
     * 
     * ChannelInfo Store Mediator methods
     */
    @Override
    public void onBeforeChannelInfoDelete(ChannelInfo channelInfo)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            QueryMappingException,
            ClientException {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    @Override
    public void onAfterChannelInfoDelete(ChannelInfo channelInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    /*
     * 
     * MetricInfo Store Mediator methods
     */
    @Override
    public void onAfterMetricInfoDelete(KapuaId scopeId, MetricInfo metricInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    public void refreshAllIndexes() throws ClientException {
        messageStoreFacade.refreshAllIndexes();
    }

    public void deleteAllIndexes() throws ClientException {
        messageStoreFacade.deleteAllIndexes();
        clearCache();
    }

    public void clearCache() {
        DatastoreCacheManager.getInstance().getChannelsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getClientsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetricsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetadataCache().invalidateAll();
    }

}
