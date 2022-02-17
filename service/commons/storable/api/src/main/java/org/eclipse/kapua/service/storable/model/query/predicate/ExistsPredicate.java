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
