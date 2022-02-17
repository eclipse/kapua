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

import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSetting;
import org.eclipse.kapua.app.api.core.settings.KapuaApiCoreSettingKeys;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.PrintWriter;
import java.io.StringWriter;

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

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected ThrowableInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus The {@link Status} of the {@link Response}
     * @param throwable  The cause of the error.
     * @since 1.0.0
     */
    public ThrowableInfo(@NotNull Status httpStatus, Throwable throwable) {
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

    /**
     * Gets the {@link Status#getStatusCode()} of the {@link Response}.
     *
     * @return The {@link Status#getStatusCode()} of the {@link Response}.
     * @since 1.0.0
     */
    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    /**
     * Sets the {@link Status#getStatusCode()} of the {@link Response}.
     *
     * @param httpErrorCode The {@link Status#getStatusCode()} of the {@link Response}.
     * @since 1.0.0
     */
    public void setHttpErrorCode(Status httpErrorCode) {
        this.httpErrorCode = httpErrorCode.getStatusCode();
    }

    /**
     * Gets the {@link Throwable#getMessage()}.
     *
     * @return The {@link Throwable#getMessage()}.
     * @since 1.0.0
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the {@link Throwable#getMessage()}.
     *
     * @param message The {@link Throwable#getMessage()}.
     * @since 1.0.0
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the {@link Throwable#getStackTrace()} if {@code api.exception.stacktrace.show} is {@code true}.
     *
     * @return The {@link Throwable#getStackTrace()} if {@code api.exception.stacktrace.show} is {@code true}.
     * @since 1.0.0
     */
    public String getStackTrace() {
        return stackTrace;
    }

    /**
     * Sets the {@link Throwable#getStackTrace()}.
     *
     * @param stackTrace The {@link Throwable#getStackTrace()}.
     * @since 1.0.0
     */
    private void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

}
