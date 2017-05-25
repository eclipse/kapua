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
package org.eclipse.kapua.service.datastore.client.model;

/**
 * Update request
 *
 * @since 1.0
 */
public class UpdateRequest extends Request {

    private String id;

    /**
     * Construct the update request with the provided parameters
     * 
     * @param typeDescriptor
     * @param id
     * @param storable
     */
    public UpdateRequest(TypeDescriptor typeDescriptor, String id, Object storable) {
        super(typeDescriptor, storable);
        this.id = id;
    }

    /**
     * Get the object id
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set the object id
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

}
