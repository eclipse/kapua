/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;

public class CertificateCreatorImpl extends AbstractKapuaNamedEntityCreator<Certificate> implements CertificateCreator {

    private String certificate;
    private CertificateStatus status;
    private String privateKey;
    private KapuaId caId;
    private String password;
    private Set<CertificateUsageImpl> certificateUsages;
    private Boolean forwardable;

    public CertificateCreatorImpl(KapuaId scopeId) {
        super(scopeId);
        this.status = CertificateStatus.VALID;
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
    public CertificateStatus getStatus() {
        return status;
    }

    @Override
    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    @Override
    public String getPrivateKey() {
        return privateKey;
    }

    @Override
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public KapuaId getCaId() {
        return caId;
    }

    @Override
    public void setCaId(KapuaId caId) {
        this.caId = caId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
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
    public Boolean getForwardable() {
        return forwardable;
    }

    @Override
    public void setForwardable(Boolean forwardable) {
        this.forwardable = forwardable;
    }
}
