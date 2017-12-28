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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;

//@KapuaProvider
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
        List<ServiceEventClientConfiguration> selc = new ArrayList<>();
        selc.addAll(ServiceInspector.getEventBusClients(accessInfoService, AccessInfoService.class));
        selc.addAll(ServiceInspector.getEventBusClients(roleService, RoleService.class));
        selc.addAll(ServiceInspector.getEventBusClients(domainService, DomainService.class));
        selc.addAll(ServiceInspector.getEventBusClients(groupService, GroupService.class));
        return new ServiceEventModuleConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_EVENT_ADDRESS),
                AuthorizationEntityManagerFactory.getInstance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }

}