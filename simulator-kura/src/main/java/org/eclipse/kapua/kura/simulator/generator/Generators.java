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
package org.eclipse.kapua.kura.simulator.generator;

import static java.util.Collections.singletonMap;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Descriptor;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.app.data.SimplePeriodicGenerator;

public final class Generators {

    private Generators() {
    }

    public static Function<Instant, Double> sine(final Duration period, final double amplitude, final double offset, final Short shift) {
        final double freq = 1.0 / period.toMillis() * Math.PI * 2.0;
        if (shift == null) {
            return (timestamp) -> Math.sin(freq * timestamp.toEpochMilli()) * amplitude + offset;
        } else {
            final double radShift = Math.toRadians(shift);
            return (timestamp) -> Math.sin(freq * timestamp.toEpochMilli() + radShift) * amplitude + offset;
        }
    }

    public static Function<Instant, Map<String, Object>> fromSingle(final String name, final Function<Instant, Double> function) {
        return function.andThen(value -> singletonMap(name, value));
    }

    public static Application simpleDataApplication(final String applicationId, final GeneratorScheduler scheduler, final String metricName,
            final Function<Instant, Double> metricFunction) {
        return createApplication(applicationId, scheduler, "metrics", singletonMap(metricName, metricFunction));
    }

    public static Application createApplication(final String applicationId, final GeneratorScheduler scheduler, final String dataTopic,
            final String metricName, final Function<Instant, Double> metricFunction) {
        return createApplication(applicationId, scheduler, dataTopic, singletonMap(metricName, metricFunction));
    }

    public static Application createApplication(final String applicationId, final GeneratorScheduler scheduler, final String dataTopic,
            final Map<String, Function<Instant, Double>> generators) {

        final Descriptor descriptor = new Descriptor(applicationId);

        return new Application() {

            @Override
            public Descriptor getDescriptor() {
                return descriptor;
            }

            @Override
            public Handler createHandler(final ApplicationContext context) {
                return new SimplePeriodicGenerator(context, scheduler, dataTopic, generators);
            }

        };
    }
}
