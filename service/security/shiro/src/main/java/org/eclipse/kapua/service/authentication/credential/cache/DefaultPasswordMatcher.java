/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.credential.cache;

import org.eclipse.kapua.service.authentication.credential.Credential;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class DefaultPasswordMatcher implements PasswordMatcher {

    public boolean checkPassword(String tokenUsername, String tokenPassword, Credential infoCredential) {
        return BCrypt.checkpw(tokenPassword, infoCredential.getCredentialKey());
    }

}