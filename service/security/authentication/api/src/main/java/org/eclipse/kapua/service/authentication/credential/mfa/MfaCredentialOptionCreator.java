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

import org.eclipse.kapua.model.KapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * MfaCredentialOption creator service definition.
 */
@XmlRootElement(name = "mfaCredentialOptionCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"userId",
        "mfaCredentialKey"}, factoryClass = MfaCredentialOptionXmlRegistry.class, factoryMethod = "newMfaCredentialOptionCreator")
public interface MfaCredentialOptionCreator extends KapuaEntityCreator<MfaCredentialOption> {

    /**
     * Gets the user id owner of this token
     *
     * @return The user id owner of this token
     * @since 1.0
     */
    @XmlElement(name = "userId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getUserId();

    /**
     * Sets the user id owner of this token.
     *
     * @param userId The user id owner of this token.
     * @since 1.0
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
     * Set the MfaCredentialOption key
     *
     * @param mfaCredentialKey
     */
    void setMfaCredentialKey(String mfaCredentialKey);
}
