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
package org.eclipse.kapua.app.api.v1.resources;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * 
 * @author alberto.codutti
 *
 */
public abstract class AbstractKapuaResource {

    protected static final String DEFAULT_SCOPE_ID = "_"; // KapuaApiSetting.getInstance().getString(KapuaApiSettingKeys.API_PATH_PARAM_SCOPEID_WILDCARD);

    /**
     * Checks if the given entity is {@code null}.
     * If it is {@code null} a Responde
     * @param entity
     * @return The entity given if not {@code null}.
     * @throws
     */
    protected <T> T returnNotNullEntity(T entity) {
        if (entity == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return entity;
    }

    /**
     * Builds a 204 HTTP Response.
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
