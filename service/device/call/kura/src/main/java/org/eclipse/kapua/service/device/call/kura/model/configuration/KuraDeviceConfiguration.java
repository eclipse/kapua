/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A container for a list of OSGi component configurations.
 * 
 * @since 1.0
 * 
 */
@XmlRootElement(name = "configurations")
@XmlAccessorType(XmlAccessType.FIELD)
public class KuraDeviceConfiguration {

    @XmlElement(name = "configuration")
    private List<KuraDeviceComponentConfiguration> configurations;

    // Required by JAXB
    /**
     * Constructor
     */
    public KuraDeviceConfiguration() {
    }

    /**
     * Constructor
     * 
     * @param accountName
     * @param clientId
     */
    public KuraDeviceConfiguration(String accountName, String clientId) {
        this();
        configurations = new ArrayList<>();
    }

    /**
     * Get the device component configuration list
     * 
     * @return
     */
    public List<KuraDeviceComponentConfiguration> getConfigurations() {
        if (configurations == null) {
            configurations = new ArrayList<>();
        }

        return configurations;
    }

    /**
     * Set the device component configuration list
     * 
     * @param configurations
     */
    public void setConfigurations(List<KuraDeviceComponentConfiguration> configurations) {
        this.configurations = configurations;
    }
}
