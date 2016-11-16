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

import org.eclipse.kapua.model.KapuaObjectFactory;

/**
 * {@link CredentialsFactory} factory definition.
 * 
 * @since 1.0
 * 
 */
public interface CredentialsFactory extends KapuaObjectFactory {

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance based on provided username and password
     * 
     * @param username
     * @param password
     * @return
     */
    public UsernamePasswordCredentials newUsernamePasswordCredentials(String username, char[] password);

    /**
     * Creates a new {@link UsernamePasswordCredentials} instance based on provided username and password
     * 
     * @param username
     * @param password
     * @return
     */
    public ApiKeyCredentials newApiKeyCredentials(String apiKey);

    /**
     * Creates a new {@link AccessTokenCredentials} instance based on provided tokenId
     * 
     * @param tokenId
     * @return
     */
    public AccessTokenCredentials newAccessTokenCredentials(String tokenId);

}
