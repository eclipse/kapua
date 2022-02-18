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
package org.eclipse.kapua.service.device.call.message.kura.app.notification;

import org.eclipse.kapua.service.device.call.message.app.notification.DeviceNotifyChannel;
import org.eclipse.kapua.service.device.call.message.kura.app.KuraAppChannel;

import java.util.Arrays;
import java.util.List;

/**
 * {@link DeviceNotifyChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public class KuraNotifyChannel extends KuraAppChannel implements DeviceNotifyChannel {

    /**
     * The notification resource.
     *
     * @since 1.0.0
     */
    private String[] resources;

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public KuraNotifyChannel(String messageClassification, String scopeNamespace, String clientId) {
        this.messageClassification = messageClassification;
        this.scopeNamespace = scopeNamespace;
        this.clientId = clientId;
    }

    @Override
    public String[] getResources() {
        if (resources == null) {
            resources = new String[0];
        }

        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }

    @Override
    public List<String> getParts() {
        List<String> parts = super.getParts();
        parts.addAll(Arrays.asList(getResources()));
        return parts;
    }
}
