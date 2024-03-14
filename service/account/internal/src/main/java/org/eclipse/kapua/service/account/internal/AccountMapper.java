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

import org.eclipse.kapua.commons.model.mappers.KapuaBaseMapper;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.CurrentAccountUpdateRequest;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(uses = KapuaBaseMapper.class, componentModel = MappingConstants.ComponentModel.JSR330, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {

    @KapuaBaseMapper.IgnoreKapuaNamedEntityReadonlyFields
    @Mapping(target = "parentAccountPath", ignore = true)
    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "childAccounts", ignore = true)
    void merge(@MappingTarget Account account, CurrentAccountUpdateRequest request);
}
