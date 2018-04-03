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
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.kapua.app.api.core.settings.KapuaApiSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiSettingKeys;

@XmlRootElement(name = "throwableInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrowableInfo {

    @XmlElement(name = "httpErrorCode")
    private int httpErrorCode;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "stackTrace")
    private String stackTrace;

    @XmlTransient
    private final boolean showStacktrace = KapuaApiSetting.getInstance().getBoolean(KapuaApiSettingKeys.API_EXCEPTION_STACKTRACE_SHOW, false);

    protected ThrowableInfo() {
        super();
    }

    public ThrowableInfo(Status httpStatus, Throwable throwable) {
        this.httpErrorCode = httpStatus.getStatusCode();
        this.message = throwable.getMessage();
        // Print stack trace
        if (showStacktrace) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            setStackTrace(stringWriter.toString());
        }

    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(Status httpErrorCode) {
        this.httpErrorCode = httpErrorCode.getStatusCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    private void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
