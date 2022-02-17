/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Utility class to have an unified way of obtaining a {@link Random} instance to generate random values.
 * <p>
 * On first invocation of {@link #getInstance()} it initializes the singleton {@link #randomInstance}.
 * On init it tries to obtain an instance of {@link SecureRandom} with algorithm defined by {@link #ALGORITHM} value.
 * In the event of the algorithm not being available, it defaults the instance to the regular {@link Random}.
 *
 * @since 1.2.0
 */
public class RandomUtils {

    private static final Logger LOG = LoggerFactory.getLogger(RandomUtils.class);

    /**
     * Algorithm of the {@link SecureRandom}.
     *
     * @since 1.2.0
     */
    private static final String ALGORITHM = "SHA1PRNG";

    /**
     * The singleton instance of {@link Random}
     */
    private static Random randomInstance = getInstance();

    private RandomUtils() {
    }

    /**
     * Gets the singleton instance of {@link Random}.
     * <p>
     * This can be safely set into a {@code private static final} variable inside other classes.
     *
     * @return The singleton instance of {@link Random}.
     * @since 1.2.0
     */
    public static Random getInstance() {
        if (randomInstance == null) {
            createInstance();
        }

        return randomInstance;
    }

    /**
     * Initializes the {@link #randomInstance}.
     * <p>
     * It tries to obtain an instance of {@link SecureRandom} with algorithm defined by {@link #ALGORITHM} value.
     * In the event of the algorithm not being available, logs the execption and it defaults the instance to the regular {@link Random}.
     *
     * @since 1.2.0
     */
    private static synchronized void createInstance() {
        if (randomInstance != null) {
            return;
        }

        try {
            randomInstance = SecureRandom.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn("Cannot obtain java.security.SecureRandom instance for algorithm: {}. Defaulting to java.util.Random instance!", ALGORITHM);
            randomInstance = new Random();
        }
    }
}
