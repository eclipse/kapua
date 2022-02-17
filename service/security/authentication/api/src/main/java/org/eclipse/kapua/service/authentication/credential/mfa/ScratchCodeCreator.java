/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
 * {@link ScratchCode} creator service definition.
 */
@XmlRootElement(name = "scratchCodeCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ScratchCodeXmlRegistry.class, factoryMethod = "newScratchCodeCreator")
public interface ScratchCodeCreator extends KapuaEntityCreator<ScratchCode> {

    /**
     * Return the {@link KapuaId} of the corresponding {@link MfaOption}
     *
     * @return
     */
    @XmlElement(name = "mfaOptionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getMfaOptionId();

    /**
     * Set the {@link KapuaId} of the corresponding {@link MfaOption}
     *
     * @param mfaOptionId
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
     * Set the scratch code
     *
     * @param code
     */
    void setCode(String code);
}
