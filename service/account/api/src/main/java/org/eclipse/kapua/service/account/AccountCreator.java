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
package org.eclipse.kapua.service.account;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * {@link AccountCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "accountCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"organizationName",
        "organizationPersonName",
        "organizationEmail",
        "organizationPhoneNumber",
        "organizationAddressLine1",
        "organizationAddressLine2",
        "organizationCity",
        "organizationZipPostCode",
        "organizationStateProvinceCounty",
        "organizationCountry",
        "expirationDate"}, factoryClass = AccountXmlRegistry.class, factoryMethod = "newAccountCreator")
public interface AccountCreator extends KapuaNamedEntityCreator<Account> {

    /**
     * Get the organization name
     *
     * @return
     */
    @XmlElement(name = "organizationName")
    String getOrganizationName();

    /**
     * Set the organization name
     *
     * @param organizationName
     */
    void setOrganizationName(String organizationName);

    /**
     * Get organization referent name
     *
     * @return
     */
    @XmlElement(name = "organizationPersonName")
    String getOrganizationPersonName();

    /**
     * Set organization referent name
     *
     * @param organizationPersonName
     */
    void setOrganizationPersonName(String organizationPersonName);

    /**
     * Get the organization email
     *
     * @return
     */
    @XmlElement(name = "organizationEmail")
    String getOrganizationEmail();

    /**
     * Set the organization email
     *
     * @param organizationEmail
     */
    void setOrganizationEmail(String organizationEmail);

    /**
     * Get the organization phone number
     *
     * @return
     */
    @XmlElement(name = "organizationPhoneNumber")
    String getOrganizationPhoneNumber();

    /**
     * Set the organization phone number
     *
     * @param organizationPhoneNumber
     */
    void setOrganizationPhoneNumber(String organizationPhoneNumber);

    /**
     * Get organization address (first line)
     *
     * @return
     */
    @XmlElement(name = "organizationAddressLine1")
    String getOrganizationAddressLine1();

    /**
     * Set organization address (first line)
     *
     * @param organizationAddressLine1
     */
    void setOrganizationAddressLine1(String organizationAddressLine1);

    /**
     * Get organization address (second line)
     *
     * @return
     */
    @XmlElement(name = "organizationAddressLine2")
    String getOrganizationAddressLine2();

    /**
     * Set organization address (second line)
     *
     * @param organizationAddressLine2
     */
    void setOrganizationAddressLine2(String organizationAddressLine2);

    /**
     * Get the organization city
     *
     * @return
     */
    @XmlElement(name = "organizationCity")
    String getOrganizationCity();

    /**
     * Set the organization city
     *
     * @param organizationCity
     */
    void setOrganizationCity(String organizationCity);

    /**
     * Get organization postal zip code
     *
     * @return
     */
    @XmlElement(name = "organizationZipPostCode")
    String getOrganizationZipPostCode();

    /**
     * Set organization postal zip code
     *
     * @param organizationZipPostCode
     */
    void setOrganizationZipPostCode(String organizationZipPostCode);

    /**
     * Get organization province or state (if it is a federal state) within a country
     *
     * @return
     */
    @XmlElement(name = "organizationStateProvinceCounty")
    String getOrganizationStateProvinceCounty();

    /**
     * Set organization province or state (if it is a federal state) within a country
     *
     * @param organizationStateProvinceCounty
     */
    void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty);

    /**
     * Get the organization country
     *
     * @return
     */
    @XmlElement(name = "organizationCountry")
    String getOrganizationCountry();

    /**
     * Set the organization country
     *
     * @param organizationCountry
     */
    void setOrganizationCountry(String organizationCountry);

    /**
     * Get the expiration date
     *
     * @return the current Account expiration date
     */
    @XmlElement(name = "expirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpirationDate();

    /**
     * Set the expiration date
     *
     * @param expirationDate the current Account expiration date
     */
    void setExpirationDate(Date expirationDate);
}
