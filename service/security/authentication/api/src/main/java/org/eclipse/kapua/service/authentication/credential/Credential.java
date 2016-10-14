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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Credential definition.<br>
 * Used to handle credentials needed by the various authentication algorithms.
 * 
 * @since 1.0
 *
 */
public interface Credential extends KapuaEntity
{
    public static final String TYPE = "credential";

    default public String getType()
    {
        return TYPE;
    }

    /**
     * Return the user identifier
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Return the credential type
     * 
     * @return
     */
    public CredentialType getCredentialType();

    /**
     * Return the credential key
     * 
     * @return
     */
    public String getCredentialKey();
}
