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

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.kapua.KapuaEntityUniquenessException;

@XmlRootElement(name = "entityUniquenessExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityUniquenessExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "entityType")
    private String entityType;

    protected EntityUniquenessExceptionInfo() {
        super();
    }

    public EntityUniquenessExceptionInfo(Status httpStatus, KapuaEntityUniquenessException kapuaException) {
        super(httpStatus, kapuaException.getCode(), kapuaException);

        setEntityType(kapuaException.getEntityType());
    }

    public String getEntityType() {
        return entityType;
    }

    private void setEntityType(String entityType) {
        this.entityType = entityType;
    }

}
