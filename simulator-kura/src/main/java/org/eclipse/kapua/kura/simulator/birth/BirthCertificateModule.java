/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
