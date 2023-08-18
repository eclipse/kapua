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
import org.eclipse.kapua.KapuaException;
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
import org.eclipse.kapua.commons.cache.LocalCache;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ReflectionUtil;

import javax.inject.Named;
import javax.inject.Singleton;

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

    @Singleton
    @Provides
    ServiceClient authServiceClient(
            MessageListener messageListener,
            @Named("clusterName") String clusterName,
            @Named("brokerHost") String brokerHost) {
        return new ServiceClientMessagingImpl(messageListener, clusterName, brokerHost);
    }

    @Singleton
    @Provides
    BrokerIdResolver brokerIdResolver(BrokerSetting brokerSettings) throws KapuaException {
        return ReflectionUtil.newInstance(brokerSettings.getString(BrokerSettingKey.BROKER_ID_RESOLVER_CLASS_NAME), DefaultBrokerIdResolver.class);
    }

    @Singleton
    @Provides
    BrokerHostResolver brokerHostResolver(BrokerSetting brokerSettings) throws KapuaException {
        return ReflectionUtil.newInstance(brokerSettings.getString(BrokerSettingKey.BROKER_HOST_RESOLVER_CLASS_NAME), DefaultBrokerHostResolver.class);
    }
}
