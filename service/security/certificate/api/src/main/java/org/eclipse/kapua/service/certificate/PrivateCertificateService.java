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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.KapuaNamedEntityService;
import org.eclipse.kapua.service.KapuaUpdatableEntityService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import java.util.List;

public interface PrivateCertificateService extends KapuaEntityService<PrivateCertificate, PrivateCertificateCreator>,
        KapuaNamedEntityService<PrivateCertificate>,
        KapuaUpdatableEntityService<PrivateCertificate>,
        KapuaConfigurableService {

    @Override
    PrivateCertificateListResult query(KapuaQuery<PrivateCertificate> query) throws KapuaException;

    PrivateCertificate generate(CertificateGenerator generator) throws KapuaException;

    List<PrivateCertificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) throws KapuaException;
}
