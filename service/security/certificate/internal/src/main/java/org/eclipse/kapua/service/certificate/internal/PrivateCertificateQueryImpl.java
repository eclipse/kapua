/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.certificate.PrivateCertificate;
import org.eclipse.kapua.service.certificate.PrivateCertificateQuery;

public class PrivateCertificateQueryImpl extends AbstractKapuaQuery<PrivateCertificate> implements PrivateCertificateQuery {

    private Boolean includeInherited = Boolean.FALSE;

    /**
     * Constructor
     */
    private PrivateCertificateQueryImpl() {
        super();
    }

    /**
     * Constructor.
     *
     * @param scopeId The scopeId of the {@link KapuaQuery}
     */
    public PrivateCertificateQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

    /**
     * Constructor
     * <p>
     * This deeply clones the given {@link PrivateCertificateQuery}
     *
     * @param query the query to clone
     */
    public PrivateCertificateQueryImpl(KapuaQuery query) {
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
