/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.artemis;

import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.artemis.plugin.security.BrokerJAXBContextProvider;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSetting;
import org.eclipse.kapua.broker.artemis.plugin.security.setting.BrokerSettingKey;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.BrokerIdentity;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerHostResolver;
import org.eclipse.kapua.broker.artemis.plugin.utils.DefaultBrokerIdResolver;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.liquibase.DatabaseCheckUpdate;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;

import com.google.inject.Provides;

public class AppModule extends AbstractKapuaModule {

    @Override
    protected void configureModule() {
        bind(DatabaseCheckUpdate.class).in(Singleton.class);
        bind(JAXBContextProvider.class).to(BrokerJAXBContextProvider.class).in(Singleton.class);
        bind(BrokerSetting.class).in(Singleton.class);
        bind(BrokerIdentity.class).in(Singleton.class);
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
    @Named("eventsModuleName")
    String eventModuleName() {
        return "telemetry";
    }

    @Provides
    @Singleton
    @Named("brokerHost")
    String brokerHost(BrokerHostResolver brokerHostResolver) {
        return brokerHostResolver.getBrokerHost();
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
