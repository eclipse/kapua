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
package org.eclipse.kapua.service.device.management.configuration;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.commons.configuration.metatype.XmlConfigPropertiesAdapter;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;

/**
 * Device component configuration entity definition.
 * 
 * @since 1.0
 *
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "name",
        "definition",
        "properties"
}, factoryClass = DeviceConfigurationXmlRegistry.class, factoryMethod = "newComponentConfiguration")
public interface DeviceComponentConfiguration {

    /**
     * Get device configuration component identifier
     * 
     * @return
     */
    @XmlElement(name = "id")
    public String getId();

    /**
     * Set device configuration component identifier
     * 
     * @param Id
     */
    public void setId(String id);

    /**
     * Get device configuration component name
     * 
     * @return
     */
    @XmlAttribute(name = "name")
    public String getName();

    /**
     * Set device configuration component name
     * 
     * @param unescapedComponentName
     */
    public void setName(String unescapedComponentName);

    /**
     * Get device configuration component definition
     * 
     * @return
     */
    @XmlElement(name = "definition")
    public KapuaTocd getDefinition();

    /**
     * Set device configuration component definition
     * 
     * @param definition
     */
    public void setDefinition(KapuaTocd definition);

    /**
     * Get device configuration component properties
     * 
     * @return
     */
    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(XmlConfigPropertiesAdapter.class)
    public Map<String, Object> getProperties();

    /**
     * Set device configuration component properties
     * 
     * @param properties
     */
    public void setProperties(Map<String, Object> properties);
}
