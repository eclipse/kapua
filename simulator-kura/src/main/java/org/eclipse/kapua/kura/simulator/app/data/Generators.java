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

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Descriptor;
import org.eclipse.kapua.kura.simulator.app.Handler;

public final class Generators {

    private Generators() {
    }

    public static Function<Instant, Double> sine(final double amplitude, final double offset, final Duration period) {
        final double freq = 1.0 / period.toMillis() * Math.PI * 2.0;
        return (timestamp) -> Math.sin(freq * timestamp.toEpochMilli()) * amplitude + offset;
    }

    public static Application simpleDataApplication(final String applicationId, final GeneratorScheduler scheduler, final String metricName,
            final Function<Instant, Double> metricFunction) {
        return createApplication(applicationId, scheduler, "metrics", Collections.singletonMap(metricName, metricFunction));
    }

    public static Application createApplication(final String applicationId, final GeneratorScheduler scheduler, final String dataTopic,
            final String metricName, final Function<Instant, Double> metricFunction) {
        return createApplication(applicationId, scheduler, dataTopic, Collections.singletonMap(metricName, metricFunction));
    }

    public static Application createApplication(final String applicationId, final GeneratorScheduler scheduler, final String dataTopic,
            final Map<String, Function<Instant, Double>> generators) {
        return new Application() {

            @Override
            public Descriptor getDescriptor() {
                return new Descriptor(applicationId);
            }

            @Override
            public Handler createHandler(final ApplicationContext context) {
                return new SimplePeriodicGenerator(context, scheduler, dataTopic, generators);
            }

        };
    }
}
