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

import java.util.Set;

import org.eclipse.kapua.service.datastore.model.StorableId;

/**
 * Query predicate definition for matching identifier values fields
 * 
 * @since 1.0
 *
 */
public interface IdsPredicate extends StorablePredicate
{

    /**
     * Get the identifier type
     * 
     * @return
     */
    public String getType();

    /**
     * Get the identifier set.<br>
     * This set is used a comparison term by the query predicate.
     * 
     * @return
     */
    public Set<StorableId> getIdSet();
}
