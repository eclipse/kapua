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
import org.eclipse.kapua.service.authentication.credential.CredentialXmlRegistry;
import org.eclipse.kapua.service.authentication.token.AccessToken;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Scratch Codes definition.<br>
 * Used to handle credentials needed by the various authentication algorithms.
 */
@XmlRootElement(name = "scratchCode")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "mfaCredentialOptionId", //
        "scratchCode"}, //
        factoryClass = CredentialXmlRegistry.class, //
        factoryMethod = "newScratchCode") //
public interface ScratchCode extends KapuaUpdatableEntity {

    String TYPE = "scratchCode";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the mfaCredentialOption identifier
     *
     * @return The mfaCredentialOption identifier.
     */
    @XmlElement(name = "mfaCredentialOptionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getMfaCredentialOptionId();

    /**
     * Sets the {@link MfaCredentialOption} id of this {@link AccessToken}
     *
     * @param mfaCredentialOptionId The {@link MfaCredentialOption} id to set.
     */
    void setMfaCredentialOptionId(KapuaId mfaCredentialOptionId);

    /**
     * Return the scratch code
     *
     * @return
     */
    @XmlElement(name = "scratchCode")
    String getCode();

    /**
     * Sets the scratch code
     *
     * @param code
     */
    void setCode(String code);
}
