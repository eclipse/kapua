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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

import org.eclipse.kapua.model.config.metatype.KapuaTocd;

/**
 * Service component configuration entity definition.
 *
 * @since 1.0
 */
@XmlRootElement(name = "configuration")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "id",
        "name",
        "definition",
        "properties"
}, factoryClass = ServiceConfigurationXmlRegistry.class, factoryMethod = "newComponentConfiguration")
public interface ServiceComponentConfiguration {

    /**
     * Get service configuration component identifier
     *
     * @return
     */
    @XmlElement(name = "id")
    String getId();

    /**
     * Set service configuration component identifier
     *
     * @param id
     */
    void setId(String id);

    /**
     * Get service configuration component name
     *
     * @return
     */
    @XmlAttribute(name = "name")
    String getName();

    /**
     * Set service configuration component name
     *
     * @param unescapedComponentName
     */
    void setName(String unescapedComponentName);

    /**
     * Get service configuration component definition
     *
     * @return
     */
    @XmlElement(name = "definition")
    KapuaTocd getDefinition();

    /**
     * Set service configuration component definition
     *
     * @param definition
     */
    void setDefinition(KapuaTocd definition);

    /**
     * Get service configuration component properties
     *
     * @return
     */
    @XmlElement(name = "properties")
    @XmlJavaTypeAdapter(ServiceXmlConfigPropertiesAdapter.class)
    Map<String, Object> getProperties();

    /**
     * Set service configuration component properties
     *
     * @param properties
     */
    void setProperties(Map<String, Object> properties);
}
