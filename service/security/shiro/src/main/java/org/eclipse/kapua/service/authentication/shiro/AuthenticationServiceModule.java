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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventHouseKeeperFactory;
import org.eclipse.kapua.commons.event.ServiceEventTransactionalModule;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.event.ServiceEventBus;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;

import java.util.Arrays;
import java.util.stream.Collectors;

public class AuthenticationServiceModule extends ServiceEventTransactionalModule {

    public AuthenticationServiceModule(
            CredentialService credentialService,
            AccessTokenService accessTokenService,
            KapuaAuthenticationSetting authenticationSetting,
            ServiceEventHouseKeeperFactory serviceEventTransactionalHousekeeperFactory,
            ServiceEventBus serviceEventBus) {
        super(Arrays.asList(
                                ServiceInspector.getEventBusClients(credentialService, CredentialService.class),
                                ServiceInspector.getEventBusClients(accessTokenService, AccessTokenService.class)
                        )
                        .stream()
                        .flatMap(l -> l.stream())
                        .collect(Collectors.toList())
                        .toArray(new ServiceEventClientConfiguration[0]),
                authenticationSetting.getString(KapuaAuthenticationSettingKeys.AUTHENTICATION_EVENT_ADDRESS),
                serviceEventTransactionalHousekeeperFactory,
                serviceEventBus);
    }
}
