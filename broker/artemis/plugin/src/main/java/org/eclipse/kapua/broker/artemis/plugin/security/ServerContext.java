/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis.plugin.security;

import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.artemis.plugin.security.context.SecurityContextHandler;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdentity;
import org.eclipse.kapua.client.security.ServiceClient;
import org.eclipse.kapua.client.security.ServiceClientMessagingImpl;

public class ServerContext {

    //TODO provide client pluggability once the rest one will be implemented (now just the AMQP client is available)
    //TODO manage through injection if possible
    protected ServiceClient authServiceClient;
    protected SecurityContextHandler securityContextHandler;
    protected BrokerIdentity brokerIdentity;

    private final static ServerContext INSTANCE = new ServerContext();

    public static ServerContext getInstance() {
        return INSTANCE;
    }

    public void init(ActiveMQServer server) throws KapuaException {
        //TODO see comment above
        brokerIdentity = BrokerIdentity.getInstance();
        brokerIdentity.init(server);
        authServiceClient = new ServiceClientMessagingImpl(brokerIdentity.getBrokerHost());
        securityContextHandler = SecurityContextHandler.getInstance();
        brokerIdentity.init(server);
    }

    public ServiceClient getAuthServiceClient() {
        return authServiceClient;
    }

    public SecurityContextHandler getSecurityContextHandler() {
        return securityContextHandler;
    }

    public BrokerIdentity getBrokerIdentity() {
        return brokerIdentity;
    }

}
