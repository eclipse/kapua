/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.user.PasswordResetRequest;
import org.eclipse.kapua.storage.TxContext;

/**
 * @since 2.0.0
 */
public interface PasswordResetter {
    /**
     * @since 2.0.0
     */
    Credential resetPassword(TxContext tx, KapuaId scopeId, KapuaId userId, boolean failIfAbsent, PasswordResetRequest passwordResetRequest) throws KapuaException;

    /**
     * @since 2.0.0
     */
    Credential resetPassword(TxContext tx, KapuaId scopeId, KapuaId credentialId, PasswordResetRequest passwordResetRequest) throws KapuaException;
}
