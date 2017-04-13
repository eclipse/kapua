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

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.eclipse.kapua.service.account.Organization;

/**
 * Organization entity implementation
 * 
 * @since 1.0
 *
 */
@Embeddable
public class OrganizationImpl implements Organization, Serializable {

	private static final long serialVersionUID = -2963244741663925288L;

	private String name;
    private String personName;
    private String email;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String zipPostCode;
    private String city;
    private String stateProvinceCounty;
    private String country;

    /**
     * Constructor
     */
    public OrganizationImpl() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPersonName() {
        return personName;
    }

    @Override
    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getAddressLine1() {
        return addressLine1;
    }

    @Override
    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Override
    public String getAddressLine2() {
        return addressLine2;
    }

    @Override
    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    /**
     * Get organization address (third line)
     * 
     * @return
     */
    public String getAddressLine3() {
        return addressLine3;
    }

    /**
     * Set organization address (third line)
     * 
     * @param addressLine3
     */
    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    @Override
    public String getZipPostCode() {
        return zipPostCode;
    }

    @Override
    public void setZipPostCode(String zipPostCode) {
        this.zipPostCode = zipPostCode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String getStateProvinceCounty() {
        return stateProvinceCounty;
    }

    @Override
    public void setStateProvinceCounty(String stateProvinceCounty) {
        this.stateProvinceCounty = stateProvinceCounty;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }
}
