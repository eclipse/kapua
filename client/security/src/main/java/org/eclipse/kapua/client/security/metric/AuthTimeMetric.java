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

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Timer;

public class AuthTimeMetric {

    private static final String ADD_CONNECTION = "add_connection";
    private static final String CHECK_ACCESS = "check_access";
    private static final String FIND_DEVICE = "find_device";
    private static final String UPDATE_DEVICE = "update_device";
    private static final String RAISE_LIFECYCLE_EVENT = "raise_lifecycle_event";
    private static final String LOGOUT_ON_CONNECTION = "logout_on_connection";
    private static final String REMOVE_CONNECTION = "remove_connection";

    private Timer user;
    private Timer userCheckAccess;
    private Timer userFindDevice;
    private Timer userUpdateDevice;
    private Timer logoutOnConnect;
    private Timer admin;
    private Timer raiseLifecycleEvent;
    private Timer removeConnection;

    public AuthTimeMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        user = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        userCheckAccess = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, CHECK_ACCESS, MetricsLabel.TIME, MetricsLabel.SECONDS);
        userFindDevice = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, FIND_DEVICE, MetricsLabel.TIME, MetricsLabel.SECONDS);
        userUpdateDevice = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, UPDATE_DEVICE, MetricsLabel.TIME, MetricsLabel.SECONDS);
        logoutOnConnect = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, LOGOUT_ON_CONNECTION, AuthMetric.LOGOUT, MetricsLabel.TIME, MetricsLabel.SECONDS);
        admin = metricsService.getTimer(CommonsMetric.module, AuthMetric.ADMIN, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        raiseLifecycleEvent = metricsService.getTimer(CommonsMetric.module, AuthMetric.USER, RAISE_LIFECYCLE_EVENT, MetricsLabel.TIME, MetricsLabel.SECONDS);
        removeConnection = metricsService.getTimer(CommonsMetric.module, REMOVE_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
    }

    /**
     * Add connection user login total time
     * @return
     */
    public Timer getUser() {
        return user;
    }

    /**
     * Add connection user login check access time
     * @return
     */
    public Timer getUserCheckAccess() {
        return userCheckAccess;
    }

    /**
     * Add connection user login find device time
     * @return
     */
    public Timer getUserFindDevice() {
        return userFindDevice;
    }

    /**
     * Add connection user login update device time
     * @return
     */
    public Timer getUserUpdateDevice() {
        return userUpdateDevice;
    }

    /**
     * Add connection after connect logout time
     * @return
     */
    public Timer getLogoutOnConnect() {
        return logoutOnConnect;
    }

    /**
     * Add connection admin total time
     * @return
     */
    public Timer getAdmin() {
        return admin;
    }

    /**
     * Raise lifecycle event time (could be on connect or disconnect event)
     * @return
     */
    public Timer getRaiseLifecycleEvent() {
        return raiseLifecycleEvent;
    }

    /**
     * Remove connection total time
     * @return
     */
    public Timer getRemoveConnection() {
        return removeConnection;
    }

}