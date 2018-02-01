/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.metric;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

public class LoginMetric {

    private final static LoginMetric LOGIN_METRIC = new LoginMetric();

    private Counter success;
    private Counter failure;
    private Counter invalidUserPassword;
    private Counter invalidClientId;
    private Counter kapuasysTokenAttempt;
    private Counter normalUserAttempt;
    private Counter stealingLinkConnect;
    private Counter stealingLinkDisconnect;
    protected Counter remoteStealingLinkDisconnect;
    private Timer addConnectionTime;
    private Timer normalUserTime;
    private Timer shiroLoginTime;
    private Timer checkAccessTime;
    private Timer findClientIdTime;
    private Timer findDevTime;
    private Timer shiroLogoutTime;
    private Timer sendLoginUpdateMsgTime;
    private Timer removeConnectionTime;

    public static LoginMetric getInstance() {
        return LOGIN_METRIC;
    }

    private LoginMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // login
        success = metricsService.getCounter("security", "login", "success", "count");
        failure = metricsService.getCounter("security", "login", "failure", "count");
        invalidUserPassword = metricsService.getCounter("security", "login", "failure_password", "count");
        invalidClientId = metricsService.getCounter("security", "login", "failure_client_id", "count");
        kapuasysTokenAttempt = metricsService.getCounter("security", "login", "kapuasys", "count");
        normalUserAttempt = metricsService.getCounter("security", "login", "normal", "count");
        stealingLinkConnect = metricsService.getCounter("security", "login", "stealing_link", "connect", "count");
        stealingLinkDisconnect = metricsService.getCounter("security", "login", "stealing_link", "disconnect", "count");
        remoteStealingLinkDisconnect = metricsService.getCounter("security", "login", "remote_stealing_link", "disconnect", "count");
        // login time
        addConnectionTime = metricsService.getTimer("security", "login", "add_connection", "time", "s");
        normalUserTime = metricsService.getTimer("security", "login", "user", "time", "s");
        shiroLoginTime = metricsService.getTimer("security", "login", "shiro", "login", "time", "s");
        checkAccessTime = metricsService.getTimer("security", "login", "check_access", "time", "s");
        findClientIdTime = metricsService.getTimer("security", "login", "find_client_id", "time", "s");
        findDevTime = metricsService.getTimer("security", "login", "find_device", "time", "s");
        shiroLogoutTime = metricsService.getTimer("security", "login", "shiro", "logout", "time", "s");
        sendLoginUpdateMsgTime = metricsService.getTimer("security", "login", "send_login_update", "time", "s");
        removeConnectionTime = metricsService.getTimer("security", "login", "remove_connection", "time", "s");
    }

    public Counter getSuccess() {
        return success;
    }

    public Counter getFailure() {
        return failure;
    }

    public Counter getInvalidUserPassword() {
        return invalidUserPassword;
    }

    public Counter getInvalidClientId() {
        return invalidClientId;
    }

    public Counter getKapuasysTokenAttempt() {
        return kapuasysTokenAttempt;
    }

    public Counter getNormalUserAttempt() {
        return normalUserAttempt;
    }

    public Counter getStealingLinkConnect() {
        return stealingLinkConnect;
    }

    public Counter getStealingLinkDisconnect() {
        return stealingLinkDisconnect;
    }

    public Counter getRemoteStealingLinkDisconnect() {
        return remoteStealingLinkDisconnect;
    }

    public Timer getAddConnectionTime() {
        return addConnectionTime;
    }

    public Timer getNormalUserTime() {
        return normalUserTime;
    }

    public Timer getShiroLoginTime() {
        return shiroLoginTime;
    }

    public Timer getCheckAccessTime() {
        return checkAccessTime;
    }

    public Timer getFindClientIdTime() {
        return findClientIdTime;
    }

    public Timer getFindDevTime() {
        return findDevTime;
    }

    public Timer getShiroLogoutTime() {
        return shiroLogoutTime;
    }

    public Timer getSendLoginUpdateMsgTime() {
        return sendLoginUpdateMsgTime;
    }

    public Timer getRemoveConnectionTime() {
        return removeConnectionTime;
    }

}
