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
package org.eclipse.kapua.app.api.core.exception.model;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.kapua.KapuaErrorCode;

public abstract class AbstractInfo {

    private int httpErrorCode;
    private String kapuaErrorCode;

    public AbstractInfo() {
    }

    public AbstractInfo(Status httpStatus, KapuaErrorCode kapuaErrorCode) {
        setHttpErrorCode(httpStatus);
        setKapuaErrorCode(kapuaErrorCode);
    }

    @XmlElement(name = "httpErrorCode")
    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    private void setHttpErrorCode(Status httpErrorCode) {
        this.httpErrorCode = httpErrorCode.getStatusCode();
    }

    @XmlElement(name = "kapuaErrorCode")
    public String getKapuaErrorCode() {
        return kapuaErrorCode;
    }

    private void setKapuaErrorCode(KapuaErrorCode kapuaErrorCode) {
        this.kapuaErrorCode = kapuaErrorCode.name();
    }

}
