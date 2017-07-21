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
 * Base request object
 * 
 * @since 1.0
 */
public abstract class Request {

    /**
     * Object identifier
     */
    private String id;
    /**
     * index/type descriptor
     */
    private TypeDescriptor typeDescriptor;
    /**
     * Object to be stored
     */
    private Object storable;

    /**
     * Default constructor
     * 
     * @param typeDescriptor
     *            index/type descriptor
     * @param storable
     */
    protected Request(String id, TypeDescriptor typeDescriptor, Object storable) {
        this.id = id;
        this.typeDescriptor = typeDescriptor;
        this.storable = storable;
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
     * Get the storable object (Object to be inserted/updated)
     * 
     * @return
     */
    public Object getStorable() {
        return storable;
    }

    /**
     * Set the storable object (Object to be inserted/updated)
     * 
     * @param storable
     */
    public void setStorable(Object storable) {
        this.storable = storable;
    }

}
