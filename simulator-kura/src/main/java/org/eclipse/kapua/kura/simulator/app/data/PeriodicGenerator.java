/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.GeneratorScheduler;
import org.eclipse.kapua.kura.simulator.generator.Payload;
import org.eclipse.kapua.kura.simulator.payload.Metrics;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.MetricsMapping;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;

public class PeriodicGenerator implements Handler {

    public static class Topic {

        private final String positionGenerator;

        private final String bodyGenerator;

        private final Map<String, MetricsMapping> metricMappings;

        public Topic(final String positionGenerator, final String bodyGenerator, final Map<String, MetricsMapping> metricMappings) {
            this.positionGenerator = positionGenerator;
            this.bodyGenerator = bodyGenerator;

            if (metricMappings == null) {
                this.metricMappings = Collections.emptyMap();
            } else {
                this.metricMappings = Collections.unmodifiableMap(new HashMap<>(metricMappings));
            }
        }

        public String getBodyGenerator() {
            return this.bodyGenerator;
        }

        public String getPositionGenerator() {
            return this.positionGenerator;
        }

        public Map<String, MetricsMapping> getMetricMappings() {
            return this.metricMappings;
        }

    }

    private final ApplicationContext context;

    private final GeneratorScheduler.Handle handle;

    private final Map<String, Generator> generators;

    private final Map<String, Topic> topics;

    public PeriodicGenerator(final ApplicationContext context, final GeneratorScheduler scheduler, final Map<String, Generator> generators, final Map<String, Topic> topics) {
        this.context = context;
        this.handle = scheduler.add(this::tick);

        this.generators = Objects.requireNonNull(generators);
        this.topics = Objects.requireNonNull(topics);
    }

    @Override
    public void close() throws Exception {
        this.handle.remove();
    }

    protected void tick(final Instant timestamp) {

        // process generators

        final Map<String, Payload> values = new HashMap<>();
        for (final Map.Entry<String, Generator> entry : this.generators.entrySet()) {
            values.put(entry.getKey(), entry.getValue().update(timestamp));
        }

        // process topics

        for (final Map.Entry<String, Topic> topicEntry : this.topics.entrySet()) {

            final Sender sender = this.context.sender(org.eclipse.kapua.kura.simulator.topic.Topic.data(topicEntry.getKey()));

            final Builder payload = KuraPayload.newBuilder();
            payload.setTimestamp(timestamp.toEpochMilli());

            final Topic topic = topicEntry.getValue();

            // set body

            if (topic.getBodyGenerator() != null) {
                final Payload generatorPayload = values.get(topic.getBodyGenerator());
                if (generatorPayload != null) {
                    Metrics.buildBody(payload, generatorPayload.getBody());
                }
            }

            // set position

            if (topic.getPositionGenerator() != null) {
                final Payload generatorPayload = values.get(topic.getPositionGenerator());
                if (generatorPayload != null) {
                    Metrics.buildPosition(payload, generatorPayload.getPosition());
                }
            }

            // set metrics

            for (final Map.Entry<String, MetricsMapping> entry : topic.getMetricMappings().entrySet()) {

                // get ref

                String generator = entry.getValue().getGenerator();
                if (generator == null || generator.isEmpty()) {
                    generator = null;
                }
                String value = entry.getValue().getValue();
                if (value == null || value.isEmpty()) {
                    value = "value";
                }

                // get generator values

                final Map<String, Object> generatorValues;
                if (generator == null) {
                    // pick first generator
                    final Iterator<Payload> i = values.values().iterator();
                    if (i.hasNext()) {
                        generatorValues = i.next().getMetrics();
                    } else {
                        generatorValues = Collections.emptyMap();
                    }
                } else {
                    final Payload generatorPayload = values.get(generator);
                    if (generatorPayload != null) {
                        generatorValues = generatorPayload.getMetrics();
                    } else {
                        generatorValues = Collections.emptyMap();
                    }
                }

                // map metrics

                Metrics.addMetric(payload, entry.getKey(), generatorValues.get(value));
            }

            sender.send(payload);
        }
    }
}
