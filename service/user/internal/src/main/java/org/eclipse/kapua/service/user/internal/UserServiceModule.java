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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.event.bus.EventBusManager;
import org.eclipse.kapua.commons.event.service.EventStoreHouseKeeperJob;
import org.eclipse.kapua.commons.event.service.internal.ServiceMap;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class UserServiceModule implements ServiceModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceModule.class);

    private final static int MAX_WAIT_LOOP_ON_SHUTDOWN = 30;
    private final static int SCHEDULED_EXECUTION_TIME_WINDOW = 30;
    private final static long WAIT_TIME = 1000;

    @Inject
    private UserService userService;

    private List<String> servicesNames;
    private ScheduledExecutorService houseKeeperScheduler;
    private ScheduledFuture<?> houseKeeperHandler;
    private EventStoreHouseKeeperJob houseKeeperJob;

    @Override
    public void start() throws KapuaException {

        KapuaEventBus eventbus = EventBusManager.getInstance();

        // Listen to upstream service events

        String upEvAccountUserAddressSub = KapuaUserSetting.getInstance().getString(KapuaUserSettingKeys.ACCOUNT_USER_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountUserAddressSub, userService); 

        //register events to the service map
        String serviceInternalEventAddress = KapuaUserSetting.getInstance().getString(KapuaUserSettingKeys.USER_INTERNAL_EVENT_ADDRESS);
        servicesNames = KapuaUserSetting.getInstance().getList(String.class, KapuaUserSettingKeys.USER_SERVICES_NAMES);
        ServiceMap.registerServices(serviceInternalEventAddress, servicesNames);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        houseKeeperJob = new EventStoreHouseKeeperJob(UserEntityManagerFactory.getInstance(), eventbus, serviceInternalEventAddress, servicesNames);
        // Start time can be made random from 0 to 30 seconds
        houseKeeperHandler = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, SCHEDULED_EXECUTION_TIME_WINDOW, SCHEDULED_EXECUTION_TIME_WINDOW, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        if (houseKeeperJob!=null) {
            houseKeeperJob.stop();
        }
        int waitLoop = 0;
        while(houseKeeperHandler.isDone()) {
            try {
                Thread.sleep(WAIT_TIME);
            } catch (InterruptedException e) {
                //do nothing
            }
            if (waitLoop++ > MAX_WAIT_LOOP_ON_SHUTDOWN) {
                LOGGER.warn("Cannot cancel the house keeper task afeter a while!");
                break;
            }
        }
        if (houseKeeperScheduler != null) {
            houseKeeperScheduler.shutdown();
        }
        ServiceMap.unregisterServices(servicesNames);
    }
}
