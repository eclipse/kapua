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

package org.eclipse.kapua.test;

import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;
import org.eclipse.kapua.service.authentication.UsernamePasswordCredentialsFactory;

@TestService
public class UsernamePasswordCredentialsFactoryMock implements UsernamePasswordCredentialsFactory
{

    @Override
    public UsernamePasswordCredentials newInstance(String username, char[] password)
    {
        return new UsernamePasswordCredentialsMock(username, password);
    }

}
