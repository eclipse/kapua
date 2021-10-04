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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "entityNotFoundExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityNotFoundExceptionInfo extends ExceptionInfo {

    @XmlElement(name = "entityType")
    private String entityType;

    @XmlElement(name = "entityId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    private KapuaId entityId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected EntityNotFoundExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param httpStatus                   The {@link Status} of the {@link Response}
     * @param kapuaEntityNotFoundException The root exception.
     * @since 1.0.0
     */
    public EntityNotFoundExceptionInfo(Status httpStatus, KapuaEntityNotFoundException kapuaEntityNotFoundException) {
        super(httpStatus, kapuaEntityNotFoundException);

        this.entityType = kapuaEntityNotFoundException.getEntityType();
        this.entityId = kapuaEntityNotFoundException.getEntityId();
    }

    /**
     * Gets the {@link KapuaEntityNotFoundException#getEntityType()}.
     *
     * @return The {@link KapuaEntityNotFoundException#getEntityType()}.
     * @since 1.0.0
     */
    public String getEntityType() {
        return entityType;
    }

    /**
     * Gets the {@link KapuaEntityNotFoundException#getEntityId()}.
     *
     * @return The {@link KapuaEntityNotFoundException#getEntityId()}.
     * @since 1.0.0
     */
    public KapuaId getEntityId() {
        return entityId;
    }
}
