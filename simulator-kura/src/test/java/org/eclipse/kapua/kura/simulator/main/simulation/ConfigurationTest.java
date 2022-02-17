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
package org.eclipse.kapua.kura.simulator.main.simulation;

import org.assertj.core.api.Assertions;

import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Application;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ConfigurationTest {

    @Test
    public void testNotNull() {
        Assertions.assertThatThrownBy(() -> new Configuration().setApplications(null)).isInstanceOf(NullPointerException.class);

        Assertions.assertThatThrownBy(() -> new Application().setScheduler(null)).isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> new Application().setTopics(null)).isInstanceOf(NullPointerException.class);
        Assertions.assertThatThrownBy(() -> new Application().setGenerators(null)).isInstanceOf(NullPointerException.class);

        Assertions.assertThatThrownBy(() -> new Topic().setMetrics(null)).isInstanceOf(NullPointerException.class);
    }
}
