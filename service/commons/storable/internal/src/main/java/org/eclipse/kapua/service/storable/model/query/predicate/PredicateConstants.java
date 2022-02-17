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
 * {@link StorablePredicate}s constants.
 *
 * @since 1.0.0
 */
public interface PredicateConstants {

    /**
     * Boolean term.
     *
     * @since 1.0.0
     */
    String BOOL_KEY = "bool";

    /**
     * Exists term.
     *
     * @since 1.0.0
     */
    String EXISTS_KEY = "exists";

    /**
     * Field.
     *
     * @since 1.0.0
     */
    String FIELD_KEY = "field";

    /**
     * Ids term.
     *
     * @since 1.0.0
     */
    String IDS_KEY = "ids";

    /**
     * Must term.
     *
     * @since 1.0.0
     */
    String MUST_KEY = "must";

    /**
     * Must term.
     *
     * @since 1.0.0
     */
    String SHOULD_KEY = "should";

    /**
     * Prefix term.
     *
     * @since 1.0.0
     */
    String PREFIX_KEY = "prefix";

    /**
     * Range term.
     *
     * @since 1.0.0
     */
    String RANGE_KEY = "range";

    /**
     * Term.
     *
     * @since 1.0.0
     */
    String TERM_KEY = "term";

    /**
     * Values term.
     *
     * @since 1.0.0
     */
    String VALUES_KEY = "values";

    /**
     * Greater than comparator term.
     *
     * @since 1.0.0
     */
    String GTE_KEY = "gte";

    /**
     * Less than comparator term.
     *
     * @since 1.0.0
     */
    String LTE_KEY = "lte";
}
