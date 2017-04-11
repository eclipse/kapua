/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.model;

import java.io.Serializable;

public class GwtDatastoreAsset extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 5356482049849648552L;

    public GwtDatastoreAsset(String asset, String driver) {
        set("asset", asset);
        set("driver", driver);
    }

    public String getAsset() {
        return get("asset");
    }

    public String getDriver() {
        return get("driver");
    }
}
