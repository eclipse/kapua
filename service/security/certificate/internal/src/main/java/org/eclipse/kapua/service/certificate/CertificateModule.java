/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate;

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.service.certificate.info.CertificateInfoFactory;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.certificate.info.internal.CertificateInfoFactoryImpl;
import org.eclipse.kapua.service.certificate.info.internal.CertificateInfoServiceImpl;
import org.eclipse.kapua.service.certificate.internal.CertificateFactoryImpl;
import org.eclipse.kapua.service.certificate.internal.CertificateServiceImpl;

public class CertificateModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(CertificateInfoFactory.class).to(CertificateInfoFactoryImpl.class);
        bind(CertificateInfoService.class).to(CertificateInfoServiceImpl.class);

        bind(CertificateFactory.class).to(CertificateFactoryImpl.class);
        bind(CertificateService.class).to(CertificateServiceImpl.class);
    }
}
