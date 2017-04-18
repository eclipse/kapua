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
package org.eclipse.kapua.service.device.call.kura.model.asset;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Kura asset definition.
 * 
 * @since 1.0
 *
 */
//@XmlRootElement(name="asset")
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(propOrder= {"name","version","id","state"})
public class KuraAsset
{
//    @XmlElement(name="id")
//    public long id;

//    @XmlElement(name="name")
    public String name;

//    @XmlElement(name="version")
//    public String version;
//
//
//    @XmlElement(name = "state")
//    public String state;
    
//    /**
//     * Get asset identifier
//     * 
//     * @return
//     */
//    public long getId()
//    {
//        return id;
//    }
//    
//    /**
//     * Set asset identifier
//     * 
//     * @param id
//     */
//    public void setId(long id)
//    {
//        this.id = id;
//    }

    /**
     * Get asset name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set asset name
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

//    /**
//     * Get asset version
//     * 
//     * @return
//     */
//    public String getVersion()
//    {
//        return version;
//    }
//
//    /**
//     * Set asset version
//     * 
//     * @param version
//     */
//    public void setVersion(String version)
//    {
//        this.version = version;
//    }
//
//
//    /**
//     * Get asset state
//     * 
//     * @return
//     */
//    public String getState()
//    {
//        return state;
//    }
//
//    /**
//     * Set asset state
//     * 
//     * @param state
//     */
//    public void setState(String state)
//    {
//        this.state = state;
//    }
}
