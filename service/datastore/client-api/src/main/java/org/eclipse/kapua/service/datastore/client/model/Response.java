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

import org.eclipse.kapua.KapuaSerializable;

/**
 * Base response object
 * 
 * @since 1.0
 */
public abstract class Response implements KapuaSerializable {

    /**
     * Record id (it should set by the datastore client component)
     */
    private String id;
    /**
     * Schema and index name
     */
    private TypeDescriptor typeDescriptor;
    private boolean result;

    /**
     * Default constructor
     * 
     * @param id
     * @param typeDescriptor
     *            index/type descriptor
     */
    protected Response(String id, TypeDescriptor typeDescriptor) {
        this.id = id;
        this.typeDescriptor = typeDescriptor;
    }

    /**
     * Get the object id (the subject of the operation)
     * 
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set the object id (the subject of the operation)
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the type descriptor
     * 
     * @return
     */
    public TypeDescriptor getTypeDescriptor() {
        return typeDescriptor;
    }

    /**
     * Set the type descriptor
     * 
     * @param typeDescriptor
     */
    public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
    }

    /**
     * Get the result condition
     * 
     * @return
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Set the result condition
     * 
     * @param result
     */
    public void setResult(boolean result) {
        this.result = result;
    }

}
