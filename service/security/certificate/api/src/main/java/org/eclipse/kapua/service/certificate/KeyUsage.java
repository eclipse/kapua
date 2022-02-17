/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate;

/**
 * @since 1.0.0
 */
public enum KeyUsage {

    /**
     * @since 1.0.0
     */
    DIGITAL_SIGNATURE,
    /**
     * @since 1.0.0
     */
    NON_REPUDIATION,
    /**
     * @since 1.0.0
     */
    KEY_ENCIPHERMENT,
    /**
     * @since 1.0.0
     */
    DATA_ENCIPHERMENT,
    /**
     * @since 1.0.0
     */
    KEY_AGREEMENT,
    /**
     * @since 1.0.0
     */
    KEY_CERT_SIGN,
    /**
     * @since 1.0.0
     */
    CRL_SIGN,
    /**
     * @since 1.0.0
     */
    ENCIPHER_ONLY,
    /**
     * @since 1.0.0
     */
    DECIPHER_ONLY,
}
