/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.broker.core.plugin.authentication;

import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

/**
 * Authenticator api
 * 
 * @since 1.0
 *
 */
public interface Authenticator {

    String ADDRESS_ADVISORY_PREFIX_KEY = "address_advisory_prefix";
    String ADDRESS_CLASSIFIER_KEY = "address_classifier";
    String ADDRESS_PREFIX_KEY = "address_prefix";
    String ADDRESS_CONNECT_PATTERN_KEY = "address_connect_pattern";
    String ADDRESS_DISCONNECT_PATTERN_KEY = "address_disconnect_pattern";

    /**
     * Execute the connect logic returning the authorization list (ACL)
     * 
     * @param kapuaSecurityContext
     * @return
     * @throws KapuaException
     *             if any checks fails (credential not valid, profile missing, ...)
     */
    public abstract List<org.eclipse.kapua.broker.core.plugin.authentication.AuthorizationEntry> connect(KapuaSecurityContext kapuaSecurityContext)
            throws KapuaException;

    /**
     * Execute the disconnect logic
     * 
     * @param kapuaSecurityContext
     * @param error
     *            not null if the disconnection is due to an error not related to the client (network I/O error, server side error, ...)
     */
    public abstract void disconnect(KapuaSecurityContext kapuaSecurityContext, Throwable error);

    /**
     * Send the connect message (this message is mainly for internal use to enforce the stealing link)
     * 
     * @param kapuaSecurityContext
     */
    public abstract void sendConnectMessage(KapuaSecurityContext kapuaSecurityContext);

    /**
     * Send the disconnect message (this message is mainly for internal use)
     * 
     * @param kapuaSecurityContext
     */
    public abstract void sendDisconnectMessage(KapuaSecurityContext kapuaSecurityContext);

}