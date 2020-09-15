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

import java.util.ArrayList;
import java.util.List;

/**
 * {@link BulkUpdateRequest} definition.
 * <p>
 * It bundles a {@link List} of {@link UpdateRequest}
 *
 * @since 1.0.0
 */
public class BulkUpdateRequest {

    List<UpdateRequest> requestList;

    /**
     * Adds an {@link UpdateRequest} to the {@link List}.
     *
     * @param updateRequest The {@link UpdateRequest} to add.
     * @since 1.0.0
     */
    public void add(UpdateRequest updateRequest) {
        getRequest().add(updateRequest);
    }

    /**
     * Gets the {@link List} of {@link UpdateRequest}s.
     *
     * @return The {@link List} of {@link UpdateRequest}s.
     * @since 1.0.0
     */
    public List<UpdateRequest> getRequest() {
        if (requestList == null) {
            requestList = new ArrayList<>();
        }

        return requestList;
    }

    /**
     * Sets the {@link List} of {@link UpdateRequest}s.
     *
     * @param requestList The {@link List} of {@link UpdateRequest}s.
     * @since 1.0.0
     */
    public void setRequest(List<UpdateRequest> requestList) {
        this.requestList = requestList;
    }

}
