/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.model.query.predicate;

import java.util.List;

/**
 * {@link MatchPredicate} definition.
 *
 * @param <T> Attribute value type.
 * @since 1.3.0
 */
public interface MatchPredicate<T> extends QueryPredicate {

    /**
     * Gets the name of the {@link org.eclipse.kapua.model.KapuaEntityAttributes} to compare.
     *
     * @return The name name of the {@link org.eclipse.kapua.model.KapuaEntityAttributes} to compare.
     * @since 1.3.0
     */
    List<String> getAttributeNames();

    /**
     * Gets the value to compare the results.
     *
     * @return The value to compare the results.
     * @since 1.3.0
     */
    T getMatchTerm();

}
