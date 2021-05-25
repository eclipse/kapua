/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.kura.model.keystore;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * {@link KuraKeystoreSignedCertificate} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreSignedCertificate")
public class KuraKeystoreSignedCertificate {

    @JsonProperty("signedCertificate")
    private String signedCertificate;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreSignedCertificate() {
    }

    /**
     * Gets the signed certificate.
     *
     * @return The signed certificate.
     * @since 1.5.0
     */
    public String getSignedCertificate() {
        return signedCertificate;
    }

    /**
     * Sets the signed certificate.
     *
     * @param signedCertificate The signed certificate.
     * @since 1.5.0
     */
    public void setSignedCertificate(String signedCertificate) {
        this.signedCertificate = signedCertificate;
    }
}
