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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.mediator;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.ChannelInfoRegistryService;
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.ChannelInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.ClientInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.DatastoreCacheManager;
import org.eclipse.kapua.service.datastore.internal.MessageStoreFacade;
import org.eclipse.kapua.service.datastore.internal.MetricInfoRegistryFacade;
import org.eclipse.kapua.service.datastore.internal.model.ChannelInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.ClientInfoImpl;
import org.eclipse.kapua.service.datastore.internal.model.MetricInfoImpl;
import org.eclipse.kapua.service.datastore.internal.schema.Metadata;
import org.eclipse.kapua.service.datastore.internal.schema.Schema;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.id.StorableIdFactory;

import java.util.Map;

/**
 * Datastore mediator definition
 *
 * @since 1.0.0
 */
public class DatastoreMediator implements MessageStoreMediator,
        ClientInfoRegistryMediator,
        ChannelInfoRegistryMediator,
        MetricInfoRegistryMediator {

    private static final DatastoreMediator INSTANCE;

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final StorableIdFactory STORABLE_ID_FACTORY = LOCATOR.getFactory(StorableIdFactory.class);

    private final Schema esSchema;

    private MessageStoreFacade messageStoreFacade;
    private ClientInfoRegistryFacade clientInfoRegistryFacade;
    private ChannelInfoRegistryFacade channelInfoStoreFacade;
    private MetricInfoRegistryFacade metricInfoStoreFacade;

    static {
        INSTANCE = new DatastoreMediator();

        // Be sure the data registry services are instantiated
        KapuaLocator.getInstance().getService(ClientInfoRegistryService.class);
        KapuaLocator.getInstance().getService(ChannelInfoRegistryService.class);
        KapuaLocator.getInstance().getService(MetricInfoRegistryService.class);
    }

    private DatastoreMediator() {
        esSchema = new Schema();
    }

    /**
     * Gets the {@link DatastoreMediator} instance.
     *
     * @return The {@link DatastoreMediator} instance.
     * @since 1.0.0
     */
    public static DatastoreMediator getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the {@link MessageStoreFacade}.
     *
     * @param messageStoreFacade The {@link MessageStoreFacade}.
     * @since 1.0.0
     */
    public void setMessageStoreFacade(MessageStoreFacade messageStoreFacade) {
        this.messageStoreFacade = messageStoreFacade;
    }

    /**
     * Sets the {@link ClientInfoRegistryFacade}.
     *
     * @param clientInfoRegistryFacade The {@link ClientInfoRegistryFacade}.
     * @since 1.0.0
     */
    public void setClientInfoStoreFacade(ClientInfoRegistryFacade clientInfoRegistryFacade) {
        this.clientInfoRegistryFacade = clientInfoRegistryFacade;
    }

    /**
     * Sets the {@link ChannelInfoRegistryFacade}.
     *
     * @param channelInfoStoreFacade The {@link ChannelInfoRegistryFacade}.
     * @since 1.0.0
     */
    public void setChannelInfoStoreFacade(ChannelInfoRegistryFacade channelInfoStoreFacade) {
        this.channelInfoStoreFacade = channelInfoStoreFacade;
    }

    /**
     * Sets the {@link MetricInfoRegistryFacade}.
     *
     * @param metricInfoStoreFacade The {@link MetricInfoRegistryFacade}.
     * @since 1.0.0
     */
    public void setMetricInfoStoreFacade(MetricInfoRegistryFacade metricInfoStoreFacade) {
        this.metricInfoStoreFacade = metricInfoStoreFacade;
    }

    //
    // Message Store Mediator methods
    //

    @Override
    public Metadata getMetadata(KapuaId scopeId, long indexedOn) throws ClientException, MappingException {
        return esSchema.synch(scopeId, indexedOn);
    }

    @Override
    public void onUpdatedMappings(KapuaId scopeId, long indexedOn, Map<String, Metric> metrics) throws ClientException, MappingException {
        esSchema.updateMessageMappings(scopeId, indexedOn, metrics);
    }

    @Override
    public void onAfterMessageStore(MessageInfo messageInfo, DatastoreMessage message)
            throws KapuaIllegalArgumentException,
            ConfigurationException,
            MappingException,
            ClientException {

        // convert semantic channel to String
        String semanticChannel = message.getChannel() != null ? message.getChannel().toString() : "";

        ClientInfoImpl clientInfo = new ClientInfoImpl(message.getScopeId());
        clientInfo.setClientId(message.getClientId());
        clientInfo.setId(STORABLE_ID_FACTORY.newStorableId(ClientInfoField.getOrDeriveId(null, message.getScopeId(), message.getClientId())));
        clientInfo.setFirstMessageId(message.getDatastoreId());
        clientInfo.setFirstMessageOn(message.getTimestamp());
        clientInfoRegistryFacade.upstore(clientInfo);

        ChannelInfoImpl channelInfo = new ChannelInfoImpl(message.getScopeId());
        channelInfo.setClientId(message.getClientId());
        channelInfo.setName(semanticChannel);
        channelInfo.setFirstMessageId(message.getDatastoreId());
        channelInfo.setFirstMessageOn(message.getTimestamp());
        channelInfo.setId(STORABLE_ID_FACTORY.newStorableId(ChannelInfoField.getOrDeriveId(null, channelInfo)));
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
            metricInfo.setId(STORABLE_ID_FACTORY.newStorableId(MetricInfoField.getOrDeriveId(null, metricInfo)));
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
    public void onAfterClientInfoDelete(KapuaId scopeId, ClientInfo clientInfo) {
        // nothing to do at the present
        // the datastore coherence will be guarantee by a periodic task that will scan the datastore looking for a no more referenced info registry record
        // otherwise the computational cost for each delete operation will be too high
    }

    /*
     * ChannelInfo Store Mediator methods
     */
    @Override
    public void onBeforeChannelInfoDelete(ChannelInfo channelInfo) {
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

    public void deleteIndexes(String indexExp) throws ClientException {
        messageStoreFacade.deleteIndexes(indexExp);
        clearCache();
    }

    public void clearCache() {
        DatastoreCacheManager.getInstance().getChannelsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getClientsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetricsCache().invalidateAll();
        DatastoreCacheManager.getInstance().getMetadataCache().invalidateAll();
    }

}
