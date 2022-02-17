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
package org.eclipse.kapua.service.device.management.asset.xml;

import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.model.xml.XmlAdaptedNameTypeValueObject;
import org.eclipse.kapua.model.xml.XmlAdaptedTypeValueObject;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannel;
import org.eclipse.kapua.service.device.management.asset.DeviceAssetChannelMode;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * {@link DeviceAssetChannel} {@link XmlAdaptedTypeValueObject} implementation.
 *
 * @since 1.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
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
