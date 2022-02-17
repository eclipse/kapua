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

import org.eclipse.kapua.service.authentication.shiro.KapuaAuthenticationException;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mfaRequiredExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class MfaRequiredExceptionInfo extends ExceptionInfo {

    protected MfaRequiredExceptionInfo() {
        super();
    }

    public MfaRequiredExceptionInfo(Response.Status httpStatus, KapuaAuthenticationException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);
    }

}
