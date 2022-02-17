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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.service.account.xml.AccountXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link Organization} definition.
 *
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = AccountXmlRegistry.class, factoryMethod = "newOrganization")
public interface Organization {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    String getName();

    /**
     * Sets the name
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the referent name.
     *
     * @return The referent name.
     * @since 1.0.0
     */
    String getPersonName();

    /**
     * Sets the referent name.
     *
     * @param personName The referent name
     * @since 1.0.0
     */
    void setPersonName(String personName);

    /**
     * Gets the email.
     *
     * @return The email.
     * @since 1.0.0
     */
    String getEmail();

    /**
     * Sets the email.
     *
     * @param email The email.
     * @since 1.0.0
     */
    void setEmail(String email);

    /**
     * Gets the phone number.
     *
     * @return The phone number.
     * @since 1.0.0
     */
    String getPhoneNumber();

    /**
     * Sets the phone number.
     *
     * @param phoneNumber The phone number.
     * @since 1.0.0
     */
    void setPhoneNumber(String phoneNumber);

    /**
     * Gets the address first line.
     *
     * @return The address first line.
     * @since 1.0.0
     */
    String getAddressLine1();

    /**
     * Sets the address first line.
     *
     * @param addressLine1 The address first line.
     * @since 1.0.0
     */
    void setAddressLine1(String addressLine1);

    /**
     * Gets the address second line.
     *
     * @return The address second line.
     * @since 1.0.0
     */
    String getAddressLine2();

    /**
     * Sets the address second line.
     *
     * @param addressLine2 The address second line.
     * @since 1.0.0
     */
    void setAddressLine2(String addressLine2);


    /**
     * Gets the address third line.
     *
     * @return The address third line.
     * @since 1.1.0
     */
    String getAddressLine3();

    /**
     * Sets the address third line.
     *
     * @param addressLine3 The address third line.
     * @since 1.1.0
     */
    void setAddressLine3(String addressLine3);

    /**
     * Gets the postal ZIP code.
     *
     * @return The postal ZIP code.
     * @since 1.0.0
     */
    String getZipPostCode();

    /**
     * Sets the postal ZIP code.
     *
     * @param zipPostalCode The postal ZIP code.
     * @since 1.0.0
     */
    void setZipPostCode(String zipPostalCode);

    /**
     * Gets the city.
     *
     * @return The city.
     * @since 1.0.0
     */
    String getCity();

    /**
     * Sets the city.
     *
     * @param city The city.
     * @since 1.0.0
     */
    void setCity(String city);

    /**
     * Gets the province or state (if it is a federal state) within a country.
     *
     * @return The province or state (if it is a federal state) within a country.
     * @since 1.0.0
     */
    String getStateProvinceCounty();

    /**
     * Sets the province or state (if it is a federal state) within a country.
     *
     * @param stateProvinceCounty The province or state (if it is a federal state) within a country.
     * @since 1.0.0
     */
    void setStateProvinceCounty(String stateProvinceCounty);

    /**
     * Gets the country.
     *
     * @return The country.
     * @since 1.0.0
     */
    String getCountry();

    /**
     * Sets the country.
     *
     * @param country The country.
     * @since 1.0.0
     */
    void setCountry(String country);
}
