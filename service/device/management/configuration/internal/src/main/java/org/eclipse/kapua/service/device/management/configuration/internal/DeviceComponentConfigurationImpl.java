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
package org.eclipse.kapua.service.device.management.configuration.internal;

import java.util.Map;

import org.eclipse.kapua.commons.configuration.metatype.TocdImpl;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;

/**
 * Device component configuration entity implementation.
 *
 * @since 1.0
 */
public class DeviceComponentConfigurationImpl implements DeviceComponentConfiguration {

    private String id;
    private String name;
    private TocdImpl definition;
    private Map<String, Object> properties;

    /**
     * Constructor
     */
    public DeviceComponentConfigurationImpl() {
    }

    /**
     * Constructor
     *
     * @param id
     */
    public DeviceComponentConfigurationImpl(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDefinition(KapuaTocd definition) {
        this.definition = (TocdImpl) definition;
    }

    @Override
    public KapuaTocd getDefinition() {
        return definition;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
