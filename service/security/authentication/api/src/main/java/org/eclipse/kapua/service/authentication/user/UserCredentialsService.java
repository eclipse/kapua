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
package org.eclipse.kapua.service.authentication.user;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.credential.Credential;

/**
 * Credential service definition.
 *
 * @since 2.0.0
 */
public interface UserCredentialsService extends KapuaService {

    /**
     * Change the password of the authenticated user, according to the given {@link PasswordChangeRequest}
     *
     * @param passwordChangeRequest request for change the password
     * @return The updated credential
     * @throws KapuaException
     */
    Credential changePasswordRequest(PasswordChangeRequest passwordChangeRequest) throws KapuaException;


    /**
     * Reset the password of a user, according to the given {@link PasswordResetRequest}
     *
     * @param scopeId              scope of the {@link Credential} in which to change the password
     * @param credentialId         id of the {@link Credential} to change the password
     * @param passwordResetRequest request for resetting password
     * @return The updated credential
     */
    Credential resetPassword(KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException;
}
