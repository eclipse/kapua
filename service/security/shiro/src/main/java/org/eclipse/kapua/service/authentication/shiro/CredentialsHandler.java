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
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.authentication.exception.KapuaAuthenticationException;

import java.util.Optional;

public interface CredentialsHandler {

    boolean canProcess(LoginCredentials loginCredentials);

    ImmutablePair<AuthenticationToken, Optional<String>> mapToShiro(LoginCredentials loginCredentials) throws KapuaAuthenticationException;
}
