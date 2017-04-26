/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.asset.xml;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.model.xml.XmlAdaptedNameTypeValueObject;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;

/**
 * XML friendly {@link DeviceAssetChannel}.
 * 
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlAdaptedDeviceAssetChannel extends XmlAdaptedNameTypeValueObject {

    @XmlElement(name = "mode")
    private DeviceAssetChannelMode mode;

    @XmlElement(name = "error")
    private String error;

    @XmlElement(name = "timestamp")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    private Date timestamp;

    public DeviceAssetChannelMode getMode() {
        return mode;
    }

    public void setMode(DeviceAssetChannelMode mode) {
        this.mode = mode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
