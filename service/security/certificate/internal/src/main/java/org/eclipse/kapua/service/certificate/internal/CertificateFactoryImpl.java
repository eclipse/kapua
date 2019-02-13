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
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateCreator;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateListResult;
import org.eclipse.kapua.service.certificate.CertificateQuery;

/**
 * {@link CertificateFactory} implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class CertificateFactoryImpl implements CertificateFactory {

    @Override
    public Certificate newEntity(KapuaId scopeId) {
        return new CertificateImpl(scopeId);
    }

    @Override
    public CertificateCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuery newQuery(KapuaId scopeId) {
        return new CertificateQueryImpl(scopeId);
    }

    @Override
    public CertificateListResult newListResult() {
        return new CertificateListResultImpl();
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
    public Certificate clone(Certificate certificate) {
        try {
            return new CertificateImpl(certificate);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, Certificate.TYPE, certificate);
        }
    }
}
