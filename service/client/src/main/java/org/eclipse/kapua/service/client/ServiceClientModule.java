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
package org.eclipse.kapua.service.client;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.jms.ConnectionFactory;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.event.jms.KapuaJavaxEventConnectionFactory;
import org.eclipse.kapua.commons.event.jms.KapuaJavaxServiceConnectionFactory;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.client.jms.ServiceConnectionFactoryImpl;
import org.eclipse.kapua.service.client.setting.ServiceClientSetting;
import org.eclipse.kapua.service.client.setting.ServiceClientSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provides;

public class ServiceClientModule extends AbstractKapuaModule {

    private static final Logger logger = LoggerFactory.getLogger(ServiceClientModule.class);

    @Override
    protected void configureModule() {
        bind(KapuaJavaxServiceConnectionFactory.class).to(KapuaJavaxServiceConnectionFactoryImpl.class).in(Singleton.class);
        bind(KapuaJavaxEventConnectionFactory.class).to(KapuaJavaxEventConnectionFactoryImpl.class).in(Singleton.class);
    }

    //needed once injection will be available
    @Provides
    @Singleton
    @Named("event-bus")
    ConnectionFactory getEventBusConnectionFactory() {
        String eventbusUrl = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_URL);
        String eventbusUsername = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_USERNAME);
        String eventbusPassword = SystemSetting.getInstance().getString(SystemSettingKey.EVENT_BUS_PASSWORD);
        String clientId = ServiceClientSetting.getInstance().getString(ServiceClientSettingKey.COMPONENT_ID) + "-evt";
        logger.info("Setting factory for url {} / client id {}", eventbusUrl, clientId);
        return new ServiceConnectionFactoryImpl(eventbusUrl, eventbusUsername, eventbusPassword, clientId);
    }

    @Provides
    @Singleton
    @Named("service-bus")
    ConnectionFactory getServiceBusConnectionFactory() {
        String servicebusUrl = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_URL);
        String servicebusUsername = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_USERNAME);
        String servicebusPassword = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_PASSWORD);
        String clientId = ServiceClientSetting.getInstance().getString(ServiceClientSettingKey.COMPONENT_ID) + "-srv";
        logger.info("Setting factory for url {} / client id {}", servicebusUrl, clientId);
        return new ServiceConnectionFactoryImpl(servicebusUrl, servicebusUsername, servicebusPassword, clientId);
    }
}