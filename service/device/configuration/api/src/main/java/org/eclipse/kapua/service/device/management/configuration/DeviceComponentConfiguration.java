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
 *
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration;

import java.util.Map;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.commons.configuration.metatype.XmlConfigPropertiesAdapter;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;

@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "name",
        "definition",
        "properties"
}, factoryClass = DeviceConfigurationXmlRegistry.class, factoryMethod = "newComponentConfiguration")
public interface DeviceComponentConfiguration
{
    @XmlElement(name = "id")
    public String getId();

    public void setId(String Id);

    @XmlAttribute(name = "name")
    public String getName();

    public void setName(String unescapedComponentName);

    @XmlElement(name = "definition")
    public KapuaTocd getDefinition();

    public void setDefinition(KapuaTocd definition);

    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(XmlConfigPropertiesAdapter.class)
    public Map<String, Object> getProperties();

    public void setProperties(Map<String, Object> properties);
}
