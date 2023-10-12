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

import org.eclipse.kapua.service.authentication.JwtCredentials;

public class JwtCredentialsAnotherImpl implements JwtCredentials {
    private String accessToken;
    private String idToken;

    public JwtCredentialsAnotherImpl(String accessToken, String idToken) {
        setAccessToken(accessToken);
        setIdToken(idToken);
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getIdToken() {
        return idToken;
    }

    @Override
    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
