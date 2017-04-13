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
@XmlRootElement(name = "apiKeyCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "apiKey" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newApiKeyCredentials")
public interface ApiKeyCredentials extends LoginCredentials {

    /**
     * return the api key
     * 
     * @return
     */
    @XmlElement(name = "apiKey")
    public String getApiKey();

    /**
     * Set the api key
     * 
     * @param apiKey
     */
    public void setApiKey(String apiKey);
}
