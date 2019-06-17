/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.eclipse.kapua.service.certificate.CertificateUsage;

import java.util.Objects;

@Embeddable
public class CertificateUsageImpl implements CertificateUsage {

    @Basic
    @Column(name = "name")
    private String name;

    public CertificateUsageImpl() { }

    public CertificateUsageImpl(String name) {
        setName(name);
    }

    public CertificateUsageImpl(CertificateUsage certificateUsage) {
        setName(certificateUsage.getName());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public static CertificateUsageImpl parse(CertificateUsage certificateUsage) {
        return certificateUsage != null ? certificateUsage instanceof CertificateUsageImpl ? (CertificateUsageImpl) certificateUsage : new CertificateUsageImpl(certificateUsage) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CertificateUsageImpl that = (CertificateUsageImpl) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
