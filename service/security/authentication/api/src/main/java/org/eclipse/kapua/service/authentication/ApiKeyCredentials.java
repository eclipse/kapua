/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
    String getApiKey();

    /**
     * Set the api key
     *
     * @param apiKey
     */
    void setApiKey(String apiKey);
}
