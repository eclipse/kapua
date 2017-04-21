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
    public String BOOL_KEY = "bool";
    /**
     * Exists term
     */
    public String EXISTS_KEY = "exists";
    /**
     * Field
     */
    public String FIELD_KEY = "field";
    /**
     * Ids term
     */
    public String IDS_KEY = "ids";
    /**
     * Must term
     */
    public String MUST_KEY = "must";
    /**
     * Prefix term
     */
    public String PREFIX_KEY = "prefix";
    /**
     * Range term
     */
    public String RANGE_KEY = "range";
    /**
     * Term
     */
    public String TERM_KEY = "term";
    /**
     * Type term
     */
    public String TYPE_KEY = "type";
    /**
     * Values term
     */
    public String VALUES_KEY = "values";

    /**
     * Greater than comparator term
     */
    public String GTE_KEY = "gte";
    /**
     * Less than comparator term
     */
    public String LTE_KEY = "lte";

}
