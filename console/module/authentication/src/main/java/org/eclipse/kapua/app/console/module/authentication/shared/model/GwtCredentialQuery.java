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
package org.eclipse.kapua.app.console.module.authentication.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;

public class GwtCredentialQuery extends GwtQuery {

    private static final long serialVersionUID = 1L;

    private String username;
    private GwtCredentialType type;
    private String userId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String key) {
        this.username = key;
    }

    public GwtCredentialType getType() {
        return type;
    }

    public void setType(GwtCredentialType type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
