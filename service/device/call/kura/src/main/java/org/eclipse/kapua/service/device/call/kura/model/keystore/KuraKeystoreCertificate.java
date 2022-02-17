/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
 * {@link KuraKeystoreCertificate} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreCertificate")
public class KuraKeystoreCertificate extends AbstractKuraKeystoreItem {

    @JsonProperty("certificate")
    private String certificate;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreCertificate() {
    }

    /**
     * Gets the certificate.
     *
     * @return The certificate.
     * @since 1.5.0
     */
    public String getCertificate() {
        return certificate;
    }

    /**
     * Sets the certificate.
     *
     * @param certificate The certificate.
     * @since 1.5.0
     */
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }
}
