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

import org.eclipse.kapua.app.console.shared.model.GwtEntityModel;

public class GwtCredential extends GwtEntityModel {

    private static final long serialVersionUID = -469650746033310482L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("credentialTypeEnum".equals(property)) {
            return (X) GwtCredentialType.valueOf(getCredentialType());
        } else {
            return super.get(property);
        }
    }

    public GwtCredential() {
        super();
    }

    public String getUserId() {
        return get("userId");
    }

    public void setUserId(String userId) {
        set("userId", userId);
    }

    public String getCredentialType() {
        return get("credentialType");
    }

    public GwtCredentialType getCredentialTypeEnum() {
        return get("credentialTypeEnum");
    }

    public void setCredentialType(String credentialType) {
        set("credentialType", credentialType);
    }

    public String getCredentialKey() {
        return get("credentialKey");
    }

    public void setCredentialKey(String credentialKey) {
        set("credentialKey", credentialKey);
    }

}