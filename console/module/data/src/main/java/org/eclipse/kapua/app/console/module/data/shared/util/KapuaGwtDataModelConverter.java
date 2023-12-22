/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.data.shared.util;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.data.client.GwtTopic;
import org.eclipse.kapua.app.console.module.data.client.util.GwtMessage;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientLimitsExceededException;
import org.eclipse.kapua.service.storable.StorableService;
import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.StorableListResult;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

import java.util.List;

public class KapuaGwtDataModelConverter {

    private KapuaGwtDataModelConverter() { }

    public static GwtTopic convertToTopic(ChannelInfo channelInfo) {
        return new GwtTopic(channelInfo.getName(), channelInfo.getName(), channelInfo.getName(), channelInfo.getLastMessageOn());
    }

    public static GwtDatastoreAsset convertToAssets(ChannelInfo channelInfo) {
        return new GwtDatastoreAsset(channelInfo.getName().substring(6), channelInfo.getName(), channelInfo.getName(), channelInfo.getLastMessageOn());
    }

    public static GwtHeader convertToHeader(MetricInfo metric) {
        GwtHeader header = new GwtHeader();
        header.setName(metric.getName());
        header.setType(metric.getMetricType().getSimpleName());
        return header;
    }

    /**
     * @param client
     * @return
     */
    public static GwtDatastoreDevice convertToDatastoreDevice(ClientInfo client) {
        GwtDatastoreDevice device = new GwtDatastoreDevice(client.getClientId(), client.getLastMessageOn());
        device.setClientId(client.getClientId());
        return device;
    }

    /**
     * @param message
     * @param headers
     * @return
     */
    public static GwtMessage convertToMessage(DatastoreMessage message, List<GwtHeader> headers) {
        GwtMessage gwtMessage = new GwtMessage();
        List<String> semanticParts = message.getChannel().getSemanticParts();
        StringBuilder semanticTopic = new StringBuilder();
        for (int i = 0; i < semanticParts.size() - 1; i++) {
            semanticTopic.append(semanticParts.get(i));
            semanticTopic.append("/");
        }
        semanticTopic.append(semanticParts.get(semanticParts.size() - 1));
        gwtMessage.setChannel(semanticTopic.toString());
        gwtMessage.setClientId(message.getClientId());
        gwtMessage.setTimestamp(message.getTimestamp());
        for (GwtHeader header : headers) {
            gwtMessage.set(header.getName(), message.getPayload().getMetrics().get(header.getName()));
        }
        return gwtMessage;
    }

    /**
     * This method counts the documents in ES (clients, channels, messages etc.) with a cap to 10k (the maximum value that the Datastore it's capable of counting).
     * This is accomplished converting the ClientLimitsExceededException thrown when there are more than 10k messages to a result of 10k
     * In a sense, this method is an adapter inserted into the console code for the introduction of the ClientLimitsExceededException, because before counting was capped to 10k as well in the console logic
     * @param service the StorableService instance where you want to count documents
     * @param query the count query that will be performed on the service
     * @return the number of requested documents
     */
    public static <S extends Storable, L extends StorableListResult<S>, Q extends StorableQuery> long countEsDataCap10k(StorableService<S,L,Q> service, Q query) throws KapuaException {
        long totalLength = 0;
        try {
            totalLength = service.count(query);
        } catch (KapuaException e) {
            if (e.getCause() != null && (e.getCause() instanceof ClientLimitsExceededException)) { //case in which there are more than 10k results in datastore but ES cannot count them precisely
                totalLength = 10000;
            } else {
                throw e;
            }
        }
        return totalLength;
    }
}
