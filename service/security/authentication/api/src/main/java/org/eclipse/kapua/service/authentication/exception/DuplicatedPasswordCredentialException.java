/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.exception;

import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.user.User;

/**
 * {@link KapuaAuthenticationException} to {@code throw} when a {@link Credential} with {@link CredentialType#PASSWORD}
 * gets created for {@link User} that has already one {@link Credential} with {@link CredentialType#PASSWORD}.
 * <p>
 * A {@link User} can only have one {@link Credential} with {@link CredentialType#PASSWORD}.
 *
 * @since 2.0.0
 */
public class DuplicatedPasswordCredentialException extends KapuaAuthenticationException {

    /**
     * Constructor.
     *
     * @since 2.0.0
     */
    public DuplicatedPasswordCredentialException() {
        super(AuthenticationErrorCodes.DUPLICATED_PASSWORD_CREDENTIAL);
    }
}
