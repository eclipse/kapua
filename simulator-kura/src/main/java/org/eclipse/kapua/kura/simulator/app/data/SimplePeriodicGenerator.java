/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.app.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;

public class SimplePeriodicGenerator extends AbstractSingleTopicPeriodicGenerator {

    private final Map<String, Function<Instant, Double>> generators;

    public SimplePeriodicGenerator(final ApplicationContext context, final GeneratorScheduler scheduler, final String dataTopic, final Map<String, Function<Instant, Double>> generators) {
        super(context, scheduler, dataTopic);
        this.generators = generators;
    }

    @Override
    protected void update(final Instant timestamp, final Sender sender) {
        final Map<String, Object> metrics = generateMetrics(timestamp);

        final Builder builder = KuraPayloadProto.KuraPayload.newBuilder();
        Metrics.buildMetrics(builder, metrics);
        builder.setTimestamp(timestamp.toEpochMilli());
        sender.send(builder);
    }

    protected Map<String, Object> generateMetrics(final Instant timestamp) {
        final Map<String, Object> result = new HashMap<>(this.generators.size());

        for (final Map.Entry<String, Function<Instant, Double>> entry : this.generators.entrySet()) {
            result.put(entry.getKey(), entry.getValue().apply(timestamp));
        }

        return result;
    }
}