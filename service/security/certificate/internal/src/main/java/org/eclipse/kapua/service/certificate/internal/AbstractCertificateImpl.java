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
package org.eclipse.kapua.service.certificate.internal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractCertificateImpl extends AbstractKapuaNamedEntity implements CertificateInfo {

    @Basic
    @Column(name = "certificate", nullable = false, updatable = false)
    private String certificate;

    @Basic
    @Column(name = "version", nullable = false, updatable = false)
    private Integer version;

    @Basic
    @Column(name = "serial", nullable = false, updatable = false)
    private String serial;

    @Basic
    @Column(name = "algorithm", nullable = false, updatable = false)
    private String algorithm;

    @Basic
    @Column(name = "subject", nullable = false, updatable = false)
    private String subject;

    @Basic
    @Column(name = "issuer", nullable = false, updatable = false)
    private String issuer;

    @Basic
    @Column(name = "not_before", nullable = false, updatable = false)
    private Date notBefore;

    @Basic
    @Column(name = "not_after", nullable = false, updatable = false)
    private Date notAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CertificateStatus status;

    @Basic
    @Column(name = "signature", nullable = false, updatable = false)
    private byte[] signature;

    @Basic
    @Column(name = "is_ca", nullable = false)
    private Boolean isCa;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "ca_id"))
    })
    @Column(name = "ca_id")
    private KapuaEid caId;

    @ElementCollection
    @CollectionTable(name = "crt_key_usage_setting", joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"))
    private Set<KeyUsageSettingImpl> keyUsageSettings;

    @ElementCollection
    @CollectionTable(name = "crt_certificate_usage", joinColumns = @JoinColumn(name = "certificate_id", referencedColumnName = "id"))
    private Set<CertificateUsageImpl> certificateUsages;

    @Basic
    @Column(name = "forwardable", nullable = false)
    private Boolean forwardable;

    protected AbstractCertificateImpl() {
    }

    protected AbstractCertificateImpl(KapuaId scopeId) {
        super(scopeId);
    }

    protected AbstractCertificateImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
    }

    protected AbstractCertificateImpl(CertificateInfo certificateInfo) {
        super(certificateInfo);

        setAlgorithm(certificateInfo.getAlgorithm());
        setCa(certificateInfo.getCa());
        setCaId(certificateInfo.getCaId());
        setCertificate(certificateInfo.getCertificate());
        setCertificateUsages(certificateInfo.getCertificateUsages());
        setForwardable(certificateInfo.getForwardable());
        setIssuer(certificateInfo.getIssuer());
        setKeyUsageSettings(certificateInfo.getKeyUsageSettings());
        setNotAfter(certificateInfo.getNotAfter());
        setNotBefore(certificateInfo.getNotBefore());
        setSerial(certificateInfo.getSerial());
        setSignature(certificateInfo.getSignature());
        setStatus(certificateInfo.getStatus());
        setSubject(certificateInfo.getSubject());
        setVersion(certificateInfo.getVersion());
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
        return version;
    }

    @Override
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String getSerial() {
        return serial;
    }

    @Override
    public void setSerial(String serial) {
        this.serial = serial;
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
    public String getSubject() {
        return subject;
    }

    @Override
    public void setSubject(String subject) {
        this.subject = subject;
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
    public CertificateStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    @Override
    public byte[] getSignature() {
        return signature;
    }

    @Override
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    @Override
    public Boolean getCa() {
        return isCa;
    }

    @Override
    public void setCa(Boolean isCa) {
        this.isCa = isCa;
    }

    @Override
    public KapuaId getCaId() {
        return caId;
    }

    @Override
    public void setCaId(KapuaId caId) {
        this.caId = (KapuaEid) caId;
    }

    @Override
    public Set<KeyUsageSettingImpl> getKeyUsageSettings() {
        return keyUsageSettings;
    }

    @Override
    public void setKeyUsageSettings(Set<KeyUsageSetting> set) {
        Set<KeyUsageSettingImpl> newSet = new HashSet<>();
        for (KeyUsageSetting setting : set) {
            newSet.add(new KeyUsageSettingImpl(setting));
        }
        keyUsageSettings = newSet;
    }

    @Override
    public void addKeyUsageSetting(KeyUsageSetting keyUsageSetting) {
        KeyUsageSettingImpl impl = new KeyUsageSettingImpl(keyUsageSetting);
        keyUsageSettings.add(impl);
    }

    @Override
    public void removeKeyUsageSetting(KeyUsageSetting keyUsageSetting) {
        keyUsageSettings.remove(keyUsageSetting);
    }

    @Override
    public Set<CertificateUsageImpl> getCertificateUsages() {
        return certificateUsages;
    }

    @Override
    public void setCertificateUsages(Set<CertificateUsage> set) {
        Set<CertificateUsageImpl> newSet = new HashSet<>();
        for (CertificateUsage certificateUsage : set) {
            newSet.add(new CertificateUsageImpl(certificateUsage));
        }
        certificateUsages = newSet;
    }

    @Override
    public void addCertificateUsage(CertificateUsage certificateUsage) {
        CertificateUsageImpl usageImpl = new CertificateUsageImpl(certificateUsage);
        certificateUsages.add(usageImpl);
    }

    @Override
    public void removeCertificateUsage(CertificateUsage certificateUsage) {
        certificateUsages.remove(certificateUsage);
    }

    @Override
    public Boolean getForwardable() {
        return forwardable;
    }

    @Override
    public void setForwardable(Boolean forwardable) {
        this.forwardable = forwardable;
    }

}
