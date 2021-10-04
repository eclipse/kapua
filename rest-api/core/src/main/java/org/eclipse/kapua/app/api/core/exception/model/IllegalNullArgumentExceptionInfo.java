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

import org.eclipse.kapua.KapuaIllegalNullArgumentException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "illegalNullArgumentExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class IllegalNullArgumentExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "argumentName")
    private String argumentName;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected IllegalNullArgumentExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus                        The {@link Status} of the {@link Response}
     * @param kapuaIllegalNullArgumentException The root exception.
     * @since 1.0.0
     */
    public IllegalNullArgumentExceptionInfo(Status httpStatus, KapuaIllegalNullArgumentException kapuaIllegalNullArgumentException) {
        super(httpStatus, kapuaIllegalNullArgumentException);

        this.argumentName = kapuaIllegalNullArgumentException.getArgumentName();
    }

    /**
     * Gets the {@link KapuaIllegalNullArgumentException#getArgumentName()}.
     *
     * @return The {@link KapuaIllegalNullArgumentException#getArgumentName()}.
     * @since 1.0.0
     */
    public String getArgumenName() {
        return argumentName;
    }
}
