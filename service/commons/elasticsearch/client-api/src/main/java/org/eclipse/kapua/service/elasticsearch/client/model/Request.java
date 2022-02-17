/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Base {@link Request} definition.
 *
 * @since 1.0.0
 */
public abstract class Request {

    /**
     * The Object identifier
     *
     * @since 1.0.0
     */
    private String id;

    /**
     * The {@link TypeDescriptor}.
     *
     * @since 1.0.0
     */
    private TypeDescriptor typeDescriptor;

    /**
     * The Object of the {@link Request}
     */
    private Object storable;

    /**
     * Constructor.
     *
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @param storable       the objetc of the request
     * @since 1.0.0
     */
    protected Request(String id, TypeDescriptor typeDescriptor, Object storable) {
        setId(id);
        setTypeDescriptor(typeDescriptor);
        setStorable(storable);
    }

    /**
     * Gets the object id.
     *
     * @return The object id.
     * @since 1.0.0
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the object id.
     *
     * @param id The object id.
     * @since 1.0.0
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the {@link TypeDescriptor}.
     *
     * @return The {@link TypeDescriptor}.
     * @since 1.0.0
     */
    public TypeDescriptor getTypeDescriptor() {
        return typeDescriptor;
    }

    /**
     * Sets the {@link TypeDescriptor}.
     *
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @since 1.0.0
     */
    public void setTypeDescriptor(TypeDescriptor typeDescriptor) {
        this.typeDescriptor = typeDescriptor;
    }

    /**
     * Gets the object of the request.
     *
     * @return The object of the request.
     * @since 1.0.0
     */
    public Object getStorable() {
        return storable;
    }

    /**
     * Sets the object of the request.
     *
     * @param storable The object of the request.
     * @since 1.0.0
     */
    public void setStorable(Object storable) {
        this.storable = storable;
    }

}
