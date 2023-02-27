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

import org.eclipse.kapua.commons.jpa.EntityManagerSession;
import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityRepositoryJpaImpl;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoRepository;

import java.util.function.Supplier;

public class AccessInfoImplJpaRepository
        extends KapuaUpdatableEntityRepositoryJpaImpl<AccessInfo, AccessInfoImpl, AccessInfoListResult>
        implements AccessInfoRepository {
    public AccessInfoImplJpaRepository(Supplier<AccessInfoListResult> listSupplier, EntityManagerSession entityManagerSession) {
        super(AccessInfoImpl.class, listSupplier, entityManagerSession);
    }
}