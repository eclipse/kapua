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
package org.eclipse.kapua.commons.core.vertx;

/**
 * Holds the configuration parameters of an {@link HttpService}
 *
 */
public class HttpServiceConfig {

    private String metricsRoot;
    private String host;
    private int port;

    public String getMetricsRoot() {
        return metricsRoot;
    }

    public void setMetricsRoot(String metricsRoot) {
        this.metricsRoot = metricsRoot;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
