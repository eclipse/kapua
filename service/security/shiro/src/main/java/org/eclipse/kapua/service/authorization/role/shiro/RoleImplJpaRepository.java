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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityRepositoryJpaImpl;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleRepository;

import java.util.function.Supplier;

public class RoleImplJpaRepository
        extends KapuaNamedEntityRepositoryJpaImpl<Role, RoleImpl>
        implements RoleRepository {
    public RoleImplJpaRepository(Supplier<? extends KapuaListResult<Role>> listProvider, EntityManagerSession entityManagerSession) {
        super(RoleImpl.class, listProvider, entityManagerSession);
    }
}
