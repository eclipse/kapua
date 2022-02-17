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

import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.shiro.exception.SubjectUnauthorizedException;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "subjectUnauthorizedExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubjectUnauthorizedExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "permission")
    private Permission permission;

    protected SubjectUnauthorizedExceptionInfo() {
        super();
    }

    public SubjectUnauthorizedExceptionInfo(Status httpStatus, SubjectUnauthorizedException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);

        setPermission(kapuaException.getPermission());
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
