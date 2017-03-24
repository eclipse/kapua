/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client.model;

/**
 * Base request object
 * 
 * @since 1.0
 */
public abstract class Request {

    /**
     * index/type descriptor
     */
    private TypeDescriptor typeDescriptor;
    /**
     * Storable object
     */
    private Object storable;

    /**
     * Default constructor
     * 
     * @param typeDescriptor
     *            index/type descriptor
     * @param storable
     */
    protected Request(TypeDescriptor typeDescriptor, Object storable) {
        this.typeDescriptor = typeDescriptor;
        this.storable = storable;
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
