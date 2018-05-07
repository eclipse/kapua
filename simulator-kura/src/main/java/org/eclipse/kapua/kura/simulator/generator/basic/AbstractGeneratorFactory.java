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

import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.GeneratorFactory;
import org.eclipse.kapua.kura.simulator.util.Get;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractGeneratorFactory implements GeneratorFactory {

    private final Optional<String> type;

    protected AbstractGeneratorFactory(final String typeName) {
        this.type = Optional.of(typeName);
    }

    @Override
    public Optional<Generator> create(final Map<String, Object> configuration) {
        final Optional<String> type = Get.getNonEmptyString(configuration, "type");
        if (!this.type.equals(type)) {
            return Optional.empty();
        }

        return createFrom(configuration);
    }

    protected abstract Optional<Generator> createFrom(Map<String, Object> configuration);
}
