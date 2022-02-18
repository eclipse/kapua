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
 * The {@link TypeDescriptor} defines the index and type of the object(s) which iit is associated with.
 *
 * @since 1.0.0
 */
public class TypeDescriptor {

    private String index;
    private String type;

    /**
     * Empty constructor required by JAXB.
     *
     * @since 1.0.0
     */
    public TypeDescriptor() {
    }

    /**
     * Constuctor.
     *
     * @param index The index of this {@link #getType()}
     * @param type  The type of the object.
     * @since 1.0.0
     */
    public TypeDescriptor(String index, String type) {
        this.index = index;
        this.type = type;
    }

    /**
     * Gets the index name.
     *
     * @return The index name.
     * @since 1.0.0
     */
    public String getIndex() {
        return index;
    }

    /**
     * Sets the index name.
     *
     * @param index The index name.
     * @since 1.0.0
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * Gets the type of the object in the index.
     *
     * @return The type of the object in the index.
     * @since 1.0.0
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the object in the index.
     *
     * @param type The type of the object in the index.
     * @since 1.0.0
     */
    public void setType(String type) {
        this.type = type;
    }

}
