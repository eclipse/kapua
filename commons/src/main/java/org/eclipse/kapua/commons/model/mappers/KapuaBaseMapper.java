/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.model.mappers;

import java.math.BigInteger;
import java.util.Optional;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface KapuaBaseMapper {

    /**
     * Use this annotation for merge-mappers between dtos and KapuaEntities, to ignore all read-only fields of the target entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scopeId", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public @interface IgnoreKapuaEntityReadonlyFields {

    }

    /**
     * Use this annotation for merge-mappers between update request dtos and KapuaUpdatableEntities, to ignore all read-only fields of the target entity
     */
    @Mapping(target = "modifiedOn", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "optlock", ignore = true)
    @Mapping(target = "entityAttributes", ignore = true)
    @Mapping(target = "entityProperties", ignore = true)
    @IgnoreKapuaEntityReadonlyFields
    public @interface IgnoreKapuaUpdatableEntityReadonlyFields {

    }

    @Mapping(target = "name", ignore = true)
    @IgnoreKapuaUpdatableEntityReadonlyFields
    public @interface IgnoreKapuaNamedEntityReadonlyFields {

    }

    default String map(String value) {
        return Optional.ofNullable(value).map(String::trim).orElse(null);
    }

    default String map(int value) {
        return Integer.toString(value);
    }

    default String map(long value) {
        return Long.toString(value);
    }

    default String map(double value) {
        return Double.toString(value);
    }

    default String map(float value) {
        return Float.toString(value);
    }

    default KapuaId map(BigInteger value) {
        return new KapuaIdImpl(value);
    }
}
