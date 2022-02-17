/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.registration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.JwtCredentials;

public interface RegistrationService extends KapuaService {

    boolean createAccount(JwtCredentials credentials) throws KapuaException;

    /**
     * Test if account creation is enabled
     *
     * @return {@code true} if account creation is enabled, {@code false} otherwise
     */
    boolean isAccountCreationEnabled();
}
