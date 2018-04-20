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
