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

import org.eclipse.kapua.KapuaIllegalNullArgumentException;

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

    protected IllegalNullArgumentExceptionInfo() {
        super();
    }

    public IllegalNullArgumentExceptionInfo(Status httpStatus, KapuaIllegalNullArgumentException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);

        setArgumentName(kapuaException.getArgumentName());
    }

    public String getArgumenName() {
        return argumentName;
    }

    private void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }
}
