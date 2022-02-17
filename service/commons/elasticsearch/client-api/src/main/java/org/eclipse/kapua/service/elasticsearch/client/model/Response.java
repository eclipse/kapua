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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.model;

import org.eclipse.kapua.KapuaSerializable;

/**
 * Base {@link Response} definition.
 *
 * @since 1.0.0
 */
public abstract class Response implements KapuaSerializable {

    /**
     * Record id (it should set by the datastore client component)
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
     * The result of the {@link Response}
     *
     * @since 1.0.0
     */
    private boolean result;

    /**
     * Constructor.
     *
     * @param id             the record id.
     * @param typeDescriptor The {@link TypeDescriptor}
     * @since 1.0.0
     */
    protected Response(String id, TypeDescriptor typeDescriptor) {
        setId(id);
        setTypeDescriptor(typeDescriptor);
    }

    /**
     * Gets the object id (the subject of the operation).
     *
     * @return The object id (the subject of the operation).
     * @since 1.0.0
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the object id (the subject of the operation).
     *
     * @param id The object id (the subject of the operation).
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
     * Gets the result condition.
     *
     * @return The result condition.
     * @since 1.0.0
     */
    public boolean isResult() {
        return result;
    }

    /**
     * Sets the result condition.
     *
     * @param result The result condition.
     * @since 1.0.0
     */
    public void setResult(boolean result) {
        this.result = result;
    }

}
