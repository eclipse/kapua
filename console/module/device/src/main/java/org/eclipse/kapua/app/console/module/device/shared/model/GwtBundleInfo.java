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

public class GwtBundleInfo extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7285859217584861659L;

    public void setName(String name) {
        set("name", name);
    }

    public String getName() {
        return (String) get("name");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getVersion() {
        return (String) get("version");
    }
}
