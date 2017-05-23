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
 * Update response
 *
 * @since 1.0
 */
public class UpdateResponse extends Response {

    /**
     * Result description
     */
    private String description;
    /**
     * Update exception
     */
    private Throwable t;

    /**
     * Positive result constructor (result true)
     * 
     * @param id
     * @param typeDescriptor
     */
    public UpdateResponse(String id, TypeDescriptor typeDescriptor) {
        super(id, typeDescriptor);
        setResult(true);
    }

    /**
     * Negative result constructor (result false)
     * 
     * @param id
     * @param typeDescriptor
     * @param description
     */
    public UpdateResponse(String id, TypeDescriptor typeDescriptor, String description) {
        this(id, typeDescriptor);
        setResult(false);
        this.description = description;
    }

    /**
     * Get the update description (may be an error condition)
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the update description (may be an error condition)
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the throwable (if an error occurred)
     * 
     * @return
     */
    public Throwable getT() {
        return t;
    }

    /**
     * Set the throwable (if an error occurred)
     * 
     * @param t
     */
    public void setT(Throwable t) {
        this.t = t;
    }

}
