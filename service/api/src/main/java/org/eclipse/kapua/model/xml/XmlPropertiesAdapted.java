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
package org.eclipse.kapua.model.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;

/**
 * A container for XmlConfigPropertyAdapted organized into an array.
 *
 * @since 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class XmlPropertiesAdapted<T extends Enum<T>, V extends XmlPropertyAdapted<T>> {

    @XmlElement(name = "property")
    private Collection<V> properties;

    /**
     * Constructor
     */
    public XmlPropertiesAdapted() {
    }

    /**
     * Get the adapted properties as array
     *
     * @return
     */
    public Collection<V> getProperties() {
        return properties;
    }

    /**
     * Set the adapted properties from the array
     *
     * @param properties
     */
    public void setProperties(Collection<V> properties) {
        this.properties = properties;
    }
}
