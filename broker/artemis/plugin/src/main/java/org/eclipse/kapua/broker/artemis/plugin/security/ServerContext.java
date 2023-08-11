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
import org.eclipse.kapua.broker.artemis.plugin.security.context.SecurityContext;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdentity;
import org.eclipse.kapua.client.security.ServiceClient;
import org.eclipse.kapua.client.security.ServiceClientMessagingImpl;

import javax.inject.Inject;
import javax.inject.Named;

public class ServerContext {

    //TODO provide client pluggability once the rest one will be implemented (now just the AMQP client is available)
    protected final String clusterName;
    protected final ServiceClient authServiceClient;
    protected final SecurityContext securityContext;
    protected final BrokerIdentity brokerIdentity;
    protected ActiveMQServer server;
    protected AddressAccessTracker addressAccessTracker;

    @Inject
    public ServerContext(
            @Named("clusterName") String clusterName,
            BrokerIdentity brokerIdentity,
            SecurityContext securityContext) {
        this.clusterName = clusterName;
        this.brokerIdentity = brokerIdentity;
        this.securityContext = securityContext;
        this.authServiceClient = new ServiceClientMessagingImpl(clusterName, brokerIdentity.getBrokerHost());
        addressAccessTracker = new AddressAccessTracker();
    }

    public void init(ActiveMQServer server) throws KapuaException {
        this.server = server;
        brokerIdentity.init(server);
        securityContext.init(server);
    }

    public void shutdown(ActiveMQServer server) throws KapuaException {
        securityContext.shutdown(server);
    }

    public ActiveMQServer getServer() {
        return server;
    }

    public ServiceClient getAuthServiceClient() {
        return authServiceClient;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public BrokerIdentity getBrokerIdentity() {
        return brokerIdentity;
    }

    public String getClusterName() {
        return clusterName;
    }

    public AddressAccessTracker getAddressAccessTracker() {
        return addressAccessTracker;
    }
}
