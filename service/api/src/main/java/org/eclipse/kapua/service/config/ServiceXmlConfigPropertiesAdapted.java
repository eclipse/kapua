/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * A container for XmlConfigPropertyAdapted organized into an array.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ServiceXmlConfigPropertiesAdapted {

    @XmlElement(name = "property")
    private ServiceXmlConfigPropertyAdapted[] properties;

    /**
     * Constructor
     */
    public ServiceXmlConfigPropertiesAdapted() {
    }

    /**
     * Get the adapted properties as array
     *
     * @return
     */
    public ServiceXmlConfigPropertyAdapted[] getProperties() {
        return properties;
    }

    /**
     * Set the adapted properties from the array
     *
     * @param properties
     */
    public void setProperties(ServiceXmlConfigPropertyAdapted[] properties) {
        this.properties = properties;
    }
}
