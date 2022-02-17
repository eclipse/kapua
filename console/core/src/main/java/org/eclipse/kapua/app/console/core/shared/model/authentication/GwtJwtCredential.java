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

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtJwtCredential extends KapuaBaseModel {

    private static final long serialVersionUID = -7683275209937145099L;

    public GwtJwtCredential() {
        super();
    }

    public GwtJwtCredential(String accessToken) {
        this();
        setAccessToken(accessToken);
    }

    public String getAccessToken() {
        return get("accessToken");
    }

    public void setAccessToken(String accessToken) {
        set("accessToken", accessToken);
    }
}
