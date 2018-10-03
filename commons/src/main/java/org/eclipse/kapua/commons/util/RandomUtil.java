/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {

    private static SecureRandom random;
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);

    static {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unable to initialize RNG", e);
        }
    }

    private RandomUtil() { }

    public static String getRandomString(int length) {

        byte[] bPre = new byte[length];
        random.nextBytes(bPre);
        return Base64.getEncoder().encodeToString(bPre).substring(0, length);

    }
}
