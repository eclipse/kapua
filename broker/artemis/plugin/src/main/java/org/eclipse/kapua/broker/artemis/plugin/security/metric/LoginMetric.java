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
package org.eclipse.kapua.broker.artemis.plugin.security.metric;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;

import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

public class LoginMetric {

    private static final LoginMetric LOGIN_METRIC = new LoginMetric();

    public static final String COMPONENT_LOGIN = "login";
    //action
    private static final String DISCONNECT = "disconnect";

    private static final String INTERNAL_CONNECTOR = "internal_connector";
    private static final String EXTERNAL_CONNECTOR = "external_connector";
    private static final String SUCCESS_FROM_CACHE = "success_from_cache";
    private static final String PASSWORD = "password";
    private static final String CONNECTION_CLEANUP = "connection_cleanup";
    private static final String CONNECTION_CLEANUP_NULL_SESSION = "connection_cleanup_null_session";
    private static final String CRITICAL = "critical";
    private static final String LOGIN_CLOSED_CONNECTION = "login_closed_connection";
    private static final String DUPLICATE_SESSION_METADATA = "duplicate_session_metadata";
    private static final String CONNECT_CALLBACK_CALL = "connect_callback_call";
    private static final String SESSION_CONTEXT_BY_CLIENT_ID = "session_context_by_client_id";
    private static final String ACL_CACHE_HIT = "acl_cache_hit";
    private static final String ACL_CREATION = "acl_creation";
    private static final String DISCONNECT_BY_EVENT = DISCONNECT + "_by_event";
    private static final String ADD_CONNECTION = "add_connection";
    private static final String REMOVE_CONNECTION = "remove_connection";

    private ActionMetric externalConnector;
    private ActionMetric internalConnector;
    private Counter successFromCache;
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

    private Counter invalidUserPassword;
    private Counter disconnectByEvent;

    private Timer externalAddConnection;
    private Timer removeConnection;

    public static LoginMetric getInstance() {
        return LOGIN_METRIC;
    }

    private LoginMetric() {
        MetricsService metricsService = MetricServiceFactory.getInstance();
        // login by connectors
        externalConnector = new ActionMetric(CommonsMetric.module, COMPONENT_LOGIN, EXTERNAL_CONNECTOR);
        successFromCache = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, SUCCESS_FROM_CACHE);
        internalConnector = new ActionMetric(CommonsMetric.module, COMPONENT_LOGIN, INTERNAL_CONNECTOR);
        cleanupConnectionFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, CONNECTION_CLEANUP, MetricsLabel.FAILURE);
        cleanupConnectionNullSession = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, CONNECTION_CLEANUP_NULL_SESSION, MetricsLabel.FAILURE);
        criticalFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, CRITICAL, MetricsLabel.FAILURE);
        loginClosedConnectionFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, LOGIN_CLOSED_CONNECTION, MetricsLabel.FAILURE);
        duplicateSessionMetadataFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, DUPLICATE_SESSION_METADATA, MetricsLabel.FAILURE);
        disconnectCallbackCallFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, CONNECT_CALLBACK_CALL, MetricsLabel.FAILURE);
        sessionContextByClientIdFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, SESSION_CONTEXT_BY_CLIENT_ID, MetricsLabel.FAILURE);
        aclCacheHit = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, ACL_CACHE_HIT);
        aclCreationFailure = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, ACL_CREATION, MetricsLabel.FAILURE);

        invalidUserPassword = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, PASSWORD, MetricsLabel.FAILURE);
        disconnectByEvent = metricsService.getCounter(CommonsMetric.module, COMPONENT_LOGIN, DISCONNECT_BY_EVENT, DISCONNECT);

        // login time
        externalAddConnection = metricsService.getTimer(CommonsMetric.module, COMPONENT_LOGIN, ADD_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
        removeConnection = metricsService.getTimer(CommonsMetric.module, COMPONENT_LOGIN, REMOVE_CONNECTION, MetricsLabel.TIME, MetricsLabel.SECONDS);
    }

    public Counter getSuccessFromCache() {
        return successFromCache;
    }

    public ActionMetric getExternalConnector() {
        return externalConnector;
    }

    public ActionMetric getInternalConnector() {
        return internalConnector;
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

    public Counter getInvalidUserPassword() {
        return invalidUserPassword;
    }

    public Counter getDisconnectByEvent() {
        return disconnectByEvent;
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
    public Timer getExternalAddConnection() {
        return externalAddConnection;
    }

    /**
     * Remove connection total time
     * @return
     */
    public Timer getRemoveConnection() {
        return removeConnection;
    }

}
