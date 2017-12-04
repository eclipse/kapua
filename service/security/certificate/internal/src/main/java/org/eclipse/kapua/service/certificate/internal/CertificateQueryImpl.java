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

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.Certificate;
import org.eclipse.kapua.service.certificate.CertificateQuery;

public class CertificateQueryImpl extends AbstractKapuaQuery<Certificate> implements CertificateQuery {

    /**
     * Constructor
     */
    private CertificateQueryImpl() {
        super();
    }

    /**
     * Constructor
     *
     * @param scopeId
     */
    public CertificateQueryImpl(KapuaId scopeId) {
        this();
        setScopeId(scopeId);
    }

}
