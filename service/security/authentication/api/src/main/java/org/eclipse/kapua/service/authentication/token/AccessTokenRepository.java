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
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.storage.KapuaUpdatableEntityRepository;
import org.eclipse.kapua.storage.TxContext;

public interface AccessTokenRepository
        extends KapuaUpdatableEntityRepository<AccessToken, AccessTokenListResult> {
    AccessToken findByTokenId(TxContext tx, String tokenId);
}
