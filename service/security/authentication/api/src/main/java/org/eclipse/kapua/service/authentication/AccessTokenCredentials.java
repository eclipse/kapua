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

import org.eclipse.kapua.service.authentication.token.AccessToken;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link AccessToken} {@link AuthenticationCredentials} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "accessTokenCredentials")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = AuthenticationXmlRegistry.class, factoryMethod = "newAccessTokenCredentials")
public interface AccessTokenCredentials extends SessionCredentials {

    /**
     * Gets the session JWT to authenticate.
     *
     * @return The session JWT to authenticate.
     * @since 1.0.0
     */
    String getTokenId();

    /**
     * Sets the session JWT to authenticate.
     *
     * @param tokenId The session JWT to authenticate.
     * @since 1.0.0
     */
    void setTokenId(String tokenId);
}
