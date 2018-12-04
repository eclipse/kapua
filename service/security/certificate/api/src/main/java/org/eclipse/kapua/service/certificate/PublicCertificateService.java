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
package org.eclipse.kapua.service.certificate;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

public interface PublicCertificateService extends KapuaEntityService<PublicCertificate, PublicCertificateCreator>,
        KapuaNamedEntityService<PublicCertificate>,
        KapuaUpdatableEntityService<PublicCertificate>,
        KapuaConfigurableService {

    CertificateDomain CERTIFICATE_DOMAIN = new CertificateDomain();

    @Override
    PublicCertificate create(PublicCertificateCreator var1) throws KapuaException;

    @Override
    PublicCertificate update(PublicCertificate var1) throws KapuaException;

    @Override
    PublicCertificate find(KapuaId var1, KapuaId var2) throws KapuaException;

    @Override
    PublicCertificate findByName(String var1) throws KapuaException;

    @Override
    PublicCertificateListResult query(KapuaQuery<PublicCertificate> query) throws KapuaException;

    PublicCertificate generate(CertificateGenerator generator) throws KapuaException;

    List<PublicCertificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) throws KapuaException;
}
