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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationErrorCodes;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * Authentication utilities.
 * 
 * @since 1.0
 * 
 */
public class AuthenticationUtils
{

    /**
     * Encrypts and return the plain credential value (unencrypted value).
     * 
     * @param plainValue
     * @return
     * @throws KapuaException
     */
    public static String cryptCredential(String plainValue)
        throws KapuaException
    {
        //
        // Argument validator
        ArgumentValidator.notEmptyOrNull(plainValue, "plainValue");

        //
        // Do crypt
        String cryptedValue = null;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            String salt = BCrypt.gensalt(12, random);
            cryptedValue = BCrypt.hashpw(plainValue, salt);
        }
        catch (NoSuchAlgorithmException e) {
            throw new KapuaRuntimeException(KapuaAuthenticationErrorCodes.CREDENTIAL_CRYPT_ERROR, e, (Object[]) null);
        }

        return cryptedValue;
    }
}
