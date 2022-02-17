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

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * Access Token predicates used to build query predicates.
 *
 * @since 1.0
 */
public class AccessTokenAttributes extends KapuaUpdatableEntityAttributes {

    public static final String TOKEN_ID = "tokenId";
    public static final String USER_ID = "userId";
    public static final String EXPIRES_ON = "expiresOn";
    public static final String INVALIDATED_ON = "invalidatedOn";
    public static final String REFRESH_EXPIRES_ON = "refreshExpiresOn";

}
