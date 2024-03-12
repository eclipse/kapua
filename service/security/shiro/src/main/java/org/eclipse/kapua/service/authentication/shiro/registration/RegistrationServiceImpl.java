/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.registration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.initializers.KapuaInitializingMethod;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.OpenIDLocator;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.eclipse.kapua.security.registration.RegistrationProcessor;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.user.User;
import org.jose4j.jwt.consumer.JwtContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Singleton
public class RegistrationServiceImpl implements RegistrationService, AutoCloseable {

    private final JwtProcessor jwtProcessor;
    private final Set<RegistrationProcessorProvider> registrationProcessorProvider;

    private final List<RegistrationProcessor> processors = new ArrayList<>();

    private final KapuaAuthenticationSetting authenticationSetting;

    @Inject
    public RegistrationServiceImpl(KapuaAuthenticationSetting authenticationSetting, OpenIDLocator openIDLocator, Set<RegistrationProcessorProvider> registrationProcessorProvider) throws OpenIDException {
        this.authenticationSetting = authenticationSetting;
        jwtProcessor = openIDLocator.getProcessor();

        this.registrationProcessorProvider = registrationProcessorProvider;
    }

    @KapuaInitializingMethod
    public void addProcessors() {
        for (RegistrationProcessorProvider provider : registrationProcessorProvider) {
            processors.addAll(provider.createAll());
        }
    }

    @Override
    public void close() throws Exception {
        if (jwtProcessor != null) {
            jwtProcessor.close();
        }

        // FIXME: use Suppressed

        for (final RegistrationProcessor processor : processors) {
            processor.close();
        }
    }

    @Override
    public boolean isAccountCreationEnabled() {
        final String registrationServiceEnabled = authenticationSetting.getString(
                KapuaAuthenticationSettingKeys.AUTHENTICATION_REGISTRATION_SERVICE_ENABLED, String.valueOf(false));
        if (registrationServiceEnabled.equals(String.valueOf(false))) {
            return false;
        } else {
            return !processors.isEmpty();
        }
    }

    @Override
    public boolean createAccount(final JwtCredentials credentials) throws KapuaException {
        if (!isAccountCreationEnabled()) {
            // early return
            return false;
        }

        try {
            final JwtContext context = jwtProcessor.process(credentials.getIdToken());

            for (final RegistrationProcessor processor : processors) {
                final Optional<User> result = processor.createUser(context);
                if (result.isPresent()) {
                    return true;
                }
            }

            return false;

        } catch (final Exception e) {
            throw KapuaException.internalError(e);
        }
    }

}
