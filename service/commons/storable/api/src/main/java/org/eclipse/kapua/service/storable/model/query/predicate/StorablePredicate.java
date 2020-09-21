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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;

/**
 * {@link StorableQuery} predicate definition.
 *
 * @since 1.0.0
 */
public interface StorablePredicate {

    /**
     * Serializes the {@link StorablePredicate} to a {@link ObjectNode}.
     *
     * @return The serialized {@link StorablePredicate}
     * @since 1.0.0
     */
    ObjectNode toSerializedMap() throws MappingException;
}
