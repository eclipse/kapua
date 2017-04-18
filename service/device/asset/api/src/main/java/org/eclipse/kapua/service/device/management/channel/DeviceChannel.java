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
package org.eclipse.kapua.service.device.management.channel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.xml.ObjectTypeXmlAdapter;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetXmlRegistry;

/**
 * Device channel entity definition.<br>
 * This entity is used to get information about channels installed in the device.
 *
 * @since 1.0
 *
 */
@XmlRootElement(name = "deviceChannel")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceChannelXmlRegistry.class, factoryMethod = "newDeviceChannel")
public interface DeviceChannel<T> {
    
    /**
     * Get the channel name
     *
     * @return
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the channel name
     *
     * @param name
     */
    public void setName(String name);
    
    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    public Class<T> getType();
    
    public void setType(Class<T> type);
    
    @XmlElement(name = "mode")
    public ChannelMode getMode();
    
    public void setMode(ChannelMode channelMode);
    
    @XmlElement(name = "value")
    public T getValue();
    
    public void setValue(T value);    
}
