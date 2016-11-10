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
package org.eclipse.kapua.service.authentication.token;

import java.util.Date;

import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;

/**
 * Credential factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface AccessTokenFactory extends KapuaObjectFactory {

    /**
     * Create a new {@link CredentialCreator} for the specific credential type
     * 
     * @param scopeId
     * @param tokenId
     * @param userId
     * @param expiresOn
     * 
     * @return
     */
    public AccessTokenCreator newCreator(KapuaId scopeId, String tokenId, KapuaId userId, Date expiresOn);

}
