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
package org.eclipse.kapua.kura.simulator.generator.basic;

import static java.time.Duration.ofSeconds;
import static java.util.Optional.of;
import static org.eclipse.kapua.kura.simulator.generator.Generators.fromSingle;
import static org.eclipse.kapua.kura.simulator.generator.Generators.sine;
import static org.eclipse.kapua.kura.simulator.util.Get.getDouble;
import static org.eclipse.kapua.kura.simulator.util.Get.getInteger;
import static org.eclipse.kapua.kura.simulator.util.Get.getLong;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.eclipse.kapua.kura.simulator.generator.Generator;

public class SineGeneratorFactory extends AbstractGeneratorFactory {

    public SineGeneratorFactory() {
        super("sine");
    }

    @Override
    protected Optional<Generator> createFrom(final Map<String, Object> configuration) {
        final Duration period = getLong(configuration, "period").map(Duration::ofMillis).orElse(ofSeconds(60));

        final Double amplitude = getDouble(configuration, "amplitude").orElse(100.0);
        final Double offset = getDouble(configuration, "offset").orElse(0.0);
        final Short shift = getInteger(configuration, "shift").map(i -> i.shortValue()).orElse(null);

        return of(Generator.onlyMetrics(fromSingle("value", sine(period, amplitude, offset, shift))));
    }
}
