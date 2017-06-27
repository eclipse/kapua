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

import org.eclipse.kapua.app.console.commons.shared.model.KapuaBaseModel;

import java.io.Serializable;

public class GwtDownloadProtocol extends KapuaBaseModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2477264640452966413L;

    public GwtDownloadProtocol() {
    }

    public GwtDownloadProtocol(String protocol, String protocolName) {
        setProtocol(protocol);
        setProtocolName(protocolName);
    }

    public String getProtocol() {
        return get("protocol");
    }

    public void setProtocol(String protocol) {
        set("protocol", protocol);
    }

    public String getProtocolName() {
        return get("protocolName");
    }

    public void setProtocolName(String protocolName) {
        set("protocolName", protocolName);
    }

}
