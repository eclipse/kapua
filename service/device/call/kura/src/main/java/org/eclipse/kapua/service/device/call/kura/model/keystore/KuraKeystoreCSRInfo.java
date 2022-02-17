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
 * {@link KuraKeystoreCSRInfo} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreCSRInfo")
public class KuraKeystoreCSRInfo extends AbstractKuraKeystoreItem {

    @JsonProperty("signatureAlgorithm")
    private String signatureAlgorithm;

    @JsonProperty("attributes")
    private String attributes;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreCSRInfo() {
    }

    /**
     * Gets the signature algorithm.
     *
     * @return The signature algorithm.
     * @since 1.5.0
     */
    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    /**
     * Sets the signature algorithm.
     *
     * @param signatureAlgorithm The signature algorithm.
     * @since 1.5.0
     */
    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * Gets the attributes.
     *
     * @return The attributes.
     * @since 1.5.0
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes The attributes.
     * @since 1.5.0
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
