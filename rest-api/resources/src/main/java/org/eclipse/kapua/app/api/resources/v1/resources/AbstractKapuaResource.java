/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.kapua.model.KapuaEntity;

/**
 * 
 * @author alberto.codutti
 *
 */
public abstract class AbstractKapuaResource {

    protected static final String DEFAULT_SCOPE_ID = "_"; // KapuaApiSetting.getInstance().getString(KapuaApiSettingKeys.API_PATH_PARAM_SCOPEID_WILDCARD);

    /**
     * Checks if the given entity is {@code null}.
     * If it is <code>null</code> a {@link WebApplicationException} is raised.
     * 
     * @param entity
     *            The {@link KapuaEntity} to check.
     * @return The entity given if not <code>null</code>.
     * @throws WebApplicationException
     *             with {@link Status#NOT_FOUND} if the entity is <code>null</code>.
     * @since 1.0.0
     */
    protected <T> T returnNotNullEntity(T entity) {
        if (entity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return entity;
    }

    /**
     * Builds a 200 HTTP Response.
     * 
     * <pre>
     * return javax.ws.rs.core.Response.ok().build();
     * </pre>
     * 
     * @return A build {@link Response#ok()}
     * @since 1.0.0
     */
    protected Response returnOk() {
        return Response.ok().build();
    }

}
