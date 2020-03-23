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

import org.eclipse.kapua.app.api.core.settings.KapuaApiSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiSettingKeys;

@XmlRootElement(name = "throwableInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ThrowableInfo {

    private int httpErrorCode;
    private String message;
    private String stackTrace;

    protected ThrowableInfo() {
        super();
    }

    public ThrowableInfo(Status httpStatus, Throwable throwable) {
        this.httpErrorCode = httpStatus.getStatusCode();
        this.message = throwable.getMessage();
        // Print stack trace
        boolean showStacktrace = KapuaApiSetting.getInstance().getBoolean(KapuaApiSettingKeys.API_EXCEPTION_STACKTRACE_SHOW, false);
        if (showStacktrace) {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            setStackTrace(stringWriter.toString());
        }

    }

    @XmlElement(name = "httpErrorCode")
    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(Status httpErrorCode) {
        this.httpErrorCode = httpErrorCode.getStatusCode();
    }

    @XmlElement(name = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(name = "stackTrace")
    public String getStackTrace() {
        return stackTrace;
    }

    private void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
