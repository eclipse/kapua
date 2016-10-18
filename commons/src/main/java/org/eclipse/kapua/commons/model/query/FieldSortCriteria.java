/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query;

import org.eclipse.kapua.model.query.KapuaSortCriteria;

/**
 * Field sort criteria.
 * 
 * @since 1.0
 *
 */
public class FieldSortCriteria implements KapuaSortCriteria {

    /**
     * Sort order
     */
    public enum SortOrder {
        /**
         * Ascending
         */
        ASCENDING,
        /**
         * Descending
         */
        DESCENDING;
    }

    /**
     * Field attribute name
     */
    private String    attributeName;

    /**
     * Field sort order
     */
    private SortOrder sortOrder;

    /**
     * Constructor
     * 
     * @param attributeName
     * @param sortOrder
     */
    public FieldSortCriteria(String attributeName, SortOrder sortOrder) {
        this.attributeName = attributeName;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the sort attribute name
     * 
     * @return
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Get the sort attribute order
     * 
     * @return
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }
}
