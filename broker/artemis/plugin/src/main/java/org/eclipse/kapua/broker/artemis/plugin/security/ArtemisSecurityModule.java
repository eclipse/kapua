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
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdentity;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerIdResolver;
import org.eclipse.kapua.client.security.ServiceClient;
import org.eclipse.kapua.client.security.ServiceClientMessagingImpl;
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
        bind(SecurityContext.class).in(Singleton.class);
        bind(ServerContext.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    @Named("clusterName")
    String clusterName(SystemSetting systemSetting) {
        return systemSetting.getString(SystemSettingKey.CLUSTER_NAME);
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
            @Named("clusterName") String clusterName,
            @Named("brokerHost") String brokerHost) {
        return new ServiceClientMessagingImpl(clusterName, brokerHost);
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
