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
package org.eclipse.kapua.service.device.call.message.kura.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum DeviceCallSettingKeys implements SettingKey
{
    DESTINATION_MESSAGE_CLASSIFIER("destination.message.classifier"),

    DESTINATION_REPLY_PART("destination.reply.part");

    private String key;

    private DeviceCallSettingKeys(String key)
    {
        this.key = key;
    }

    public String key()
    {
        return key;
    }
}
