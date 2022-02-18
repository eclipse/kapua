/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
