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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.kura.simulator.app.Application;
import org.eclipse.kapua.kura.simulator.app.ApplicationContext;
import org.eclipse.kapua.kura.simulator.app.Handler;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.simulation.Configuration;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.MetricsMapping;
import org.eclipse.kapua.kura.simulator.simulation.Configuration.Topic;
import org.eclipse.kapua.kura.simulator.simulation.Configurations;
import org.eclipse.kapua.kura.simulator.simulation.Simulation;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto.KuraPayload.Builder;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class AppTest {

    @Test
    public void test1() throws Exception {
        final Configuration cfg = new Configuration();
        final Configuration.Application app1 = new Configuration.Application();
        cfg.getApplications().put("app1", app1);

        final Map<String, Object> data = new HashMap<>();
        data.put("type", "sine");
        data.put("period", 120.0 * 1_000.0);
        app1.getGenerators().put("gen1", data);

        final Topic t1 = new Topic();
        t1.getMetrics().put("m1", new MetricsMapping("gen1", "value"));
        app1.getTopics().put("t1", t1);

        final List<Simulation> sims = Configurations.createSimulations(cfg);

        final List<Builder> payloads = new LinkedList<>();

        for (final Simulation sim : sims) {
            final Application app = sim.createApplication("sim1");
            try (final Handler handler = app.createHandler(new ApplicationContext() {

                @Override
                public Sender sender(org.eclipse.kapua.kura.simulator.topic.Topic topic) {
                    return new Sender() {

                        @Override
                        public void send(Builder payload) {
                            System.out.format("Sending: %s%n", payload);
                            payloads.add(payload);
                        }
                    };
                }
            })) {
                handler.connected();
                Thread.sleep(5_000);
            }
        }

        for (final Simulation sim : sims) {
            sim.close();
        }

        System.out.println(payloads);
    }
}
