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
package org.eclipse.kapua.service.device.call.message.kura.lifecycle;

import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

/**
 * Kura device application channel implementation.
 *
 * @since 1.0
 *
 */
public class KuraAppsChannel extends KuraChannel {

    /**
     * Constructor
     */
    public KuraAppsChannel() {
    }

    /**
     * Constructor
     *
     * @param scopeNamespace
     * @param clientId
     */
    public KuraAppsChannel(String scopeNamespace, String clientId) {
        this(null, scopeNamespace, clientId);
    }

    /**
     * Constructor
     *
     * @param messageClassification
     * @param scopeNamespace
     * @param clientId
     */
    public KuraAppsChannel(String messageClassification, String scopeNamespace, String clientId) {
        this.messageClassification = messageClassification;
        this.scopeNamespace = scopeNamespace;
        this.clientId = clientId;
    }

}
