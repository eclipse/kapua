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
package org.eclipse.kapua.kura.simulator.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Configurations {

    private Configurations() {
    }

    public static List<Simulation> createSimulations(final Configuration configuration) {
        if (configuration == null) {
            return Collections.emptyList();
        }

        configuration.validate();

        final List<Simulation> result = new ArrayList<>(configuration.getApplications().size());

        for (final Map.Entry<String, Configuration.Application> entry : configuration.getApplications().entrySet()) {
            result.add(ConfiguredSimulation.from(entry.getValue(), entry.getKey()));
        }

        return result;
    }

}
