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
package org.eclipse.kapua.kura.simulator.main.simulation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.MetricsMapping;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.eclipse.kapua.kura.simulator.simulation.JsonReader;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class JsonTest {

    @Test
    public void test1() throws IOException {
        final Configuration cfg;

        try (final InputStream in = JsonTest.class.getResourceAsStream("test1.json")) {
            cfg = JsonReader.parse(in, StandardCharsets.UTF_8);
        }

        Assert.assertNotNull(cfg);
        Assert.assertNotNull(cfg.getApplications());

        // applications

        Assertions.assertThat(cfg.getApplications()).isEmpty();
    }

    @Test
    public void test2() throws IOException {
        final Configuration cfg;

        try (final InputStream in = JsonTest.class.getResourceAsStream("test2.json")) {
            cfg = JsonReader.parse(in, StandardCharsets.UTF_8);
        }

        Assert.assertNotNull(cfg);
        Assert.assertNotNull(cfg.getApplications());

        Assertions.assertThat(cfg.getApplications()).containsOnlyKeys("app1");

        // application : app1

        final Configuration.Application app1 = cfg.getApplications().get("app1");

        Assert.assertNotNull(app1.getScheduler());
        Assert.assertNotNull(app1.getGenerators());
        Assert.assertNotNull(app1.getTopics());

        // properties

        Assert.assertEquals(app1.getScheduler().getPeriod(), 2000);

        // metrics

        Assertions.assertThat(app1.getTopics())
                .isNotEmpty()
                .containsOnlyKeys("t1/data", "t2/data");

        final Topic t1 = app1.getTopics().get("t1/data");
        Assertions.assertThat(t1.getMetrics())
                .isNotEmpty()
                .containsOnlyKeys("temp1", "temp2");

        final MetricsMapping temp1 = t1.getMetrics().get("temp1");
        Assert.assertEquals(temp1.getGenerator(), "sine1");
        Assert.assertEquals(temp1.getValue(), "value");

        final MetricsMapping temp2 = t1.getMetrics().get("temp2");
        Assert.assertEquals(temp2.getGenerator(), "sine2");
        Assert.assertEquals(temp2.getValue(), "value");

        // generators

        Assertions.assertThat(app1.getGenerators())
                .isNotEmpty()
                .containsOnlyKeys("body-gen", "position-gen", "sine1", "sine2", "foo");

        Assertions.assertThat(app1.getGenerators().get("sine1"))
                .containsEntry("type", "sine")
                .containsEntry("period", 1000.0)
                .containsEntry("offset", 50.0)
                .containsEntry("amplitude", 100.0);

        Assertions.assertThat(app1.getGenerators().get("sine2"))
                .containsEntry("type", "sine")
                .containsEntry("period", 2000.0)
                .containsEntry("shift", 45.5)
                .containsEntry("offset", 30.0);

        Assertions.assertThat(app1.getGenerators().get("foo"))
                .containsEntry("mydata", Collections.singletonMap("foo", "bar"));
    }
}
