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

import org.eclipse.kapua.locator.KapuaLocator;

public class AccessTokenXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AccessTokenFactory ACCESS_TOKEN_FACTORY = LOCATOR.getFactory(AccessTokenFactory.class);

    public AccessToken newAccessToken() {
        return ACCESS_TOKEN_FACTORY.newEntity(null);
    }

    public AccessTokenCreator newAccessTokenCreator() {
        return ACCESS_TOKEN_FACTORY.newCreator(null, null, null, null, null, null);
    }
}
