/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.commons.app;

/**
 * This class defines the configurations used to create the Vertx instance 
 *
 */
public class VertxConfig {

    private long warningExceptionTime = 10000;
    private long blockedThreadCheckInterval = 10000;
    private MetricsConfig metrics = new MetricsConfig();

    public long getWarningExceptionTime() {
        return warningExceptionTime;
    }

    public void setWarningExceptionTime(long aWarningExceptionTime) {
        warningExceptionTime = aWarningExceptionTime;
    }

    public long getBlockedThreadCheckInterval() {
        return blockedThreadCheckInterval;
    }

    public void setBlockedThreadCheckInterval(long aBlockedThreadCheckInterval) {
        blockedThreadCheckInterval = aBlockedThreadCheckInterval;
    }

    public MetricsConfig getMetrics() {
        return metrics;
    }

    public void setMetrics(MetricsConfig aConfig) {
        metrics = aConfig;
    }

    @Override
    public String toString() {
        return String.format("\"warningExceptionTime\":\"%d\""
                + ", \"blockedThreadCheckInterval\":\"%d\""
                + ", \"metrics\":{%s}", warningExceptionTime, blockedThreadCheckInterval, metrics == null ? "null" : metrics.toString());
    }
}
