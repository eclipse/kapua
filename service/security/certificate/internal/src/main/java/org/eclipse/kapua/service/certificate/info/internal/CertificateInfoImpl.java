/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.info.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.internal.CertificateUsageImpl;
import org.eclipse.kapua.service.certificate.internal.KeyUsageSettingImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CertificateInfoImpl extends AbstractKapuaNamedEntity implements CertificateInfo {

    private String certificate;
    private Set<KeyUsageSettingImpl> keyUsageSettings;
    private Set<CertificateUsageImpl> certificateUsages;

    public CertificateInfoImpl() {
    }

    public CertificateInfoImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public CertificateInfoImpl(CertificateInfo certificateInfo) {
        setCertificate(certificateInfo.getCertificate());
        setKeyUsageSettings(certificateInfo.getKeyUsageSettings());
        setCertificateUsages(certificateInfo.getCertificateUsages());
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
    public Integer getVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVersion(Integer version) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSerial() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSerial(String serial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAlgorithm() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAlgorithm(String algorithm) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSubject() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSubject(String subject) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getIssuer() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIssuer(String issuer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getNotBefore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNotBefore(Date notBefore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getNotAfter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setNotAfter(Date notAfter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateStatus getStatus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setStatus(CertificateStatus status) {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getSignature() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSignature(byte[] digest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean getCa() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCa(Boolean isCa) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaId getCaId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setCaId(KapuaId caId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<KeyUsageSettingImpl> getKeyUsageSettings() {
        if (keyUsageSettings == null) {
            keyUsageSettings = new HashSet<>();
        }

        return keyUsageSettings;
    }

    @Override
    public void setKeyUsageSettings(Set<KeyUsageSetting> keyUsageSettings) {
        this.keyUsageSettings = new HashSet<>();

        keyUsageSettings.forEach(k -> this.keyUsageSettings.add(KeyUsageSettingImpl.parse(k)));
    }

    @Override
    public void addKeyUsageSetting(KeyUsageSetting keyUsageSetting) {
        getKeyUsageSettings().add(KeyUsageSettingImpl.parse(keyUsageSetting));
    }

    @Override
    public void removeKeyUsageSetting(KeyUsageSetting keyUsageSetting) {
        getKeyUsageSettings().remove(KeyUsageSettingImpl.parse(keyUsageSetting));
    }

    @Override
    public Set<CertificateUsageImpl> getCertificateUsages() {
        if (certificateUsages == null) {
            certificateUsages = new HashSet<>();
        }

        return certificateUsages;
    }

    @Override
    public void setCertificateUsages(Set<CertificateUsage> certificateUsages) {
        this.certificateUsages = new HashSet<>();

        certificateUsages.forEach(k -> this.certificateUsages.add(CertificateUsageImpl.parse(k)));
    }

    @Override
    public void addCertificateUsage(CertificateUsage certificateUsage) {
        getCertificateUsages().add(CertificateUsageImpl.parse(certificateUsage));
    }

    @Override
    public void removeCertificateUsage(CertificateUsage certificateUsage) {
        getCertificateUsages().remove(CertificateUsageImpl.parse(certificateUsage));
    }

    @Override
    public Boolean getForwardable() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setForwardable(Boolean forwardable) {
        throw new UnsupportedOperationException();
    }
}
