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
package org.eclipse.kapua.service.authentication;

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
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.event.KapuaEventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class AuthenticationServiceModule implements ServiceModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceModule.class);

    private final static int MAX_WAIT_LOOP_ON_SHUTDOWN = 30;
    private final static int SCHEDULED_EXECUTION_TIME_WINDOW = 30;
    private final static long WAIT_TIME = 1000;

    @Inject
    private CredentialService credentialService;

    @Inject
    private AccessTokenService accessTokenService;

    private List<String> servicesNames;
    private ScheduledExecutorService houseKeeperScheduler;
    private ScheduledFuture<?> houseKeeperHandler;
    private EventStoreHouseKeeperJob houseKeeperJob;

    @Override
    public void start() throws KapuaException {

        KapuaEventBus eventbus = EventBusManager.getInstance();

        // Listen to upstream service events
        eventbus.subscribe("user", "user-credential", credentialService);
        eventbus.subscribe("user", "user-accesstoken", accessTokenService);
        eventbus.subscribe("account", "account-credential", credentialService);
        eventbus.subscribe("account", "account-accesstoken", accessTokenService);

        //register events to the service map
        String serviceInternalEventAddress = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_INTERNAL_EVENT_ADDRESS);
        servicesNames = KapuaAuthenticationSetting.getInstance().getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_SERVICES_NAMES);
        ServiceMap.registerServices(serviceInternalEventAddress, servicesNames);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        houseKeeperJob = new EventStoreHouseKeeperJob(AuthenticationEntityManagerFactory.getInstance(), eventbus, serviceInternalEventAddress, servicesNames);
        // Start time can be made random from 0 to 30 seconds
        houseKeeperHandler = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, SCHEDULED_EXECUTION_TIME_WINDOW, SCHEDULED_EXECUTION_TIME_WINDOW, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        if (houseKeeperJob!=null) {
            houseKeeperJob.stop();
        }
        if (houseKeeperHandler!=null) {
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
        }
        if (houseKeeperScheduler != null) {
            houseKeeperScheduler.shutdown();
        }
        ServiceMap.unregisterServices(servicesNames);
    }
}
