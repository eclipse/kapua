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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * A container for XmlConfigPropertyAdapted organized into an array.
 *
 * @since 1.0
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraXmlConfigPropertiesAdapted {

    @XmlElement(name = "property")
    private XmlConfigPropertyAdapted[] properties;

    /**
     * Constructor
     */
    public KuraXmlConfigPropertiesAdapted() {
    }

    /**
     * Get the adapted configuration properties array
     *
     * @return
     */
    public XmlConfigPropertyAdapted[] getProperties() {
        return properties;
    }

    /**
     * Set the adapted configuration properties array
     *
     * @param properties
     */
    public void setProperties(XmlConfigPropertyAdapted[] properties) {
        this.properties = properties;
    }
}
