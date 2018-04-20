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
package org.eclipse.kapua.kura.simulator.app;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.eclipse.kapua.kura.simulator.Transport;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kapua.kura.simulator.topic.Topic;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;

import com.google.protobuf.ByteString;

public interface Sender {

    public void send(KuraPayload.Builder payload);

    public default void send(final byte[] body) {
        send(Collections.emptyMap(), body);
    }

    public default void send(final String body) {
        send(Collections.emptyMap(), body.getBytes(StandardCharsets.UTF_8));
    }

    public default void send(final String body, final Charset charset) {
        send(Collections.emptyMap(), body.getBytes(charset));
    }

    public default void send(final Map<String, Object> metrics) {
        send(metrics, null);
    }

    public default void send() {
        send(null, (byte[]) null);
    }

    public default void send(final Map<String, Object> metrics, final byte[] body) {
        final Builder payload = KuraPayload.newBuilder();

        if (metrics != null) {
            Metrics.buildMetrics(payload, metrics);
        }

        if (body != null) {
            payload.setBody(ByteString.copyFrom(body));
        }

        send(payload);
    }

    public static Sender transportSender(final Topic topic, final Transport transport) {
        return new Sender() {

            @Override
            public void send(final Builder payload) {
                transport.sendMessage(topic, payload.build().toByteArray());
            }
        };
    }
}
