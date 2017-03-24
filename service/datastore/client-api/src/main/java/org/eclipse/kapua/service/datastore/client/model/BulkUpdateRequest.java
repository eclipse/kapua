/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
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
 * Bulk update request container
 * 
 * @since 1.0
 */
public class BulkUpdateRequest {

    List<UpdateRequest> requestList;

    /**
     * Default constructor
     */
    public BulkUpdateRequest() {
        requestList = new ArrayList<>();
    }

    /**
     * Add an update request to the bulk request
     * 
     * @param request
     */
    public void add(UpdateRequest request) {
        requestList.add(request);
    }

    /**
     * Get the update request list
     * 
     * @return
     */
    public List<UpdateRequest> getRequest() {
        return requestList;
    }

    /**
     * Set the update request list
     * 
     * @param requestList
     */
    public void setRequest(List<UpdateRequest> requestList) {
        this.requestList = requestList;
    }

}
