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
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;

/**
 * Account creator service implementation.
 * 
 * @since 1.0
 * 
 */
public class AccountCreatorImpl extends AbstractKapuaNamedEntityCreator<Account> implements AccountCreator {

    private static final long serialVersionUID = -2460883485294616032L;

    private String accountName;

    private String organizationName;

    private String organizationPersonName;

    private String organizationEmail;

    private String organizationPhoneNumber;

    private String organizationAddressLine1;

    private String organizationAddressLine2;

    private String organizationCity;

    private String organizationZipPostCode;

    private String organizationStateProvinceCounty;

    private String organizationCountry;

    /**
     * Constructor
     * 
     * @param scopeId
     * @param name
     *            account name
     */
    public AccountCreatorImpl(KapuaId scopeId, String name) {
        super(scopeId, name);
    }

    /**
     * Get the account name
     * 
     * @return
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * Set the account name
     * 
     * @param accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @Override
    public String getOrganizationPersonName() {
        return organizationPersonName;
    }

    @Override
    public void setOrganizationPersonName(String organizationPersonName) {
        this.organizationPersonName = organizationPersonName;
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
        return organizationPhoneNumber;
    }

    @Override
    public void setOrganizationPhoneNumber(String organizationPhoneNumber) {
        this.organizationPhoneNumber = organizationPhoneNumber;
    }

    @Override
    public String getOrganizationAddressLine1() {
        return organizationAddressLine1;
    }

    @Override
    public void setOrganizationAddressLine1(String organizationAddressLine1) {
        this.organizationAddressLine1 = organizationAddressLine1;
    }

    @Override
    public String getOrganizationAddressLine2() {
        return organizationAddressLine2;
    }

    @Override
    public void setOrganizationAddressLine2(String organizationAddressLine2) {
        this.organizationAddressLine2 = organizationAddressLine2;
    }

    @Override
    public String getOrganizationCity() {
        return organizationCity;
    }

    @Override
    public void setOrganizationCity(String organizationCity) {
        this.organizationCity = organizationCity;
    }

    @Override
    public String getOrganizationZipPostCode() {
        return organizationZipPostCode;
    }

    @Override
    public void setOrganizationZipPostCode(String organizationZipPostCode) {
        this.organizationZipPostCode = organizationZipPostCode;
    }

    @Override
    public String getOrganizationStateProvinceCounty() {
        return organizationStateProvinceCounty;
    }

    @Override
    public void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty) {
        this.organizationStateProvinceCounty = organizationStateProvinceCounty;
    }

    @Override
    public String getOrganizationCountry() {
        return organizationCountry;
    }

    @Override
    public void setOrganizationCountry(String organizationCountry) {
        this.organizationCountry = organizationCountry;
    }
}
