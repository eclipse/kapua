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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;

//@KapuaProvider
public class AuthenticationServiceModule extends ServiceEventModule {

    @Inject
    private CredentialService credentialService;
    @Inject
    private AccessTokenService accessTokenService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaAuthenticationSetting kas = KapuaAuthenticationSetting.getInstance();
        List<ServiceEventClientConfiguration> selc = new ArrayList<>();
        selc.addAll(ServiceInspector.getEventBusClients(credentialService, CredentialService.class));
        selc.addAll(ServiceInspector.getEventBusClients(accessTokenService, AccessTokenService.class));
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_EVENT_ADDRESS),
                AuthenticationEntityManagerFactory.getInstance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }

}
