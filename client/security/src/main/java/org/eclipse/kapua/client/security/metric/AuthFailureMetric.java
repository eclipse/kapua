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

import javax.inject.Inject;

public class AuthFailureMetric {

    private static final String FIND_DEVICE = "find_device";
    private static final String BROKER_HOST = "broker_host";
    private static final String LOGOUT_ON_FAILURE = "logout_failure_on_login";

    private Counter logoutFailureOnLogin;
    private Counter disconnectFailure;
    private Counter findDeviceConnectionFailure;
    private Counter brokerHostFailure;

    @Inject
    public AuthFailureMetric(MetricsService metricsService) {
        logoutFailureOnLogin = metricsService.getCounter(CommonsMetric.module, LOGOUT_ON_FAILURE);
        disconnectFailure = metricsService.getCounter(CommonsMetric.module, AuthMetric.DISCONNECT, MetricsLabel.FAILURE);
        findDeviceConnectionFailure = metricsService.getCounter(CommonsMetric.module, FIND_DEVICE, MetricsLabel.FAILURE);
        brokerHostFailure = metricsService.getCounter(CommonsMetric.module, BROKER_HOST, MetricsLabel.FAILURE);
    }

    /**
     * Failure while doing Shiro logout (Internal error)
     *
     * @return
     */
    public Counter getLogoutFailureOnLogin() {
        return logoutFailureOnLogin;
    }

    /**
     * Failure while calling authenticator disconnect (Internal error)
     *
     * @return
     */
    public Counter getDisconnectFailure() {
        return disconnectFailure;
    }

    /**
     * Failure while getting device connection (Internal error)
     *
     * @return
     */
    public Counter getFindDeviceConnectionFailure() {
        return findDeviceConnectionFailure;
    }

    /**
     * Failure while getting broker host from authentication context (Internal error)
     *
     * @return
     */
    public Counter getBrokerHostFailure() {
        return brokerHostFailure;
    }

}
