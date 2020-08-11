/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.device.management.assets;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtDeviceAssets extends KapuaBaseModel implements Serializable {

    private static final String ASSETS = "assets";

    private static final long serialVersionUID = 5527880046980004171L;

    public GwtDeviceAssets() {
        set(ASSETS, new ArrayList<GwtDeviceAssets>());
    }

    public List<GwtDeviceAsset> getAssets() {
        return get(ASSETS);
    }

    public void setAssets(List<GwtDeviceAsset> assets) {
        set(ASSETS, assets);
    }

}
