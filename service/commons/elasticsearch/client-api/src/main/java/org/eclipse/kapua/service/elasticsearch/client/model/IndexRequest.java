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
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.model;

/**
 * Index request definition.
 *
 * @since 1.0.0
 */
public class IndexRequest {

    private String index;

    /**
     * Constructor.
     *
     * @param index The index name.
     * @since 1.0.0
     */
    public IndexRequest(String index) {
        this.index = index;
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
     * @since 1.3.0
     */
    public void setIndex(String index) {
        this.index = index;
    }
}
