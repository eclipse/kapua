/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.assets;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GwtDeviceAsset extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 5527880046980004171L;

    private static final String CHANNELS = "channels";

    public GwtDeviceAsset() {
        set(CHANNELS, new ArrayList<GwtDeviceAssetChannel>());
    }

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getDescription() {
        return get("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public List<GwtDeviceAssetChannel> getChannels() {
        return get(CHANNELS);
    }

    public void setChannels(List<GwtDeviceAssetChannel> channels) {
        set(CHANNELS, channels);
    }

    public GwtDeviceAssetChannel getChannel(String name) {
        for (GwtDeviceAssetChannel channel : getChannels()) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        return null;
    }
}
