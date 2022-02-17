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
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Generator {

    public Payload update(Instant timestamp);

    public static Generator onlyMetrics(final Function<Instant, Map<String, Object>> metrics) {
        Objects.requireNonNull(metrics);

        return new Generator() {

            @Override
            public Payload update(final Instant timestamp) {
                return new Payload(metrics.apply(timestamp));
            }
        };
    }
}
