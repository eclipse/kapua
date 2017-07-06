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
package org.eclipse.kapua.app.console.module.data.shared.util;

import org.eclipse.kapua.app.console.module.data.client.GwtTopic;
import org.eclipse.kapua.app.console.module.data.client.util.GwtMessage;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtHeader;
import org.eclipse.kapua.service.datastore.model.ChannelInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MetricInfo;

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
        return new GwtDatastoreDevice(client.getClientId(), client.getLastMessageOn());
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
}
