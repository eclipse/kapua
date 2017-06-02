/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

public abstract class AbstractMqttTransport implements Transport {

    protected final Map<String, String> topicContext;

    public AbstractMqttTransport(final GatewayConfiguration configuration) {
        final Map<String, String> topicContext = new HashMap<>();
        topicContext.put("account-name", configuration.getAccountName());
        topicContext.put("client-id", configuration.getClientId());

        this.topicContext = Collections.unmodifiableMap(topicContext);

    }

    protected MqttConnectOptions createConnectOptions(final String brokerUrl) {
        try {
            final MqttConnectOptions result = new MqttConnectOptions();
            result.setAutomaticReconnect(true);

            final String ui = (new URIBuilder(brokerUrl)).getUserInfo();
            if (ui != null && !ui.isEmpty()) {
                final String[] toks = ui.split("\\:", 2);
                if (toks.length == 2) {
                    result.setUserName(toks[0]);
                    result.setPassword(toks[1].toCharArray());
                }
            }

            return result;
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Failed to create MQTT options", e);

        }
    }

    protected static String plainUrl(final String brokerUrl) {
        try {
            final URIBuilder u = new URIBuilder(brokerUrl);
            u.setUserInfo(null);
            return u.build().toString();
        } catch (final URISyntaxException e) {
            throw new RuntimeException("Failed to clean up broker URL", e);
        }
    }

}
