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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Configuration {

    public static final String DEFAULT_VALUE_NAME = "value";

    public static class Scheduler {

        private long period = 1000L;

        public void setPeriod(final long period) {
            this.period = period;
        }

        public long getPeriod() {
            return this.period;
        }

        private void validate(final List<Exception> violations) {
            if (this.period <= 0) {
                violations.add(new IllegalArgumentException(String.format("Scheduler period must be greater than zero: ", this.period)));
            }
        }
    }

    public static class Topic {

        private String bodyGenerator;
        private String positionGenerator;

        private Map<String, MetricsMapping> metrics = new HashMap<>();

        public void setBodyGenerator(final String bodyGenerator) {
            this.bodyGenerator = bodyGenerator;
        }

        public String getBodyGenerator() {
            return this.bodyGenerator;
        }

        public void setPositionGenerator(final String positionGenerator) {
            this.positionGenerator = positionGenerator;
        }

        public String getPositionGenerator() {
            return this.positionGenerator;
        }

        public void setMetrics(final Map<String, MetricsMapping> metrics) {
            Objects.requireNonNull(metrics);
            this.metrics = metrics;
        }

        public Map<String, MetricsMapping> getMetrics() {
            return this.metrics;
        }

        private void validate(final Map<String, Map<String, Object>> generators, final String id, final List<Exception> violations) {

            if (this.bodyGenerator != null && !this.bodyGenerator.isEmpty()) {
                validateGeneratorReference(generators, String.format("Topic %s/body", id), this.bodyGenerator, violations);
            }

            if (this.positionGenerator != null && !this.positionGenerator.isEmpty()) {
                validateGeneratorReference(generators, String.format("Topic %s/position", id), this.positionGenerator, violations);
            }

            for (final Map.Entry<String, MetricsMapping> entry : this.metrics.entrySet()) {
                final String generator = entry.getValue().getGenerator();
                if (generator != null && !generator.isEmpty()) {
                    validateGeneratorReference(generators, String.format("Topic %s/Metric %s", id, entry.getKey()), generator, violations);
                } else if (generators.size() != 1) {
                    violations.add(new IllegalStateException(
                            String.format("Topic %s/Metric %s uses 'default' generator which requires exactly one generation, but there are: %s", id, entry.getKey(), generators.size())));
                }
            }

        }

        private void validateGeneratorReference(final Map<String, Map<String, Object>> generators, final String location, final String generator, final List<Exception> violations) {
            if (!generators.containsKey(generator)) {
                violations.add(new IllegalStateException(String.format(" %s reference non-existing generator %s", location, generator)));
            }
        }

    }

    public static class MetricsMapping {

        private String generator;

        private String value = DEFAULT_VALUE_NAME;

        public MetricsMapping() {
        }

        public MetricsMapping(final String generator, final String value) {
            this.generator = generator;
            this.value = value;
        }

        public String getGenerator() {
            return this.generator;
        }

        public void setGenerator(final String generator) {
            this.generator = generator;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(final String value) {
            this.value = value;
        }
    }

    public static class Application {

        private Scheduler scheduler = new Scheduler();

        private Map<String, Topic> topics = new HashMap<>();

        private Map<String, Map<String, Object>> generators = new HashMap<>();

        public void setScheduler(final Scheduler scheduler) {
            Objects.requireNonNull(scheduler);
            this.scheduler = scheduler;
        }

        public Scheduler getScheduler() {
            return this.scheduler;
        }

        public void setGenerators(final Map<String, Map<String, Object>> generators) {
            Objects.requireNonNull(generators);
            this.generators = generators;
        }

        public Map<String, Map<String, Object>> getGenerators() {
            return this.generators;
        }

        public void setTopics(final Map<String, Topic> topics) {
            Objects.requireNonNull(topics);
            this.topics = topics;
        }

        public Map<String, Topic> getTopics() {
            return this.topics;
        }

        public void validate() throws IllegalStateException {
            final List<Exception> violations = new LinkedList<>();

            validate(violations);

            if (!violations.isEmpty()) {
                final IllegalStateException ex = new IllegalStateException("Configuration has validation errors");
                violations.forEach(ex::addSuppressed);
                throw ex;
            }
        }

        void validate(final List<Exception> violations) {
            this.scheduler.validate(violations);
            this.topics.forEach((id, topic) -> topic.validate(this.generators, id, violations));
        }
    }

    private Map<String, Application> applications = new HashMap<>();

    public void setApplications(final Map<String, Application> applications) {
        Objects.requireNonNull(applications);
        this.applications = applications;
    }

    public Map<String, Application> getApplications() {
        return this.applications;
    }

    public void validate() throws IllegalStateException {
        final List<Exception> violations = new LinkedList<>();

        this.applications.values().forEach(app -> app.validate(violations));

        if (!violations.isEmpty()) {
            final IllegalStateException ex = new IllegalStateException("Configuration has validation errors");
            violations.forEach(ex::addSuppressed);
            throw ex;
        }
    }
}
