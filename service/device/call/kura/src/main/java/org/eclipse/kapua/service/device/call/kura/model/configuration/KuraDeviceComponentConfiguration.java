/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.kura.model.configuration;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.commons.configuration.metatype.TocdImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;

/**
 * Describes the configuration of an OSGi Component.
 * The Component configuration groups all the information related to the configuration of a Component.
 * It provides access to parsed ObjectClassDefintion associated to this Component.
 * The configuration does not reuse the OSGi ObjectClassDefinition as the latter
 * does not provide access to certain aspects such as the required attribute,
 * the min and max values. Instead it returns the raw ObjectClassDefintion as parsed
 * from the MetaType Information XML resource associated to this Component.
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraDeviceComponentConfiguration
{
    /**
     * The PID (service's persistent identity) of the OSGi Component
     * associated to this configuration.
     * The service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     */
    @XmlAttribute(name = "pid")
    private String componentId;

    /**
     * The raw ObjectClassDefinition as parsed from the MetaType
     * Information XML resource associated to this Component.
     */
    @XmlElementRef(type = KapuaTocd.class)
    private TocdImpl definition;

    /**
     * The Dictionary of properties currently used by this component.
     */
    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(KuraXmlConfigPropertiesAdapter.class)
    private Map<String, Object> properties;

    // Required by JAXB
    public KuraDeviceComponentConfiguration()
    {}

    public String getComponentId()
    {
        return componentId;
    }

    public void setComponentId(String componentId)
    {
        this.componentId = componentId;
    }

    public KapuaTocd getDefinition()
    {
        return definition;
    }

    public void setDefinition(KapuaTocd definition)
    {
        this.definition = (TocdImpl) definition;
    }

    public Map<String, Object> getProperties()
    {
        return properties;
    }

    public void setProperties(Map<String, Object> properties)
    {
        this.properties = properties;
    }
}
