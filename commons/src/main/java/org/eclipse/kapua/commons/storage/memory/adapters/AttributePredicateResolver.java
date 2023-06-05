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
package org.eclipse.kapua.commons.storage.memory.adapters;

import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class AttributePredicateResolver implements QueryPredicateResolver {

    @Override
    public <E> Optional<Predicate<E>> mapToPredicate(QueryPredicate queryPredicate, Map<String, Function<E, Object>> pluckers, BiFunction<QueryPredicate, Map<String, Function<E, Object>>, Predicate<E>> innerVisitor) {
        AttributePredicate<?> attributePredicate = (AttributePredicate<?>) queryPredicate;
        final Function<E, Object> plucker = Optional.ofNullable(pluckers.get(attributePredicate.getAttributeName()))
                .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid field %s", attributePredicate.getAttributeName())));
        Predicate<E> res = e -> {
            Object fieldValue = plucker.apply(e);

            // Convert old Object[] support to List<?>
            if (fieldValue instanceof Object[]) {
                fieldValue = Arrays.asList((Object[]) fieldValue);
            }
            if (fieldValue instanceof Collection) {
                Collection<?> fieldValues = (Collection<?>) fieldValue;
                return fieldValues
                        .stream()
                        .map(value -> {
                            return evaluatePredicate(attributePredicate, value);
                        })
                        .reduce(true, (lhs, rhs) -> lhs || rhs);
            } else {
                return evaluatePredicate(attributePredicate, fieldValue);
            }
        };
        return Optional.of(res);
    }

    private static boolean evaluatePredicate(AttributePredicate<?> attributePredicate, Object fieldValue) {
        final Object attributeValue = attributePredicate.getAttributeValue();
        switch (attributePredicate.getOperator()) {
            case EQUAL:
                return Objects.equals(attributeValue, fieldValue);
            case NOT_EQUAL:
                return !Objects.equals(attributeValue, fieldValue);
            case IS_NULL:
                return fieldValue == null;
            case NOT_NULL:
                return fieldValue != null;
            case STARTS_WITH:
                if (fieldValue instanceof String && attributeValue instanceof String) {
                    return ((String) fieldValue).startsWith((String) attributeValue);
                }
                return false;
            case STARTS_WITH_IGNORE_CASE:
                if (fieldValue instanceof String && attributeValue instanceof String) {
                    return ((String) fieldValue).toLowerCase().startsWith(((String) attributeValue).toLowerCase());
                }
                return false;
            case LIKE:
                if (fieldValue instanceof String && attributeValue instanceof String) {
                    return ((String) fieldValue).contains((String) attributeValue);
                }
                return false;
            case LIKE_IGNORE_CASE:
                if (fieldValue instanceof String && attributeValue instanceof String) {
                    return ((String) fieldValue).toLowerCase().contains(((String) attributeValue).toLowerCase());
                }
                return false;
            case GREATER_THAN:
                if (attributeValue instanceof Comparable && fieldValue instanceof Comparable) {
                    return ((Comparable) attributeValue).compareTo(fieldValue) > 0;
                } else {
                    throw new IllegalArgumentException("Trying to compare a non-comparable value");
                }
            case GREATER_THAN_OR_EQUAL:
                if (attributeValue instanceof Comparable && fieldValue instanceof Comparable) {
                    return ((Comparable) attributeValue).compareTo(fieldValue) >= 0;
                } else {
                    throw new IllegalArgumentException("Trying to compare a non-comparable value");
                }
            case LESS_THAN:
                if (attributeValue instanceof Comparable && fieldValue instanceof Comparable) {
                    return ((Comparable) attributeValue).compareTo(fieldValue) < 0;
                } else {
                    throw new IllegalArgumentException("Trying to compare a non-comparable value");
                }
            case LESS_THAN_OR_EQUAL:
                if (attributeValue instanceof Comparable && fieldValue instanceof Comparable) {
                    return ((Comparable) attributeValue).compareTo(fieldValue) <= 0;
                } else {
                    throw new IllegalArgumentException("Trying to compare a non-comparable value");
                }
            default:
                return false;
        }
    }
}
