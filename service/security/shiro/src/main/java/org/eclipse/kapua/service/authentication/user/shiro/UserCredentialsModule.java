/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.user.shiro;

import com.google.inject.Module;
import com.google.inject.Provides;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.jpa.KapuaJpaTxManagerFactory;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.CredentialsFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialRepository;
import org.eclipse.kapua.service.authentication.credential.shiro.CredentialMapper;
import org.eclipse.kapua.service.authentication.credential.shiro.PasswordValidator;
import org.eclipse.kapua.service.authentication.user.UserCredentialsFactory;
import org.eclipse.kapua.service.authentication.user.UserCredentialsService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.user.UserService;

import javax.inject.Singleton;

/**
 * {@code kapua-security-shiro} {@link Module} implementation.
 *
 * @since 2.0.0
 */
public class UserCredentialsModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(UserCredentialsFactory.class).to(UserCredentialsFactoryImpl.class);
    }

    @Provides
    @Singleton
    UserCredentialsService userCredentialsService(
            AuthenticationService authenticationService,
            AuthorizationService authorizationService,
            PermissionFactory permissionFactory,
            UserCredentialsFactory userCredentialsFactory,
            CredentialsFactory credentialsFactory,
            CredentialFactory credentialFactory,
            KapuaJpaTxManagerFactory txManagerFactory,
            UserService userService,
            CredentialRepository credentialRepository,
            CredentialMapper credentialMapper,
            PasswordValidator passwordValidator) {
        return new UserCredentialsServiceImpl(
                authenticationService,
                authorizationService,
                permissionFactory,
                userCredentialsFactory,
                credentialsFactory,
                credentialFactory,
                txManagerFactory.create("kapua-authorization"),
                userService,
                credentialRepository,
                credentialMapper,
                passwordValidator);
    }
}
