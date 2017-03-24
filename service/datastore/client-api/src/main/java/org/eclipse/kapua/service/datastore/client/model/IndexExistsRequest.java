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
 * Index find request
 * 
 * @since 1.0
 */
public class IndexExistsRequest {

    private String index;

    /**
     * Default constructor
     * 
     * @param index
     */
    public IndexExistsRequest(String index) {
        this.index = index;
    }

    /**
     * Get the index name
     * 
     * @return
     */
    public String getIndex() {
        return index;
    }

}
