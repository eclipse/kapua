/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

    private static final long serialVersionUID = 5527880046980004171L;

    public GwtDeviceAssets() {
        set("assets", new ArrayList<GwtDeviceAssets>());
    }

    public List<GwtDeviceAsset> getAssets() {
        return get("assets");
    }

    public void setAssets(List<GwtDeviceAsset> assets) {
        set("assets", assets);
    }

}
