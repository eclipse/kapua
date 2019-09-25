/*******************************************************************************
 * Copyright (c) 2018, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.info;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.KapuaEntityService;
import org.eclipse.kapua.service.certificate.CertificateUsage;

import java.util.List;

/**
 * @since 1.1.0
 */
public interface CertificateInfoService extends KapuaEntityService<CertificateInfo, CertificateInfoCreator> {
    @Override
    CertificateInfoListResult query(KapuaQuery<CertificateInfo> query) throws KapuaException;

    List<CertificateInfo> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) throws KapuaException;
}
