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
package org.eclipse.kapua.app.api.v1.resources.model;

import java.math.BigInteger;
import java.util.Base64;

import javax.ws.rs.PathParam;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.datastore.model.Storable;
import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * {@link KapuaId} implementation to be used on REST API to parse the {@link PathParam} entityId.
 * 
 * @since 1.0.0
 */
public class StorableEntityId implements StorableId {

    private static final long serialVersionUID = 6893262093856905182L;

    private String id;

    /**
     * Builds the {@link Storable} from the given {@link String} representation.
     * 
     * @param id
     *            The string storable id to parse.
     * @since 1.0.0
     */
    public StorableEntityId(String id) {
        setId(id);
    }

    @Override
    public String toString() {
        return id;
    }
    
    private void setId(String storableId) {
        this.id = storableId;
    }
}
