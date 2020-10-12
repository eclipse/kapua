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

/**
 * Exists {@link StorablePredicate} defintion.
 * <p>
 * It matches that a field exists.
 *
 * @since 1.0.0
 */
public interface ExistsPredicate extends StorablePredicate {

    /**
     * Gets the field name to check existence.
     *
     * @return The field name to check existence.
     * @since 1.0.0
     */
    String getName();

    /**
     * Sets the field name to check existence.
     *
     * @param name The field name to check existence.
     * @return Itself, to chain method invocation.
     * @since 1.3.0
     */
    ExistsPredicate setName(String name);

}
