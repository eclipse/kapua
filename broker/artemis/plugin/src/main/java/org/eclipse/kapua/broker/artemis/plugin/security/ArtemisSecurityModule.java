/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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

import com.google.inject.Provides;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.broker.artemis.plugin.security.context.SecurityContext;
import org.eclipse.kapua.broker.artemis.plugin.security.metric.LoginMetric;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdentity;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerIdResolver;
import org.eclipse.kapua.client.security.MessageListener;
import org.eclipse.kapua.client.security.ServiceClient;
import org.eclipse.kapua.client.security.ServiceClientMessagingImpl;
import org.eclipse.kapua.client.security.amqpclient.Client;
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.jms.JMSException;
import java.util.UUID;

public class ArtemisSecurityModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(BrokerSetting.class).in(Singleton.class);
        bind(BrokerIdentity.class).in(Singleton.class);
        bind(ServerContext.class).in(Singleton.class);
        bind(MetricsSecurityPlugin.class).in(Singleton.class);
        bind(PluginUtility.class).in(Singleton.class);
        bind(RunWithLock.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    SecurityContext securityContext(LoginMetric loginMetric,
                                    BrokerSetting brokerSettings,
                                    MetricsSecurityPlugin metricsSecurityPlugin,
                                    RunWithLock runWithLock) {
        return new SecurityContext(loginMetric,
                brokerSettings.getBoolean(BrokerSettingKey.PRINT_SECURITY_CONTEXT_REPORT, false),
                new LocalCache<>(
                        brokerSettings.getInt(BrokerSettingKey.CACHE_CONNECTION_TOKEN_SIZE),
                        brokerSettings.getInt(BrokerSettingKey.CACHE_CONNECTION_TOKEN_TTL),
                        null),
                new LocalCache<>(
                        brokerSettings.getInt(BrokerSettingKey.CACHE_SESSION_CONTEXT_SIZE),
                        brokerSettings.getInt(BrokerSettingKey.CACHE_SESSION_CONTEXT_TTL),
                        null),
                new LocalCache<>(
                        brokerSettings.getInt(BrokerSettingKey.CACHE_SESSION_CONTEXT_SIZE),
                        brokerSettings.getInt(BrokerSettingKey.CACHE_SESSION_CONTEXT_TTL),
                        null),
                metricsSecurityPlugin,
                runWithLock
        );
    }

    @Provides
    @Singleton
    @Named("clusterName")
    String clusterName(SystemSetting systemSetting) {
        return systemSetting.getString(SystemSettingKey.CLUSTER_NAME);
    }

    @Provides
    @Singleton
    @Named("metricModuleName")
    String metricModuleName() {
        return "broker-telemetry";
    }

    @Provides
    @Singleton
    @Named("brokerHost")
    String brokerHost(BrokerHostResolver brokerHostResolver) {
        return brokerHostResolver.getBrokerHost();
    }

    public static final String REQUEST_QUEUE = "$SYS/SVC/auth/request";
    public static final String RESPONSE_QUEUE_PATTERN = "$SYS/SVC/auth/response/%s_%s";

    @Singleton
    @Provides
    ServiceClient authServiceClient(
            MessageListener messageListener,
            @Named("clusterName") String clusterName,
            @Named("brokerHost") String brokerHost,
            SystemSetting systemSetting) {
        return new ServiceClientMessagingImpl(messageListener, buildClient(systemSetting, clusterName, brokerHost, messageListener));
    }

    public Client buildClient(SystemSetting systemSetting, String clusterName, String brokerHost, MessageListener messageListener) {
        //TODO change configuration (use service event broker for now)
        String clientId = "svc-ath-" + UUID.randomUUID().toString();
        String host = systemSetting.getString(SystemSettingKey.SERVICE_BUS_HOST, "events-broker");
        int port = systemSetting.getInt(SystemSettingKey.SERVICE_BUS_PORT, 5672);
        String username = systemSetting.getString(SystemSettingKey.SERVICE_BUS_USERNAME, "username");
        String password = systemSetting.getString(SystemSettingKey.SERVICE_BUS_PASSWORD, "password");
        try {
            return new Client(username, password, host, port, clientId,
                    REQUEST_QUEUE, String.format(RESPONSE_QUEUE_PATTERN, clusterName, brokerHost), messageListener);
        } catch (JMSException e) {
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, (Object[]) null);
        }
    }

    @Singleton
    @Provides
    BrokerIdResolver brokerIdResolver(BrokerSetting brokerSettings) throws KapuaException {
        return new DefaultBrokerIdResolver();
    }

    @Singleton
    @Provides
    BrokerHostResolver brokerHostResolver(BrokerSetting brokerSettings) throws KapuaException {
        return new DefaultBrokerHostResolver(brokerSettings.getString(BrokerSettingKey.BROKER_HOST));
    }
}
