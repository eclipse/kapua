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
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Access token creator service definition
 *
 * @since 1.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "tokenId",
        "userId",
        "expiresOn",
        "refreshToken",
        "refreshExpiresOn"
}, //
        factoryClass = AccessTokenXmlRegistry.class, //
        factoryMethod = "newAccessTokenCreator")
public interface AccessTokenCreator extends KapuaEntityCreator<AccessToken> {

    /**
     * Gets the token id
     *
     * @return The token id
     * @since 1.0
     */
    @XmlElement(name = "tokenId")
    String getTokenId();

    /**
     * Sets the token id
     *
     * @param tokenId the token id to set
     * @since 1.0
     */
    void setTokenId(String tokenId);

    /**
     * Gets the user id owner of this token
     *
     * @return The user id owner of this token
     * @since 1.0
     */
    @XmlElement(name = "userId")
    KapuaId getUserId();

    /**
     * Sets the user id owner of this token.
     *
     * @param userId The user id owner of this token.
     * @since 1.0
     */
    void setUserId(KapuaId userId);

    /**
     * Gets the expire date of this token.
     *
     * @return The expire date of this token.
     * @since 1.0
     */
    @XmlElement(name = "expiresOn")
    Date getExpiresOn();

    /**
     * Sets the expire date of this token.
     *
     * @param expiresOn The expire date of this token.
     * @since 1.0
     */
    void setExpiresOn(Date expiresOn);

    /**
     * Gets the refresh token to obtain a new {@link AccessToken} after expiration.
     *
     * @since 1.0
     */
    @XmlElement(name = "refreshToken")
    String getRefreshToken();

    /**
     * Sets the refresh token to obtain a new {@link AccessToken} after expiration.
     *
     * @param refreshToken The refresh token
     * @since 1.0
     */
    void setRefreshToken(String refreshToken);

    /**
     * Gets the expiration date of the refresh token.
     *
     * @since 1.0
     */
    @XmlElement(name = "refreshExpiresOn")
    Date getRefreshExpiresOn();

    /**
     * Sets the expire date of this token.
     *
     * @param refreshExpiresOn The expiration date of the refresh token.
     * @since 1.0
     */
    void setRefreshExpiresOn(Date refreshExpiresOn);
}
