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
import org.eclipse.kapua.service.authentication.token.AccessToken;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link ScratchCode} definition.<br>
 */
@XmlRootElement(name = "scratchCode")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "mfaOptionId", //
        "scratchCode"}, //
        factoryClass = ScratchCodeXmlRegistry.class, //
        factoryMethod = "newScratchCode") //
public interface ScratchCode extends KapuaUpdatableEntity {

    String TYPE = "scratchCode";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the {@link KapuaId} of the {@link MfaOption}
     *
     * @return The {@link MfaOption} identifier.
     */
    @XmlElement(name = "mfaOptionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getMfaOptionId();

    /**
     * Sets the {@link MfaOption} id of this {@link AccessToken}
     *
     * @param mfaOptionId The {@link MfaOption} id to set.
     */
    void setMfaOptionId(KapuaId mfaOptionId);

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
