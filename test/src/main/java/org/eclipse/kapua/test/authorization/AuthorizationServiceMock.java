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
package org.eclipse.kapua.test.authorization;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;

@TestService
@KapuaProvider
public class AuthorizationServiceMock implements AuthorizationService
{

    @Override
    public boolean isPermitted(Permission permission)
        throws KapuaException
    {
        // Always true
        return true;
    }

    @Override
    public void checkPermission(Permission permission)
        throws KapuaException
    {
        // Never thorws
    }

}
