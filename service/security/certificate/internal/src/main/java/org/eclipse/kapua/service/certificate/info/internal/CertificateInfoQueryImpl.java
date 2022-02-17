/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfoQuery;

/**
 * {@link CertificateInfoQuery} implementation.
 *
 * @since 1.0.0
 */
public class CertificateInfoQueryImpl extends AbstractKapuaNamedQuery implements CertificateInfoQuery {

    private Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private CertificateInfoQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.0.0
     */
    public CertificateInfoQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

    @Override
    public Boolean getIncludeInherited() {
        return includeInherited;
    }

    @Override
    public void setIncludeInherited(Boolean includeInherited) {
        this.includeInherited = includeInherited;
    }
}
