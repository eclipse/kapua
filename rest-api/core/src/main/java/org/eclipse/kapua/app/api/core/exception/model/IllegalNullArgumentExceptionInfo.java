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

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.KapuaIllegalNullArgumentException;

@XmlRootElement(name = "illegalNullArgumentExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class IllegalNullArgumentExceptionInfo extends AbstractInfo {

    @XmlElement(name = "argumentName")
    private String argumentName;

    protected IllegalNullArgumentExceptionInfo() {
        super();
    }

    public IllegalNullArgumentExceptionInfo(Status httpStatus, KapuaIllegalNullArgumentException kapuaException) {
        super(httpStatus, kapuaException.getCode());

        setArgumentName(kapuaException.getArgumentName());
    }

    public String getArgumenName() {
        return argumentName;
    }

    private void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }
}
