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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.token;

import org.eclipse.kapua.locator.KapuaLocator;

public class AccessTokenXmlRegistry {
    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final AccessTokenFactory factory = locator.getFactory(AccessTokenFactory.class);
    
    public AccessToken newAccessToken() {
        return factory.newAccessToken();
    }
    
    public AccessTokenCreator newAccessTokenCreator() {
        return factory.newCreator(null, null, null, null, null, null);
    }
}
