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
package org.eclipse.kapua.app.console.module.data.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.Date;

public class GwtDatastoreAsset extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 5356482049849648552L;

    public GwtDatastoreAsset(String asset, String driver, String semanticTopic, Date timestamp) {
        set("asset", asset);
        set("driver", driver);
        set("semanticTopic", semanticTopic);
        set("timestamp", timestamp);
    }

    public GwtDatastoreAsset() {
    }

    public String getAsset() {
        return get("asset");
    }

    public String getDriver() {
        return get("driver");
    }

    public String getTopick() {
        return get("semanticTopic");
    }

    public Date getTimestamp() {
        return (Date) get("timestamp");
    }
}
