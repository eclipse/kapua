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
package org.eclipse.kapua.client.gateway;

import java.time.Duration;

import org.eclipse.kapua.client.gateway.mqtt.fuse.FuseChannel;
import org.eclipse.kapua.client.gateway.profile.kura.KuraMqttProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class KuraProfileExample {

    private static final Logger logger = LoggerFactory.getLogger(KuraProfileExample.class);

    private KuraProfileExample() {
    }

    public static void main(final String[] args) throws Exception {

        try (final Client client = KuraMqttProfile.newProfile(FuseChannel.Builder::new)
                .accountName("kapua-sys")
                .clientId("foo-bar-1")
                .brokerUrl("tcp://localhost:1883")
                .credentials(Credentials.userAndPassword("kapua-broker", "kapua-password"))
                .build()) {

            try (final Application application = client.buildApplication("app1").build()) {

                // wait for connection

                if (!Transport.waitForConnection(application.transport(), Duration.ofSeconds(30))) {
                    throw new RuntimeException("Unable to connect to target broker");
                }

                // subscribe to a topic

                application.data(Topic.of("my", "topic")).subscribe(message -> {
                    System.out.format("Received: %s%n", message);
                });

                // example payload

                final Payload.Builder payload = new Payload.Builder();
                payload.put("foo", "bar");
                payload.put("a", 1);

                try {

                    // send, handling error ourself

                    application
                            .data(Topic.of("my", "topic"))
                            .send(payload)
                            .toCompletableFuture().get();
                } catch (final Exception e) {
                    logger.info("Failed to publish", e);
                }

                // send data

                application
                        .data(Topic.of("my", "topic"))
                        .send(payload);

                // cache sender instance

                final Sender sender = application.data(Topic.of("my", "topic"));

                for (int i = 0; i < 10; i++) {

                    // send

                    sender.send(Payload.of("counter", i)).whenComplete((value, error) -> {
                        System.out.format("Send complete - value: %s, error: %s%n", value, error);
                    });

                    Thread.sleep(1_000);
                }

                // sleep to not run into Paho thread starvation
                // Thread.sleep(100_000);
            }

            Thread.sleep(1_000);

        }
    }
}
