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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionInfo extends ThrowableInfo {

    @XmlElement(name = "kapuaErrorCode")
    private String kapuaErrorCode;

    public ExceptionInfo() {
    }

    public ExceptionInfo(Status httpStatus, KapuaErrorCode kapuaErrorCode, KapuaException exception) {
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
