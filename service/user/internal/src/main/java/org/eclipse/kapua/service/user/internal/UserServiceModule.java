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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModuleTransactionalConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventTransactionalModule;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.commons.jpa.KapuaJpaRepositoryConfiguration;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;

import javax.inject.Inject;
import java.util.List;

public class UserServiceModule extends ServiceEventTransactionalModule {

    @Inject
    private UserService userService;
    @Inject
    private KapuaJpaRepositoryConfiguration jpaRepoConfig;

    @Override
    protected ServiceEventModuleTransactionalConfiguration initializeConfiguration() {
        final KapuaUserSetting kas = KapuaUserSetting.getInstance();
        final List<ServiceEventClientConfiguration> selc = ServiceInspector.getEventBusClients(userService, UserService.class);
        return new ServiceEventModuleTransactionalConfiguration(
                kas.getString(KapuaUserSettingKeys.USER_EVENT_ADDRESS),
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-user")),
                selc.toArray(new ServiceEventClientConfiguration[0]),
                jpaRepoConfig);
    }
}
