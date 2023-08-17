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
package org.eclipse.kapua.client.security.metric;

import com.codahale.metrics.Counter;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class AuthLoginMetric {

    private static final String CONNECT = "connect";
    private static final String ILLEGAL_STATE = "illegal_state";
    private static final String STEALING_LINK = "stealing_link";

    private Counter attempt;
    private Counter connected;
    private Counter disconnected;
    private Counter stealingLinkConnect;
    private Counter stealingLinkDisconnect;
    private Counter illegalStateDisconnect;

    public AuthLoginMetric(MetricsService metricsService, String type) {
        connected = metricsService.getCounter(CommonsMetric.module, type, CONNECT);
        attempt = metricsService.getCounter(CommonsMetric.module, type, MetricsLabel.ATTEMPT);
        disconnected = metricsService.getCounter(CommonsMetric.module, type, AuthMetric.DISCONNECT);
        stealingLinkConnect = metricsService.getCounter(CommonsMetric.module, type, STEALING_LINK, CONNECT);
        stealingLinkDisconnect = metricsService.getCounter(CommonsMetric.module, type, STEALING_LINK, AuthMetric.DISCONNECT);
        illegalStateDisconnect = metricsService.getCounter(CommonsMetric.module, type, ILLEGAL_STATE, AuthMetric.DISCONNECT);
    }

    public Counter getAttempt() {
        return attempt;
    }

    public Counter getConnected() {
        return connected;
    }

    public Counter getDisconnected() {
        return disconnected;
    }

    public Counter getStealingLinkConnect() {
        return stealingLinkConnect;
    }

    public Counter getStealingLinkDisconnect() {
        return stealingLinkDisconnect;
    }

    public Counter getIllegalStateDisconnect() {
        return illegalStateDisconnect;
    }

}
