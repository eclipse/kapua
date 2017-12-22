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

import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;

//@KapuaProvider
public class UserServiceModule extends ServiceEventModule {

    @Inject
    private UserService userService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaUserSetting kas = KapuaUserSetting.getInstance();
        List<ServiceEventClientConfiguration> selc = ServiceInspector.getEventBusClients(userService, UserService.class);
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaUserSettingKeys.USER_EVENT_ADDRESS), 
                UserEntityManagerFactory.getInstance(), 
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }
}
