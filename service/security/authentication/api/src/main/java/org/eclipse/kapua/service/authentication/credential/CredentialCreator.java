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

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * Credential creator service definition.
 * 
 * @since 1.0
 *
 */
public interface CredentialCreator extends KapuaEntityCreator<Credential>
{

    /**
     * Return the user identifier
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Return the credential type.<br>
     * The returned object will depend on the authentication algorithm.
     * 
     * @return
     */
    public CredentialType getCredentialType();

    /**
     * Return the plain credential (unencrypted value).
     * 
     * @return
     */
    public String getCredentialPlainKey();
}
