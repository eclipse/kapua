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
package org.eclipse.kapua.service.datastore.internal.model.query;

import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;

/**
 * Sortable field implementation
 * 
 * @since 1.0
 *
 */
public class SortFieldImpl implements SortField
{

    private String        field;
    private SortDirection sortDirection;

    /**
     * Default constructor
     */
    public SortFieldImpl()
    {

    }

    @Override
    public String getField()
    {
        return field;
    }

    @Override
    public void setField(String field)
    {
        this.field = field;
    }

    @Override
    public SortDirection getSortDirection()
    {
        return sortDirection;
    }

    @Override
    public void setSortDirection(SortDirection sortDirection)
    {
        this.sortDirection = sortDirection;
    }

}
