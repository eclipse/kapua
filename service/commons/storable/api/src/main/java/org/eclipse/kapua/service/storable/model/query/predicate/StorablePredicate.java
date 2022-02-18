/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;

/**
 * {@link StorablePredicate} definition.
 * <p>
 * Is the base {@code interface} for {@link StorablePredicate}s.
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
