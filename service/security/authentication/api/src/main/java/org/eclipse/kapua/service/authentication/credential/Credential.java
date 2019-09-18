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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
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
 * Credential definition.<br>
 * Used to handle credentials needed by the various authentication algorithms.
 *
 * @since 1.0
 */
@XmlRootElement(name = "credential")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "userId",
        "credentialType",
        "credentialKey",
        "status",
        "expirationDate",
        "loginFailures",
        "firstLoginFailure",
        "loginFailuresReset",
        "lockoutReset" }, //
        factoryClass = CredentialXmlRegistry.class, //
        factoryMethod = "newCredential") //
public interface Credential extends KapuaUpdatableEntity {

    String TYPE = "credential";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the user identifier
     *
     * @return
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Sets the user identifier
     */
    void setUserId(KapuaId userId);

    /**
     * Return the credential type
     *
     * @return
     */
    @XmlElement(name = "credentialType")
    CredentialType getCredentialType();

    /**
     * Sets the user credential type
     */
    void setCredentialType(CredentialType credentialType);

    /**
     * Return the credential key
     *
     * @return
     */
    @XmlElement(name = "credentialKey")
    String getCredentialKey();

    /**
     * Sets the credential key
     *
     * @param credentialKey
     */
    void setCredentialKey(String credentialKey);

    /**
     * Returns the current Credential status
     *
     * @return the Credential status
     * @see CredentialStatus
     */
    @XmlElement(name = "status")
    CredentialStatus getStatus();

    /**
     * Sets the current Credential status
     *
     * @param status The credential status
     */
    void setStatus(CredentialStatus status);

    /**
     * Gets the current Credential expiration date
     *
     * @return the current Credential expiration date
     */
    @XmlElement(name = "expirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpirationDate();

    /**
     * Sets the current Credential expiration date
     *
     * @param expirationDate the current Credential expiration date
     */
    void setExpirationDate(Date expirationDate);

    /**
     * Gets how many times this credential failed a login
     *
     * @return an integer representing login failures for this credential
     */
    int getLoginFailures();

    /**
     * Sets how many times this credential failed a login
     *
     * @param loginFailures the new failed logins count
     */
    void setLoginFailures(int loginFailures);

    /**
     * Gets a date indicating when the first login attempt failed after
     * the login attempts have been reset
     *
     * @return A {@link Date} object indicating when the first login attempt failed after the login attempts have been reset
     */
    @XmlElement(name = "firstLoginFailure")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getFirstLoginFailure();

    /**
     * Sets a date indicating when the first login attempt failed after
     * the login attempts have been reset
     *
     * @param firstLoginFailure A {@link Date} object indicating when the first login attempt failed after the login attempts have been reset
     */
    void setFirstLoginFailure(Date firstLoginFailure);

    /**
     * Gets a date indicating when the failed login attempts will be reset
     *
     * @return A {@link Date} object indicating when the failed login attempts will be reset
     */
    @XmlElement(name = "loginFailuresReset")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getLoginFailuresReset();

    /**
     * Sets a date indicating when the failed login attempts will be reset
     *
     * @param loginFailuresReset A {@link Date} object indicating when the failed login attempts will be reset
     */
    void setLoginFailuresReset(Date loginFailuresReset);

    /**
     * Gets a date indicating when the lockout will be lifted from the credential
     *
     * @return A {@link Date} object indicating when the lockout will be reset
     */
    @XmlElement(name = "lockoutReset")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getLockoutReset();

    /**
     * Sets a date indicating when the lockout will be lifted from the credential
     *
     * @param lockoutReset A {@link Date} object indicating when the lockout will be reset
     */
    void setLockoutReset(Date lockoutReset);
}
