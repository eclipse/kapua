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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.authentication.KapuaAuthenticationErrorCodes;

/**
 * KapuaDuplicateNameException is thrown when a password is going to be created that doesn't match the minimum required length.
 * 
 * @since 1.0
 * 
 */
public class KapuaPasswordTooShortException extends KapuaException {

    private static final long serialVersionUID = -2761138212317761216L;
    private static final String MESSAGE_FORMAT = "The specified password doesn't meet the required minimum length: %s.";

    /**
     * Constructor for the {@link KapuaPasswordTooShortException} taking in the duplicated name.
     */
    public KapuaPasswordTooShortException(int length) {
        super(KapuaAuthenticationErrorCodes.PASSWORD_CANNOT_BE_CHANGED, String.format(MESSAGE_FORMAT, length));
    }

}
