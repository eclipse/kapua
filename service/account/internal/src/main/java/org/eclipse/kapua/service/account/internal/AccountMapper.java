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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigInteger;

@Mapper
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scopeId", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    public @interface IgnoreKapuaEntityFields {
    }

    @Mapping(target = "modifiedOn", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "entityAttributes", ignore = true)
    @Mapping(target = "entityProperties", ignore = true)
    @IgnoreKapuaEntityFields
    public @interface IgnoreKapuaUpdatableEntityFields {
    }

    @Mapping(target = "name", ignore = true)
    @IgnoreKapuaUpdatableEntityFields
    public @interface IgnoreKapuaNamedEntityFields {
    }

    default KapuaId map(BigInteger value) {
        return new KapuaIdImpl(value);
    }

    @IgnoreKapuaNamedEntityFields
    @Mapping(target = "parentAccountPath", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "childAccounts", ignore = true)
    void merge(@MappingTarget Account account, AccountService.CurrentAccountUpdateRequest request);
}
