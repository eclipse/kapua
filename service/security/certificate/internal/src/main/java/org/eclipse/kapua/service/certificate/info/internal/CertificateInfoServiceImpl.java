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

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.certificate.CertificateDomains;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoCreator;
import org.eclipse.kapua.service.certificate.info.CertificateInfoListResult;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.certificate.internal.CertificateEntityManagerFactory;
import org.eclipse.kapua.service.certificate.internal.CertificateInfoDAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class CertificateInfoServiceImpl extends AbstractKapuaService implements CertificateInfoService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateInfoServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    public CertificateInfoServiceImpl() {
        super(CertificateEntityManagerFactory.getInstance());
    }

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

        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(CertificateDomains.CERTIFICATE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> CertificateInfoDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery<CertificateInfo> query) throws KapuaException {

        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(CertificateDomains.CERTIFICATE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> CertificateInfoDAO.count(em, query));
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
