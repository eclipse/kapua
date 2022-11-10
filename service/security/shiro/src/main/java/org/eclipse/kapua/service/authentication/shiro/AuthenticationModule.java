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

import com.google.inject.Provides;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
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
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Named;
import java.util.Map;
import java.util.Optional;

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

    @Provides
    @Named("CredentialServiceConfigurationManager")
    public CredentialServiceConfigurationManager deviceConnectionServiceConfigurationManager(
            AuthenticationEntityManagerFactory authenticationEntityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester) {
        final CredentialServiceConfigurationManagerImpl credentialServiceConfigurationManager = new CredentialServiceConfigurationManagerImpl(
                new EntityManagerSession(authenticationEntityManagerFactory),
                permissionFactory,
                authorizationService,
                rootUserTester);

        final ServiceConfigurationManagerCachingWrapper cached = new ServiceConfigurationManagerCachingWrapper(credentialServiceConfigurationManager);
        return new CredentialServiceConfigurationManager() {
            @Override
            public int getSystemMinimumPasswordLength() {
                return credentialServiceConfigurationManager.getSystemMinimumPasswordLength();
            }

            @Override
            public void checkAllowedEntities(KapuaId scopeId, String entityType) throws KapuaException {
                cached.checkAllowedEntities(scopeId, entityType);
            }

            @Override
            public void setConfigValues(KapuaId scopeId, Optional<KapuaId> parentId, Map<String, Object> values) throws KapuaException {
                cached.setConfigValues(scopeId, parentId, values);
            }

            @Override
            public Map<String, Object> getConfigValues(KapuaId scopeId, boolean excludeDisabled) throws KapuaException {
                return cached.getConfigValues(scopeId, excludeDisabled);
            }

            @Override
            public KapuaTocd getConfigMetadata(KapuaId scopeId, boolean excludeDisabled) throws KapuaException {
                return cached.getConfigMetadata(scopeId, excludeDisabled);
            }
        };
    }

}