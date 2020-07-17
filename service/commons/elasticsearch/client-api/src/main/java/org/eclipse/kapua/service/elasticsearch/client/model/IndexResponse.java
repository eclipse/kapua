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
 * Index response definitin.
 *
 * @since 1.0.0
 */
public class IndexResponse {

    private boolean indexExists;
    private String[] indexes;

    /**
     * Constructor.
     *
     * @param indexExists Whether or not the index esists.
     * @since 1.0.0
     */
    public IndexResponse(boolean indexExists) {
        this.indexExists = indexExists;
    }

    /**
     * Constructor.
     *
     * @param indexes The index names.
     * @since 1.0.0
     */
    public IndexResponse(String[] indexes) {
        this.indexes = indexes;
    }

    /**
     * Gets whether or not the index esists.
     *
     * @return {@code true} if index exists, {@code false} otherwise.
     * @since 1.0.0
     */
    public boolean isIndexExists() {
        return indexExists;
    }

    /**
     * Gets the index names.
     *
     * @return The index names.
     * @since 1.0.0
     */
    public String[] getIndexes() {
        return indexes;
    }

}
