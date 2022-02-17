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
package org.eclipse.kapua.service.device.management.keystore.model.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreSubjectAN;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link DeviceKeystoreItem} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreItemImpl implements DeviceKeystoreItem {

    private String keystoreId;
    private String itemType;
    private Integer size;
    private String algorithm;
    private String alias;
    private String subjectDN;
    private List<DeviceKeystoreSubjectAN> subjectAN;
    private String issuer;
    private Date notBefore;
    private Date notAfter;
    private String certificate;
    private List<String> certificateChain;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreItemImpl() {
    }

    @Override
    public String getKeystoreId() {
        return keystoreId;
    }

    @Override
    public void setKeystoreId(String keystoreId) {
        this.keystoreId = keystoreId;
    }

    @Override
    public String getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Override
    public Integer getSize() {
        return size;
    }

    @Override
    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String getAlgorithm() {
        return algorithm;
    }

    @Override
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String getSubjectDN() {
        return subjectDN;
    }

    @Override
    public void setSubjectDN(String subjectDN) {
        this.subjectDN = subjectDN;
    }

    @Override
    public List<DeviceKeystoreSubjectAN> getSubjectAN() {
        if (subjectAN == null) {
            subjectAN = new ArrayList<>();
        }

        return subjectAN;
    }

    @Override
    public void addSubjectAN(DeviceKeystoreSubjectAN subjectAN) {
        getSubjectAN().add(subjectAN);
    }

    @Override
    public void setSubjectAN(List<DeviceKeystoreSubjectAN> subjectAN) {
        this.subjectAN = subjectAN;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @Override
    public Date getNotBefore() {
        return notBefore;
    }

    @Override
    public void setNotBefore(Date notBefore) {
        this.notBefore = notBefore;
    }

    @Override
    public Date getNotAfter() {
        return notAfter;
    }

    @Override
    public void setNotAfter(Date notAfter) {
        this.notAfter = notAfter;
    }

    @Override
    public String getCertificate() {
        return certificate;
    }

    @Override
    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public List<String> getCertificateChain() {
        if (certificateChain == null) {
            certificateChain = new ArrayList<>();
        }

        return certificateChain;
    }

    @Override
    public void addCertificateChain(String certificate) {
        getCertificateChain().add(certificate);
    }

    @Override
    public void setCertificateChain(List<String> certificateChain) {
        this.certificateChain = certificateChain;
    }
}
