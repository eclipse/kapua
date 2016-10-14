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
package org.eclipse.kapua.service.device.management.bundle;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "bundle")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(factoryClass = DeviceBundleXmlRegistry.class,
         factoryMethod = "newDeviceBundle")
public interface DeviceBundle
{
    @XmlElement(name = "id")
    public long getId();

    public void setId(long id);

    @XmlElement(name = "name")
    public String getName();

    public void setName(String name);

    @XmlElement(name = "state")
    public String getState();

    public void setState(String state);

    @XmlElement(name = "version")
    public String getVersion();

    public void setVersion(String version);

}
