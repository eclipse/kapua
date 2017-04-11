/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Device assets list entity definition.
 *
 * @since 1.0
 *
 */
@XmlType(propOrder = { "assets" }, factoryClass = DeviceAssetXmlRegistry.class, factoryMethod = "newAssetListResult")
@XmlRootElement(name = "assets")
public interface DeviceAssets {

    /**
     * Get the device assets list
     *
     * @return
     */
    @XmlElement(name = "asset", namespace = "http://eurotech.com/esf/2.0")
    public List<DeviceAsset> getAssets();
}
