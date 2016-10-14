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
package org.eclipse.kapua.service.authentication;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Access token entity.
 * 
 * @since 1.0
 *
 */
public interface AccessToken extends KapuaEntity
{
	
	public static final String TYPE = "accessToken";

    default public String getType()
    {
        return TYPE;
    }
    
    /**
     * Return the token identifier
     * 
     * @return
     */
    public String getTokenId();
    
    /**
     * Return the user scope identifier.<br>
     * It is not ull if the user acts on a different scope identifier (on which he has the right)
     * 
     * @return
     */
    public KapuaId getUserScopeId();
	
    /**
     * Return the user identifier
     * 
     * @return
     */
	public KapuaId getUserId();

}
