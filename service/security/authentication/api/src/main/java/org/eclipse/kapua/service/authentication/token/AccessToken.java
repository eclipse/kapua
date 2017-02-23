/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import java.io.Serializable;
import java.util.Date;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access token entity.
 *
 * @since 1.0
 *
 */
public interface AccessToken extends KapuaUpdatableEntity, Serializable {

    public static final String TYPE = "accessToken";

    default public String getType() {
        return TYPE;
    }

    /**
     * Return the token identifier
     *
     * @return the token identifier
     * @since 1.0
     */
    public String getTokenId();

    /**
     * Sets the token id
     *
     * @param tokenId
     *            The token id.
     * @since 1.0
     */
    public void setTokenId(String tokenId);

    /**
     * Return the user identifier
     *
     * @return
     *
     * @since 1.0
     */
    public KapuaId getUserId();

    /**
     * Sets the {@link User} id of this {@link AccessToken}
     *
     * @param userId
     *            The {@link User} id to set.
     */
    public void setUserId(KapuaId userId);

    /**
     * Gets the expire date of this token.
     *
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
