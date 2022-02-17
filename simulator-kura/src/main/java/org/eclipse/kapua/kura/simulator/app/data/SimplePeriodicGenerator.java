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
package org.eclipse.kapua.kura.simulator.app.data;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.GeneratorScheduler;
import org.eclipse.kapua.kura.simulator.generator.Payload;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;

public class SimplePeriodicGenerator extends AbstractSingleTopicPeriodicGenerator {

    private final Generator generator;

    public SimplePeriodicGenerator(final ApplicationContext context, final GeneratorScheduler scheduler, final String dataTopic, final Generator generator) {
        super(context, scheduler, dataTopic);
        this.generator = generator;
    }

    public SimplePeriodicGenerator(final ApplicationContext context, final GeneratorScheduler scheduler, final String dataTopic, final Map<String, Function<Instant, ?>> generators) {
        super(context, scheduler, dataTopic);
        this.generator = timestamp -> new Payload(generateMetrics(timestamp, generators));
    }

    @Override
    protected void update(final Instant timestamp, final Sender sender) {
        final Payload payload = this.generator.update(timestamp);

        if (payload == null) {
            return;
        }

        final Builder builder = KuraPayloadProto.KuraPayload.newBuilder();
        Metrics.buildPayload(builder, payload);
        builder.setTimestamp(timestamp.toEpochMilli());
        sender.send(builder);
    }

    protected static Map<String, Object> generateMetrics(final Instant timestamp, final Map<String, Function<Instant, ?>> generators) {
        final Map<String, Object> result = new HashMap<>(generators.size());

        for (final Map.Entry<String, Function<Instant, ?>> entry : generators.entrySet()) {
            result.put(entry.getKey(), entry.getValue().apply(timestamp));
        }

        return result;
    }
}
