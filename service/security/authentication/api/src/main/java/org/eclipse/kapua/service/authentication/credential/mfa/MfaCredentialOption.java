/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.credential.mfa;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.authentication.credential.CredentialXmlRegistry;
import org.eclipse.kapua.service.user.User;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * MfaCredentialOption definition.<br>
 * Used to handle MfaCredentialOption needed by the various authentication algorithms.
 */
@XmlRootElement(name = "mfaCredentialOption")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "userId",
        "mfaCredentialKey",
        "trustKey",
        "trustExpirationDate"}, //
        factoryClass = CredentialXmlRegistry.class, //
        factoryMethod = "newMfaCredentialOption") //
public interface MfaCredentialOption extends KapuaUpdatableEntity {

    String TYPE = "mfaCredentialOption";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the user identifier
     *
     * @return The user identifier.
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Sets the {@link User} id of this {@link MfaCredentialOption}
     *
     * @param userId The {@link User} id to set.
     */
    void setUserId(KapuaId userId);

    /**
     * Return the MfaCredentialOption key
     *
     * @return
     */
    @XmlElement(name = "mfaCredentialKey")
    String getMfaCredentialKey();

    /**
     * Sets the MfaCredentialOption key
     *
     * @param mfaCredentialKey
     */
    void setMfaCredentialKey(String mfaCredentialKey);

    /**
     * Return the trust key
     *
     * @return
     */
    @XmlElement(name = "trustKey")
    String getTrustKey();

    /**
     * Sets the trust key
     *
     * @param trustKey
     */
    void setTrustKey(String trustKey);

    /**
     * Gets the current trust key expiration date
     *
     * @return the current trust key expiration date
     */
    @XmlElement(name = "trustExpirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getTrustExpirationDate();

    /**
     * Sets the current trust expiration date
     *
     * @param trustExpirationDate the current trust expiration date
     */
    void setTrustExpirationDate(Date trustExpirationDate);
}
