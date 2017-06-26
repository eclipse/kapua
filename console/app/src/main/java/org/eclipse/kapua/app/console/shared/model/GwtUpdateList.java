/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import org.eclipse.kapua.app.console.commons.shared.models.KapuaBaseModel;

import java.io.Serializable;

public class GwtUpdateList extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7953082858219194327L;

    public GwtUpdateList() {}

    public String getCurrentVersion() {
        return get("currentVersion");
    }

    public void setCurrentVersion(String currentVersion) {
        set("currentVersion", currentVersion);
    }

    public String getVersion() {
        return get("version");
    }

    public void setVersion(String version) {
        set("version", version);
    }

    public String getRequisite() {
        return get("requisite");
    }

    public void setRequisite(String requisite) {
        set("requisite", requisite);
    }
}
