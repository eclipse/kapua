/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
