/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.user.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.Date;

/**
 * {@link AccessToken} entity.
 *
 * @since 1.0.0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { //
        "tokenId", //
        "userId", //
        "expiresOn", //
        "refreshToken", //
        "refreshExpiresOn", //
        "invalidatedOn" //
}, //
        factoryClass = AccessTokenXmlRegistry.class, //
        factoryMethod = "newAccessToken")
public interface AccessToken extends KapuaUpdatableEntity, Serializable {

    String TYPE = "accessToken";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the token identifier
     *
     * @return the token identifier
     * @since 1.0.0
     */
    @XmlElement(name = "tokenId")
    String getTokenId();

    /**
     * Sets the token id
     *
     * @param tokenId The token id.
     * @since 1.0.0
     */
    void setTokenId(String tokenId);

    /**
     * Return the user identifier
     *
     * @return The user identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Sets the {@link User} id of this {@link AccessToken}
     *
     * @param userId The {@link User} id to set.
     * @since 1.0.0
     */
    void setUserId(KapuaId userId);

    /**
     * Gets the expire date of this token.
     *
     * @return The expire date of this token.
     * @since 1.0.0
     */
    @XmlElement(name = "expiresOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpiresOn();

    /**
     * Sets the expire date of this token.
     *
     * @param expiresOn The expire date of this token.
     * @since 1.0.0
     */
    void setExpiresOn(Date expiresOn);

    /**
     * Gets the refresh token to obtain a new {@link AccessToken} after expiration.
     *
     * @return The refresh token to obtain a new {@link AccessToken} after expiration.
     * @since 1.0.0
     */
    @XmlElement(name = "refreshToken")
    String getRefreshToken();

    /**
     * Sets the refresh token to obtain a new {@link AccessToken} after expiration.
     *
     * @param refreshToken The refresh token
     * @since 1.0.0
     */
    void setRefreshToken(String refreshToken);

    /**
     * Gets the expiration date of the refresh token.
     *
     * @return The expiration date of the refresh token.
     * @since 1.0.0
     */
    @XmlElement(name = "refreshExpiresOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getRefreshExpiresOn();

    /**
     * Sets the expire date of this token.
     *
     * @param refreshExpiresOn The expiration date of the refresh token.
     * @since 1.0.0
     */
    void setRefreshExpiresOn(Date refreshExpiresOn);

    /**
     * Gets the date the token has been invalidated (i.e. the date
     * the refresh token has been used, or it has been invalidated due
     * to a logout)
     *
     * @return The date the token has been invalidated.
     * @since 1.0.0
     */
    @XmlElement(name = "invalidatedOn")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getInvalidatedOn();

    /**
     * Sets the date the token has been invalidated (i.e. the date
     * the refresh token has been used, or it has been invalidated due
     * to a logout)
     *
     * @param invalidatedOn The date when the token has been invalidated.
     * @since 1.0.0
     */
    void setInvalidatedOn(Date invalidatedOn);

}
