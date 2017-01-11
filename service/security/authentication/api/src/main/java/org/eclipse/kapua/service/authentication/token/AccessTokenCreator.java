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
package org.eclipse.kapua.service.authentication.token;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authorization.subject.SubjectType;

/**
 * {@link AccessToken} creator definition.
 *
 * @since 1.0.0
 * 
 */
@XmlRootElement(name = "credentialCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "subjectType",
        "subjectId",
        "tokenId",
        "expiresOn",
})// , //
  // factoryClass = CredentialXmlRegistry.class, factoryMethod = "newCredentialCreator")
public interface AccessTokenCreator extends KapuaEntityCreator<AccessToken> {

    /**
     * Returns the {@link SubjectType} of this {@link AccessTokenCreator}.
     * 
     * @return The {@link SubjectType} of this {@link AccessTokenCreator}.
     * @since 1.0.0
     */
    @XmlElement(name = "subjectType")
    public SubjectType getSubjectType();

    /**
     * Sets the {@link SubjectType} of this {@link AccessTokenCreator}.
     * 
     * @param subjectType
     *            The {@link SubjectType} of this {@link AccessTokenCreator}.
     * @since 1.0.0
     */
    public void setSubjectType(SubjectType subjectType);

    /**
     * Return the {@link AccessToken} subject id.
     * 
     * @return The {@link AccessToken} subject id.
     * @since 1.0.0
     */
    @XmlElement(name = "subjectId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getSubjectId();

    /**
     * Sets the {@link AccessToken} subject id.
     * 
     * @param subjectId
     *            The {@link AccessToken} subject id.
     * @since 1.0.0
     */
    public void setSubjectId(KapuaId subjectId);

    /**
     * Return the {@link AccessTokenCreator} {@link Credential} id.
     * 
     * @return The {@link AccessTokenCreator} {@link Credential} id.
     * @since 1.0.0
     */
    @XmlElement(name = "credentialId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getCredentialId();

    /**
     * Sets the {@link AccessTokenCreator} {@link Credential} id.
     * 
     * @param credentialId
     *            The {@link AccessTokenCreator} {@link Credential} id.
     * @since 1.0.0
     */
    public void setCredentialId(KapuaId credentialId);

    /**
     * Gets the token id
     * 
     * @return The token id
     * @since 1.0
     */
    @XmlElement(name = "tokenId")
    public String getTokenId();

    /**
     * Sets the token id
     * 
     * @param tokenId
     *            the token id to set
     * @since 1.0
     */
    public void setTokenId(String tokenId);

    /**
     * Gets the expire date of this token.
     * 
     * @return The expire date of this token.
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
