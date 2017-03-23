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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Api key {@link LoginCredentials} definition.
 * 
 * @since 1.0
 * 
 */
@XmlRootElement(name = "refreshTokenCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "tokenId", "refreshToken" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newRefreshTokenCredentials")
public interface RefreshTokenCredentials extends LoginCredentials {

    /**
     * return the token id
     * 
     * @return
     */
    @XmlElement(name = "tokenId")
    public String getTokenId();

    /**
     * Set the token id
     * 
     * @param tokenId
     */
    public void setTokenId(String tokenId);
    
    /**
     * return the refresh token
     * 
     * @return
     */
    @XmlElement(name = "refreshToken")
    public String getRefreshToken();

    /**
     * Set the refresh token
     * 
     * @param refreshToken
     */
    public void setRefreshToken(String refreshToken);
}
