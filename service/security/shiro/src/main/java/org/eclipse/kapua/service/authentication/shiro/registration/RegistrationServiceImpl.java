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

import static java.util.Optional.empty;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.JwtCredentials;
import org.eclipse.kapua.service.authentication.registration.RegistrationService;
import org.eclipse.kapua.service.authentication.shiro.utils.JwtProcessors;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSetting;
import org.eclipse.kapua.service.authorization.shiro.setting.KapuaAuthorizationSettingKeys;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.sso.jwt.JwtProcessor;
import org.jose4j.jwt.consumer.JwtContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class RegistrationServiceImpl implements RegistrationService, AutoCloseable {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationServiceImpl.class);

    private final JwtProcessor jwtProcessor;

    private final List<RegistrationProcessor> processors = new ArrayList<>();

    public RegistrationServiceImpl() {
        jwtProcessor = JwtProcessors.createDefault();

        final Optional<KapuaId> rootAccount = evalRootAccount();
        if (rootAccount.isPresent()) {
            processors.add(new SimpleRegistrationProcessor("preferred_username", rootAccount.get()));
        }
    }

    private Optional<KapuaId> evalRootAccount() {
        try {
            final String accountName = KapuaAuthorizationSetting.getInstance().getString(KapuaAuthorizationSettingKeys.AUTO_REGISTRATION_SIMPLE_ROOT_ACCOUNT);
            if (accountName != null && !accountName.isEmpty()) {
                return loadFrom(accountName);
            }
            return empty();
        } catch (KapuaException e) {
            throw new RuntimeException("Failed to load root account ID", e);
        }
    }

    private static Optional<KapuaId> loadFrom(final String accountName) throws KapuaException {
        final User user = KapuaLocator.getInstance().getService(UserService.class).findByName("kapua-sys");
        if (user != null) {
            return Optional.ofNullable(user).map(User::getScopeId);
        }
        logger.warn("Failed to load ID of '{}'. Entry not found.", accountName);
        return Optional.empty();
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
