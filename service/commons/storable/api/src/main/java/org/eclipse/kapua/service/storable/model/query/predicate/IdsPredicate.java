/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Collection;
import java.util.Set;

/**
 * Query predicate definition for matching identifier values fields
 *
 * @since 1.0.0
 */
public interface IdsPredicate extends StorablePredicate {

    /**
     * Get the identifier type
     *
     * @return
     * @since 1.0.0
     */
    String getType();

    /**
     * @param storableId
     * @since 1.3.0
     */
    IdsPredicate addId(StorableId storableId);


    /**
     * @param storableId
     * @since 1.3.0
     */
    IdsPredicate addIds(Collection<StorableId> storableId);

    /**
     * Get the identifier set.<br>
     * This set is used a comparison term by the query predicate.
     *
     * @return
     * @since 1.0.0
     */
    Set<StorableId> getIdSet();
}
