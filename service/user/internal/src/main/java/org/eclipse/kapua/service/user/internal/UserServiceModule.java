/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.user.internal;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.module.ServiceEventListenerConfiguration;
import org.eclipse.kapua.commons.event.module.ServiceEventModule;
import org.eclipse.kapua.commons.event.module.ServiceEventModuleConfiguration;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;

@KapuaProvider
public class UserServiceModule extends ServiceEventModule {

    @Inject
    private UserService userService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaUserSetting kas = KapuaUserSetting.getInstance();
        ServiceEventListenerConfiguration[] selc = new ServiceEventListenerConfiguration[1];
        selc[0] = new ServiceEventListenerConfiguration(
                kas.getString(KapuaUserSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kas.getString(KapuaUserSettingKeys.USER_SUBSCRIPTION_NAME),
                userService);
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaUserSettingKeys.USER_INTERNAL_EVENT_ADDRESS),
                kas.getList(String.class, KapuaUserSettingKeys.USER_SERVICES_NAMES), 
                UserEntityManagerFactory.getInstance(), 
                selc);
    }

}
