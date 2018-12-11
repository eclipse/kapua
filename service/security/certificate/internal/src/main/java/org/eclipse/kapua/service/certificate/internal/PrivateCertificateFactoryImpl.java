/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.CertificateGenerator;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.PrivateCertificate;
import org.eclipse.kapua.service.certificate.PrivateCertificateCreator;
import org.eclipse.kapua.service.certificate.PrivateCertificateFactory;
import org.eclipse.kapua.service.certificate.PrivateCertificateListResult;
import org.eclipse.kapua.service.certificate.PrivateCertificateQuery;

/**
 * {@link PrivateCertificateFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class PrivateCertificateFactoryImpl implements PrivateCertificateFactory {

    @Override
    public PrivateCertificate newEntity(KapuaId scopeId) {
        return new PrivateCertificateImpl(scopeId);
    }

    @Override
    public PrivateCertificateCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrivateCertificateQuery newQuery(KapuaId scopeId) {
        return new PrivateCertificateQueryImpl(scopeId);
    }

    @Override
    public PrivateCertificateListResult newListResult() {
        return new PrivateCertificateListResultImpl();
    }

    @Override
    public CertificateUsage newCertificateUsage(String name) {
        return new CertificateUsageImpl(name);
    }

    @Override
    public KeyUsageSetting newKeyUsageSetting(KeyUsage keyUsage, boolean allowed, Boolean kapuaAllowed) {
        KeyUsageSetting keyUsageSetting = new KeyUsageSettingImpl();

        keyUsageSetting.setKeyUsage(keyUsage);
        keyUsageSetting.setAllowed(allowed);
        keyUsageSetting.setKapuaAllowed(kapuaAllowed);

        return keyUsageSetting;
    }

    @Override
    public CertificateGenerator newCertificateGenerator() {
        return null;
    }

    @Override
    public PrivateCertificate clone(PrivateCertificate certificate) {
        try {
            return new PrivateCertificateImpl(certificate);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, PrivateCertificate.TYPE, certificate);
        }
    }
}
