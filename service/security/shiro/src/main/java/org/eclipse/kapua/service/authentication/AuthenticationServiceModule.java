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
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authorization.shiro.AuthorizationEntityManagerFactory;
import org.eclipse.kapua.service.event.KapuaEventbus;

@KapuaProvider
public class AuthenticationServiceModule implements ServiceModule {

    @Inject
    private CredentialService credentialService;
    @Inject
    private AccessTokenService accessTokenService;

    private EventStoreListener eventStoreListener;
    private ScheduledExecutorService houseKeeperScheduler;

    @Override
    public void start() throws KapuaException {

        KapuaEventbus eventbus = EventbusProvider.getInstance();

        // Listen to upstream service events

        String upEvUserCredentialSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.USER_CREDENTIAL_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvUserCredentialSubscribe, credentialService);

        String upEvUserAccessTokenSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.USER_ACCESS_TOKEN_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvUserAccessTokenSubscribe, accessTokenService);

        String upEvAccountCredentialSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.ACCOUNT_CREDENTIAL_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountCredentialSubscribe, credentialService);

        String upEvAccountAccessTokenSubscribe = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.ACCOUNT_ACCESS_TOKEN_UPSTREAM_EVENT_ADDRESS);
        //the event bus implicitly will add event. as prefix for each publish/subscribe
        eventbus.subscribe(upEvAccountAccessTokenSubscribe, accessTokenService);

        // Event store listener
        KapuaEventServiceImpl kapuaEventService = new KapuaEventServiceImpl(AuthorizationEntityManagerFactory.getInstance());
        eventStoreListener = new EventStoreListener(kapuaEventService);

        //the event bus implicitly will add event. as prefix for each publish/subscribe
        String internalEventsAddressSub = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_INTERNAL_EVENT_ADDRESS); 
        eventbus.subscribe(internalEventsAddressSub, eventStoreListener);

        // Start the House keeper
        houseKeeperScheduler = Executors.newScheduledThreadPool(1);
        String publishInternalEventsAddress = KapuaAuthenticationSetting.getInstance().getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_PUBLISH_INTERNAL_EVENT_ADDRESS); //the event bus implicitly will add event. as prefix for each publish/subscribe
        Runnable houseKeeperJob = new HouseKeeperJob(eventbus, publishInternalEventsAddress);
        // Start time can be made random from 0 to 30 seconds
        final ScheduledFuture<?> houseKeeperHandle = houseKeeperScheduler.scheduleAtFixedRate(houseKeeperJob, 30, 30, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws KapuaException {
        houseKeeperScheduler.shutdown();
    }
}
