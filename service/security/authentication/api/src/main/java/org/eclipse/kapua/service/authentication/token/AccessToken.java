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
package org.eclipse.kapua.service.authentication.token;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.subject.Subject;
import org.eclipse.kapua.service.authentication.credential.Credential;

/**
 * {@link AccessToken} entity.<br>
 * The {@link AccessToken} entity is returned to the caller of a successful login. <br>
 * Then all subsequent action on the system must be performed providing the {@link AccessToken#getTokenId()}
 * instead of using the {@link Credential} used during login.<br>
 * The {@link AccessToken} also acts as a session tracker as all action performed on the system are associated with a
 * {@link AccessToken#getTokenId()}.
 * 
 * 
 * @since 1.0.0
 *
 */
@XmlRootElement(name = "accessToken")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "subject", //
        "tokenId", //
        "expiresOn",//
})// , //
  // factoryClass = AccessTokenXmlRegistry.class, factoryMethod = "newAccessToken")
public interface AccessToken extends KapuaUpdatableEntity {

    static public final String TYPE = "accessToken";

    @XmlTransient
    default public String getType() {
        return TYPE;
    }

    /**
     * Returns the {@link Subject} of this {@link AccessToken}.
     * 
     * @return The {@link Subject} of this {@link AccessToken}.
     * @since 1.0.0
     */
    @XmlElement(name = "subject")
    public Subject getSubject();

    /**
     * Sets the {@link Subject} of this {@link AccessToken}.
     * 
     * @param subject
     *            The {@link Subject} of this {@link AccessToken}.
     * @since 1.0.0
     */
    public void setSubject(Subject subject);

    /**
     * Return the {@link AccessToken} {@link Credential} id.
     * 
     * @return The {@link AccessToken} {@link Credential} id.
     * @since 1.0.0
     */
    @XmlElement(name = "credentialId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getCredentialId();

    /**
     * Sets the {@link AccessToken} {@link Credential} id.
     * 
     * @param credentialId
     *            The {@link AccessToken} {@link Credential} id.
     * @since 1.0.0
     */
    public void setCredentialId(KapuaId credentialId);

    /**
     * Return the token identifier
     * 
     * @return the token identifier
     * @since 1.0
     */
    @XmlElement(name = "tokenId")
    public String getTokenId();

    /**
     * Sets the token id
     * 
     * @param tokenId
     *            The token id.
     * @since 1.0
     */
    public void setTokenId(String tokenId);

    /**
     * Gets the expire date of this token.
     * 
     * @since 1.0
     */
    @XmlElement(name = "expiresOn")
    public Date getExpiresOn();

    /**
     * Sets the expire date of this token.
     * 
     * @param expiresOn
     *            The expire date of this token.
     * @since 1.0
     */
    public void setExpiresOn(Date expiresOn);

}
