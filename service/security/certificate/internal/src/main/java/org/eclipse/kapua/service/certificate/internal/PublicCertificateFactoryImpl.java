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

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.PublicCertificate;
import org.eclipse.kapua.service.certificate.PublicCertificateCreator;
import org.eclipse.kapua.service.certificate.PublicCertificateFactory;
import org.eclipse.kapua.service.certificate.PublicCertificateListResult;
import org.eclipse.kapua.service.certificate.PublicCertificateQuery;

@KapuaProvider
public class PublicCertificateFactoryImpl implements PublicCertificateFactory {

    @Override
    public PublicCertificate newEntity(KapuaId scopeId) {
        return new PublicCertificateImpl(scopeId);
    }

    @Override
    public PublicCertificateCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PublicCertificateQuery newQuery(KapuaId scopeId) {
        return new PublicCertificateQueryImpl(scopeId);
    }

    @Override
    public PublicCertificateListResult newListResult() {
        return new PublicCertificateListResultImpl();
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
    public PublicCertificate clone(PublicCertificate publicCertificate) throws KapuaEntityCloneException {
        try {
            return new PublicCertificateImpl(publicCertificate);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, PublicCertificate.TYPE, publicCertificate);
        }
    }
}
