/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.registration;

import static org.eclipse.kapua.service.authentication.shiro.registration.SimpleRegistrationProcessor.Settings.loadSimpleSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.sso.jwt.JwtProcessor;
import org.jose4j.jwt.consumer.JwtContext;

@KapuaProvider
public class RegistrationServiceImpl implements RegistrationService, AutoCloseable {

    private final JwtProcessor jwtProcessor;

    private final List<RegistrationProcessor> processors = new ArrayList<>();

    public RegistrationServiceImpl() {
        jwtProcessor = JwtProcessors.createDefault();

        loadSimpleSettings(KapuaAuthorizationSetting.getInstance())
                .ifPresent(settings -> processors.add(new SimpleRegistrationProcessor("preferred_username", settings)));
    }

    @Override
    public void close() throws Exception {
        if (jwtProcessor != null) {
            jwtProcessor.close();
        }
    }

    @Override
    public boolean isAccountCreationEnabled() {
        return !processors.isEmpty();
    }

    @Override
    public boolean createAccount(final JwtCredentials credentials) throws KapuaException {
        if (!isAccountCreationEnabled()) {
            // early return
            return false;
        }

        try {
            final JwtContext context = jwtProcessor.process(credentials.getJwt());

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
