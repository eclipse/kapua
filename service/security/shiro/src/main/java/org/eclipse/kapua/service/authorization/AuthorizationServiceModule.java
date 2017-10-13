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
package org.eclipse.kapua.service.authorization;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.module.ServiceEventListenerConfiguration;
import org.eclipse.kapua.commons.event.module.ServiceEventModule;
import org.eclipse.kapua.commons.event.module.ServiceEventModuleConfiguration;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;

@KapuaProvider
public class AuthorizationServiceModule extends ServiceEventModule {

    @Inject
    private AccessInfoService accessInfoService;
    @Inject
    private DomainService domainService;
    @Inject
    private GroupService groupService;
    @Inject
    private RoleService roleService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAuthorizationSetting kdrs = KapuaAuthorizationSetting.getInstance();
        ServiceEventListenerConfiguration[] selc = new ServiceEventListenerConfiguration[5];
        selc[0] = new ServiceEventListenerConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCESS_INFO_SUBSCRIPTION_NAME),
                accessInfoService);
        selc[1] = new ServiceEventListenerConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kdrs.getString(KapuaAuthorizationSettingKeys.ROLE_SUBSCRIPTION_NAME),
                roleService);
        selc[2] = new ServiceEventListenerConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kdrs.getString(KapuaAuthorizationSettingKeys.DOMAIN_SUBSCRIPTION_NAME),
                domainService);
        selc[3] = new ServiceEventListenerConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kdrs.getString(KapuaAuthorizationSettingKeys.GROUP_SUBSCRIPTION_NAME),
                groupService);
        selc[4] = new ServiceEventListenerConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.USER_EVENT_ADDRESS),
                kdrs.getString(KapuaAuthorizationSettingKeys.ACCESS_INFO_SUBSCRIPTION_NAME),
                accessInfoService);
        return new ServiceEventModuleConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_INTERNAL_EVENT_ADDRESS),
                kdrs.getList(String.class, KapuaAuthorizationSettingKeys.AUTHORIZATION_SERVICES_NAMES),
                AuthorizationEntityManagerFactory.getInstance(),
                selc);
    }

}