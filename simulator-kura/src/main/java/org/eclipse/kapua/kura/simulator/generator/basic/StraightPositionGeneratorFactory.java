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
package org.eclipse.kapua.kura.simulator.generator.basic;

import static java.time.Duration.ofDays;
import static java.time.Duration.ofMinutes;
import static java.util.Optional.of;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.Generators;
import org.eclipse.kapua.kura.simulator.generator.Payload;
import org.eclipse.kapua.kura.simulator.generator.Position;

/**
 * A position generator, moving along a straight line
 * <p>
 * This generation creates a position which:
 * </p>
 * <ul>
 * <li>Moves in 80 days around the globe</li>
 * <li>With a speed between 10 and 100 m/s</li>
 * <li>On the equator</li>
 * <li>Always heads east</li>
 * <li>Reports a precision between 0.5 and 25.0</li>
 * <li>Reports between 0 and 10 satellites</li>
 * </ul>
 */
public class StraightPositionGeneratorFactory extends AbstractGeneratorFactory {

    private static final ToDoubleFunction<Instant> satellitesFunction = Generators.sineDoubleBetween(ofMinutes(10), 0, 10, null);
    private static final ToDoubleFunction<Instant> precisionFunction = Generators.sineDoubleBetween(ofMinutes(10), 0.5, 25.0, null);

    private static final ToDoubleFunction<Instant> altitudeFunction = Generators.sineDoubleBetween(ofMinutes(10), 0.0, 1_000.0, null);
    private static final ToDoubleFunction<Instant> longitudeFunction = Generators.sineDouble(ofDays(80), -90.0, 90.0, null);
    private static final ToDoubleFunction<Instant> speedFunction = Generators.sineDouble(ofMinutes(2), 10.0, 100.0, null);

    public StraightPositionGeneratorFactory() {
        super("spos");
    }

    @Override
    protected Optional<Generator> createFrom(final Map<String, Object> configuration) {
        return of(timestamp -> new Payload(null, createPosition(timestamp), null));
    }

    protected static Position createPosition(final Instant timestamp) {
        final double latitude = 0.0; // equator
        final double longitude = longitudeFunction.applyAsDouble(timestamp);
        final double heading = Math.toRadians(90.0D); // heading east
        final double speed = speedFunction.applyAsDouble(timestamp);

        final int satellites = (int) Math.round(satellitesFunction.applyAsDouble(timestamp));
        final double precision = precisionFunction.applyAsDouble(timestamp);
        final double altitude = altitudeFunction.applyAsDouble(timestamp);

        return Position.from(altitude, heading, latitude, longitude, precision, satellites, speed, timestamp);
    }

}
