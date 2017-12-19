/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.user.steps;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

/**
 * Data object used in Gherkin to transfer User data.
 */
public class TestUser {

    private String name;

    private String displayName;

    private String email;

    private String phoneNumber;

    private String status;

    private String userType;

    private int scopeId;

    private String password;

    private String expirationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatus getStatus() {

        if (status == null) {
            return null;
        }

        switch (status.trim().toUpperCase()) {
            case "ENABLED": return UserStatus.ENABLED;
            case "DISABLED": return UserStatus.DISABLED;
            default: return null;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserType getUserType() {

        if (userType == null) {
            return null;
        }

        switch (userType.trim().toUpperCase()) {
            case "DEVICE": return UserType.DEVICE;
            case "INTERNAL": return UserType.INTERNAL;
            case "EXTERNAL": return UserType.EXTERNAL;
            default: return null;
        }
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public KapuaId getScopeId() {
        return new KapuaEid(BigInteger.valueOf(scopeId)); }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }

    public Date getExpirationDate() {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date expDate = null;
        Instant now = Instant.now();

        if (expirationDate == null) {
            return null;
        }
        // Special keywords for date
        switch (expirationDate) {
            case "yesterday":
                expDate = Date.from(now.minus(Duration.ofDays(1)));
                break;
            case "today":
                expDate = Date.from(now);
                break;
            case "tomorrow":
                expDate = Date.from(now.plus(Duration.ofDays(1)));
                break;
        }
        // Just parse date
        try {
            expDate = df.parse(expirationDate);
        } catch (ParseException | NullPointerException e) {
            // skip, leave date null
        }

        return expDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
