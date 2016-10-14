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
package org.eclipse.kapua.service.authorization.token;

import java.util.Date;
import java.util.UUID;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Authorization token entity definition.
 * 
 * @since 1.0
 *
 */
public interface AuthorizationToken extends KapuaEntity
{
    /**
     * Return the user identifier
     * 
     * @return
     */
    public KapuaId getSubjectUserId();

    /**
     * Return the token expires on
     * 
     * @return
     */
    public Date getExpiresOn();

    /**
     * Return the token identifier
     * 
     * @return
     */
    public UUID getTokenId();
}
