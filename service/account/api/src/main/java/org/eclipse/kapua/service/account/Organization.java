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
import javax.xml.bind.annotation.XmlType;

/**
 * Organization entity
 * 
 * @since 1.0
 * 
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass=AccountXmlRegistry.class, factoryMethod="newOrganization")
public interface Organization
{

    /**
     * Get the organization name
     * 
     * @return
     */
    public String getName();

    /**
     * Set the organization name
     * 
     * @param name
     */
    public void setName(String name);

    /**
     * Get organization referent name
     * 
     * @return
     */
    public String getPersonName();

    /**
     * Set organization referent name
     * 
     * @param name
     */
    public void setPersonName(String name);

    /**
     * Get the organization email
     * 
     * @return
     */
    public String getEmail();

    /**
     * Set the organization email
     * 
     * @param email
     */
    public void setEmail(String email);

    /**
     * Get the organization phone number
     * 
     * @return
     */
    public String getPhoneNumber();

    /**
     * Set the organization phone number
     * 
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber);

    /**
     * Get organization address (first line)
     * 
     * @return
     */
    public String getAddressLine1();

    /**
     * Set organization address (first line)
     * 
     * @param addressLine1
     */
    public void setAddressLine1(String addressLine1);

    /**
     * Get organization address (second line)
     * 
     * @return
     */
    public String getAddressLine2();

    /**
     * Set organization address (second line)
     * 
     * @param addressLine2
     */
    public void setAddressLine2(String addressLine2);

    /**
     * Get organization postal zip code
     * 
     * @return
     */
    public String getZipPostCode();

    /**
     * Set organization postal zip code
     * 
     * @param zipPostalCode
     */
    public void setZipPostCode(String zipPostalCode);

    /**
     * Get the organization city
     * 
     * @return
     */
    public String getCity();

    /**
     * Set the organization city
     * 
     * @param city
     */
    public void setCity(String city);

    /**
     * Get organization province or state (if it is a federal state) within a country
     * 
     * @return
     */
    public String getStateProvinceCounty();

    /**
     * Set organization province or state (if it is a federal state) within a country
     * 
     * @param stateProvinceCounty
     */
    public void setStateProvinceCounty(String stateProvinceCounty);

    /**
     * Get the organization country
     * 
     * @return
     */
    public String getCountry();

    /**
     * Set the organization country
     * 
     * @param country
     */
    public void setCountry(String country);
}
