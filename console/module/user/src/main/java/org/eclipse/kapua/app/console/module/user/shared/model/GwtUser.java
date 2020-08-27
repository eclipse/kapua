/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.user.shared.model;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class GwtUser extends GwtUpdatableEntityModel implements IsSerializable {

    private static final long serialVersionUID = -3731370307878410611L;

    private static final String USERNAME = "username";
    private static final String DISPLAY_NAME = "displayName";
    private static final String EMAIL = "email";
    private static final String PHONE_NUMBER = "phoneNumber";

    public enum GwtUserStatus implements IsSerializable {
        ENABLED, DISABLED, ANY;

        GwtUserStatus() {
        }
    }

    /**
     * Mimic the UserType
     */
    public enum GwtUserType implements IsSerializable {
        /**
         * Internal user type (user credentials from Kapua)
         */
        INTERNAL,

        /**
         * External user type (user credentials from SSO)
         */
        EXTERNAL,

        /***
         * Any is used only for the filter
         */
        ANY;

        GwtUserType() {
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("statusEnum".equals(property)) {
            return (X) (GwtUserStatus.valueOf(getStatus()));
        } else if ("userTypeEnum".equals(property)) {
            return (X) (GwtUserType.valueOf(getUserType()));
        } else if ("expirationDateFormatted".equals(property)) {
            if (getExpirationDate() != null) {
                return (X) (DateUtils.formatDateTime(getExpirationDate()));
            } else {
                return (X) null;
            }
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
        return (String) get(USERNAME);
    }

    public String getUnescapedUsername() {
        return (String) getUnescaped(USERNAME);
    }

    public void setUsername(String username) {
        set(USERNAME, username);
    }

    public String getDisplayName() {
        return (String) get(DISPLAY_NAME);
    }

    public String getUnescapedDisplayName() {
        return (String) getUnescaped(DISPLAY_NAME);
    }

    public void setDisplayName(String displayName) {
        set(DISPLAY_NAME, displayName);
    }

    public String getEmail() {
        return (String) get(EMAIL);
    }

    public String getUnescapedEmail() {
        return (String) getUnescaped(EMAIL);
    }

    public void setEmail(String email) {
        set(EMAIL, email);
    }

    public String getPhoneNumber() {
        return (String) get(PHONE_NUMBER);
    }

    public String getUnescapedPhoneNumber() {
        return (String) getUnescaped(PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        set(PHONE_NUMBER, phoneNumber);
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

    public Date getExpirationDate() {
        return get("expirationDate");
    }

    public String getExpirationDateFormatted() {
        return get("expirationDateFormatted");
    }

    public void setExpirationDate(Date expirationDate) {
        set("expirationDate", expirationDate);
    }

    public String getUserType() {
        return (String) get("userType");
    }

    public GwtUserType getUserTypeEnum() {
        return (GwtUserType) get("userTypeEnum");
    }

    public void setUserType(String userType) {
        set("userType", userType);
    }

    public String getExternalId() {
        return (String) get("externalId");
    }

    public String getUnescapedExternalId() {
        return (String) getUnescaped("externalId");
    }

    public void setExternalId(String externalId) {
        set("externalId", externalId);
    }
}
