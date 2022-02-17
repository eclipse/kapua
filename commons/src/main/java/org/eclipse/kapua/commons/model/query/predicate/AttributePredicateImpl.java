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
package org.eclipse.kapua.commons.model.query.predicate;

import org.eclipse.kapua.model.query.predicate.AttributePredicate;

/**
 * {@link AttributePredicate} implementation.
 *
 * @since 1.0.0
 */
public class AttributePredicateImpl<T> implements AttributePredicate<T> {

    private String attributeName;
    private T attributeValue;
    private Operator operator;

    /**
     * Constructor.
     * <p>
     * Defaults ot {@link org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator#EQUAL}
     *
     * @param attributeName  The name of {@link org.eclipse.kapua.model.KapuaEntityAttributes} to set into the {@link AttributePredicate}.
     * @param attributeValue The value of {@link org.eclipse.kapua.model.KapuaEntityAttributes} to set into the {@link AttributePredicate}.
     * @since 1.0.0
     */
    public AttributePredicateImpl(String attributeName, T attributeValue) {
        this(attributeName, attributeValue, Operator.EQUAL);
    }

    /**
     * Constructor.
     *
     * @param attributeName  The name of {@link org.eclipse.kapua.model.KapuaEntityAttributes} to set into the {@link AttributePredicate}.
     * @param attributeValue The value of {@link org.eclipse.kapua.model.KapuaEntityAttributes} to set into the {@link AttributePredicate}.
     * @param operator       The {@link org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator} to set into the {@link AttributePredicate}.
     * @since 1.0.0
     */
    public AttributePredicateImpl(String attributeName, T attributeValue, Operator operator) {
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
        this.operator = operator;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public T getAttributeValue() {
        return attributeValue;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

}
