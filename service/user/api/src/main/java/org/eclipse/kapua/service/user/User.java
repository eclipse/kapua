/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.xml.DateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * User entity
 *
 * @since 1.0
 */
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "status",
        "displayName",
        "email",
        "phoneNumber",
        "userType",
        "externalId",
        "expirationDate"
}, //
        factoryClass = UserXmlRegistry.class, //
        factoryMethod = "newUser")
public interface User extends KapuaNamedEntity {

    String TYPE = "user";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * Return the user status
     *
     * @return
     */
    @XmlElement(name = "status")
    UserStatus getStatus();

    /**
     * Get the user status
     *
     * @param status
     */
    void setStatus(UserStatus status);

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
     * Get the user email
     *
     * @return
     */
    @XmlElement(name = "email")
    String getEmail();

    /**
     * Set the user email
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
}
