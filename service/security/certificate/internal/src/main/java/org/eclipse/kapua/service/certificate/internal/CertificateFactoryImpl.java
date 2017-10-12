/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.api.Certificate;
import org.eclipse.kapua.service.certificate.api.CertificateCreator;
import org.eclipse.kapua.service.certificate.api.CertificateFactory;
import org.eclipse.kapua.service.certificate.api.CertificateListResult;
import org.eclipse.kapua.service.certificate.api.CertificateQuery;

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
}
