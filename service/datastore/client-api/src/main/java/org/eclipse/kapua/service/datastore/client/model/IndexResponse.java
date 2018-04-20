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
 * Index find response
 * 
 * @since 1.0
 */
public class IndexResponse {

    private boolean indexExists;
    private String[] indexes;

    /**
     * Default constructor
     * 
     * @param indexExists
     */
    public IndexResponse(boolean indexExists) {
        this.indexExists = indexExists;
    }

    public IndexResponse(String[] indexes) {
        this.indexes = indexes;
    }

    /**
     * Get the index exists
     * 
     * @return
     */
    public boolean isIndexExists() {
        return indexExists;
    }

    public String[] getIndexes() {
        return indexes;
    }

}
