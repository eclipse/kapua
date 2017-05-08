/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.model.query.predicate;

/**
 * Kapua attribute predicate definition.
 *
 * @param <T>
 *            attribute value
 * 
 * @since 1.0
 * 
 */
public interface KapuaAttributePredicate<T> extends KapuaPredicate {

    /**
     * Allowed predicate operator
     */
    public enum Operator {
        /**
         * equal
         */
        EQUAL,
        /**
         * not equal
         */
        NOT_EQUAL,

        /**
         * is null
         */
        IS_NULL,
        /**
         * is not null
         */
        NOT_NULL,

        /**
         * starts with
         */
        STARTS_WITH,
        /**
         * like
         */
        LIKE,

        /**
         * greater than
         */
        GREATER_THAN,
        /**
         * greater or equal than
         */
        GREATER_THAN_OR_EQUAL,
        /**
         * less than
         */
        LESS_THAN,
        /**
         * less or equal than
         */
        LESS_THAN_OR_EQUAL;
    }

    /**
     * Get the attribute name
     * 
     * @return
     */
    public String getAttributeName();

    /**
     * Get the attribute value
     * 
     * @return
     */
    public T getAttributeValue();

    /**
     * Get the operator
     * 
     * @return
     */
    public Operator getOperator();
}
