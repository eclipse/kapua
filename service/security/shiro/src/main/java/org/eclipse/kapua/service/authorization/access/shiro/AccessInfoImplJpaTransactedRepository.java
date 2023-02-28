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

import org.eclipse.kapua.commons.jpa.KapuaUpdatableEntityJpaTransactedRepository;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;
import org.eclipse.kapua.service.authorization.access.AccessInfoTransactedRepository;
import org.eclipse.kapua.storage.TxManager;

import java.util.function.Supplier;

public class AccessInfoImplJpaTransactedRepository
        extends KapuaUpdatableEntityJpaTransactedRepository<AccessInfo, AccessInfoImpl, AccessInfoListResult>
        implements AccessInfoTransactedRepository {
    public AccessInfoImplJpaTransactedRepository(TxManager txManager, Supplier<AccessInfoListResult> listSupplier) {
        super(txManager, AccessInfoImpl.class, listSupplier);
    }
}
