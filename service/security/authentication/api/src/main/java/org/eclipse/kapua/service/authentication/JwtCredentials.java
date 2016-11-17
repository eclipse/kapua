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
 * Jwt {@link LoginCredentials} definition.
 * 
 * @since 1.0
 * 
 */
@XmlRootElement(name = "jwtCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "jwt" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newJwtCredentials")
public interface JwtCredentials extends LoginCredentials {

    /**
     * Gets the Json Web Token
     * 
     * @return
     */
    @XmlElement(name = "jwt")
    public String getJwt();

    /**
     * Set the Json Web Token
     * 
     * @param jwt
     */
    public void setApiKey(String jwt);
}
