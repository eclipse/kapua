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
package org.eclipse.kapua.service.device.management.keystore.model;

import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.device.registry.Device;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

/**
 * {@link DeviceKeystoreItem} definition.
 * <p>
 * Identifies an item of the {@link DeviceKeystore} for the {@link Device}.
 *
 * @since 1.5.0
 */
@XmlRootElement(name = "deviceKeystoreItem")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceKeystoreXmlRegistry.class, factoryMethod = "newDeviceKeystoreItem")
public interface DeviceKeystoreItem {

    /**
     * Gets the keystore id.
     *
     * @return The keystore id.
     * @since 1.5.0
     */
    @XmlElement(name = "keystoreId")
    String getKeystoreId();

    /**
     * Sets the keystore id.
     *
     * @param keystoreId The keystore id.
     * @since 1.5.0
     */
    void setKeystoreId(String keystoreId);

    /**
     * Gets the alias.
     *
     * @return The alias.
     * @since 1.5.0
     */
    @XmlElement(name = "alias")
    String getAlias();

    /**
     * Sets the alias.
     *
     * @param alias The alias.
     * @since 1.5.0
     */
    void setAlias(String alias);

    /**
     * Gets the item type.
     * <p>
     * Examples:
     * <ul>
     *     <li>PRIVATE_KEY</li>
     *     <li>TRUSTED_CERTIFICATE</li>
     * </ul>
     *
     * @return The keystore item type.
     * @return The item type.
     * @since 1.5.0
     */
    @XmlElement(name = "itemType")
    String getItemType();

    /**
     * Sets the item type.
     *
     * @param itemType The item type.
     * @since 1.5.0
     */
    void setItemType(String itemType);

    /**
     * Gets the size in bytes.
     *
     * @return The size in bytes.
     * @since 1.5.0
     */
    @XmlElement(name = "size")
    Integer getSize();

    /**
     * Sets the size in bytes.
     *
     * @param size The size in bytes.
     * @since 1.5.0
     */
    void setSize(Integer size);

    /**
     * Gets the algorithm.
     *
     * @return The algorithm.
     * @since 1.5.0
     */
    @XmlElement(name = "algorithm")
    String getAlgorithm();

    /**
     * Sets the algorithm.
     *
     * @param algorithm The algorithm.
     * @since 1.5.0
     */
    void setAlgorithm(String algorithm);

    /**
     * Gets the subject distinguished name.
     *
     * @return The subject distinguished name.
     * @since 1.5.0
     */
    @XmlElement(name = "subjectDN")
    String getSubjectDN();

    /**
     * Sets the subject distinguished name.
     *
     * @param subjectDN The subject distinguished name.
     * @since 1.5.0
     */
    void setSubjectDN(String subjectDN);

    /**
     * Gets the {@link List} of {@link DeviceKeystoreSubjectAN}s.
     *
     * @return The {@link List} of {@link DeviceKeystoreSubjectAN}s.
     * @since 1.5.0
     */
    @XmlElement(name = "subjectANs")
    List<DeviceKeystoreSubjectAN> getSubjectAN();

    /**
     * Adds a {@link DeviceKeystoreSubjectAN} to {@link #getSubjectAN()}.
     *
     * @param subjectAN The {@link DeviceKeystoreSubjectAN} to add.
     * @since 1.5.0
     */
    void addSubjectAN(DeviceKeystoreSubjectAN subjectAN);

    /**
     * Sets the {@link List} of {@link DeviceKeystoreSubjectAN}s.
     *
     * @param subjectAN The {@link List} of {@link DeviceKeystoreSubjectAN}s.
     * @since 1.5.0
     */
    void setSubjectAN(List<DeviceKeystoreSubjectAN> subjectAN);

    /**
     * Gets the issuer.
     *
     * @return The issuer.
     * @since 1.5.0
     */
    @XmlElement(name = "issuer")
    String getIssuer();

    /**
     * Sets the issuer.
     *
     * @param issuer The issuer.
     * @since 1.5.0
     */
    void setIssuer(String issuer);

    /**
     * Gets the not before {@link Date}.
     *
     * @return The not before {@link Date}.
     * @since 1.5.0
     */
    @XmlElement(name = "notBefore")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotBefore();

    /**
     * Sets the not before {@link Date}.
     *
     * @param notBefore The not before {@link Date}.
     * @since 1.5.0
     */
    void setNotBefore(Date notBefore);

    /**
     * Gets the not after {@link Date}.
     *
     * @return The not after {@link Date}.
     * @since 1.5.0
     */
    @XmlElement(name = "notAfter")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotAfter();

    /**
     * Sets the not after {@link Date}.
     *
     * @param notAfter The not after {@link Date}.
     * @since 1.5.0
     */
    void setNotAfter(Date notAfter);

    /**
     * Gets the certificate.
     *
     * @return The certificate.
     * @since 1.5.0
     */
    @XmlElement(name = "certificate")
    String getCertificate();

    /**
     * Sets the certificate.
     *
     * @param certificate The certificate.
     * @since 1.5.0
     */
    void setCertificate(String certificate);

    /**
     * Gets the certificate chain.
     *
     * @return The certificate chain.
     * @since 1.5.0
     */
    @XmlElement(name = "certificateChain")
    List<String> getCertificateChain();

    /**
     * Adds a certificate to the {@link #getCertificateChain()}.
     *
     * @param certificate The certificate to add.
     * @since 1.5.0
     */
    void addCertificateChain(String certificate);

    /**
     * Sets the certificate chain.
     *
     * @param certificateChain The certificate chain.
     * @since 1.5.0
     */
    void setCertificateChain(List<String> certificateChain);

}
