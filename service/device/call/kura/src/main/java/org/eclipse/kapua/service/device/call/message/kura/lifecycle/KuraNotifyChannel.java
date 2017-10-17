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
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.app.kura.KuraAppChannel;

/**
 * Kura device notification message channel implementation.
 *
 * @since 1.0
 */
public class KuraNotifyChannel extends KuraAppChannel {

    protected String notifyPart;
    protected String senderClientId;
    protected String resources;

    /**
     * Constructor
     */
    public KuraNotifyChannel() {
    }

    /**
     * Constructor
     *
     * @param topicTokens
     */
    public KuraNotifyChannel(String[] topicTokens) {
        this.messageClassification = topicTokens[0];
        this.scopeNamespace = topicTokens[1];
        this.clientId = topicTokens[2];
        this.appId = topicTokens[3];
        this.notifyPart = topicTokens[4];
        this.senderClientId = topicTokens[5];
        this.resources = topicTokens[6];
    }

    public String getSenderClientId() {
        return senderClientId;
    }

    public void setSenderClientId(String senderClientId) {
        this.senderClientId = senderClientId;
    }

    public String getNotifyPart() {
        return notifyPart;
    }

    public void setNotifyPart(String notifyPart) {
        this.notifyPart = notifyPart;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }
}
