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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.info.internal.CertificateInfoImpl;

/**
 * {@link Certificate} implementation
 *
 * @since 1.0.0
 */
public class CertificateImpl extends CertificateInfoImpl implements Certificate {

    private String privateKey;
    private String password;

    public CertificateImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public CertificateImpl(Certificate certificate) {
        throw new UnsupportedOperationException();
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
