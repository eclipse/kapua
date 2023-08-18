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
package org.eclipse.kapua.broker.artemis.plugin.security;

import com.codahale.metrics.Gauge;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.artemis.plugin.security.metric.LoginMetric;
import org.eclipse.kapua.commons.metric.CommonsMetric;
import org.eclipse.kapua.commons.metric.MetricsLabel;
import org.eclipse.kapua.commons.metric.MetricsService;

import javax.inject.Inject;

public class MetricsSecurityPlugin {

    public static final String BROKER_TELEMETRY = "broker-telemetry";

    private static final String CONNECTION = "connection";
    private static final String SESSION = "session";
    private static final String ACL = "acl";
    private static final String BROKER_CONNECTION = "broker_connection";
    private static final String SESSION_CONTEXT = "session_context";
    private static final String SESSION_CONTEXT_BY_CLIENT = "session_context_by_client";
    private static final String ACTIVE_CONNECTION = "active_connection";
    private static final String DISK_USAGE = "disk_usage";
    private static final String TOTAL_CONNECTION = "total_connection";
    private static final String TOTAL_MESSAGE = "total_message";
    private static final String TOTAL_MESSAGE_ACKNOWLEDGED = "total_message_acknowledged";
    private static final String TOTAL_MESSAGE_ADDED = "total_message_added";

    private final MetricsService metricsService;

    @Inject
    public MetricsSecurityPlugin(
            MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    public void init(ActiveMQServer server, Gauge<Integer> mapSize, Gauge<Integer> mapByClientSize, Gauge<Integer> aclSize, Gauge<Integer> activeConnection) throws KapuaException {
        metricsService.registerGauge(mapSize, CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, SESSION_CONTEXT);
        metricsService.registerGauge(mapByClientSize, CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, SESSION_CONTEXT_BY_CLIENT);
        metricsService.registerGauge(aclSize, CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, ACL);
        metricsService.registerGauge(activeConnection, CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, ACTIVE_CONNECTION);

        metricsService.registerGauge(() -> server.getSessions().size(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, SESSION);
        metricsService.registerGauge(() -> server.getConnectionCount(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, CONNECTION);
        metricsService.registerGauge(() -> server.getBrokerConnections().size(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, BROKER_CONNECTION);
        //from broker
        metricsService.registerGauge(() -> server.getDiskStoreUsage(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, DISK_USAGE, MetricsLabel.SIZE);
        metricsService.registerGauge(() -> server.getTotalConnectionCount(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, TOTAL_CONNECTION, MetricsLabel.SIZE);
        metricsService.registerGauge(() -> server.getTotalMessageCount(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, TOTAL_MESSAGE, MetricsLabel.SIZE);
        metricsService.registerGauge(() -> server.getTotalMessagesAcknowledged(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, TOTAL_MESSAGE_ACKNOWLEDGED, MetricsLabel.SIZE);
        metricsService.registerGauge(() -> server.getTotalMessagesAdded(), CommonsMetric.module, LoginMetric.COMPONENT_LOGIN, TOTAL_MESSAGE_ADDED, MetricsLabel.SIZE);
    }

}