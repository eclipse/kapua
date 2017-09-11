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
 *******************************************************************************/
package org.eclipse.kapua.test.account;

import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountCreator;

public class AccountCreatorMock implements AccountCreator {

    private KapuaId scopeId;
    private String name;
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
    public KapuaId getScopeId() {
        return this.scopeId;
    }

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationPersonName(String organizationPersonName) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationPhoneNumber(String organizationPhoneNumber) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationAddressLine1() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationAddressLine1(String organizationAddressLine1) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationAddressLine2() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationAddressLine2(String organizationAddressLine2) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationCity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationCity(String organizationCity) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationZipPostCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationZipPostCode(String organizationZipPostCode) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationStateProvinceCounty() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getOrganizationCountry() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setOrganizationCountry(String organizationCountry) {
        // TODO Auto-generated method stub

    }

    @Override
    public Properties getEntityAttributes() throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntityAttributes(Properties entityAttributes) throws KapuaException {
        // TODO Auto-generated method stub

    }

}
