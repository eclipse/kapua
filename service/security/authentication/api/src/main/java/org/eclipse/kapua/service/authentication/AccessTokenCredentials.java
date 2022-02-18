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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Username and password {@link AuthenticationCredentials} definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "accessTokenCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "tokenId" }, factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newAccessTokenCredentials")
public interface AccessTokenCredentials extends SessionCredentials {

    /**
     * Return the token id
     *
     * @return
     */
    String getTokenId();

    /**
     * Set the token id
     *
     * @param tokenId
     */
    void setTokenId(String tokenId);
}
