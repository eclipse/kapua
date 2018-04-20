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
package org.eclipse.kapua.app.console.module.device.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.List;

public class GwtDeploymentPackage extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7648638193931336835L;

    // Needed to prevent serialization errors
    @SuppressWarnings("unused")
    private GwtBundleInfo unused;

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return (String) get("name");
    }

    public String getUnescapedName() {
        return (String) getUnescaped("name");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getVersion() {
        return (String) get("version");
    }

    public List<GwtBundleInfo> getBundleInfos() {
        return get("bundles");
    }

    public void setBundleInfos(List<GwtBundleInfo> bundleInfos) {
        set("bundles", bundleInfos);
    }
}
