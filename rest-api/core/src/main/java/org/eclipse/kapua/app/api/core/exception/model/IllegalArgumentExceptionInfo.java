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

import org.eclipse.kapua.KapuaIllegalArgumentException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "illegalArgumentExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class IllegalArgumentExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "argumentName")
    private String argumentName;

    @XmlElement(name = "argumentValue")
    private String argumentValue;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected IllegalArgumentExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus                    The {@link Status} of the {@link Response}
     * @param kapuaIllegalArgumentException The root exception.
     * @since 1.0.0
     */
    public IllegalArgumentExceptionInfo(Status httpStatus, KapuaIllegalArgumentException kapuaIllegalArgumentException) {
        super(httpStatus, kapuaIllegalArgumentException);

        this.argumentName = kapuaIllegalArgumentException.getArgumentName();
        this.argumentValue = kapuaIllegalArgumentException.getArgumentValue();
    }

    /**
     * Gets the {@link KapuaIllegalArgumentException#getArgumentName()}.
     *
     * @return The {@link KapuaIllegalArgumentException#getArgumentName()}.
     * @since 1.0.0
     */
    public String getArgumenName() {
        return argumentName;
    }

    /**
     * Gets the {@link KapuaIllegalArgumentException#getArgumentValue()}.
     *
     * @return The {@link KapuaIllegalArgumentException#getArgumentValue()}.
     * @since 1.0.0
     */
    public String getArgumentValue() {
        return argumentValue;
    }

}
