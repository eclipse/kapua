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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.KapuaSerializable;

@XmlRootElement(name = "configurations")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceConfigurationXmlRegistry.class, factoryMethod = "newConfiguration")
public interface DeviceConfiguration extends KapuaSerializable {
    @XmlElement(name = "configuration")
    public <C extends DeviceComponentConfiguration> List<C> getComponentConfigurations();
}
