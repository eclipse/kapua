/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.endpoint.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtEndpoint extends GwtUpdatableEntityModel {

    public enum GwtEndpointSecure implements IsSerializable {
        ANY, TRUE, FALSE;

        private GwtEndpointSecure() {
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X get(String property) {
        if ("secureEnum".equals(property)) {
            return (X) (Boolean.toString(getSecure()));
        } else {
            return super.get(property);
        }
    }

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

    public GwtEndpointSecure gwtEndpointSecureEnum() {
        return (GwtEndpointSecure) get("secureEnum");
    }

    @Override
    public String toString() {
        return getSchema() + "://" + getDns() + ":" + getPort();
    }
}
