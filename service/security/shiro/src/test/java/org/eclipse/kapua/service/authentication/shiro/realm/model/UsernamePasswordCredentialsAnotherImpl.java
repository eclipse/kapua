/*******************************************************************************
 * Copyright (c) 2023, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.authentication.shiro.realm.model;

import org.eclipse.kapua.service.authentication.UsernamePasswordCredentials;

public class UsernamePasswordCredentialsAnotherImpl implements UsernamePasswordCredentials {

    private String username;
    private String password;
    private String authenticationCode;
    private String trustKey;
    private boolean trustMe;

    public UsernamePasswordCredentialsAnotherImpl(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getAuthenticationCode() {
        return authenticationCode;
    }

    @Override
    public void setAuthenticationCode(String authenticationCode) {
        this.authenticationCode = authenticationCode;
    }

    @Override
    public String getTrustKey() {
        return trustKey;
    }

    @Override
    public void setTrustKey(String trustKey) {
        this.trustKey = trustKey;
    }

    @Override
    public boolean getTrustMe() {
        return trustMe;
    }

    @Override
    public void setTrustMe(boolean trustMe) {
        this.trustMe = trustMe;
    }
}
