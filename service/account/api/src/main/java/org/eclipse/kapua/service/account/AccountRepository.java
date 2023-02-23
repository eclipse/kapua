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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.repository.KapuaEntityRepository;
import org.eclipse.kapua.repository.KapuaNamedEntityRepository;
import org.eclipse.kapua.repository.KapuaUpdatableEntityRepository;

public interface AccountRepository extends
        KapuaEntityRepository<Account>,
        KapuaNamedEntityRepository<Account>,
        KapuaUpdatableEntityRepository<Account> {
    KapuaListResult<Account> findChildAccountsRecursive(String parentAccountPath);
}
