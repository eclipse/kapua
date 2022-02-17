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
package org.eclipse.kapua.transport.utils;


import org.eclipse.kapua.commons.util.RandomUtils;

import java.util.Random;

/**
 * Utility class that generates random IDs for the transport layer.
 *
 * @author alberto.codutti
 * @since 1.0.0
 */
public class ClientIdGenerator {

    /**
     * Generated ID format
     *
     * @since 1.0.0
     */
    private static final String GENERATED_ID_STRING_FORMAT = "%s-%d-%d";

    /**
     * {@link Random} instance.
     *
     * @since 1.2.0
     */
    private static final Random RANDOM = RandomUtils.getInstance();

    /**
     * {@code static} instance singleton reference
     *
     * @since 1.0.0
     */
    private static final ClientIdGenerator INSTANCE = new ClientIdGenerator();

    /**
     * Private default constructor. To obtain an instance of {@link ClientIdGenerator} use {@link ClientIdGenerator#getInstance()}.
     *
     * @since 1.0.0
     */
    private ClientIdGenerator() {
    }

    /**
     * Returns a {@code static} instance of the {@link ClientIdGenerator}.
     *
     * @return The singleton instance of {@link ClientIdGenerator}
     * @since 1.0.0
     */
    public static ClientIdGenerator getInstance() {
        return INSTANCE;
    }

    /**
     * Shortcut method for {@link ClientIdGenerator#next(String)} with prefix "Id"
     *
     * @return The generated {@link String} to be used as client id.
     * @since 1.0.0
     */
    public String next() {
        return next("Id");
    }

    /**
     * Generates an String that can be used as client id in the transport layer.
     * <p>
     * The format is: {prefix}-{currentMillis}-{randomNumber}
     * </p>
     *
     * @param prefix The prefix to use to build the String.
     * @return The generated {@link String} to be used as client id.
     * @since 1.0.0
     */
    public String next(String prefix) {
        long timestamp = System.currentTimeMillis();
        long randomNumber = RANDOM.nextLong();

        return String.format(GENERATED_ID_STRING_FORMAT,
                prefix,
                timestamp,
                randomNumber >= 0 ? randomNumber : -randomNumber);
    }
}
