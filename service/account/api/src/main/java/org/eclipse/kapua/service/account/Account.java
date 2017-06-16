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
package org.eclipse.kapua.service.account;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.KapuaNamedEntity;

/**
 * User account entity.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "organization",
        "parentAccountPath" }, factoryClass = AccountXmlRegistry.class, factoryMethod = "newAccount")
public interface Account extends KapuaNamedEntity {

    public static final String TYPE = "account";

    public default String getType() {
        return TYPE;
    }

    /**
     * Get the account's organization
     * 
     * @return
     */
    @XmlElement(name = "organization")
    public Organization getOrganization();

    /**
     * Set the account's organization
     * 
     * @param organization
     */
    public void setOrganization(Organization organization);

    /**
     * Return the parent account path.<br>
     * The account path is a '/' separated list of the parents account identifiers in reverse order (so it should be read from right to left).<br>
     * e.g. The parent account path 7/14/15 mens that the current account has 15 as parent, then 15 has 14 as parent and 14 has 7 as parent.
     * 
     * @return
     */
    @XmlElement(name = "parentAccountPath")
    public String getParentAccountPath();

    /**
     * Set the parent account path.
     * 
     * @param parentAccountPath
     */
    public void setParentAccountPath(String parentAccountPath);
}
