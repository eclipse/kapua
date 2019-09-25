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

/**
 * @since 1.0.0
 */
public interface CertificateService extends KapuaEntityService<Certificate, CertificateCreator>,
        KapuaNamedEntityService<Certificate>,
        KapuaUpdatableEntityService<Certificate>,
        KapuaConfigurableService {

    @Override
    CertificateListResult query(KapuaQuery<Certificate> query) throws KapuaException;

    Certificate generate(CertificateGenerator generator) throws KapuaException;

    List<Certificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) throws KapuaException;
}
