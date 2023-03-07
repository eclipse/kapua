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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaRepository;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoAttributes;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;
import org.eclipse.kapua.storage.TxContext;

public class AccessInfoImplJpaRepository
        extends KapuaUpdatableEntityJpaRepository<AccessInfo, AccessInfoImpl, AccessInfoListResult>
        implements AccessInfoRepository {
    public AccessInfoImplJpaRepository() {
        super(AccessInfoImpl.class, () -> new AccessInfoListResultImpl());
    }

    @Override
    public AccessInfo findByUserId(TxContext txContext, KapuaId scopeId, KapuaId userId) throws KapuaException {
        AccessInfoQuery query = new AccessInfoQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(AccessInfoAttributes.USER_ID, userId));
        AccessInfoListResult result = this.query(txContext, query);
        if (!result.isEmpty()) {
            return result.getFirstItem();
        }
        return null;
    }

    // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
    //  without TYPE)
    @Override
    public AccessInfo delete(TxContext txContext, KapuaId scopeId, KapuaId accessInfoId) throws KapuaException {
        final AccessInfo found = super.find(txContext, scopeId, accessInfoId);
        if (found == null) {
            throw new KapuaEntityNotFoundException(AccessInfo.TYPE, accessInfoId);
        }
        return super.delete(txContext, found);
    }
}
