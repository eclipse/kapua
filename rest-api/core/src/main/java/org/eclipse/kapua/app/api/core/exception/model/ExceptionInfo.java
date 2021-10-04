/*******************************************************************************
 * Copyright (c) 2017, 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.KapuaErrorCode;
import org.eclipse.kapua.KapuaException;

import javax.ws.rs.core.Response;
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

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected ExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus The {@link Status} of the {@link Response}
     * @param exception  The cause of the error.
     * @since 1.6.0
     */
    public ExceptionInfo(Status httpStatus, KapuaException exception) {
        super(httpStatus, exception);

        setKapuaErrorCode(exception.getCode());
    }

    /**
     * Constructor.
     *
     * @param httpStatus     The {@link Status} of the {@link Response}
     * @param kapuaErrorCode The associated {@link KapuaErrorCode}.
     * @param exception      The cause of the error.
     * @since 1.0.0
     * @deprecated Since 1.6.0. Please make use of {@link #ExceptionInfo(Status, KapuaException)} to avoid misalignment of {@link #getKapuaErrorCode()}.
     */
    @Deprecated
    public ExceptionInfo(Status httpStatus, KapuaErrorCode kapuaErrorCode, KapuaException exception) {
        super(httpStatus, exception);

        setKapuaErrorCode(kapuaErrorCode);
    }

    /**
     * Gets the {@link KapuaException#getCode()}.
     *
     * @return The {@link KapuaException#getCode()}.
     * @since 1.0.0
     */
    public String getKapuaErrorCode() {
        return kapuaErrorCode;
    }

    /**
     * Sets the {@link KapuaException#getCode()}.
     *
     * @param kapuaErrorCode The {@link KapuaException#getCode()}.
     * @since 1.0.0
     */
    private void setKapuaErrorCode(KapuaErrorCode kapuaErrorCode) {
        this.kapuaErrorCode = kapuaErrorCode.name();
    }
}
