/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.test.account;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountCreator;

import java.util.Date;
import java.util.Properties;

public class AccountCreatorMock implements AccountCreator {

    private KapuaId scopeId;
    private String name;
    private String description;
    private String organizationName;
    private String organizationEmail;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public KapuaId getScopeId() {
        return this.scopeId;
    }

    @Override
    public void setScopeId(KapuaId scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public String getOrganizationName() {
        return this.organizationName;
    }

    @Override
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String getOrganizationPersonName() {
        return null;
    }

    @Override
    public void setOrganizationPersonName(String organizationPersonName) {
        // Not used
    }

    @Override
    public String getOrganizationEmail() {
        return organizationEmail;
    }

    @Override
    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    @Override
    public String getOrganizationPhoneNumber() {
        return null;
    }

    @Override
    public void setOrganizationPhoneNumber(String organizationPhoneNumber) {
        // Not used
    }

    @Override
    public String getOrganizationAddressLine1() {
        return null;
    }

    @Override
    public void setOrganizationAddressLine1(String organizationAddressLine1) {
        // Not used
    }

    @Override
    public String getOrganizationAddressLine2() {
        return null;
    }

    @Override
    public void setOrganizationAddressLine2(String organizationAddressLine2) {
        // Not used
    }

    @Override
    public String getOrganizationCity() {
        return null;
    }

    @Override
    public void setOrganizationCity(String organizationCity) {
        // Not used
    }

    @Override
    public String getOrganizationZipPostCode() {
        return null;
    }

    @Override
    public void setOrganizationZipPostCode(String organizationZipPostCode) {
        // Not used
    }

    @Override
    public String getOrganizationStateProvinceCounty() {
        return null;
    }

    @Override
    public void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty) {
        // Not used
    }

    @Override
    public String getOrganizationCountry() {
        return null;
    }

    @Override
    public void setOrganizationCountry(String organizationCountry) {
        // Not used
    }

    @Override
    public Properties getEntityAttributes() throws KapuaException {
        return null;
    }

    @Override
    public void setEntityAttributes(Properties entityAttributes) throws KapuaException {
        // Not used
    }

    @Override
    public Date getExpirationDate() {
        return null;
    }

    @Override
    public void setExpirationDate(Date expirationDate) {
        // Not used
    }
}
