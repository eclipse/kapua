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
package org.eclipse.kapua.service.device.call.message.kura.event.configuration;

import org.eclipse.kapua.service.device.call.kura.Kura;
import org.eclipse.kapua.service.device.call.kura.model.configuration.KuraDeviceConfiguration;
import org.eclipse.kapua.service.device.call.message.app.event.DeviceManagementEventChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

import java.util.Arrays;
import java.util.List;

/**
 * {@link KuraDeviceConfiguration} {@link DeviceManagementEventChannel} {@link Kura} implementation.
 *
 * @since 2.0.0
 */
public class KuraConfigurationEventChannel extends KuraChannel implements DeviceManagementEventChannel {

    private String appName;
    private String appVersion;
    private String[] resources;

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 2.0.0
     */
    public KuraConfigurationEventChannel(String messageClassification, String scopeNamespace, String clientId) {
        this.messageClassification = messageClassification;
        this.scopeNamespace = scopeNamespace;
        this.clientId = clientId;
    }

    @Override
    public String getAppName() {
        return appName;
    }

    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String getAppVersion() {
        return appVersion;
    }

    @Override
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
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
        parts.add(getAppName());
        parts.add(getAppVersion());
        parts.addAll(Arrays.asList(getResources()));
        return parts;
    }
}
