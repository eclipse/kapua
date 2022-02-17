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

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;
import org.eclipse.kapua.app.console.module.endpoint.client.EndpointModel;

public class GwtEndpointCreator extends GwtEntityCreator implements EndpointModel {

    private String schema;
    private String dns;
    private Number port;
    private boolean secure;
    private String endpointType;

    public GwtEndpointCreator() {
        super();
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public void setSchema(String schema) {
        this.schema = schema;
    }

    @Override
    public String getDns() {
        return dns;
    }

    @Override
    public void setDns(String dns) {
        this.dns = dns;
    }

    @Override
    public Number getPort() {
        return port;
    }

    @Override
    public void setPort(Number port) {
        this.port = port;
    }

    @Override
    public boolean getSecure() {
        return secure;
    }

    @Override
    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String getEndpointType() {
        return endpointType;
    }

    @Override
    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

}
