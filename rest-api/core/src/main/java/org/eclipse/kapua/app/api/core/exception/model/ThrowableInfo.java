/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSettingKeys;

@XmlRootElement(name = "throwableInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ThrowableInfo {

    private static final boolean SHOW_STACKTRACE = KapuaApiCoreSetting.getInstance().getBoolean(KapuaApiCoreSettingKeys.API_EXCEPTION_STACKTRACE_SHOW, false);

    @XmlElement(name = "httpErrorCode")
    private int httpErrorCode;

    @XmlElement(name = "message")
    private String message;

    @XmlElement(name = "stackTrace")
    private String stackTrace;

    protected ThrowableInfo() {
        super();
    }

    public ThrowableInfo(Status httpStatus, Throwable throwable) {
        this.httpErrorCode = httpStatus.getStatusCode();
        if (throwable != null) {
            this.message = throwable.getMessage();
            // Print stack trace
            if (SHOW_STACKTRACE) {
                StringWriter stringWriter = new StringWriter();
                throwable.printStackTrace(new PrintWriter(stringWriter));
                setStackTrace(stringWriter.toString());
            }
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
