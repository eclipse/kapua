/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.account.internal;

import org.eclipse.kapua.service.account.Organization;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * {@link Organization} implementation.
 *
 * @since 1.0.0
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
     * Constructor.
     *
     * @since 1.0.0
     */
    public OrganizationImpl() {
    }

    /**
     * Clone constructor.
     *
     * @since 1.1.0
     */
    public OrganizationImpl(Organization organization) {
        this();

        setName(organization.getName());
        setPersonName(organization.getPersonName());
        setEmail(organization.getEmail());
        setPhoneNumber(organization.getPhoneNumber());
        setAddressLine1(organization.getAddressLine1());
        setAddressLine2(organization.getAddressLine2());
        setAddressLine3(organization.getAddressLine3());
        setZipPostCode(organization.getZipPostCode());
        setCity(organization.getCity());
        setStateProvinceCounty(organization.getStateProvinceCounty());
        setCountry(organization.getCountry());
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

    @Override
    public String getAddressLine3() {
        return addressLine3;
    }

    @Override
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
