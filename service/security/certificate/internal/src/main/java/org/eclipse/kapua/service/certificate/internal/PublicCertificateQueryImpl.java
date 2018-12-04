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

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.PublicCertificate;
import org.eclipse.kapua.service.certificate.PublicCertificateQuery;

public class PublicCertificateQueryImpl extends AbstractKapuaQuery<PublicCertificate> implements PublicCertificateQuery {

    private Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor
     */
    private PublicCertificateQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public PublicCertificateQueryImpl(KapuaId scopeId) {
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
