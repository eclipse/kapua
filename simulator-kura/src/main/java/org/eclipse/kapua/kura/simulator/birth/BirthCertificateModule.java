/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.kura.simulator.birth;

import static java.time.Instant.now;
import static org.eclipse.kapua.kura.simulator.app.Sender.transportSender;
import static org.eclipse.kapua.kura.simulator.topic.Topic.device;

import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.kapua.kura.simulator.GatewayConfiguration;
import org.eclipse.kapua.kura.simulator.Module;
import org.eclipse.kapua.kura.simulator.Transport;

public class BirthCertificateModule implements Module {

	private final BirthCertificateBuilder birthCertificateBuilder;

	public BirthCertificateModule(final GatewayConfiguration configuration,
			final Supplier<Set<String>> applicationIds) {
		this.birthCertificateBuilder = new BirthCertificateBuilder(configuration, now(), applicationIds);
	}

	@Override
	public void connected(final Transport transport) {
		transportSender(device("MQTT/BIRTH"), transport).send(this.birthCertificateBuilder.build());
	}
}
