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

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.KapuaErrorCodes;

@XmlRootElement(name = "throwableInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrowableInfo extends AbstractInfo {

    @XmlElement(name = "stackTrace")
    private String stackTrace;

    protected ThrowableInfo() {
        super();
    }

    public ThrowableInfo(Status httpStatus, Throwable throwable) {
        super(httpStatus, KapuaErrorCodes.SEVERE_INTERNAL_ERROR);

        // Print stack trace
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        setStackTrace(stringWriter.toString());
    }

    public String getStackTrace() {
        return stackTrace;
    }

    private void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
