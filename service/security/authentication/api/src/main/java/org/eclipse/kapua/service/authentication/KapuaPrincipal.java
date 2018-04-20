/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication;

import java.security.Principal;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * Kapua {@link Principal} implementation.<br>
 * Uniquely identifies a user.
 * 
 * @since 1.0
 * 
 */
// TODO it's an object used by both authorization and authentication... should leave it in authentication module?
public interface KapuaPrincipal extends Principal, java.io.Serializable {

    /**
     * Return the token identifier
     * 
     * @return
     */
    public String getTokenId();

    /**
     * Return the user id
     * 
     * @return
     */
    public KapuaId getUserId();

    /**
     * Retur the account it
     * 
     * @return
     */
    public KapuaId getAccountId();

    /**
     * Return the remote client ip from which the user should be connected
     * 
     * @return
     */
    public String getClientIp();

    /**
     * Return the client identifiers from which the user should be connected
     * 
     * @return
     */
    public String getClientId();

}
