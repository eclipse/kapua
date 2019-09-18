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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Credential creator service definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "credentialCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "userId",
        "credentialType",
        "credentialPlainKey",
        "credentialStatus",
        "expirationDate" }, factoryClass = CredentialXmlRegistry.class, factoryMethod = "newCredentialCreator")
public interface CredentialCreator extends KapuaEntityCreator<Credential> {

    /**
     * Return the user identifier
     *
     * @return
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Set the credential user id
     *
     * @param userId
     */
    void setUserId(KapuaId userId);

    /**
     * Return the credential type.<br>
     * The returned object will depend on the authentication algorithm.
     *
     * @return
     */
    @XmlElement(name = "credentialType")
    CredentialType getCredentialType();

    /**
     * Set the credential type
     *
     * @param credentialType
     */
    void setCredentialType(CredentialType credentialType);

    /**
     * Return the plain credential (unencrypted value).
     *
     * @return
     */
    @XmlElement(name = "credentialKey")
    String getCredentialPlainKey();

    /**
     * Set the credential plain key
     *
     * @param plainKey
     */
    void setCredentialPlainKey(String plainKey);

    @XmlElement(name = "expirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpirationDate();

    void setExpirationDate(Date expirationDate);

    @XmlElement(name = "credentialStatus")
    CredentialStatus getCredentialStatus();

    void setCredentialStatus(CredentialStatus credentialStatus);
}
