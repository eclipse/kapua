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
package org.eclipse.kapua.service.certificate.info.internal;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.internal.AbstractCertificateImpl;

@Entity(name = "CertificateInfo")
@Table(name = "crt_certificate")
public class CertificateInfoImpl extends AbstractCertificateImpl implements CertificateInfo {

    public CertificateInfoImpl() { }

    public CertificateInfoImpl(KapuaId scopeId) {
        super(scopeId);
    }

    public CertificateInfoImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
    }

    public CertificateInfoImpl(CertificateInfo certificateInfo) {
        super(certificateInfo);
    }

}
