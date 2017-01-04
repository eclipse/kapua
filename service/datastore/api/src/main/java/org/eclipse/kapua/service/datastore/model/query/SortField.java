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
package org.eclipse.kapua.service.datastore.model.query;

/**
 * Sortable field definition
 * 
 * @since 1.0
 *
 */
public interface SortField
{

    /**
     * Get the field name
     * 
     * @return
     */
    public String getField();

    /**
     * Set the field name
     * 
     * @param field
     */
    public void setField(String field);

    /**
     * Get the sort direction
     * 
     * @return
     */
    public SortDirection getSortDirection();

    /**
     * Set the sort direction
     * 
     * @param sortDirection
     */
    public void setSortDirection(SortDirection sortDirection);

}
