/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.registration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.JwtCredentials;

public interface RegistrationService extends KapuaService {

    public boolean createAccount(JwtCredentials credentials) throws KapuaException;

    /**
     * Test if account creation is enabled
     * @return {@code true} if account creation is enabled, {@code false} otherwise
     */
    public boolean isAccountCreationEnabled();
}
