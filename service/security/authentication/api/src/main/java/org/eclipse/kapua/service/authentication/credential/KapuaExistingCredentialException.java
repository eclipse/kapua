/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;

/**
 * KapuaDuplicateNameException is thrown when an operation cannot be completed because an unique name constraint has been violated.
 *
 * @since 1.0
 *
 */
public class KapuaExistingCredentialException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;
    private static final String MESSAGE_FORMAT = "Credential of type %s already exists for this user.";

    /**
     * Constructor for the {@link KapuaExistingCredentialException} taking in the duplicated name.
     */
    public KapuaExistingCredentialException(CredentialType credentialType) {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXISTS, String.format(MESSAGE_FORMAT, credentialType));
    }
}
