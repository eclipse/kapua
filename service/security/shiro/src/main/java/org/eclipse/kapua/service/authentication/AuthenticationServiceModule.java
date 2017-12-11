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

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventListenerConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;

@KapuaProvider
public class AuthenticationServiceModule extends ServiceEventModule {

    @Inject
    private CredentialService credentialService;
    @Inject
    private AccessTokenService accessTokenService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAuthenticationSetting kas = KapuaAuthenticationSetting.getInstance();
        ServiceEventListenerConfiguration[] selc = new ServiceEventListenerConfiguration[4];
        selc[0] = new ServiceEventListenerConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.USER_EVENT_ADDRESS),
                kas.getString(KapuaAuthenticationSettingKeys.CREDENTIAL_SUBSCRIPTION_NAME),
                credentialService);
        selc[1] = new ServiceEventListenerConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.USER_EVENT_ADDRESS),
                kas.getString(KapuaAuthenticationSettingKeys.ACCESS_TOKEN_SUBSCRIPTION_NAME),
                accessTokenService);
        selc[2] = new ServiceEventListenerConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kas.getString(KapuaAuthenticationSettingKeys.CREDENTIAL_SUBSCRIPTION_NAME),
                credentialService);
        selc[3] = new ServiceEventListenerConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.ACCOUNT_EVENT_ADDRESS),
                kas.getString(KapuaAuthenticationSettingKeys.ACCESS_TOKEN_SUBSCRIPTION_NAME),
                accessTokenService);
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_INTERNAL_EVENT_ADDRESS),
                kas.getList(String.class, KapuaAuthenticationSettingKeys.AUTHENTICATION_SERVICES_NAMES),
                AuthenticationEntityManagerFactory.getInstance(),
                selc);
    }

}
