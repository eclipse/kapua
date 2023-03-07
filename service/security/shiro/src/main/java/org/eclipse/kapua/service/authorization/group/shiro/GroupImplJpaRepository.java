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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaNamedEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupListResult;
import org.eclipse.kapua.service.authorization.group.GroupRepository;
import org.eclipse.kapua.storage.TxContext;

public class GroupImplJpaRepository
        extends KapuaNamedEntityJpaRepository<Group, GroupImpl, GroupListResult>
        implements GroupRepository {
    public GroupImplJpaRepository() {
        super(GroupImpl.class, () -> new GroupListResultImpl());
    }

    @Override
    public Group delete(TxContext tx, KapuaId scopeId, KapuaId groupId) throws KapuaException {
        //
        // Check existence
        final Group toBeDeleted = super.find(tx, scopeId, groupId);
        if (toBeDeleted == null) {
            throw new KapuaEntityNotFoundException(Group.TYPE, groupId);
        }

        //
        // Do delete
        return super.delete(tx, toBeDeleted);
    }
}
