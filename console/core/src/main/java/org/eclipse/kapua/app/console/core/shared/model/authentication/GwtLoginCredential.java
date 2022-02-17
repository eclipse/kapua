/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.core.shared.model.authentication;

import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtLoginCredential extends KapuaBaseModel {

    private static final long serialVersionUID = -7683275209937145099L;

    public GwtLoginCredential() {
        super();
    }

    public GwtLoginCredential(String username, String password) {
        this();
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return get("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getPassword() {
        return KapuaSafeHtmlUtils.htmlUnescape(get("password").toString());
    }

    public void setPassword(String password) {
        set("password", password);
    }

    public String getAuthenticationCode() {
        if (get("authenticationCode") != null) {
            return KapuaSafeHtmlUtils.htmlUnescape(get("authenticationCode").toString());
        }
        return null;
    }

    public void setAuthenticationCode(String authenticationCode) {
        set("authenticationCode", authenticationCode);
    }

    public String getTrustKey() {
        if (get("trustKey") != null) {
            return KapuaSafeHtmlUtils.htmlUnescape(get("trustKey").toString());
        }
        return null;
    }

    public void setTrustKey(String trustKey) {
        set("trustKey", trustKey);
    }

    public boolean isTrustReq() {
        return get("trustReq");
    }

    public void setTrustReq(boolean trustReq) {
        set("trustReq", trustReq);
    }

}
