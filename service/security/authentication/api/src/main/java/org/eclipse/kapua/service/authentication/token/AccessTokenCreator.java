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

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access token creator service definition
 *
 * @since 1.0
 * 
 */
public interface AccessTokenCreator extends KapuaEntityCreator<AccessToken> {

    /**
     * Gets the token id
     * 
     * @return The token id
     * @since 1.0
     */
    public String getTokenId();

    /**
     * Sets the token id
     * 
     * @param tokenId
     *            the token id to set
     * @since 1.0
     */
    public void setTokenId(String tokenId);

    /**
     * Gets the user id owner of this token
     * 
     * @return The user id owner of this token
     * @since 1.0
     */
    public KapuaId getUserId();

    /**
     * Sets the user id owner of this token.
     * 
     * @param userId
     *            The user id owner of this token.
     * @since 1.0
     */
    public void setUserId(KapuaId userId);

    /**
     * Gets the expire date of this token.
     * 
     * @return The expire date of this token.
     * @since 1.0
     */
    public Date getExpiresOn();

    /**
     * Sets the expire date of this token.
     * 
     * @param expiresOn
     *            The expire date of this token.
     * @since 1.0
     */
    public void setExpiresOn(Date expiresOn);
}
