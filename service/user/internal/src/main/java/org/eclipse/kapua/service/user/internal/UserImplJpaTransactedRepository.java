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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaTransactedRepository;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserTransactedRepository;

import java.util.function.Supplier;

public class UserImplJpaTransactedRepository
        extends KapuaNamedEntityJpaTransactedRepository<User, UserImpl, UserListResult>
        implements UserTransactedRepository {
    public UserImplJpaTransactedRepository(Supplier<UserListResult> listProvider, EntityManagerSession entityManagerSession) {
        super(UserImpl.class, listProvider, entityManagerSession);
    }

}
