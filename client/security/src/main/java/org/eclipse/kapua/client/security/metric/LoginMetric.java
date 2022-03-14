/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
import com.codahale.metrics.Timer;

import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;

public class LoginMetric {

    private static final LoginMetric LOGIN_METRIC = new LoginMetric();

    private Counter attempt;
    private Counter success;
    private Counter successFromCache;
    private Counter failure;
    private Counter connected;
    private Counter disconnected;
    private Counter invalidUserPassword;
    private Counter invalidClientId;
    private Counter adminAttempt;
    private Counter adminConnected;
    private Counter adminDisconnected;
    private Counter internalConnectorAttempt;
    private Counter internalConnectorConnected;
    private Counter internalConnectorDisconnected;
    private Counter stealingLinkConnect;
    private Counter stealingLinkDisconnect;
    private Counter adminStealingLinkConnect;
    private Counter adminStealingLinkDisconnect;
    private Counter disconnectByEvent;
    private Counter illegalStateDisconnect;

    private Timer addConnectionTime;
    private Timer normalUserTime;
    private Timer shiroLoginTime;
    private Timer checkAccessTime;
    private Timer findDeviceConnectionTime;
    private Timer updateDeviceConnectionTime;
    private Timer shiroLogoutTime;
    private Timer sendLoginUpdateMsgTime;
    private Timer removeConnectionTime;

    public static LoginMetric getInstance() {
        return LOGIN_METRIC;
    }

    private LoginMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // login
        attempt = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_NORMAL, SecurityMetrics.METRIC_COUNT);
        success = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_SUCCESS, SecurityMetrics.METRIC_COUNT);
        successFromCache = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_SUCCESS_FROM_CACHE, SecurityMetrics.METRIC_COUNT);
        failure = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_FAILURE, SecurityMetrics.METRIC_COUNT);
        connected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_CLIENTS, SecurityMetrics.METRIC_CONNECTED, SecurityMetrics.METRIC_COUNT);
        disconnected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_CLIENTS, SecurityMetrics.METRIC_DISCONNECTED, SecurityMetrics.METRIC_COUNT);
        invalidUserPassword = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_FAILURE_PASSWORD, SecurityMetrics.METRIC_COUNT);
        invalidClientId = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_FAILURE_CLIENT_ID, SecurityMetrics.METRIC_COUNT);
        adminAttempt = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_KAPUASYS, SecurityMetrics.METRIC_COUNT);
        adminConnected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_KAPUASYS, SecurityMetrics.METRIC_CONNECTED, SecurityMetrics.METRIC_COUNT);
        adminDisconnected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_KAPUASYS, SecurityMetrics.METRIC_DISCONNECTED, SecurityMetrics.METRIC_COUNT);
        internalConnectorAttempt = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_INTERNAL_CONNECTOR, SecurityMetrics.METRIC_COUNT);
        internalConnectorConnected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_INTERNAL_CONNECTOR, SecurityMetrics.METRIC_CONNECT, SecurityMetrics.METRIC_COUNT);
        internalConnectorDisconnected = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_INTERNAL_CONNECTOR, SecurityMetrics.METRIC_DISCONNECT, SecurityMetrics.METRIC_COUNT);
        stealingLinkConnect = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_STEALING_LINK, SecurityMetrics.METRIC_CONNECT, SecurityMetrics.METRIC_COUNT);
        stealingLinkDisconnect = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_STEALING_LINK, SecurityMetrics.METRIC_DISCONNECT, SecurityMetrics.METRIC_COUNT);
        adminStealingLinkConnect = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_ADMIN_STEALING_LINK, SecurityMetrics.METRIC_CONNECT, SecurityMetrics.METRIC_COUNT);
        adminStealingLinkDisconnect = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_ADMIN_STEALING_LINK, SecurityMetrics.METRIC_DISCONNECT, SecurityMetrics.METRIC_COUNT);
        disconnectByEvent = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_DISCONNECT_BY_EVENT, SecurityMetrics.METRIC_DISCONNECT, SecurityMetrics.METRIC_COUNT);
        illegalStateDisconnect = metricsService.getCounter(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_ILLEGAL_STATE, SecurityMetrics.METRIC_DISCONNECT, SecurityMetrics.METRIC_COUNT);
        // login time
        addConnectionTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_ADD_CONNECTION, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        normalUserTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_USER, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        shiroLoginTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_SHIRO, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        checkAccessTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_CHECK_ACCESS, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        findDeviceConnectionTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_FIND_DEVICE_CONNECTION, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        updateDeviceConnectionTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_UPDATE_DEVICE_CONNECTION, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        shiroLogoutTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_SHIRO, SecurityMetrics.METRIC_LOGOUT, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        sendLoginUpdateMsgTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_SEND_LOGIN_UPDATE, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
        removeConnectionTime = metricsService.getTimer(SecurityMetrics.METRIC_MODULE_NAME, SecurityMetrics.METRIC_COMPONENT_LOGIN, SecurityMetrics.METRIC_REMOVE_CONNECTION, SecurityMetrics.METRIC_TIME, SecurityMetrics.METRIC_S);
    }

    public Counter getSuccess() {
        return success;
    }

    public Counter getSuccessFromCache() {
        return successFromCache;
    }

    public Counter getFailure() {
        return failure;
    }

    public Counter getConnected() {
        return connected;
    }

    public Counter getDisconnected() {
        return disconnected;
    }

    public Counter getInvalidUserPassword() {
        return invalidUserPassword;
    }

    public Counter getInvalidClientId() {
        return invalidClientId;
    }

    public Counter getAdminAttempt() {
        return adminAttempt;
    }

    public Counter getAdminConnected() {
        return adminConnected;
    }

    public Counter getAdminDisconnected() {
        return adminDisconnected;
    }

    public Counter getInternalConnectorAttempt() {
        return internalConnectorAttempt;
    }

    public Counter getAttempt() {
        return attempt;
    }

    public Counter getStealingLinkConnect() {
        return stealingLinkConnect;
    }

    public Counter getStealingLinkDisconnect() {
        return stealingLinkDisconnect;
    }

    public Counter getDisconnectByEvent() {
        return disconnectByEvent;
    }

    public Counter getAdminStealingLinkConnect() {
        return adminStealingLinkConnect;
    }

    public Counter getAdminStealingLinkDisconnect() {
        return adminStealingLinkDisconnect;
    }

    public Counter getIllegalStateDisconnect() {
        return illegalStateDisconnect;
    }

    public Counter getInternalConnectorConnected() {
        return internalConnectorConnected;
    }

    public Counter getInternalConnectorDisconnected() {
        return internalConnectorDisconnected;
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

    public Timer getFindDeviceConnectionTime() {
        return findDeviceConnectionTime;
    }

    public Timer getUpdateDeviceConnectionTime() {
        return updateDeviceConnectionTime;
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
