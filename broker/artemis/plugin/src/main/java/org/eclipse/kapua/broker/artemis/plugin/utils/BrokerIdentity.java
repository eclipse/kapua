/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.artemis.plugin.utils;

import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.eclipse.kapua.KapuaException;

import javax.inject.Inject;
import javax.inject.Named;

public class BrokerIdentity {

    private String brokerId;
    private final BrokerIdResolver brokerIdResolver;
    private final String brokerHost;

    @Inject
    public BrokerIdentity(BrokerIdResolver brokerIdResolver,
                          @Named("brokerHost") String brokerHost) {
        this.brokerIdResolver = brokerIdResolver;
        this.brokerHost = brokerHost;
    }

    public synchronized void init(ActiveMQServer server) throws KapuaException {
        this.brokerId = brokerIdResolver.getBrokerId(server);
    }

    public String getBrokerId() {
        return brokerId;
    }

    public String getBrokerHost() {
        return brokerHost;
    }

}
