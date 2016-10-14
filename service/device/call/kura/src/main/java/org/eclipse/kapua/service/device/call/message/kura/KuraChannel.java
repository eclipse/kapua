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
package org.eclipse.kapua.service.device.call.message.kura;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.service.device.call.message.DeviceChannel;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSetting;
import org.eclipse.kapua.service.device.call.message.kura.setting.DeviceCallSettingKeys;

public class KuraChannel implements DeviceChannel
{
    protected static final String DESTINATION_CONTROL_PREFIX = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);

    protected String              messageClassification;
    protected String              scopeNamespace;
    protected String              clientId;

    public KuraChannel()
    {
    }

    public KuraChannel(String scopeNamespace, String clientId)
    {
        this(null, scopeNamespace, clientId);
    }

    public KuraChannel(String messageClassification, String scopeNamespace, String clientId)
    {
        this.messageClassification = messageClassification;
        this.scopeNamespace = scopeNamespace;
        this.clientId = clientId;
    }

    public String getMessageClassification()
    {
        return messageClassification;
    }

    public void setMessageClassification(String messageClassification)
    {
        this.messageClassification = messageClassification;
    }

    public String getScope()
    {
        return scopeNamespace;
    }

    public void setScope(String scope)
    {
        this.scopeNamespace = scope;
    }

    public String getClientId()
    {
        return clientId;
    }

    @Override
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    public List<String> getSemanticChannelParts()
    {
        return new ArrayList<>();
    }
}
