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

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Application;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.MetricsMapping;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ValidationTest {

    @Test
    public void testEmpty() {
        final Configuration cfg = new Configuration();
        cfg.validate();
    }

    @Test
    public void testScheduler1() {
        final Application cfg = new Application();
        cfg.getScheduler().setPeriod(-100);
        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test1() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.position", "t1.m1", "t1.m2");
        fillTopics(app, "t1");

        cfg.validate();
    }

    @Test
    public void test2() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.position", "t1.m1", "t1.m2", "unused");
        fillTopics(app, "t1");

        cfg.validate();
    }

    @Test
    public void test3MissingBody() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.position", "t1.m1", "t1.m2", "unused");
        fillTopics(app, "t1");

        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test3MissingPosition() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.m1", "t1.m2");
        fillTopics(app, "t1");

        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test3MissingM1() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.position", "t1.m2");
        fillTopics(app, "t1");

        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void test3MissingM2() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.position", "t1.m1");
        fillTopics(app, "t1");

        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void testWrongDefault() {
        final Configuration cfg = new Configuration();
        final Application app = new Application();
        cfg.getApplications().put("app", app);

        fillGenerators(app, "t1.body", "t1.position", "t1.m1");

        final Topic t1 = new Topic();
        t1.getMetrics().put("m1", new MetricsMapping());
        app.getTopics().put("t1", t1);

        Assertions.assertThatThrownBy(cfg::validate).isInstanceOf(IllegalStateException.class);
    }

    private void fillGenerators(final Application app, final String... generators) {
        for (final String generator : generators) {
            app.getGenerators().put(generator, Collections.emptyMap());
        }
    }

    private void fillTopics(final Application app, final String... topics) {
        for (final String topic : topics) {
            final Topic t = new Topic();
            t.setBodyGenerator(topic + ".body");
            t.setPositionGenerator(topic + ".position");
            t.getMetrics().put("m1a", new MetricsMapping(topic + ".m1", "v1a"));
            t.getMetrics().put("m1b", new MetricsMapping(topic + ".m1", "v2"));

            t.getMetrics().put("m2a", new MetricsMapping(topic + ".m2", "v1"));
            t.getMetrics().put("m2b", new MetricsMapping(topic + ".m2", "v1"));
            app.getTopics().put(topic, t);
        }
    }
}
