/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.authentication.shiro;

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.service.authentication.ApiKeyCredentials;

public class ApiKeyCredentialsImpl implements ApiKeyCredentials, AuthenticationToken {

    private static final long serialVersionUID = -5920944517814926028L;

    private String apiKey;

    public ApiKeyCredentialsImpl(String apiKey) {
        setApiKey(apiKey);
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public Object getPrincipal() {
        return getApiKey();
    }

    @Override
    public Object getCredentials() {
        return getApiKey();
    }

}
