/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionServiceImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeServiceImpl;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialServiceImpl;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.registration.RegistrationServiceImpl;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenFactoryImpl;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenServiceImpl;

public class AuthenticationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(AuthenticationService.class).to(AuthenticationServiceShiroImpl.class);

        bind(CredentialFactory.class).to(CredentialFactoryImpl.class);
        bind(CredentialService.class).to(CredentialServiceImpl.class);
        bind(CredentialsFactory.class).to(CredentialsFactoryImpl.class);

        bind(MfaOptionFactory.class).to(MfaOptionFactoryImpl.class);
        bind(MfaOptionService.class).to(MfaOptionServiceImpl.class);
        bind(ScratchCodeFactory.class).to(ScratchCodeFactoryImpl.class);
        bind(ScratchCodeService.class).to(ScratchCodeServiceImpl.class);

        bind(AccessTokenFactory.class).to(AccessTokenFactoryImpl.class);
        bind(AccessTokenService.class).to(AccessTokenServiceImpl.class);

        bind(RegistrationService.class).to(RegistrationServiceImpl.class);
    }
}
