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
import org.eclipse.kapua.KapuaException;

public class KapuaExceptionInfo extends ThrowableInfo {

    @XmlElement(name = "kapuaErrorCode")
    private String kapuaErrorCode;

    public KapuaExceptionInfo() {
    }

    public KapuaExceptionInfo(Status httpStatus, KapuaErrorCode kapuaErrorCode, KapuaException exception) {
        super(httpStatus, exception);
        setKapuaErrorCode(kapuaErrorCode);
    }

    public String getKapuaErrorCode() {
        return kapuaErrorCode;
    }

    private void setKapuaErrorCode(KapuaErrorCode kapuaErrorCode) {
        this.kapuaErrorCode = kapuaErrorCode.name();
    }
}
