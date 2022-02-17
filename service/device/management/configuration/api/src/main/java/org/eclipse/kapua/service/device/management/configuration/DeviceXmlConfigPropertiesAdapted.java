/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.configuration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * A container for XmlConfigPropertyAdapted organized into an array.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceXmlConfigPropertiesAdapted {

    @XmlElement(name = "property")
    private DeviceXmlConfigPropertyAdapted[] properties;

    /**
     * Constructor
     */
    public DeviceXmlConfigPropertiesAdapted() {
    }

    /**
     * Get the adapted properties as array
     *
     * @return
     */
    public DeviceXmlConfigPropertyAdapted[] getProperties() {
        return properties;
    }

    /**
     * Set the adapted properties from the array
     *
     * @param properties
     */
    public void setProperties(DeviceXmlConfigPropertyAdapted[] properties) {
        this.properties = properties;
    }
}
