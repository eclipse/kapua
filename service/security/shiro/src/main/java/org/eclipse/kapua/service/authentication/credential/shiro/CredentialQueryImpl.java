/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;

/**
 * Credential query implementation.
 * 
 * @since 1.0
 * 
 */
public class CredentialQueryImpl extends AbstractKapuaQuery<Credential> implements CredentialQuery
{

    /**
     * Constructor
     */
    private CredentialQueryImpl()
    {
        super();
    }

    /**
     * Constructor
     * 
     * @param scopeId
     */
    public CredentialQueryImpl(KapuaId scopeId)
    {
        this();
        setScopeId(scopeId);
    }
}
