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

/**
 * Access token factory service definition.
 * 
 * @since 1.0
 * 
 */
public interface AccessTokenFactory extends KapuaObjectFactory {

    /**
     * Create a new {@link AccessTokenCreator} for the specific access credential type
     * 
     * @param scopeId
     *            The scopeId of the new {@link AccessToken}.
     * @param userId
     *            The userId owner of the new {@link AccessToken}.
     * @param tokenId
     *            The tokenId of the new {@link AccessToken}.
     * @param expiresOn
     *            The expiration date after which the token is no longer valid.
     * 
     * @return A new instance of {@link AccessToken}.
     * 
     * @since 1.0
     */
    public AccessTokenCreator newCreator(KapuaId scopeId, KapuaId userId, String tokenId, Date expiresOn);

}
