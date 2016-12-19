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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.service.authentication.UsernamePasswordToken;
import org.eclipse.kapua.service.authentication.UsernamePasswordTokenFactory;

/**
 * Username password {@link UsernamePasswordTokenFactory} factory implementation.
 * 
 * @since 1.0
 * 
 */
@KapuaProvider
public class UsernamePasswordTokenFactoryImpl implements UsernamePasswordTokenFactory
{
    @Override
    public UsernamePasswordToken newInstance(String username, char[] password)
    {
        return new UsernamePasswordTokenImpl(username, password);
    }
}
