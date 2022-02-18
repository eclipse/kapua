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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "name", "version", "id", "state" })
public class KuraBundle {

    @XmlElement(name = "name")
    public String name;

    @XmlElement(name = "version")
    public String version;

    @XmlElement(name = "id")
    public long id;

    @XmlElement(name = "state")
    public String state;

    /**
     * Get bundle name
     */
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
