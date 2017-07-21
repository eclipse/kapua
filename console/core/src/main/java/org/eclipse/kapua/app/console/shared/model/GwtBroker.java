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
package org.eclipse.kapua.app.console.shared.model;

import org.eclipse.kapua.app.console.commons.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtBroker extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -7953082858219194327L;

    public GwtBroker() {}


    public String getUrl() {
        return get("url");
    }

    public String getUnescapedUrl() {
        return getUnescaped("url");
    }

    public void setUrl(String url) {
        set("url", url);
    }

    public String getHost() {
        return get("host");
    }

    public void setHost(String host) {
        set("host", host);
    }

    public Integer getPort() {
        return get("port");
    }

    public void setPort(Integer port) {
        set("port", port);
    }

    public Boolean isSsl() {
        return get("ssl");
    }

    public void setSsl(Boolean ssl) {
        set("ssl", ssl);
    }

    public String toString() {
        return getUrl();
    }
}
