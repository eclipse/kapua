/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.id;

import java.math.BigInteger;

import javax.persistence.AttributeConverter;

/**
 * Kapua identifier converter used by persistence operations to convert the entity from and to database.
 *
 * @since 1.0
 *
 */
public class KapuaEidConverter implements AttributeConverter<KapuaEid, BigInteger> {

    @Override
    public BigInteger convertToDatabaseColumn(KapuaEid keid) {
        return keid.getId();
    }

    @Override
    public KapuaEid convertToEntityAttribute(BigInteger id) {
        return new KapuaEid(id);
    }
}
