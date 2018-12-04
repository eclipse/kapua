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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.PublicCertificate;
import org.eclipse.kapua.service.certificate.PublicCertificateCreator;
import org.eclipse.kapua.service.certificate.PublicCertificateListResult;
import org.eclipse.kapua.service.certificate.PublicCertificateService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KapuaProvider
public class PublicCertificateServiceImpl implements PublicCertificateService {

    public static final Logger LOG = LoggerFactory.getLogger(PublicCertificateServiceImpl.class);

    @Override
    public PublicCertificate create(PublicCertificateCreator creator) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificateListResult query(KapuaQuery<PublicCertificate> query) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count(KapuaQuery<PublicCertificate> query) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate findByName(String name) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate update(PublicCertificate entity) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificate generate(CertificateGenerator generator) throws KapuaException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<PublicCertificate> findAncestorsCertificates(KapuaId scopeId, CertificateUsage usage) {
        throw new UnsupportedOperationException();
    }

    @Override
    public KapuaTocd getConfigMetadata(KapuaId scopeId) throws KapuaException {
        return EmptyTocd.getInstance();
    }

    @Override
    public Map<String, Object> getConfigValues(KapuaId scopeId) throws KapuaException {
        return Collections.emptyMap();
    }

    @Override
    public void setConfigValues(KapuaId scopeId, KapuaId parentId, Map<String, Object> values) throws KapuaException {
        throw new UnsupportedOperationException();
    }

}
