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
package org.eclipse.kapua.service.user;

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
 * {@link UserCreator} {@link org.eclipse.kapua.model.KapuaEntityCreator} definition
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "userCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = UserXmlRegistry.class, factoryMethod = "newUserCreator")
public interface UserCreator extends KapuaNamedEntityCreator<User> {

    /**
     * Return the display name (may be a friendly username to show in the UI)
     *
     * @return
     */
    @XmlElement(name = "displayName")
    String getDisplayName();

    /**
     * Set the display name
     *
     * @param displayName
     */
    void setDisplayName(String displayName);

    /**
     * Get the email
     *
     * @return
     */
    @XmlElement(name = "email")
    String getEmail();

    /**
     * Set the email
     *
     * @param email
     */
    void setEmail(String email);

    /**
     * Get the phone number
     *
     * @return
     */
    @XmlElement(name = "phoneNumber")
    String getPhoneNumber();

    /**
     * Set the phone number
     *
     * @param phoneNumber
     */
    void setPhoneNumber(String phoneNumber);

    /**
     * Get the user type
     *
     * @return
     */
    @XmlElement(name = "userType")
    UserType getUserType();

    /**
     * Set the user type
     *
     * @param userType
     */
    void setUserType(UserType userType);

    /**
     * Get the external ID
     *
     * @return
     */
    @XmlElement(name = "externalId")
    String getExternalId();

    /**
     * Set the external ID
     *
     * @param externalId
     */
    void setExternalId(String externalId);

    @XmlElement(name = "expirationDate")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getExpirationDate();

    void setExpirationDate(Date expirationDate);

    @XmlElement(name = "userStatus")
    UserStatus getUserStatus();

    void setUserStatus(UserStatus userStatus);
}
