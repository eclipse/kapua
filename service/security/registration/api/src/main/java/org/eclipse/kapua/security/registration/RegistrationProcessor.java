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
package org.eclipse.kapua.security.registration;

import org.eclipse.kapua.service.user.User;
import org.jose4j.jwt.consumer.JwtContext;

import java.util.Optional;

/**
 * A registration processor
 *
 * <p>
 * A registration process may be able to create a new user based on the provided
 * OpenID SSO authentication context.
 * </p>
 */
public interface RegistrationProcessor extends AutoCloseable {

    /**
     * Ask the registration process to create a new user
     *
     * @param context the context to use as reference
     * @return an optional new user, never returns {@code null}, but may return {@link Optional#empty()}
     * @throws Exception if anything goes wrong
     */
    Optional<User> createUser(JwtContext context) throws Exception;
}
