/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Update {@link Response} definition.
 *
 * @since 1.0.0
 */
public class UpdateResponse extends Response {

    /**
     * The result description.
     *
     * @since 1.0.0
     */
    private String description;

    /**
     * The update exception, if occurred.
     *
     * @since 1.0.0
     */
    private Exception exception;

    /**
     * Positive result constructor (result true)
     *
     * @param id             The id of the result.
     * @param typeDescriptor The {@link TypeDescriptor}.
     * @since 1.0.0
     */
    public UpdateResponse(String id, TypeDescriptor typeDescriptor) {
        super(id, typeDescriptor);
        setResult(true);
    }

    /**
     * Negative result constructor (result false)
     *
     * @param id             The id of the result.
     * @param typeDescriptor The {@link TypeDescriptor}
     * @param description    The result description of the failure.
     * @since 1.0.0
     */
    public UpdateResponse(String id, TypeDescriptor typeDescriptor, String description) {
        this(id, typeDescriptor);
        setResult(false);
        this.description = description;
    }

    /**
     * Gets the result description.
     *
     * @return The result description.
     * @since 1.0.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the result description.
     *
     * @param description The result description.
     * @since 1.0.0
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the update exception, if occurred.
     *
     * @return The update exception, if occurred.
     * @since 1.3.0
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * Sets the update exception, if occurred.
     *
     * @param exception The update exception, if occurred.
     * @since 1.3.0
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

}
