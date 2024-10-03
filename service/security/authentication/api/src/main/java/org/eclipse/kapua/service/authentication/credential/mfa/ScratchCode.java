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

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * ScratchCode {@link KapuaEntity} definition.
 *
 * @since 1.3.0
 */
@XmlRootElement(name = "scratchCode")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = ScratchCodeXmlRegistry.class, factoryMethod = "newScratchCode")
public interface ScratchCode extends KapuaEntity {

    String TYPE = "scratchCode";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Gets the {@link MfaOption#getId()}
     *
     * @return The {@link MfaOption#getId()}
     * @since 1.3.0
     */
    @XmlElement(name = "mfaOptionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getMfaOptionId();

    /**
     * Sets the {@link MfaOption#getId()}
     *
     * @param mfaOptionId The {@link MfaOption#getId()}
     */
    void setMfaOptionId(KapuaId mfaOptionId);

    /**
     * Gets the scratch code
     *
     * @return The scratch code
     * @since 1.3.0
     */
    @XmlElement(name = "scratchCode")
    String getCode();

    /**
     * Sets the scratch code
     *
     * @param code The scratch code
     * @since 1.3.0
     */
    void setCode(String code);
}
