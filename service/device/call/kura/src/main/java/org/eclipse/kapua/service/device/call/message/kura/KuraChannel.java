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

/**
 * Kura device channel implementation.
 * 
 * @since 1.0
 *
 */
public class KuraChannel implements DeviceChannel
{
    protected static final String DESTINATION_CONTROL_PREFIX = DeviceCallSetting.getInstance().getString(DeviceCallSettingKeys.DESTINATION_MESSAGE_CLASSIFIER);

    protected String              messageClassification;
    protected String              scopeNamespace;
    protected String              clientId;

    /**
     * Constructor
     */
    public KuraChannel()
    {
    }

    /**
     * Constructor
     * 
     * @param scopeNamespace
     * @param clientId
     */
    public KuraChannel(String scopeNamespace, String clientId)
    {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     * 
     * @param messageClassification
     * @param scopeNamespace
     * @param clientId
     */
    public KuraChannel(String messageClassification, String scopeNamespace, String clientId)
    {
        this.messageClassification = messageClassification;
        this.scopeNamespace = scopeNamespace;
        this.clientId = clientId;
    }

    @Override
    public String getMessageClassification()
    {
        return messageClassification;
    }

    @Override
    public void setMessageClassification(String messageClassification)
    {
        this.messageClassification = messageClassification;
    }

    @Override
    public String getScope()
    {
        return scopeNamespace;
    }

    @Override
    public void setScope(String scope)
    {
        this.scopeNamespace = scope;
    }

    @Override
    public String getClientId()
    {
        return clientId;
    }

    @Override
    public void setClientId(String clientId)
    {
        this.clientId = clientId;
    }

    /**
     * Get the semantic tokens list.<br>
     * The semantic part, of a channel, describes an action or a destination inside a domain (eg a scope identifier and a client identifier)
     * 
     * @return
     */
    public List<String> getSemanticChannelParts()
    {
        return new ArrayList<>();
    }
}
