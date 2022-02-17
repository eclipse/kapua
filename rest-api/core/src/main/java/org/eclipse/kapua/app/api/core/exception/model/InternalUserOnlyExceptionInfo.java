/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.authorization.shiro.exception.InternalUserOnlyException;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "internalUserOnlyExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class InternalUserOnlyExceptionInfo extends ExceptionInfo {

    protected InternalUserOnlyExceptionInfo() {
        super();
    }

    public InternalUserOnlyExceptionInfo(Response.Status httpStatus, InternalUserOnlyException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);
    }

}
