/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Descriptor;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kapua.kura.simulator.topic.Topic;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;

public class SimulatedDeviceApplication implements Application {

    private class HandlerImpl implements Handler {

        private final ApplicationContext context;

        public HandlerImpl(final ApplicationContext context) {
            this.context = context;
        }

        @Override
        public void close() throws Exception {
            closeHandler(HandlerImpl.this);
        }

        public void publishData(final Topic data, final Builder builder) {
            context.sender(data).send(builder);
        }

    }

    private final Set<HandlerImpl> handlers = new CopyOnWriteArraySet<>();
    private Descriptor descriptor;

    public SimulatedDeviceApplication(String applicationId) {
        this.descriptor = new Descriptor(applicationId);
    }

    @Override
    public Descriptor getDescriptor() {
        return this.descriptor;
    }

    @Override
    public Handler createHandler(final ApplicationContext context) {
        final HandlerImpl handler = new HandlerImpl(context);
        handlers.add(handler);
        return handler;
    }

    public void closeHandler(final HandlerImpl handler) {
        handlers.remove(handler);
    }

    public void publishData(final String topic, final Instant timestamp, final Map<String, Object> data) {
        final Builder builder = KuraPayload.newBuilder();
        builder.setTimestamp(timestamp.toEpochMilli());
        Metrics.buildMetrics(builder, data);
        handlers.forEach(handler -> handler.publishData(Topic.data(topic), builder));
    }

}
