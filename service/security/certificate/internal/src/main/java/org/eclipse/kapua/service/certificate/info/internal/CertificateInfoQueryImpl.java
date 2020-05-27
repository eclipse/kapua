/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.certificate.info.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfoQuery;

public class CertificateInfoQueryImpl extends AbstractKapuaQuery implements CertificateInfoQuery {

    private Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor
     */
    private CertificateInfoQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
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
