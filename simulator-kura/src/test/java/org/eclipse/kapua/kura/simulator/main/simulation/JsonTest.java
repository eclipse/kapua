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

import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.JsonReader;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.MetricsMapping;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.junit.Test;

public class JsonTest {

    @Test
    public void test1() throws IOException {
        final Configuration cfg;

        try (final InputStream in = JsonTest.class.getResourceAsStream("test1.json")) {
            cfg = JsonReader.parse(in, StandardCharsets.UTF_8);
        }

        assertNotNull(cfg);
        assertNotNull(cfg.getApplications());

        // applications

        assertThat(cfg.getApplications()).isEmpty();
    }

    @Test
    public void test2() throws IOException {
        final Configuration cfg;

        try (final InputStream in = JsonTest.class.getResourceAsStream("test2.json")) {
            cfg = JsonReader.parse(in, StandardCharsets.UTF_8);
        }

        assertNotNull(cfg);
        assertNotNull(cfg.getApplications());

        assertThat(cfg.getApplications()).containsOnlyKeys("app1");

        // application : app1

        final Configuration.Application app1 = cfg.getApplications().get("app1");

        assertNotNull(app1.getScheduler());
        assertNotNull(app1.getGenerators());
        assertNotNull(app1.getTopics());

        // properties

        assertEquals(app1.getScheduler().getPeriod(), 2000);

        // metrics

        assertThat(app1.getTopics())
                .isNotEmpty()
                .containsOnlyKeys("t1/data", "t2/data");

        final Topic t1 = app1.getTopics().get("t1/data");
        assertThat(t1.getMetrics())
                .isNotEmpty()
                .containsOnlyKeys("temp1", "temp2");

        final MetricsMapping temp1 = t1.getMetrics().get("temp1");
        assertEquals(temp1.getGenerator(), "sine1");
        assertEquals(temp1.getValue(), "value");

        final MetricsMapping temp2 = t1.getMetrics().get("temp2");
        assertEquals(temp2.getGenerator(), "sine2");
        assertEquals(temp2.getValue(), "value");

        // generators

        assertThat(app1.getGenerators())
                .isNotEmpty()
                .containsOnlyKeys("body-gen", "position-gen", "sine1", "sine2", "foo");

        assertThat(app1.getGenerators().get("sine1"))
                .containsEntry("type", "sine")
                .containsEntry("period", 1000.0)
                .containsEntry("offset", 50.0)
                .containsEntry("amplitude", 100.0);

        assertThat(app1.getGenerators().get("sine2"))
                .containsEntry("type", "sine")
                .containsEntry("period", 2000.0)
                .containsEntry("shift", 45.5)
                .containsEntry("offset", 30.0);

        assertThat(app1.getGenerators().get("foo"))
                .containsEntry("mydata", singletonMap("foo", "bar"));
    }
}
