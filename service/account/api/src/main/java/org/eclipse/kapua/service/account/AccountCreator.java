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
package org.eclipse.kapua.service.account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntityCreator;

/**
 * AccountCreator encapsulates all the information needed to create a new Account in the system.<br>
 * The data provided will be used to seed the new Account and its related information such as the associated organization and users.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "accountCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "organizationName",
        "organizationPersonName",
        "organizationEmail",
        "organizationPhoneNumber",
        "organizationAddressLine1",
        "organizationAddressLine2",
        "organizationCity",
        "organizationZipPostCode",
        "organizationStateProvinceCounty",
        "organizationCountry" }, factoryClass = AccountXmlRegistry.class, factoryMethod = "newAccountCreator")
public interface AccountCreator extends KapuaNamedEntityCreator<Account> {

    /**
     * Get the organization name
     * 
     * @return
     */
    @XmlElement(name = "organizationName")
    public String getOrganizationName();

    /**
     * Set the organization name
     * 
     * @param organizationName
     */
    public void setOrganizationName(String organizationName);

    /**
     * Get organization referent name
     * 
     * @return
     */
    @XmlElement(name = "organizationPersonName")
    public String getOrganizationPersonName();

    /**
     * Set organization referent name
     * 
     * @param organizationPersonName
     */
    public void setOrganizationPersonName(String organizationPersonName);

    /**
     * Get the organization email
     * 
     * @return
     */
    @XmlElement(name = "organizationEmail")
    public String getOrganizationEmail();

    /**
     * Set the organization email
     * 
     * @param organizationEmail
     */
    public void setOrganizationEmail(String organizationEmail);

    /**
     * Get the organization phone number
     * 
     * @return
     */
    @XmlElement(name = "organizationPhoneNumber")
    public String getOrganizationPhoneNumber();

    /**
     * Set the organization phone number
     * 
     * @param organizationPhoneNumber
     */
    public void setOrganizationPhoneNumber(String organizationPhoneNumber);

    /**
     * Get organization address (first line)
     * 
     * @return
     */
    @XmlElement(name = "organizationAddressLine1")
    public String getOrganizationAddressLine1();

    /**
     * Set organization address (first line)
     * 
     * @param organizationAddressLine1
     */
    public void setOrganizationAddressLine1(String organizationAddressLine1);

    /**
     * Get organization address (second line)
     * 
     * @return
     */
    @XmlElement(name = "organizationAddressLine2")
    public String getOrganizationAddressLine2();

    /**
     * Set organization address (second line)
     * 
     * @param organizationAddressLine2
     */
    public void setOrganizationAddressLine2(String organizationAddressLine2);

    /**
     * Get the organization city
     * 
     * @return
     */
    @XmlElement(name = "organizationCity")
    public String getOrganizationCity();

    /**
     * Set the organization city
     * 
     * @param organizationCity
     */
    public void setOrganizationCity(String organizationCity);

    /**
     * Get organization postal zip code
     * 
     * @return
     */
    @XmlElement(name = "organizationZipPostCode")
    public String getOrganizationZipPostCode();

    /**
     * Set organization postal zip code
     * 
     * @param organizationZipPostCode
     */
    public void setOrganizationZipPostCode(String organizationZipPostCode);

    /**
     * Get organization province or state (if it is a federal state) within a country
     * 
     * @return
     */
    @XmlElement(name = "organizationStateProvinceCounty")
    public String getOrganizationStateProvinceCounty();

    /**
     * Set organization province or state (if it is a federal state) within a country
     * 
     * @param organizationStateProvinceCounty
     */
    public void setOrganizationStateProvinceCounty(String organizationStateProvinceCounty);

    /**
     * Get the organization country
     * 
     * @return
     */
    @XmlElement(name = "organizationCountry")
    public String getOrganizationCountry();

    /**
     * Set the organization country
     * 
     * @param organizationCountry
     */
    public void setOrganizationCountry(String organizationCountry);
}
