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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

    private UserStatus status;

    private UserType userType;

    private BigInteger scopeId;

    private String expirationDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public BigInteger getScopeId() {
        return scopeId;
    }

    public void setScopeId(BigInteger scopeId) {
        this.scopeId = scopeId;
    }

    public Date getExpirationDate() {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date expDate = null;
        LocalDateTime now = LocalDateTime.now();

        if (expirationDate == null) {
            return null;
        }
        // Special keywords for date
        switch (expirationDate) {
            case "yesterday":
                expDate = Date.from(now.minusDays(1).atZone(ZoneId.systemDefault()).toInstant());
                break;
            case "today":
                expDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case "tomorrow":
                expDate = Date.from(now.plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
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
