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

import org.eclipse.kapua.client.security.MetricLabel;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class LoginMetric {

    private static final LoginMetric LOGIN_METRIC = new LoginMetric();

    //action
    private static final String LOGOUT = "logout";
    private static final String CONNECT = "connect";
    private static final String DISCONNECT = "disconnect";

    private static final String CLIENTS = "clients";
    private static final String INTERNAL_CONNECTOR = "internal_connector";
    private static final String SUCCESS_FROM_CACHE = "success_from_cache";
    private static final String PASSWORD = "password";
    private static final String CLIENT_ID = "client_id";
    private static final String CONNECTION_CLEANUP = "connection_cleanup";
    private static final String CRITICAL = "critical";
    private static final String LOGIN_CLOSED_CONNECTION = "login_closed_connection";
    private static final String DUPLICATE_SESSION_METADATA = "duplicate_session_metadata";
    private static final String CONNECT_CALLBACK_CALL = "connect_callback_call";
    private static final String SESSION_CONTEXT_BY_CLIENT_ID = "session_context_by_client_id";
    private static final String ACL_CACHE_HIT = "acl_cache_hit";
    private static final String ACL_CREATION = "acl_creation";
    private static final String STEALING_LINK = "stealing_link";
    private static final String DISCONNECT_BY_EVENT = DISCONNECT + "_by_event";
    private static final String ILLEGAL_STATE = "illegal_state";
    private static final String ADD_CONNECTION = "add_connection";
    private static final String USER = "user";
    private static final String ADMIN = "admin";
    private static final String SHIRO = "shiro";
    private static final String CHECK_ACCESS = "check_access";
    private static final String FIND_DEVICE_ON_CONNECTION = "find_device_on_connection";
    private static final String UPDATE_DEVICE_ON_CONNECTION = "update_device_on_connection";
    private static final String RAISE_LIFECYCLE_EVENT = "raise_lifecycle_event";
    private static final String REMOVE_CONNECTION = "remove_connection";
    private static final String FIND_DEVICE = "find_device";
    private static final String BROKER_HOST = "broker_host";

    private Counter externalAttempt;
    private Counter externalSuccess;
    private Counter externalFailure;
    private Counter successFromCache;
    private Counter internalConnectorAttempt;
    private Counter internalConnectorSuccess;
    private Counter internalConnectorFailure;
    private Counter cleanupConnectionFailure;
    //other failures
    private Counter cleanupConnectionNullSession;
    private Counter criticalFailure;
    private Counter loginClosedConnectionFailure;
    private Counter duplicateSessionMetadataFailure;
    private Counter disconnectCallbackCallFailure;//disconnect callback called before the connect callback (usually when a stealing link happens)
    private Counter sessionContextByClientIdFailure;//no session context is found by client id on disconnect on cleanupConnectionData (disconnect)
    private Counter aclCacheHit;//acl found from cache (it happens when a client id disconnected but some address related to this client id deleted after)
    private Counter aclCreationFailure;//error while creating acl

    private Counter userAttempt;
    private Counter userConnected;
    private Counter userDisconnected;
    private Counter userStealingLinkConnect;
    private Counter userStealingLinkDisconnect;
    private Counter userIllegalStateDisconnect;

    private Counter invalidUserPassword;
    private Counter invalidClientId;

    private Counter adminAttempt;
    private Counter adminConnected;
    private Counter adminDisconnected;
    private Counter adminStealingLinkConnect;
    private Counter adminStealingLinkDisconnect;

    private Counter disconnectByEvent;
    private Counter authServiceLogoutFailure;
    private Counter authServiceDisconnectFailure;
    private Counter authServiceFindDeviceConnectionFailure;
    private Counter authServiceBrokerHostFailure;

    private Timer externalAddConnectionTimeTotal;
    private Timer externalAddConnectionTimeShiroLogin;
    private Timer externalAddConnectionTimeUserTotal;
    private Timer externalAddConnectionTimeUserTotalCheckAccess;
    private Timer externalAddConnectionTimeUserTotalFindDevice;
    private Timer externalAddConnectionTimeUserTotalUpdateDevice;
    private Timer externalAddConnectionTimeShiroLogout;
    private Timer externalAddConnectionTimeAdminTotal;
    private Timer removeConnectionTimeTotal;
    private Timer raiseLifecycleEventTime;

    public static LoginMetric getInstance() {
        return LOGIN_METRIC;
    }

    private LoginMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // login by connectors
        externalAttempt = getCounter(metricsService, MetricsLabel.ATTEMPT);
        externalSuccess = getCounter(metricsService, MetricsLabel.SUCCESS);
        externalFailure = getCounter(metricsService, MetricsLabel.FAILURE);
        successFromCache = getCounter(metricsService, SUCCESS_FROM_CACHE);
        internalConnectorAttempt = getCounter(metricsService, INTERNAL_CONNECTOR, MetricsLabel.ATTEMPT);
        internalConnectorSuccess = getCounter(metricsService, INTERNAL_CONNECTOR, MetricsLabel.SUCCESS);
        internalConnectorFailure = getCounter(metricsService, INTERNAL_CONNECTOR, MetricsLabel.FAILURE);
        cleanupConnectionFailure = getCounter(metricsService, CONNECTION_CLEANUP, MetricsLabel.FAILURE);
        cleanupConnectionNullSession = getCounter(metricsService, CONNECTION_CLEANUP, MetricsLabel.FAILURE);
        criticalFailure = getCounter(metricsService, CRITICAL, MetricsLabel.FAILURE);
        loginClosedConnectionFailure = getCounter(metricsService, LOGIN_CLOSED_CONNECTION, MetricsLabel.FAILURE);
        duplicateSessionMetadataFailure = getCounter(metricsService, DUPLICATE_SESSION_METADATA, MetricsLabel.FAILURE);
        disconnectCallbackCallFailure = getCounter(metricsService, CONNECT_CALLBACK_CALL, MetricsLabel.FAILURE);
        sessionContextByClientIdFailure = getCounter(metricsService, SESSION_CONTEXT_BY_CLIENT_ID, MetricsLabel.FAILURE);
        aclCacheHit = getCounter(metricsService, ACL_CACHE_HIT);
        aclCreationFailure = getCounter(metricsService, ACL_CREATION, MetricsLabel.FAILURE);
        //logins by user type
        userConnected = getCounter(metricsService, CLIENTS, CONNECT);
        userAttempt = getCounter(metricsService, CLIENTS, MetricsLabel.ATTEMPT);
        userDisconnected = getCounter(metricsService, CLIENTS, DISCONNECT);
        userStealingLinkConnect = getCounter(metricsService, STEALING_LINK, CONNECT);
        userStealingLinkDisconnect = getCounter(metricsService, STEALING_LINK, DISCONNECT);
        userIllegalStateDisconnect = getCounter(metricsService, ILLEGAL_STATE, DISCONNECT);
        adminAttempt = getCounter(metricsService, ADMIN);
        adminConnected = getCounter(metricsService, ADMIN, CONNECT);
        adminDisconnected = getCounter(metricsService, ADMIN, DISCONNECT);
        adminStealingLinkConnect = getCounter(metricsService, ADMIN, STEALING_LINK, CONNECT);
        adminStealingLinkDisconnect = getCounter(metricsService, STEALING_LINK, DISCONNECT);

        invalidUserPassword = getCounter(metricsService, PASSWORD, MetricsLabel.FAILURE);
        invalidClientId = getCounter(metricsService, CLIENT_ID, MetricsLabel.FAILURE);
        disconnectByEvent = getCounter(metricsService, DISCONNECT_BY_EVENT, DISCONNECT);
        authServiceLogoutFailure = getCounter(metricsService, LOGOUT, MetricsLabel.FAILURE);
        authServiceDisconnectFailure = getCounter(metricsService, DISCONNECT, MetricsLabel.FAILURE);
        authServiceFindDeviceConnectionFailure = getCounter(metricsService, FIND_DEVICE, MetricsLabel.FAILURE);
        authServiceBrokerHostFailure = getCounter(metricsService, BROKER_HOST, MetricsLabel.FAILURE);

        // login time
        externalAddConnectionTimeTotal = getTimer(metricsService, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeShiroLogin = getTimer(metricsService, SHIRO, MetricLabel.COMPONENT_LOGIN, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotal = getTimer(metricsService, USER, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalCheckAccess = getTimer(metricsService, CHECK_ACCESS, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalFindDevice = getTimer(metricsService, FIND_DEVICE_ON_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalUpdateDevice = getTimer(metricsService, UPDATE_DEVICE_ON_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeShiroLogout = getTimer(metricsService, SHIRO, LOGOUT, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeAdminTotal = getTimer(metricsService, ADMIN, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        removeConnectionTimeTotal = getTimer(metricsService, REMOVE_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        raiseLifecycleEventTime = getTimer(metricsService, RAISE_LIFECYCLE_EVENT, MetricsLabel.TIME, MetricsLabel.SECONDS);
    }

    private Counter getCounter(MetricsService metricsService, String... names) {
        return metricsService.getCounter(CommonsMetric.module, MetricLabel.COMPONENT_LOGIN, names);
    }

    private Timer getTimer(MetricsService metricsService, String... names) {
        return metricsService.getTimer(CommonsMetric.module, MetricLabel.COMPONENT_LOGIN, names);
    }

    public Counter getExternalAttempt() {
        return externalAttempt;
    }

    public Counter getExternalSuccess() {
        return externalSuccess;
    }

    public Counter getExternalFailure() {
        return externalFailure;
    }

    public Counter getSuccessFromCache() {
        return successFromCache;
    }

    public Counter getInternalConnectorAttempt() {
        return internalConnectorAttempt;
    }

    public Counter getInternalConnectorSuccess() {
        return internalConnectorSuccess;
    }

    public Counter getInternalConnectorFailure() {
        return internalConnectorFailure;
    }

    public Counter getCleanupConnectionFailure() {
        return cleanupConnectionFailure;
    }

    public Counter getCleanupConnectionNullSession() {
        return cleanupConnectionNullSession;
    }

    public Counter getCriticalFailure() {
        return criticalFailure;
    }

    public Counter getLoginClosedConnectionFailure() {
        return loginClosedConnectionFailure;
    }

    public Counter getDuplicateSessionMetadataFailure() {
        return duplicateSessionMetadataFailure;
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

    public Counter getAdminStealingLinkConnect() {
        return adminStealingLinkConnect;
    }

    public Counter getAdminStealingLinkDisconnect() {
        return adminStealingLinkDisconnect;
    }

    public Counter getUserAttempt() {
        return userAttempt;
    }

    public Counter getUserConnected() {
        return userConnected;
    }

    public Counter getUserDisconnected() {
        return userDisconnected;
    }

    public Counter getUserStealingLinkConnect() {
        return userStealingLinkConnect;
    }

    public Counter getUserStealingLinkDisconnect() {
        return userStealingLinkDisconnect;
    }

    public Counter getUserIllegalStateDisconnect() {
        return userIllegalStateDisconnect;
    }

    public Counter getInvalidUserPassword() {
        return invalidUserPassword;
    }

    //TODO link this metric to the clientId validation on login
    public Counter getInvalidClientId() {
        return invalidClientId;
    }

    public Counter getDisconnectByEvent() {
        return disconnectByEvent;
    }

    /**
     * Authentication service - Failure while doing Shiro logout (Internal error)
     * @return
     */
    public Counter getAuthServiceLogoutFailure() {
        return authServiceLogoutFailure;
    }

    /**
     * Authentication service - Failure while calling authenticator disconnect (Internal error)
     * @return
     */
    public Counter getAuthServiceDisconnectFailure() {
        return authServiceDisconnectFailure;
    }

    /**
     * Authentication service - Failure while getting device connection (Internal error)
     * @return
     */
    public Counter getAuthServiceFindDeviceConnectionFailure() {
        return authServiceFindDeviceConnectionFailure;
    }

    /**
     * Authentication service - Failure while getting broker host from authentication context (Internal error)
     * @return
     */
    public Counter getAuthServiceBrokerHostFailure() {
        return authServiceBrokerHostFailure;
    }

    /**
     * Disconnect callback called before the connect callback (usually when a stealing link happens)
     * @return
     */
    public Counter getDisconnectCallbackCallFailure() {
        return disconnectCallbackCallFailure;
    }

    /**
     * No session context is found by client id on disconnect on cleanupConnectionData (disconnect)
     * It's not necessary an error or failure but the metric is classified as failure
     * @return
     */
    public Counter getSessionContextByClientIdFailure() {
        return sessionContextByClientIdFailure;
    }

    /**
     * ACL found from cache (it happens when a client id disconnected but some address related to this client id deleted after)
     * @return
     */
    public Counter getAclCacheHit() {
        return aclCacheHit;
    }

    /**
     * Failure while creating ACL count (a failure doesn't mean all the ACL for a user aren't created but just one of the available ACLs)
     * @return
     */
    public Counter getAclCreationFailure() {
        return aclCreationFailure;
    }

    /**
     * External connector - Add connection total time
     * @return
     */
    public Timer getExternalAddConnectionTimeTotal() {
        return externalAddConnectionTimeTotal;
    }

    /**
     * External connector - Add connection Shiro login time
     * @return
     */
    public Timer getExternalAddConnectionTimeShiroLogin() {
        return externalAddConnectionTimeShiroLogin;
    }

    /**
     * External connector - Add connection user login total time
     * @return
     */
    public Timer getExternalAddConnectionTimeUserTotal() {
        return externalAddConnectionTimeUserTotal;
    }

    /**
     * External connector - Add connection user login check access time
     * @return
     */
    public Timer getExternalAddConnectionTimeUserTotalCheckAccess() {
        return externalAddConnectionTimeUserTotalCheckAccess;
    }

    /**
     * External connector - Add connection user login find device time
     * @return
     */
    public Timer getExternalAddConnectionTimeUserTotalFindDevice() {
        return externalAddConnectionTimeUserTotalFindDevice;
    }

    /**
     * External connector - Add connection user login update device time
     * @return
     */
    public Timer getExternalAddConnectionTimeUserTotalUpdateDevice() {
        return externalAddConnectionTimeUserTotalUpdateDevice;
    }

    /**
     * External connector - Add connection Shiro logout time
     * @return
     */
    public Timer getExternalAddConnectionTimeShiroLogout() {
        return externalAddConnectionTimeShiroLogout;
    }

    /**
     * External connector - Add connection admin total time
     * @return
     */
    public Timer getExternalAddConnectionTimeAdminTotal() {
        return externalAddConnectionTimeAdminTotal;
    }

    /**
     * Remove connection total time
     * @return
     */
    public Timer getRemoveConnectionTimeTotal() {
        return removeConnectionTimeTotal;
    }

    /**
     * Raise lifecycle event time (could be on connect or disconnect event)
     * @return
     */
    public Timer getRaiseLifecycleEventTime() {
        return raiseLifecycleEventTime;
    }

}
