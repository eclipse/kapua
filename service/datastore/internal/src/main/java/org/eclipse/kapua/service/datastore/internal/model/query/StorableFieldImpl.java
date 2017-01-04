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

import org.eclipse.kapua.service.datastore.model.query.StorableField;

/**
 * Storable field implementation
 * 
 * @since 1.0
 *
 */
public class StorableFieldImpl implements StorableField
{

    private String field;

    /**
     * Construct a storable field given the field name
     * 
     * @param field
     */
    public StorableFieldImpl(String field)
    {
        this.field = field;
    }

    @Override
    public String field()
    {
        return field;
    }

}
