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
package org.eclipse.kapua.kura.simulator.generator;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

public final class GeneratorFactories {

    private GeneratorFactories() {
    }

    /**
     * Construct a generator from a configuration map
     * <p>
     * The method will try to create a {@link Generator} instance from the configuration map
     * by iterating through all {@link GeneratorFactory} instances registered with the {@link ServiceLoader}
     * framework.
     * </p>
     * <p>
     * If no generator could be created an empty Optional is being returned
     * </p>
     *
     * @param configuration
     *            the configuration map
     * @return the optional result, never {@code null}
     */
    public static Optional<Generator> create(final Map<String, Object> configuration) {
        return create(configuration, ServiceLoader.load(GeneratorFactory.class));
    }

    /**
     * Construct a generator from a configuration map
     * <p>
     * The method will try to create a {@link Generator} instance from the configuration map
     * by iterating through all {@link GeneratorFactory} instances provided as argument
     * </p>
     * <p>
     * If no generator could be created an empty Optional is being returned
     * </p>
     *
     * @param configuration
     *            the configuration map
     * @param factories
     *            the available factories
     * @return the optional result, never {@code null}
     */
    public static Optional<Generator> create(final Map<String, Object> configuration, final Iterable<GeneratorFactory> factories) {
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(factories);

        for (final GeneratorFactory factory : factories) {
            final Optional<Generator> generator = factory.create(configuration);
            if (generator.isPresent()) {
                return generator;
            }
        }

        return Optional.empty();
    }
}
