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
