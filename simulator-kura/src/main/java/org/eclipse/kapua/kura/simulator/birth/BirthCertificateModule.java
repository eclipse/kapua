/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
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
package org.eclipse.kapua.kura.simulator.birth;

import java.time.Instant;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.Module;
import org.eclipse.kapua.kura.simulator.Transport;
import org.eclipse.kapua.kura.simulator.app.Sender;
import org.eclipse.kapua.kura.simulator.topic.Topic;

public class BirthCertificateModule implements Module {

    private final BirthCertificateBuilder birthCertificateBuilder;

    public BirthCertificateModule(final GatewayConfiguration configuration,
            final Supplier<Set<String>> applicationIds) {
        birthCertificateBuilder = new BirthCertificateBuilder(configuration, Instant.now(), applicationIds);
    }

    @Override
    public void connected(final Transport transport) {
        Sender.transportSender(Topic.device("MQTT/BIRTH"), transport).send(birthCertificateBuilder.build());
    }
}
