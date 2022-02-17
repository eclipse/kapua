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
