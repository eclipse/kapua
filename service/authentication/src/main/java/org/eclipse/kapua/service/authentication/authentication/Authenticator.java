/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.authentication;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.client.security.bean.AuthAcl;
import org.eclipse.kapua.client.security.bean.AuthContext;

/**
 * Authenticator api
 * 
 * @since 1.0
 *
 */
public interface Authenticator {

    /**
     * Execute the connect logic returning the authorization list (ACL)
     * 
     * @param authContext
     * @return
     * @throws KapuaException
     *             if any checks fails (credential not valid, profile missing, ...)
     */
    public abstract List<AuthAcl> connect(AuthContext authContext)
            throws KapuaException;

    /**
     * Execute the disconnect logic
     * 
     * @param authContext
     *            not null if the disconnection is due to an error not related to the client (network I/O error, server side error, ...)
     * @throws KapuaException
     */
    public abstract void disconnect(AuthContext authContext) throws KapuaException;

}
