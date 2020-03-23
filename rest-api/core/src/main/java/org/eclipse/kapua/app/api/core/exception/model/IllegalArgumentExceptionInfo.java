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

import org.eclipse.kapua.KapuaIllegalArgumentException;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "illegalArgumentExceptionInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class IllegalArgumentExceptionInfo extends ExceptionInfo {

    private String argumentName;
    private String argumentValue;

    protected IllegalArgumentExceptionInfo() {
        super();
    }

    public IllegalArgumentExceptionInfo(Status httpStatus, KapuaIllegalArgumentException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);

        setArgumentName(kapuaException.getArgumentName());
        setArgumentValue(kapuaException.getArgumentValue());
    }

    @XmlElement(name = "argumentName")
    public String getArgumenName() {
        return argumentName;
    }

    private void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }

    @XmlElement(name = "argumentValue")
    public String getArgumentValue() {
        return argumentValue;
    }

    private void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
    }

}
