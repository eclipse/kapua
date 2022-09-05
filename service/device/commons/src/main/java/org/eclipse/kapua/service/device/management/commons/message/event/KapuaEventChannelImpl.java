/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.commons.message.event;

import org.eclipse.kapua.service.device.management.message.event.KapuaManagementEventChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link KapuaManagementEventChannel} implementation.
 *
 * @since 2.0.0
 */
public class KapuaEventChannelImpl implements KapuaManagementEventChannel {

    private String appName;

    private String appVersion;
    private String[] resources;

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
        return resources;
    }

    @Override
    public void setResources(String[] resources) {
        this.resources = resources;
    }

    @Override
    public List<String> getSemanticParts() {
        List<String> semanticParts = new ArrayList<>();
        semanticParts.add(getAppName());
        semanticParts.add(getAppVersion());

        if (getResources() != null) {
            semanticParts.addAll(Arrays.asList(getResources()));
        }

        return semanticParts;
    }

    @Override
    public void setSemanticParts(List<String> semanticParts) {
        throw new UnsupportedOperationException();
    }
}
