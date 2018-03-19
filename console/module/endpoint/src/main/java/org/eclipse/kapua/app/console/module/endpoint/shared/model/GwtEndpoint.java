/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtEndpoint extends GwtUpdatableEntityModel {

    public String getSchema() {
        return get("schema");
    }

    public void setSchema(String schema) {
        set("schema", schema);
    }

    public String getDns() {
        return get("dns");
    }

    public void setDns(String dns) {
        set("dns", dns);
    }

    public Number getPort() {
        return get("port");
    }

    public void setPort(Number port) {
        set("port", port);
    }

    public boolean getSecure() {
        return get("secure");
    }

    public void setSecure(boolean secure) {
        set("secure", secure);
    }

    @Override
    public String toString() {
        return getSchema() + "://" + getDns() + ":" + getPort();
    }
}
