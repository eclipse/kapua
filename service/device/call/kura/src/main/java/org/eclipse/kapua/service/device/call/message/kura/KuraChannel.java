/*******************************************************************************
 * Copyright (c) 2011, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.service.device.call.message.DeviceChannel;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DeviceChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 */
public class KuraChannel implements DeviceChannel {

    protected static final String DESTINATION_CONTROL_PREFIX = SystemSetting.getInstance().getMessageClassifier();

    protected String messageClassification;
    protected String scopeNamespace;
    protected String clientId;

    /**
     * Constructor
     */
    public KuraChannel() {
    }

    /**
     * Constructor
     *
     * @param scopeNamespace
     * @param clientId
     */
    public KuraChannel(String scopeNamespace, String clientId) {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     *
     * @param messageClassification
     * @param scopeNamespace
     * @param clientId
     */
    public KuraChannel(String messageClassification, String scopeNamespace, String clientId) {
        setMessageClassification(messageClassification);
        setScope(scopeNamespace);
        setClientId(clientId);
    }

    @Override
    public String getMessageClassification() {
        return messageClassification;
    }

    @Override
    public void setMessageClassification(String messageClassification) {
        this.messageClassification = messageClassification;
    }

    @Override
    public String getScope() {
        return scopeNamespace;
    }

    @Override
    public void setScope(String scope) {
        this.scopeNamespace = scope;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Get the semantic tokens list.<br>
     * The semantic part, of a channel, describes an action or a destination inside a domain (eg a scope identifier and a client identifier)
     *
     * @return
     */
    public List<String> getSemanticChannelParts() {
        return new ArrayList<>();
    }
}
