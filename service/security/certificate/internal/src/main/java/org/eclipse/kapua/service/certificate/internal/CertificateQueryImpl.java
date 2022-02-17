/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.CertificateQuery;

/**
 * {@link CertificateQuery} implementation.
 *
 * @since 1.0.0
 */
public class CertificateQueryImpl extends AbstractKapuaNamedQuery implements CertificateQuery {

    private Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    private CertificateQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.0.0
     */
    public CertificateQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param query The {@link CertificateQuery} to clone.
     * @since 1.0.0
     */
    public CertificateQueryImpl(KapuaQuery query) {
        super(query);
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
