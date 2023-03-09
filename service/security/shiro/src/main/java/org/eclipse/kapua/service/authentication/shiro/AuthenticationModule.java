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
import com.google.inject.name.Names;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableServiceCache;
import org.eclipse.kapua.commons.configuration.CachingServiceConfigRepository;
import org.eclipse.kapua.commons.configuration.RootUserTester;
import org.eclipse.kapua.commons.configuration.ServiceConfigImplJpaRepository;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;
import org.eclipse.kapua.commons.configuration.ServiceConfigurationManagerCachingWrapper;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.JpaTxManager;
import org.eclipse.kapua.commons.jpa.KapuaEntityManagerFactory;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountRepository;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeFactory;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.ScratchCodeService;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionImplJpaRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.MfaOptionServiceImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeImplJpaRepository;
import org.eclipse.kapua.service.authentication.credential.mfa.shiro.ScratchCodeServiceImpl;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialFactoryImpl;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialImplJpaRepository;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialServiceImpl;
import org.eclipse.kapua.service.authentication.mfa.MfaAuthenticator;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.mfa.MfaAuthenticatorImpl;
import org.eclipse.kapua.service.authentication.shiro.registration.RegistrationServiceImpl;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.token.AccessTokenFactory;
import org.eclipse.kapua.service.authentication.token.AccessTokenRepository;
import org.eclipse.kapua.service.authentication.token.AccessTokenService;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenFactoryImpl;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenImplJpaRepository;
import org.eclipse.kapua.service.authentication.token.shiro.AccessTokenServiceImpl;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.UserRepository;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

public class AuthenticationModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(AuthenticationService.class).to(AuthenticationServiceShiroImpl.class);
        bind(ServiceConfigurationManager.class)
                .annotatedWith(Names.named("CredentialServiceConfigurationManager"))
                .to(CredentialServiceConfigurationManager.class);
        bind(CredentialFactory.class).to(CredentialFactoryImpl.class);
        bind(CredentialsFactory.class).to(CredentialsFactoryImpl.class);
        bind(MfaOptionFactory.class).to(MfaOptionFactoryImpl.class);
        bind(ScratchCodeFactory.class).to(ScratchCodeFactoryImpl.class);
        bind(AccessTokenFactory.class).to(AccessTokenFactoryImpl.class);
        bind(RegistrationService.class).to(RegistrationServiceImpl.class);
        bind(MfaAuthenticator.class).toInstance(new MfaAuthenticatorImpl());
    }

    @Provides
    @Singleton
    AccessTokenService accessTokenService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            AccessTokenRepository accessTokenRepository,
            AccessTokenFactory accessTokenFactory) {
        return new AccessTokenServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authentication")),
                accessTokenRepository,
                accessTokenFactory);
    }

    @Provides
    @Singleton
    MfaOptionService mfaOptionService(
            MfaAuthenticator mfaAuthenticator,
            MfaOptionRepository mfaOptionRepository,
            AccountRepository accountRepository,
            ScratchCodeRepository scratchCodeRepository,
            ScratchCodeFactory scratchCodeFactory,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            UserRepository userRepository) {

        final KapuaAuthenticationSetting authenticationSetting = KapuaAuthenticationSetting.getInstance();
        int trustKeyDuration = authenticationSetting.getInt(KapuaAuthenticationSettingKeys.AUTHENTICATION_MFA_TRUST_KEY_DURATION);

        return new MfaOptionServiceImpl(
                trustKeyDuration,
                mfaAuthenticator,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authentication")),
                mfaOptionRepository,
                accountRepository,
                scratchCodeRepository,
                scratchCodeFactory,
                authorizationService,
                permissionFactory,
                userRepository
        );
    }

    @Provides
    @Singleton
    ScratchCodeService scratchCodeService(
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            ScratchCodeRepository scratchCodeRepository,
            ScratchCodeFactory scratchCodeFactory) {
        return new ScratchCodeServiceImpl(
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authentication")),
                scratchCodeRepository,
                scratchCodeFactory);
    }

    @Provides
    @Singleton
    public AccessTokenRepository accessTokenRepository() {
        return new AccessTokenImplJpaRepository();
    }

    @Provides
    @Singleton
    public MfaOptionRepository mfaOptionRepository() {
        return new MfaOptionImplJpaRepository();
    }

    @Provides
    @Singleton
    public ScratchCodeRepository scratchCodeRepository() {
        return new ScratchCodeImplJpaRepository();
    }

    @Provides
    @Singleton
    public CredentialService credentialService(
            @Named("CredentialServiceConfigurationManager") CredentialServiceConfigurationManager serviceConfigurationManager,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            CredentialRepository credentialRepository,
            CredentialFactory credentialFactory) {
        return new CredentialServiceImpl(serviceConfigurationManager,
                authorizationService,
                permissionFactory,
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authentication")),
                credentialRepository,
                credentialFactory);
    }

    @Provides
    @Singleton
    CredentialRepository credentialRepository() {
        return new CredentialImplJpaRepository();
    }

    @Provides
    @Singleton
    @Named("CredentialServiceConfigurationManager")
    public CredentialServiceConfigurationManager credentialServiceConfigurationManager(
            AuthenticationEntityManagerFactory authenticationEntityManagerFactory,
            PermissionFactory permissionFactory,
            AuthorizationService authorizationService,
            RootUserTester rootUserTester) {
        final CredentialServiceConfigurationManagerImpl credentialServiceConfigurationManager = new CredentialServiceConfigurationManagerImpl(
                new JpaTxManager(new KapuaEntityManagerFactory("kapua-authentication")),
                new CachingServiceConfigRepository(
                        new ServiceConfigImplJpaRepository(),
                        new AbstractKapuaConfigurableServiceCache().createCache()
                ),
                permissionFactory,
                authorizationService,
                rootUserTester);

        final ServiceConfigurationManagerCachingWrapper cached = new ServiceConfigurationManagerCachingWrapper(credentialServiceConfigurationManager);
        return new CredentialServiceConfigurationManager() {
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
