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
import javax.xml.bind.annotation.XmlAnyElement;
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
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    public String getName();

    /**
     * Set the channel name
     *
     * @param name
     * @since 1.0.0
     */
    public void setName(String name);
    
    /**
     * Gets the type of the channel value.
     * 
     * @return The type of the channel value.
     * @since 1.0.0
     */
    @XmlElement(name = "type")
    @XmlJavaTypeAdapter(ObjectTypeXmlAdapter.class)
    public Class<T> getType();
    
    /**
     * Sets the type of the channel value.
     * @param type The type og the channel value.
     * @since 1.0.0
     */
    public void setType(Class<T> type);
    
    /**
     * Gets the mode of the channel.
     * @return The model of the channel.
     */
    @XmlElement(name = "mode")
    public ChannelMode getMode();
    
    public void setMode(ChannelMode channelMode);
    
    @XmlElement(name = "value")
    @XmlAnyElement
    public T getValue();
    
    public void setValue(T value);
    
    @XmlElement(name = "error")
    public String getError();
    
    public void setError(String error);
        
    @XmlElement(name = "timestamp")
    public Long getTimestamp();
    
    public void setTimestamp(Long timestamp);
}
