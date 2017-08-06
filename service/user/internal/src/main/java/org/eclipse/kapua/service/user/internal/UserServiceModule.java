/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.bus.EventBusManager;
import org.eclipse.kapua.commons.event.service.EventStoreHouseKeeperJob;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreServiceImpl;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;

@KapuaProvider
public class UserServiceModule implements ServiceModule {

    @Inject
    private UserService userService;

    private KapuaEventStoreServiceImpl kapuaEventService;
    private ScheduledExecutorService houseKeeperScheduler;

    @Override
    public void start() throws KapuaException {

        KapuaEventBus eventbus = EventBusManager.getInstance();

        // Listen to upstream service events

        String upEvAccountUserAddressSub = KapuaUserSetting.getInstance().getString(KapuaUserSettingKeys.ACCOUNT_USER_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountUserAddressSub, userService); 

        // Event store listener
        kapuaEventService = new KapuaEventStoreServiceImpl(UserEntityManagerFactory.getInstance());

        //the event bus implicitly will add event. as prefix for each publish/subscribe
        String internalEventsAddressSub = KapuaUserSetting.getInstance().getString(KapuaUserSettingKeys.USER_INTERNAL_EVENT_ADDRESS); 
        eventbus.subscribe(internalEventsAddressSub, eventStoreListener);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        String publishInternalEventsAddress = KapuaUserSetting.getInstance().getString(KapuaUserSettingKeys.USER_PUBLISH_INTERNAL_EVENT_ADDRESS); //the event bus implicitly will add event. as prefix for each publish/subscribe
        Runnable houseKeeperJob = new HouseKeeperJob(eventbus, publishInternalEventsAddress);
        // Start time can be made random from 0 to 30 seconds
        final ScheduledFuture<?> houseKeeperHandle = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        houseKeeperScheduler.shutdown();
    }
}
