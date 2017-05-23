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

import java.util.ArrayList;
import java.util.List;

/**
 * Bulk update response container
 * 
 * @since 1.0
 */
public class BulkUpdateResponse {

    List<UpdateResponse> responseList;

    /**
     * Default constructor
     */
    public BulkUpdateResponse() {
        responseList = new ArrayList<>();
    }

    /**
     * Add an update response to the bulk request
     * 
     * @param response
     */
    public void add(UpdateResponse response) {
        responseList.add(response);
    }

    /**
     * Get the update response list
     * 
     * @return
     */
    public List<UpdateResponse> getResponse() {
        return responseList;
    }

    /**
     * Set the update response list
     * 
     * @param responseList
     */
    public void setResponse(List<UpdateResponse> responseList) {
        this.responseList = responseList;
    }

}
