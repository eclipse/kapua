/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
    private static final String MESSAGE_FORMAT = "Credential of type %s for user id %s already exists";

    /**
     * Constructor for the {@link KapuaExistingCredentialException} taking in the duplicated name.
     */
    public KapuaExistingCredentialException(CredentialType credentialType, String userId) {
        super(KapuaErrorCodes.ENTITY_ALREADY_EXISTS, String.format(MESSAGE_FORMAT, credentialType, userId));
    }
}
