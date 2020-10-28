/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;

/**
 * KapuaDuplicateNameException is thrown when a password is going to be created that doesn't match the minimum required length.
 * 
 * @since 1.0
 * 
 */
public class KapuaPasswordTooLongException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;
    private static final String MESSAGE_FORMAT = "The specified password is longer than %s characters.";

    /**
     * Constructor for the {@link KapuaPasswordTooLongException} taking in the duplicated name.
     */
    public KapuaPasswordTooLongException(int length) {
        super(KapuaAuthenticationErrorCodes.PASSWORD_CANNOT_BE_CHANGED, String.format(MESSAGE_FORMAT, length));
    }

}
