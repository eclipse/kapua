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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link KuraKeystoreItem} definition.
 *
 * @since 1.5.0
 */
@JsonRootName("keystoreItem")
public class KuraKeystoreItem extends AbstractKuraKeystoreItem {

    @JsonProperty("type")
    private String type;

    @JsonProperty("size")
    private Integer size;

    @JsonProperty("algorithm")
    private String algorithm;

    @JsonProperty("subjectDN")
    private String subjectDN;

    @JsonProperty("subjectAN")
    private List<String[]> subjectAN;

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("startDate")
    private Long startDate;

    @JsonProperty("expirationDate")
    private Long expirationDate;

    @JsonProperty("certificate")
    private String certificate;

    @JsonProperty("certificateChain")
    private List<String> certificateChain;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public KuraKeystoreItem() {
    }

    /**
     * Gets the keystore item type.
     * <p>
     * Examples:
     * <ul>
     *     <li>PRIVATE_KEY</li>
     *     <li>TRUSTED_CERTIFICATE</li>
     * </ul>
     *
     * @return The keystore item type.
     * @since 1.5.0
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the keystore item type.
     *
     * @param type The keystore item type.
     * @since 1.5.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the size in bytes.
     *
     * @return The size in bytes.
     * @since 1.5.0
     */
    public Integer getSize() {
        return size;
    }

    /**
     * Sets the size in bytes.
     *
     * @param size The size in bytes.
     * @since 1.5.0
     */
    public void setSize(Integer size) {
        this.size = size;
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
     * Gets the subject distinguished name.
     *
     * @return The subject distinguished name.
     * @since 1.5.0
     */
    public String getSubjectDN() {
        return subjectDN;
    }

    /**
     * Sets the subject distinguished name.
     *
     * @param subjectDN The subject distinguished name.
     * @since 1.5.0
     */
    public void setSubjectDN(String subjectDN) {
        this.subjectDN = subjectDN;
    }

    /**
     * Gets the subject alternative names.
     *
     * @return The subject alternative names.
     * @since 1.5.0
     */
    public List<String[]> getSubjectAN() {
        if (subjectAN == null) {
            subjectAN = new ArrayList<>();
        }

        return subjectAN;
    }

    /**
     * Sets the subject alternative names.
     *
     * @param subjectAN The subject alternative names.
     * @since 1.5.0
     */
    public void setSubjectAN(List<String[]> subjectAN) {
        this.subjectAN = subjectAN;
    }

    /**
     * Gets the issuer.
     *
     * @return The issuer.
     * @since 1.5.0
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the issuer.
     *
     * @param issuer The issuer.
     * @since 1.5.0
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * Gets the not before timestamp.
     *
     * @return The not before timestamp.
     * @since 1.5.0
     */
    public Long getStartDate() {
        return startDate;
    }

    /**
     * Sets the not before timestamp.
     *
     * @param startDate The not before timestamp.
     * @since 1.5.0
     */
    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the not after timestamp.
     *
     * @return The not after timestamp.
     * @since 1.5.0
     */
    public Long getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the not after timestamp.
     *
     * @param expirationDate The not after timestamp.
     * @since 1.5.0
     */
    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
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

    /**
     * Gets the certificate chain.
     *
     * @return The certificate chain.
     * @since 1.5.0
     */
    public List<String> getCertificateChain() {
        if (certificateChain == null) {
            certificateChain = new ArrayList<>();
        }

        return certificateChain;
    }

    /**
     * Adds a certificate to the {@link List} for the {@link #getCertificateChain()}.
     *
     * @param certificate The certificate to add.
     * @since 1.5.0
     */
    public void addCertificateChain(String certificate) {
        getCertificateChain().add(certificate);
    }

    /**
     * Sets the certificate chain.
     *
     * @param certificateChain The certificate chain.
     * @since 1.5.0
     */
    public void setCertificateChain(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }
}
