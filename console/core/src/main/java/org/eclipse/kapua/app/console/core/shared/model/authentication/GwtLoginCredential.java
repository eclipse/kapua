/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.core.shared.model.authentication;

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
        return get("password");
    }

    public void setPassword(String password) {
        set("password", password);
    }
}
