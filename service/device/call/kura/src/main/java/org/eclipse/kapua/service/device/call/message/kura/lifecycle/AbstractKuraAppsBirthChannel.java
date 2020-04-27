/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.device.call.message.lifecycle.DeviceLifecycleChannel;

/**
 * {@code abstract} base class for {@link KuraAppsChannel} and {@link KuraBirthChannel}.
 * <p>
 * {@link KuraAppsChannel} and {@link KuraBirthChannel} have the same format.
 *
 * @since 1.0.0
 */
public abstract class AbstractKuraAppsBirthChannel extends AbstractKuraLifecycleChannel implements DeviceLifecycleChannel {

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId.
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public AbstractKuraAppsBirthChannel(String messageClassification, String scopeNamespace, String clientId) {
        super(messageClassification, scopeNamespace, clientId);
    }
}
