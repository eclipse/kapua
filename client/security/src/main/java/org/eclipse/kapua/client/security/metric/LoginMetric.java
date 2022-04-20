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
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class LoginMetric {

    private static final LoginMetric LOGIN_METRIC = new LoginMetric();

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
    private static final String DISCONNECT_BY_EVENT = MetricsLabel.DISCONNECT + "_by_event";
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
        externalAttempt = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.ATTEMPT, MetricsLabel.COUNT);
        externalSuccess = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.SUCCESS, MetricsLabel.COUNT);
        externalFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        successFromCache = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, SUCCESS_FROM_CACHE, MetricsLabel.COUNT);
        internalConnectorAttempt = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, INTERNAL_CONNECTOR, MetricsLabel.ATTEMPT, MetricsLabel.COUNT);
        internalConnectorSuccess = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, INTERNAL_CONNECTOR, MetricsLabel.SUCCESS, MetricsLabel.COUNT);
        internalConnectorFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, INTERNAL_CONNECTOR, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        cleanupConnectionFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CONNECTION_CLEANUP, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        cleanupConnectionNullSession = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CONNECTION_CLEANUP, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        criticalFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CRITICAL, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        loginClosedConnectionFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, LOGIN_CLOSED_CONNECTION, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        duplicateSessionMetadataFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, DUPLICATE_SESSION_METADATA, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        disconnectCallbackCallFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CONNECT_CALLBACK_CALL, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        sessionContextByClientIdFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, SESSION_CONTEXT_BY_CLIENT_ID, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        aclCacheHit = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ACL_CACHE_HIT, MetricsLabel.COUNT);
        aclCreationFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ACL_CREATION, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        //logins by user type
        userConnected = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CLIENTS, MetricsLabel.CONNECT, MetricsLabel.COUNT);
        userAttempt = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CLIENTS, MetricsLabel.ATTEMPT, MetricsLabel.COUNT);
        userDisconnected = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CLIENTS, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);
        userStealingLinkConnect = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, STEALING_LINK, MetricsLabel.CONNECT, MetricsLabel.COUNT);
        userStealingLinkDisconnect = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, STEALING_LINK, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);
        userIllegalStateDisconnect = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ILLEGAL_STATE, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);
        adminAttempt = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADMIN, MetricsLabel.COUNT);
        adminConnected = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADMIN, MetricsLabel.CONNECT, MetricsLabel.COUNT);
        adminDisconnected = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADMIN, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);
        adminStealingLinkConnect = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADMIN, STEALING_LINK, MetricsLabel.CONNECT, MetricsLabel.COUNT);
        adminStealingLinkDisconnect = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, ADMIN, STEALING_LINK, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);

        invalidUserPassword = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, PASSWORD, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        invalidClientId = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CLIENT_ID, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        disconnectByEvent = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, DISCONNECT_BY_EVENT, MetricsLabel.DISCONNECT, MetricsLabel.COUNT);
        authServiceLogoutFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.LOGOUT, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        authServiceDisconnectFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.DISCONNECT, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        authServiceFindDeviceConnectionFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, FIND_DEVICE, MetricsLabel.FAILURE, MetricsLabel.COUNT);
        authServiceBrokerHostFailure = metricsService.getCounter(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, BROKER_HOST, MetricsLabel.FAILURE, MetricsLabel.COUNT);

        // login time
        externalAddConnectionTimeTotal = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeShiroLogin = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, SHIRO, MetricsLabel.COMPONENT_LOGIN, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotal = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, USER, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalCheckAccess = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, CHECK_ACCESS, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalFindDevice = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, FIND_DEVICE_ON_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeUserTotalUpdateDevice = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, UPDATE_DEVICE_ON_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeShiroLogout = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, SHIRO, MetricsLabel.LOGOUT, MetricsLabel.TIME, MetricsLabel.SECONDS);
        externalAddConnectionTimeAdminTotal = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, ADMIN, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        removeConnectionTimeTotal = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, REMOVE_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        raiseLifecycleEventTime = metricsService.getTimer(MetricsLabel.MODULE_SECURITY, MetricsLabel.COMPONENT_LOGIN, RAISE_LIFECYCLE_EVENT, MetricsLabel.TIME, MetricsLabel.SECONDS);
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
