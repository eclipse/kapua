/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.consumer.activemq.datastore;

import org.eclipse.kapua.broker.client.amqp.ClientOptions;
import org.eclipse.kapua.broker.client.amqp.ClientOptions.AmqpClientOptions;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettings;
import org.eclipse.kapua.consumer.activemq.datastore.settings.ActiveMQDatastoreSettingsKey;

import io.vertx.proton.ProtonQoS;

public class SourceConfiguration {

    private boolean autoAccept;

    private String qos;

    private String clientId;

    private String destination;

    private int prefetchMessages;

    public SourceConfiguration() {
        clientId = ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.TELEMETRY_CLIENT_ID);
        destination = ActiveMQDatastoreSettings.getInstance().getString(ActiveMQDatastoreSettingsKey.TELEMETRY_DESTINATION);
        prefetchMessages = ActiveMQDatastoreSettings.getInstance().getInt(ActiveMQDatastoreSettingsKey.TELEMETRY_PREFETCH_MESSAGES);
        autoAccept = false;
        qos = ProtonQoS.AT_LEAST_ONCE.name();
    }

    public boolean isAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(boolean autoAccept) {
        this.autoAccept = autoAccept;
    }

    public String getQos() {
        return qos;
    }

    public void setQos(String qos) {
        this.qos = qos;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getPrefetchMessages() {
        return prefetchMessages;
    }

    public void setPrefetchMessages(int prefetchMessages) {
        this.prefetchMessages = prefetchMessages;
    }

    public ClientOptions createClientOptions(ConnectionConfiguration connectionOptions) {
        ClientOptions options = new ClientOptions(
                connectionOptions.getConnectionHost(),
                connectionOptions.getPort(),
                connectionOptions.getUsername(),
                connectionOptions.getPassword());
        options.put(AmqpClientOptions.AUTO_ACCEPT, this.isAutoAccept());
        options.put(AmqpClientOptions.QOS, ProtonQoS.valueOf(this.getQos()));
        options.put(AmqpClientOptions.CLIENT_ID, this.getClientId());
        options.put(AmqpClientOptions.DESTINATION, this.getDestination());
        options.put(AmqpClientOptions.CONNECT_TIMEOUT, connectionOptions.getConnectTimeout());
        options.put(AmqpClientOptions.MAXIMUM_RECONNECTION_ATTEMPTS, connectionOptions.getMaxReconnectAttempts());
        options.put(AmqpClientOptions.IDLE_TIMEOUT, connectionOptions.getIdelTimeout());
        options.put(AmqpClientOptions.PREFETCH_MESSAGES, this.getPrefetchMessages());
        return options;
    }
}
