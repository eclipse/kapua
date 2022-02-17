/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.device.shared.model.management.inventory;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.List;

public class GwtInventoryDeploymentPackage extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7648638193931336835L;

    // Needed to prevent serialization errors
    private GwtInventoryBundle unused;

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return get("name");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getVersion() {
        return get("version");
    }

    public List<GwtInventoryBundle> getBundles() {
        return get("bundles");
    }

    public void setBundles(List<GwtInventoryBundle> bundles) {
        set("bundles", bundles);
    }
}
