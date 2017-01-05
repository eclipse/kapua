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

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

/**
 * Credential definition.<br>
 * Used to handle credentials needed by the various authentication algorithms.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "credential")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "userId", 
                       "credentialType",
                       "credentialKey"},
        factoryClass = CredentialXmlRegistry.class, 
        factoryMethod = "newCredential")
public interface Credential extends KapuaUpdatableEntity
{
    public static final String TYPE = "credential";

    public default String getType() {
        return TYPE;
    }

    /**
     * Return the user identifier
     * 
     * @return
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    public KapuaId getUserId();

    /**
     * Sets the user identifier
     */
    public void setUserId(KapuaId userId);

    /**
     * Return the credential type
     * 
     * @return
     */
    @XmlElement(name = "credentialType")
    public CredentialType getCredentialType();

    /**
     * Sets the user credential type
     */
    public void setCredentialType(CredentialType credentialType);

    /**
     * Return the credential key
     * 
     * @return
     */
    @XmlElement(name = "credentialKey")
    public String getCredentialKey();

    /**
     * Sets the credential key
     * 
     * @param credentialKey
     */
    public void setCredentialKey(String credentialKey);
}
