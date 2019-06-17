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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;

public class CertificateGeneratorImpl implements CertificateGenerator {

    private String name;
    private String description;
    private String subject;
    private String issuer;
    private int keyLength;
    private Date notBefore;
    private Date notAfter;
    private CertificateStatus status;
    private Set<CertificateUsage> certificateUsages;
    private Boolean forwardable;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
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
    public int getKeyLength() {
        return keyLength;
    }

    @Override
    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
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
    public Set<CertificateUsage> getCertificateUsages() {
        if (certificateUsages == null) {
            certificateUsages = new HashSet<>();
        }

        return certificateUsages;
    }

    @Override
    public void setCertificateUsages(Set<CertificateUsage> certificateUsages) {
        this.certificateUsages = certificateUsages;
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
