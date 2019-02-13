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
import org.eclipse.kapua.service.certificate.CertificateInfo;
import org.eclipse.kapua.service.certificate.CertificateInfoCreator;
import org.eclipse.kapua.service.certificate.CertificateInfoFactory;
import org.eclipse.kapua.service.certificate.CertificateInfoListResult;
import org.eclipse.kapua.service.certificate.CertificateInfoQuery;

@KapuaProvider
public class CertificateInfoFactoryImpl implements CertificateInfoFactory {

    @Override
    public CertificateInfo newEntity(KapuaId scopeId) {
        return new CertificateInfoImpl(scopeId);
    }

    @Override
    public CertificateInfoCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateInfoQuery newQuery(KapuaId scopeId) {
        return new CertificateInfoQueryImpl(scopeId);
    }

    @Override
    public CertificateInfoListResult newListResult() {
        return new CertificateInfoListResultImpl();
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
    public CertificateInfo clone(CertificateInfo certificateInfo) throws KapuaEntityCloneException {
        try {
            return new CertificateInfoImpl(certificateInfo);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, CertificateInfo.TYPE, certificateInfo);
        }
    }
}
