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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.security.registration.RegistrationProcessor;
import org.eclipse.kapua.security.registration.RegistrationProcessorProvider;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSetting;
import org.eclipse.kapua.service.authentication.shiro.setting.KapuaAuthenticationSettingKeys;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.plugin.sso.openid.JwtProcessor;
import org.eclipse.kapua.plugin.sso.openid.exception.OpenIDException;
import org.jose4j.jwt.consumer.JwtContext;

@KapuaProvider
public class RegistrationServiceImpl implements RegistrationService, AutoCloseable {

    private final JwtProcessor jwtProcessor;

    private final List<RegistrationProcessor> processors = new ArrayList<>();

    private static final KapuaAuthenticationSetting SETTING = KapuaAuthenticationSetting.getInstance();

    public RegistrationServiceImpl() throws OpenIDException {
        jwtProcessor = JwtProcessors.createDefault();

        for (RegistrationProcessorProvider provider : ServiceLoader.load(RegistrationProcessorProvider.class)) {
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
        final String registrationServiceEnabled = SETTING.getString(
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
