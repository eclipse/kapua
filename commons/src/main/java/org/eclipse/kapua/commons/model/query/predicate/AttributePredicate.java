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
package org.eclipse.kapua.commons.model.query.predicate;

import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate;

/**
 * Kapua attribute predicate reference implementation.
 *
 * @param <T> attribute value
 * 
 * @since 1.0
 * 
 */
public class AttributePredicate<T> implements KapuaAttributePredicate<T>
{

    /**
     * Predicate attribute name
     */
    private String   attributeName;

    /**
     * Predicate attribute value
     */
    private T        attributeValue;

    /**
     * Comparison attribute
     */
    private Operator operator;

    /**
     * Constructor
     * 
     * @param attributeName
     * @param attributeValue
     */
    public AttributePredicate(String attributeName, T attributeValue)
    {
        this(attributeName, attributeValue, Operator.EQUAL);
    }

    /**
     * Constructor
     * 
     * @param attributeName
     * @param attributeValue
     * @param operator
     */
    public AttributePredicate(String attributeName, T attributeValue, Operator operator)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.operator = operator;
    }

    /**
     * Create an equal to predicate
     * 
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public static <T> AttributePredicate<T> attributeIsEqualTo(String attributeName, T attributeValue) {
        return new AttributePredicate<>(attributeName, attributeValue);
    }

    /**
     * Create a not equal to predicate
     * 
     * @param attributeName
     * @param attributeValue
     * @return
     */
    public static <T> AttributePredicate<T> attributeIsNotEqualTo(String attributeName, T attributeValue) {
        return new AttributePredicate<>(attributeName, attributeValue, Operator.NOT_EQUAL);
    }

    @Override
    public String getAttributeName()
    {
        return attributeName;
    }

    @Override
    public T getAttributeValue()
    {
        return attributeValue;
    }

    @Override
    public Operator getOperator()
    {
        return operator;
    }

}
