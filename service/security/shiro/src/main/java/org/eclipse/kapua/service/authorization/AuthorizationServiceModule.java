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
package org.eclipse.kapua.service.authorization;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

//@KapuaProvider
public class AuthorizationServiceModule extends ServiceEventModule {

    @Inject
    private AccessInfoService accessInfoService;
    @Inject
    private DomainRegistryService domainRegistryService;
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
        selc.addAll(ServiceInspector.getEventBusClients(domainRegistryService, DomainRegistryService.class));
        selc.addAll(ServiceInspector.getEventBusClients(groupService, GroupService.class));
        return new ServiceEventModuleConfiguration(
                kdrs.getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_EVENT_ADDRESS),
                AuthorizationEntityManagerFactory.getInstance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }

}
