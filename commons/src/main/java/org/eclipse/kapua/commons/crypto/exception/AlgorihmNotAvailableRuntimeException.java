/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.crypto.exception;

/**
 * {@link CryptoRuntimeException} to {@code throw} when the crypt algorithm is not available.
 *
 * @since 2.0.0
 */
public class AlgorihmNotAvailableRuntimeException extends CryptoRuntimeException {

    private final String algorithmName;

    /**
     * Constructor.
     *
     * @param cause         The original cause of the error.
     * @param algorithmName The algorithm that is not available.
     * @since 2.0.0
     */
    public AlgorihmNotAvailableRuntimeException(Throwable cause, String algorithmName) {
        super(CryptoErrorCodes.ALGORITHM_NOT_AVAILABLE, cause, algorithmName);

        this.algorithmName = algorithmName;
    }

    /**
     * Gets the algorithm that is not available.
     *
     * @return The algorithm that is not available.
     * @since 2.0.0
     */
    public String getAlgorithmName() {
        return algorithmName;
    }
}
