/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.info.internal;

import javax.inject.Singleton;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.certificate.CertificateService;
import org.eclipse.kapua.service.certificate.info.CertificateInfoFactory;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.utils.KapuaEntityQueryUtil;

import com.google.inject.Provides;

public class CertificateInfoModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(CertificateInfoFactory.class).to(CertificateInfoFactoryImpl.class);
    }


    @Provides
    @Singleton
    CertificateInfoService certificateInfoService(
            CertificateService certificateService,
            KapuaEntityQueryUtil entityQueryUtil) {

        return new CertificateInfoServiceImpl(
                certificateService,
                entityQueryUtil
        );
    }
}
