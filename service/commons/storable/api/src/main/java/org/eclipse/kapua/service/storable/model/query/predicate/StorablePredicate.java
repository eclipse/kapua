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

/**
 * Storable query predicate definition
 *
 * @since 1.0
 */
public interface StorablePredicate {

    /**
     * Serialize the predicate to a Json object
     *
     * @return
     */
    ObjectNode toSerializedMap() throws Exception;
}
