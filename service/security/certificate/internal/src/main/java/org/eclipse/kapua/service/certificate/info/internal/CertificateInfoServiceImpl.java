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
package org.eclipse.kapua.service.certificate.info.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.CertificateQuery;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoCreator;
import org.eclipse.kapua.service.certificate.info.CertificateInfoListResult;
import org.eclipse.kapua.service.certificate.info.CertificateInfoQuery;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.certificate.internal.CertificateQueryImpl;

import java.util.List;

@KapuaProvider
public class CertificateInfoServiceImpl implements CertificateInfoService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final CertificateService CERTIFICATE_SERVICE = LOCATOR.getService(CertificateService.class);

    @Override
    public CertificateInfo create(CertificateInfoCreator creator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateInfo find(KapuaId scopeId, KapuaId entityId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateInfoListResult query(KapuaQuery<CertificateInfo> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        CertificateQuery certificateQuery = new CertificateQueryImpl(query);
        certificateQuery.setIncludeInherited(((CertificateInfoQuery) query).getIncludeInherited());

        CertificateInfoListResult publicCertificates = new CertificateInfoListResultImpl();
        publicCertificates.addItem(CERTIFICATE_SERVICE.query(certificateQuery).getFirstItem());

        return publicCertificates;
    }

    @Override
    public long count(KapuaQuery<CertificateInfo> query) throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        CertificateQuery privateQuery = new CertificateQueryImpl(query);
        privateQuery.setIncludeInherited(((CertificateInfoQuery) query).getIncludeInherited());

        return CERTIFICATE_SERVICE.count(privateQuery);
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CertificateInfo> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) {
        throw new UnsupportedOperationException();
    }
}
