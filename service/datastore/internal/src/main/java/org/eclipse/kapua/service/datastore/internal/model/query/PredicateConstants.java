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

/**
 * Predicates constants (keys and fields names)
 *
 * @since 1.0
 */
public interface PredicateConstants {

    /**
     * Boolean term
     */
    String BOOL_KEY = "bool";

    /**
     * Exists term
     */
    String EXISTS_KEY = "exists";

    /**
     * Field
     */
    String FIELD_KEY = "field";

    /**
     * Ids term
     */
    String IDS_KEY = "ids";

    /**
     * Must term
     */
    String MUST_KEY = "must";

    /**
     * Prefix term
     */
    String PREFIX_KEY = "prefix";

    /**
     * Range term
     */
    String RANGE_KEY = "range";

    /**
     * Term
     */
    String TERM_KEY = "term";

    /**
     * Type term
     */
    String TYPE_KEY = "type";

    /**
     * Values term
     */
    String VALUES_KEY = "values";

    /**
     * Greater than comparator term
     */
    String GTE_KEY = "gte";

    /**
     * Less than comparator term
     */
    String LTE_KEY = "lte";
}
