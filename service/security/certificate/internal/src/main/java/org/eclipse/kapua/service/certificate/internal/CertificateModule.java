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
package org.eclipse.kapua.service.certificate.internal;

import com.google.inject.multibindings.ProvidesIntoSet;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.model.domains.Domains;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.domain.DomainEntry;
import org.eclipse.kapua.service.certificate.CertificateFactory;
import org.eclipse.kapua.service.certificate.CertificateService;

public class CertificateModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(CertificateFactory.class).to(CertificateFactoryImpl.class);
        bind(CertificateService.class).to(CertificateServiceImpl.class);
    }

    @ProvidesIntoSet
    public Domain certificateDomain() {
        return new DomainEntry(Domains.CERTIFICATE, false, Actions.read, Actions.delete, Actions.write);
    }
}
