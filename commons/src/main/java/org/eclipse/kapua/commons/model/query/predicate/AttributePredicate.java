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
 *
 *******************************************************************************/
package org.eclipse.kapua.commons.model.query.predicate;

import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate;

public class AttributePredicate<T> implements KapuaAttributePredicate<T>
{

    private String   attributeName;
    private T        attributeValue;
    private Operator operator;

    public AttributePredicate(String attributeName, T attributeValue)
    {
        this(attributeName, attributeValue, Operator.EQUAL);
    }

    public AttributePredicate(String attributeName, T attributeValue, Operator operator)
    {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.operator = operator;
    }

    public static <T> AttributePredicate<T> attributeIsEqualTo(String attributeName, T attributeValue) {
        return new AttributePredicate<>(attributeName, attributeValue);
    }

    public static <T> AttributePredicate<T> attributeIsNotEqualTo(String attributeName, T attributeValue) {
        return new AttributePredicate<>(attributeName, attributeValue, Operator.NOT_EQUAL);
    }

    public String getAttributeName()
    {
        return attributeName;
    }

    public T getAttributeValue()
    {
        return attributeValue;
    }

    public Operator getOperator()
    {
        return operator;
    }

}
