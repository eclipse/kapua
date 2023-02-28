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
package org.eclipse.kapua.service.authorization.group.shiro;

import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaTransactedRepository;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class GroupImplJpaTransactedRepository
        extends KapuaNamedEntityJpaTransactedRepository<Group, GroupImpl, GroupListResult>
        implements GroupTransactedRepository {
    public GroupImplJpaTransactedRepository(TxManager txManager, Supplier<GroupListResult> listProvider) {
        super(txManager, GroupImpl.class, listProvider);
    }
}
