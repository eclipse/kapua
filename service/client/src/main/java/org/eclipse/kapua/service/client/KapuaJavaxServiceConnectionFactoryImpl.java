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

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.jms.KapuaJavaxServiceConnectionFactory;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.service.client.jms.ServiceConnectionFactoryImpl;
import org.eclipse.kapua.service.client.setting.ServiceClientSetting;
import org.eclipse.kapua.service.client.setting.ServiceClientSettingKey;

/**
 * WARNING!!!!
 * this class is needed only because we don't have the full injection refactoring done yet.
 * Once will be available this class will be correctly eliminated
 * TODO delete me when direct injection will be possible
 */
public class KapuaJavaxServiceConnectionFactoryImpl extends ServiceConnectionFactoryImpl implements KapuaJavaxServiceConnectionFactory {

    @Inject
    public KapuaJavaxServiceConnectionFactoryImpl() {
        super(SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_URL),
                SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_USERNAME),
                SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_PASSWORD),
                ServiceClientSetting.getInstance().getString(ServiceClientSettingKey.COMPONENT_ID) + "-srv");
            //redundant just for logging but this code will be removed (see class description)
            String servicebusUrl = SystemSetting.getInstance().getString(SystemSettingKey.SERVICE_BUS_URL);
            String clientId = ServiceClientSetting.getInstance().getString(ServiceClientSettingKey.COMPONENT_ID) + "-srv";
            logger.info("Setting factory for url {} / client id {}", servicebusUrl, clientId);
    }

}
