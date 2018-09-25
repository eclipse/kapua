/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.processor.commons;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.kapua.commons.core.vertx.AbstractHttpRestServer;
import org.eclipse.kapua.commons.core.vertx.HttpRestServerConfig;

public class HttpRestServerImpl extends AbstractHttpRestServer {

    @Inject
    @Named("vertx.metrics-root")
    private String metricsRoot;

    @Inject
    @Named("http-server.host")
    private String host;

    @Inject
    @Named("http-server.port")
    private int port;

    @Override
    public HttpRestServerConfig getConfigs() {
        HttpRestServerConfig config = new HttpRestServerConfig();
        config.setHost(this.host);
        config.setMetricsRoot(this.metricsRoot);
        config.setPort(port);
        return config;
    }
}
