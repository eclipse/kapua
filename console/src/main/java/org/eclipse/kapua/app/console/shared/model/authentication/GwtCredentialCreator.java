/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model.authentication;

import org.eclipse.kapua.app.console.shared.model.GwtEntityCreator;

import java.io.Serializable;

public class GwtCredentialCreator extends GwtEntityCreator implements Serializable {

    private String userId;
    private GwtCredentialType credentialType;
    private String credentialPlainKey;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GwtCredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(GwtCredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialPlainKey() {
        return credentialPlainKey;
    }

    public void setCredentialPlainKey(String credentialPlainKey) {
        this.credentialPlainKey = credentialPlainKey;
    }
}
