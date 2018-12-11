/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.PrivateCertificateQuery;
import org.eclipse.kapua.service.certificate.PrivateCertificateService;
import org.eclipse.kapua.service.certificate.PublicCertificate;
import org.eclipse.kapua.service.certificate.PublicCertificateCreator;
import org.eclipse.kapua.service.certificate.PublicCertificateListResult;
import org.eclipse.kapua.service.certificate.PublicCertificateQuery;
import org.eclipse.kapua.service.certificate.PublicCertificateService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@KapuaProvider
public class PublicCertificateServiceImpl implements PublicCertificateService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final PrivateCertificateService PRIVATE_CERTIFICATE_SERVICE = LOCATOR.getService(PrivateCertificateService.class);

    @Override
    public PublicCertificate create(PublicCertificateCreator creator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate find(KapuaId scopeId, KapuaId entityId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificateListResult query(KapuaQuery<PublicCertificate> query) throws KapuaException {

        PrivateCertificateQuery privateQuery = new PrivateCertificateQueryImpl(query.getScopeId());
        privateQuery.setFetchAttributes(query.getFetchAttributes());
        privateQuery.setPredicate(query.getPredicate());
        privateQuery.setLimit(query.getLimit());
        privateQuery.setOffset(query.getOffset());
        privateQuery.setSortCriteria(query.getSortCriteria());
        privateQuery.setIncludeInherited(((PublicCertificateQuery) query).getIncludeInherited());

        PublicCertificateListResult publicCertificates = new PublicCertificateListResultImpl();
        publicCertificates.addItems(Arrays.asList(PRIVATE_CERTIFICATE_SERVICE.query(privateQuery).getFirstItem()));

        return publicCertificates;
    }

    @Override
    public long count(KapuaQuery<PublicCertificate> query) throws KapuaException {

        PrivateCertificateQuery privateQuery = new PrivateCertificateQueryImpl(query.getScopeId());
        privateQuery.setFetchAttributes(query.getFetchAttributes());
        privateQuery.setPredicate(query.getPredicate());
        privateQuery.setLimit(query.getLimit());
        privateQuery.setOffset(query.getOffset());
        privateQuery.setSortCriteria(query.getSortCriteria());
        privateQuery.setIncludeInherited(((PublicCertificateQuery) query).getIncludeInherited());

        return PRIVATE_CERTIFICATE_SERVICE.count(privateQuery);
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate findByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate update(PublicCertificate entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate generate(CertificateGenerator generator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<PublicCertificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) {
        return EmptyTocd.getInstance();
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) {
        return Collections.emptyMap();
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) {
        throw new UnsupportedOperationException();
    }
}