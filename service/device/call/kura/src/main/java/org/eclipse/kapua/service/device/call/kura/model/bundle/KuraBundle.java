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
package org.eclipse.kapua.service.device.call.kura.model.bundle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Kura bundle definition.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "bundle")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType
public class KuraBundle {

    public String name;
    public String version;
    public long id;
    public String state;

    /**
     * Get bundle name
     */
    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Set bundle name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get bundle version
     *
     * @return
     */
    @XmlElement(name = "version")
    public String getVersion() {
        return version;
    }

    /**
     * Set bundle version
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get bundle identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    public long getId() {
        return id;
    }

    /**
     * Set bundle identifier
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get bundle state
     *
     * @return
     */
    @XmlElement(name = "state")
    public String getState() {
        return state;
    }

    /**
     * Set bundle state
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }
}
