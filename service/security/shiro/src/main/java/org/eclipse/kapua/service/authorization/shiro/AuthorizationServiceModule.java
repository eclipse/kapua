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
package org.eclipse.kapua.service.authorization.shiro;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventHouseKeeperFactory;
import org.eclipse.kapua.commons.event.ServiceEventTransactionalModule;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.DomainRegistryService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthorizationServiceModule extends ServiceEventTransactionalModule {

    public AuthorizationServiceModule(AccessInfoService accessInfoService,
                                      RoleService roleService,
                                      DomainRegistryService domainRegistryService,
                                      GroupService groupService,
                                      KapuaAuthorizationSetting kapuaAuthorizationSettings,
                                      ServiceEventHouseKeeperFactory serviceEventTransactionalHousekeeperFactory,
                                      ServiceEventBus serviceEventBus,
                                      String eventModuleName) {
        super(Arrays.asList(
                                ServiceInspector.getEventBusClients(accessInfoService, AccessInfoService.class),
                                ServiceInspector.getEventBusClients(roleService, RoleService.class),
                                ServiceInspector.getEventBusClients(domainRegistryService, DomainRegistryService.class),
                                ServiceInspector.getEventBusClients(groupService, GroupService.class)
                        )
                        .stream()
                        .flatMap(l -> l.stream())
                        .collect(Collectors.toList())
                        .toArray(new ServiceEventClientConfiguration[0]),
                kapuaAuthorizationSettings.getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_EVENT_ADDRESS),
                eventModuleName + "-" + UUID.randomUUID().toString(),
                serviceEventTransactionalHousekeeperFactory,
                serviceEventBus);
    }
}
