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
 * {@link KuraKeystoreKeypair} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreKeypair")
public class KuraKeystoreKeypair extends AbstractKuraKeystoreItem {

    @JsonProperty("algorithm")
    private String algorithm;

    @JsonProperty("signatureAlgorithm")
    private String signatureAlgorithm;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("attributes")
    private String attributes;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreKeypair() {
    }

    /**
     * Gets the algorithm.
     *
     * @return The algorithm.
     * @since 1.5.0
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the algorithm.
     *
     * @param algorithm The algorithm.
     * @since 1.5.0
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
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
     * Gets the key size.
     *
     * @return The key size.
     * @since 1.5.0
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the key size.
     *
     * @param size The key size.
     * @since 1.5.0
     */
    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * Gets the certificate attributes.
     *
     * @return The certificate attributes.
     * @since 1.5.0
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * Sets the certificate attributes.
     *
     * @param attributes The certificate attributes.
     * @since 1.5.0
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }
}
