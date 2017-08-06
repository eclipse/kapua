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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.bus.EventbusProvider;
import org.eclipse.kapua.commons.event.service.EventStoreListener;
import org.eclipse.kapua.commons.event.service.HouseKeeperJob;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventServiceImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.domain.DomainService;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;
import org.eclipse.kapua.service.event.KapuaEventbus;

@KapuaProvider
public class AuthorizationServiceModule implements ServiceModule {

    @Inject
    private AccessInfoService accessInfoService;
    @Inject
    private DomainService domainService;
    @Inject
    private GroupService groupService;
    @Inject
    private RoleService roleService;

    private EventStoreListener eventStoreListener;
    private ScheduledExecutorService houseKeeperScheduler;

    @Override
    public void start() throws KapuaException {

        KapuaEventbus eventbus = EventbusProvider.getInstance();

        // Listen to upstream service events

        String upEvAccountAccessInfoSubscribe = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.ACCOUNT_ACCESS_INFO_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountAccessInfoSubscribe, accessInfoService);

        String upEvAccountRoleSubscribe = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.ACCOUNT_ROLE_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountRoleSubscribe, roleService); 

        String upEvAccountDomainSubscribe = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.ACCOUNT_DOMAIN_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountDomainSubscribe, domainService); 

        String upEvAccountGroupSubscribe = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.ACCOUNT_GROUP_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountGroupSubscribe, groupService); 

        String upEvUserAccessInfoSubscribe = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.USER_ACCESS_INFO_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvUserAccessInfoSubscribe, accessInfoService); 

        // Event store listener
        KapuaEventServiceImpl kapuaEventService = new KapuaEventServiceImpl(AuthorizationEntityManagerFactory.getInstance());
        eventStoreListener = new EventStoreListener(kapuaEventService);

        //the event bus implicitly will add event. as prefix for each publish/subscribe
        String internalEventsAddressSub = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_INTERNAL_EVENT_ADDRESS); 
        eventbus.subscribe(internalEventsAddressSub, eventStoreListener);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        String publishInternalEventsAddress = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.AUTHORIZATION_PUBLISH_INTERNAL_EVENT_ADDRESS); //the event bus implicitly will add event. as prefix for each publish/subscribe
        Runnable houseKeeperJob = new HouseKeeperJob(eventbus, publishInternalEventsAddress);
        // Start time can be made random from 0 to 30 seconds
        final ScheduledFuture<?> houseKeeperHandle = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        houseKeeperScheduler.shutdown();
    }
}
