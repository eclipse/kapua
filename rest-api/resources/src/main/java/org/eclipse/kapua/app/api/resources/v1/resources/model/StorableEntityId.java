/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.service.storable.model.Storable;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import javax.ws.rs.PathParam;

/**
 * {@link StorableId} implementation to be used on REST API to parse the {@link PathParam} entityId.
 *
 * @since 1.0.0
 */
public class StorableEntityId implements StorableId {

    private String id;

    /**
     * Builds the {@link Storable} from the given {@link String} representation.
     *
     * @param id The string storable id to parse.
     * @since 1.0.0
     */
    public StorableEntityId(String id) {
        setId(id);
    }

    public StorableEntityId() {
    }

    @Override
    public String toString() {
        return id;
    }

    public void setId(String storableId) {
        this.id = storableId;
    }

    public String getId() {
        return id;
    }
}
