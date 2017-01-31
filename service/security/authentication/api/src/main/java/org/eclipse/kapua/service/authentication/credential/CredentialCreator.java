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
package org.eclipse.kapua.service.authentication.credential;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.subject.SubjectType;

/**
 * {@link Credential} creator definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "credentialCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "subjectType",
        "subjectId",
        "type",
        "key",
        "plainSecret",
}, //
        factoryClass = CredentialXmlRegistry.class, factoryMethod = "newCredentialCreator")
public interface CredentialCreator extends KapuaEntityCreator<Credential> {

    /**
     * Returns the {@link SubjectType} of this {@link CredentialCreator}.
     * 
     * @return The {@link SubjectType} of this {@link CredentialCreator}.
     * @since 1.0.0
     */
    @XmlElement(name = "subjectType")
    public SubjectType getSubjectType();

    /**
     * Sets the {@link SubjectType} of this {@link CredentialCreator}.
     * 
     * @param subjectType
     *            The {@link SubjectType} of this {@link CredentialCreator}.
     * @since 1.0.0
     */
    public void setSubjectType(SubjectType subjectType);

    /**
     * Return the {@link Credential} subject id.
     * 
     * @return The {@link Credential} subject id.
     * @since 1.0.0
     */
    @XmlElement(name = "subjectId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getSubjectId();

    /**
     * Sets the {@link Credential} subject id.
     * 
     * @param subjectId
     *            The {@link Credential} subject id.
     * @since 1.0.0
     */
    public void setSubjectId(KapuaId subjectId);

    /**
     * Returns the {@link CredentialType}.
     * 
     * @return The {@link CredentialType}.
     * @since 1.0.0
     */
    @XmlElement(name = "type")
    public CredentialType getType();

    /**
     * Sets the {@link CredentialType}.
     * 
     * @param type
     *            The {@link CredentialType}.
     * @since 1.0.0
     */
    public void setType(CredentialType type);

    /**
     * Returns the {@link CredentialCreator} key.
     * 
     * @return The plain {@link CredentialCreator} key.
     * @since 1.0.0
     */
    @XmlElement(name = "key")
    public String getKey();

    /**
     * Sets the {@link CredentialCreator} key.
     * 
     * @param key
     *            The {@link CredentialCreator} key.
     * @since 1.0.0
     */
    public void setKey(String key);

    /**
     * Returns the plain {@link CredentialCreator} secret (unencrypted value).
     * 
     * @return The plain {@link CredentialCreator} secret (unencrypted value).
     * @since 1.0.0
     */
    @XmlElement(name = "plainSecret")
    public String getPlainSecret();

    /**
     * Sets the {@link CredentialCreator} plain secret.
     * 
     * @param plainSecret
     *            The {@link CredentialCreator} plain secret.
     * @since 1.0.0
     */
    public void setPlainSecret(String plainSecret);

}
