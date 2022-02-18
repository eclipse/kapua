/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.endpoint.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.module.endpoint.client.EndpointModel;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GwtEndpoint extends GwtUpdatableEntityModel implements EndpointModel {

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

    @Override
    public String getSchema() {
        return get("schema");
    }

    @Override
    public void setSchema(String schema) {
        set("schema", schema);
    }

    @Override
    public String getDns() {
        return get("dns");
    }

    @Override
    public void setDns(String dns) {
        set("dns", dns);
    }

    @Override
    public Number getPort() {
        return get("port");
    }

    @Override
    public void setPort(Number port) {
        set("port", port);
    }

    @Override
    public boolean getSecure() {
        return get("secure");
    }

    @Override
    public void setSecure(boolean secure) {
        set("secure", secure);
    }

    public GwtEndpointSecure gwtEndpointSecureEnum() {
        return (GwtEndpointSecure) get("secureEnum");
    }

    @Override
    public String getEndpointType() {
        return get("endpointType");
    }

    @Override
    public void setEndpointType(String endpointType) {
        set("endpointType", endpointType);
    }

    @Override
    public String toString() {
        return getSchema() + "://" + getDns() + ":" + getPort();
    }
}
