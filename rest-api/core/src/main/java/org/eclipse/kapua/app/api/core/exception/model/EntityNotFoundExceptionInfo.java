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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.swagger.annotations.ApiModelProperty;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

@XmlRootElement(name = "entityNotFoundExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityNotFoundExceptionInfo extends AbstractInfo {

    @XmlElement(name = "entityType")
    private String entityType;

    @XmlElement(name = "entityId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    @ApiModelProperty(dataType = "string")
    private KapuaId entityId;

    protected EntityNotFoundExceptionInfo() {
        super();
    }

    public EntityNotFoundExceptionInfo(Status httpStatus, KapuaEntityNotFoundException kapuaException) {
        super(httpStatus, kapuaException.getCode());

        setEntityType(kapuaException.getEntityType());
        setEntityId(kapuaException.getEntityId());
    }

    public String getEntityType() {
        return entityType;
    }

    private void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public KapuaId getEntityId() {
        return entityId;
    }

    private void setEntityId(KapuaId entityId) {
        this.entityId = entityId;
    }

}
