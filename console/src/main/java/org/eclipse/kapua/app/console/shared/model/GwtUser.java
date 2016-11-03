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
package org.eclipse.kapua.app.console.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtUser extends GwtUpdatableEntityModel implements IsSerializable {

    private static final long serialVersionUID = -3731370307878410611L;

    public enum GwtUserStatus implements IsSerializable {
        ENABLED, DISABLED;

        GwtUserStatus() {
        };
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("statusEnum".equals(property)) {
            return (X) (GwtUserStatus.valueOf(getStatus()));
        } else {
            return super.get(property);
        }
    }

    public GwtUser() {
        super();
    }

    public GwtUser(String username) {
        this();
        this.setUsername(username);
    }

    public String getUsername() {
        return (String) get("username");
    }

    public String getUnescapedUsername() {
        return (String) getUnescaped("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getDisplayName() {
        return (String) get("displayName");
    }

    public String getUnescapedDisplayName() {
        return (String) getUnescaped("displayName");
    }

    public void setDisplayName(String displayName) {
        set("displayName", displayName);
    }

    public String getEmail() {
        return (String) get("email");
    }

    public String getUnescapedEmail() {
        return (String) getUnescaped("email");
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public String getPhoneNumber() {
        return (String) get("phoneNumber");
    }

    public String getUnescapedPhoneNumber() {
        return (String) getUnescaped("phoneNumber");
    }

    public void setPhoneNumber(String phoneNumber) {
        set("phoneNumber", phoneNumber);
    }

    public String getStatus() {
        return (String) get("status");
    }

    public GwtUserStatus getStatusEnum() {
        return (GwtUserStatus) get("statusEnum");
    }

    public void setStatus(String status) {
        set("status", status);
    }
}