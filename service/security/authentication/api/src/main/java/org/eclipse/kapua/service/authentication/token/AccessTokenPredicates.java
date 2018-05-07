/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

/**
 * Access Token predicates used to build query predicates.
 *
 * @since 1.0
 */
public interface AccessTokenPredicates extends KapuaUpdatableEntityPredicates {

    String TOKEN_ID = "tokenId";
    String USER_ID = "userId";

}
