/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.eclipse.kapua.model.KapuaEntity;

import java.net.URI;

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
    public <T> T returnNotNullEntity(T entity) {
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
     * @return A built {@link Response#ok()}
     * @since 1.0.0
     */
    public Response returnOk() {
        return Response.ok().build();
    }

    /**
     * Builds a 200 HTTP Response a content.
     *
     * <pre>
     * return javax.ws.rs.core.Response.ok().entity(entity).build();
     * </pre>
     *
     * @param entity The entity to return.
     * @return A built {@link Response#ok()}
     * @since 1.0.0
     */
    public Response returnOk(Object entity) {
        return Response.ok().entity(entity).build();
    }

    /**
     * Builds a 201 HTTP Response a content.
     *
     * <pre>
     * return javax.ws.rs.core.Response.status(Status.CREATED).entity(entity).build();
     * </pre>
     *
     * @param entity The entity to return.
     * @return A built {@link Response#created(URI)}
     * @since 1.2.0
     */
    public Response returnCreated(Object entity) {
        return Response.status(Status.CREATED).entity(entity).build();
    }

    /**
     * Builds a 204 HTTP Response a content.
     *
     * <pre>
     * return javax.ws.rs.core.Response.noContent().build();
     * </pre>
     *
     * @return A built {@link Response#noContent()}
     * @since 1.2.0
     */
    public Response returnNoContent() {
        return Response.noContent().build();
    }

}
