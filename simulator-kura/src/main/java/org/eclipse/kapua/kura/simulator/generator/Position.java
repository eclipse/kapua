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
package org.eclipse.kapua.kura.simulator.generator;

import java.time.Instant;

public class Position {

    private static final Position EMPTY = new Position(null, null, null, null, null, null, null, null);

    private final Double altitude;
    private final Double heading;
    private final Double latitude;
    private final Double longitude;
    private final Double precision;
    private final Integer satellites;
    private final Double speed;
    private final Instant timestamp;

    private Position(
            final Double altitude,
            final Double heading,
            final Double latitude,
            final Double longitude,
            final Double precision,
            final Integer satellites,
            final Double speed,
            final Instant timestamp) {

        this.altitude = altitude;
        this.heading = heading;
        this.latitude = latitude;
        this.longitude = longitude;
        this.precision = precision;
        this.satellites = satellites;
        this.speed = speed;
        this.timestamp = timestamp;

    }

    public Double getAltitude() {
        return this.altitude;
    }

    public Double getHeading() {
        return this.heading;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Double getPrecision() {
        return this.precision;
    }

    public Integer getSatellites() {
        return this.satellites;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public static Position from(final Double altitude, final Double heading, final Double latitude, final Double longitude, final Double precision, final Integer satellites, final Double speed,
            final Instant timestamp) {
        return new Position(altitude, heading, latitude, longitude, precision, satellites, speed, timestamp);
    }

    public static Position empty() {
        return EMPTY;
    }
}
