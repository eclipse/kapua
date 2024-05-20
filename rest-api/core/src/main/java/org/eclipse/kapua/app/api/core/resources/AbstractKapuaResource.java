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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.app.api.core.model.StorableEntityId;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URI;

/**
 * @author alberto.codutti
 */
public abstract class AbstractKapuaResource {

    /**
     * Checks if the given entity is {@code null}.
     * If it is <code>null</code> a {@link WebApplicationException} is raised.
     *
     * @param entity The {@link KapuaEntity} to check.
     * @return The entity given if not <code>null</code>.
     * @throws WebApplicationException with {@link Status#NOT_FOUND} if the entity is <code>null</code>.
     * @deprecated Since 2.0.0. To leverage {@link ExceptionMapper}s we need to throw {@link org.eclipse.kapua.KapuaEntityNotFoundException}. Please make use of {@link #returnNotNullEntity(Object, String, KapuaId)}
     * @throws WebApplicationException if given entity is {@code null}
     * @since 1.0.0
     */
    @Deprecated
    public <T> T returnNotNullEntity(T entity) {
        if (entity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return entity;
    }

    /**
     * Checks id the given {@link KapuaEntity} is {@code null}.
     * <p>
     * It replaces {@link #returnNotNullEntity(Object)} which was throwing {@link WebApplicationException} instead of {@link KapuaEntityNotFoundException} which is handled by {@link ExceptionMapper}s.
     *
     * @param entity The {@link KapuaEntity} to check.
     * @param entityType The {@link KapuaEntity#getType()}
     * @param entityId The {@link KapuaEntity#getId()}
     * @return The given entity if not {@code null}
     * @param <T> The {@link KapuaEntity} type.
     * @throws KapuaEntityNotFoundException if given {@link KapuaEntity} is {@code null}.
     * @since 2.0.0
     */
    public <T extends KapuaEntity> T returnNotNullEntity(T entity, String entityType, KapuaId entityId) throws KapuaEntityNotFoundException {
        if (entity == null) {
            throw new KapuaEntityNotFoundException(entityType, entityId);
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
