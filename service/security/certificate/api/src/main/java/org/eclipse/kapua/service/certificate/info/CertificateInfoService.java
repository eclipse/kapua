/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * {@link CertificateInfo} {@link KapuaEntityService} definition.
 *
 * @since 1.1.0
 */
public interface CertificateInfoService extends KapuaEntityService<CertificateInfo, CertificateInfoCreator> {

    @Override
    CertificateInfoListResult query(KapuaQuery query) throws KapuaException;

    /**
     * @param scopeId
     * @param usage
     * @return
     * @throws KapuaException
     * @since 1.1.0
     */
    List<CertificateInfo> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) throws KapuaException;
}
