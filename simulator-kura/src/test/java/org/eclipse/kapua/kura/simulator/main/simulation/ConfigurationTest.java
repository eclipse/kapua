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
package org.eclipse.kapua.kura.simulator.main.simulation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Application;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void testNotNull() {
        assertThatThrownBy(() -> new Configuration().setApplications(null)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Application().setScheduler(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Application().setTopics(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Application().setGenerators(null)).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Topic().setMetrics(null)).isInstanceOf(NullPointerException.class);
    }
}
