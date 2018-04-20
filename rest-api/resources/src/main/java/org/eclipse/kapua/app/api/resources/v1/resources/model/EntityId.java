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
package org.eclipse.kapua.app.api.resources.v1.resources.model;

import java.math.BigInteger;
import java.util.Base64;

import javax.ws.rs.PathParam;

import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link KapuaId} implementation to be used on REST API to parse the {@link PathParam} entityId.
 *
 * @since 1.0.0
 */
public class EntityId implements KapuaId {

    private static final long serialVersionUID = 6893262093856905182L;

    private BigInteger id;

    /**
     * Builds the {@link KapuaId} from the given {@link String} compact entity id.
     *
     * @param compactEntityId The compact entityId to parse.
     * @since 1.0.0
     */
    public EntityId(String compactEntityId) {
        byte[] bytes = Base64.getUrlDecoder().decode(compactEntityId);
        setId(new BigInteger(bytes));
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    protected void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
