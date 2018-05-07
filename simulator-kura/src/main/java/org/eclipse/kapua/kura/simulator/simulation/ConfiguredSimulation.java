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
package org.eclipse.kapua.kura.simulator.simulation;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Descriptor;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.app.data.PeriodicGenerator;
import org.eclipse.kapua.kura.simulator.app.data.PeriodicGenerator.Topic;
import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.GeneratorFactories;
import org.eclipse.kapua.kura.simulator.generator.GeneratorScheduler;

public class ConfiguredSimulation implements Simulation {

    public static ConfiguredSimulation from(final Configuration.Application configuration, final String applicationId) {
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(applicationId);

        // create generators

        final Map<String, Generator> generators = new HashMap<>(configuration.getGenerators().size());

        for (final Map.Entry<String, Map<String, Object>> entry : configuration.getGenerators().entrySet()) {
            final Optional<Generator> generator = GeneratorFactories.create(entry.getValue());
            if (!generator.isPresent()) {
                throw new IllegalStateException(String.format("Unable to find generator factory for: " + entry.getValue()));
            }
            generators.put(entry.getKey(), generator.get());
        }

        // create metric mappings

        final Map<String, Topic> topics = new HashMap<>(configuration.getTopics().size());
        configuration.getTopics().forEach((key, topic) -> topics.put(key, convertTopic(topic)));

        // create scheduler

        final GeneratorScheduler scheduler = new GeneratorScheduler(Duration.ofMillis(configuration.getScheduler().getPeriod()));

        // return result

        return new ConfiguredSimulation(applicationId, scheduler, generators, topics);
    }

    private static Topic convertTopic(final Configuration.Topic value) {
        if (value == null) {
            return null;
        }

        return new Topic(value.getPositionGenerator(), value.getBodyGenerator(), value.getMetrics());
    }

    private final Descriptor descriptor;
    private final GeneratorScheduler scheduler;

    private final Map<String, Generator> generators;
    private final Map<String, Topic> topics;

    private ConfiguredSimulation(final String applicationId, final GeneratorScheduler scheduler, final Map<String, Generator> generators, final Map<String, Topic> topics) {
        this.descriptor = new Descriptor(applicationId);
        this.scheduler = scheduler;
        this.generators = generators;
        this.topics = topics;
    }

    @Override
    public void close() throws Exception {
        this.scheduler.close();
    }

    @Override
    public Application createApplication(final String simulatorId) {
        return new Application() {

            @Override
            public Descriptor getDescriptor() {
                return ConfiguredSimulation.this.descriptor;
            }

            @Override
            public Handler createHandler(final ApplicationContext context) {
                return new PeriodicGenerator(context, ConfiguredSimulation.this.scheduler, ConfiguredSimulation.this.generators, ConfiguredSimulation.this.topics);
            }
        };
    }

}
