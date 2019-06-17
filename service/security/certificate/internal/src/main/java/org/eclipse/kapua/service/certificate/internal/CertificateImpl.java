/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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
import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.Certificate;

/**
 * {@link Certificate} implementation
 *
 * @since 1.0.0
 */
@Entity(name = "Certificate")
@Table(name = "crt_certificate")
public class CertificateImpl extends AbstractCertificateImpl implements Certificate {

    @Basic
    @Column(name = "private_key", updatable = false)
    private String privateKey;

    @Basic
    @Column(name = "password", updatable = false)
    private String password;

    public CertificateImpl() { }

    public CertificateImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public CertificateImpl(Certificate certificate) throws KapuaException {
        super(certificate);
        setPassword(certificate.getPassword());
        setPrivateKey(certificate.getPrivateKey());
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
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

}
