/*******************************************************************************
 * Copyright (c) 2016, 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.id;

import org.eclipse.kapua.KapuaSerializable;
import org.eclipse.kapua.model.KapuaEntity;

import javax.xml.bind.annotation.XmlTransient;
import java.math.BigInteger;
import java.util.Base64;

/**
 * {@link KapuaEntity} identifier definition.
 * <p>
 * This object it's used to identify each entity.
 * <b>It should be unique within the same entity.</b>
 *
 * @since 1.0.0
 */
public interface KapuaId extends KapuaSerializable {

    @XmlTransient
    KapuaId ANY = new KapuaIdImpl(BigInteger.ONE.negate());

    @XmlTransient
    KapuaId ONE = new KapuaIdImpl(BigInteger.ONE);

    /**
     * Get the identifier
     *
     * @return
     * @since 1.0.0
     */
    BigInteger getId();

    /**
     * Get the identifier Base64 URL encoded formatted.
     *
     * @return
     * @since 1.0.0
     */
    default String toCompactId() {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(getId().toByteArray());
    }

    /**
     * Get the identifier numeric String formatted.
     *
     * @return
     * @since 1.0.0
     */
    default String toStringId() {
        return getId().toString();
    }

}
