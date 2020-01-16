/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
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

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link AndPredicate} definition.
 * <p>
 * Used to link multiple {@link QueryPredicate}s in AND clause.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "andPredicate")
@XmlType(factoryClass = PredicateXmlRegistry.class, factoryMethod = "newAndPredicate")
public interface AndPredicate extends QueryPredicate {

    /**
     * Adds the given {@link QueryPredicate} to the {@link AndPredicate}.
     *
     * @param predicate The {@link AndPredicate} to concatenate
     * @return {@code this} {@link AndPredicate}.
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.0.0
     */
    @XmlTransient
    AndPredicate and(@NotNull QueryPredicate predicate);

    /**
     * Gets all {@link QueryPredicate} set for this {@link AndPredicate}
     *
     * @return The {@link List} of {@link QueryPredicate}s
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "predicates")
    @XmlElement(name = "predicate")
    List<QueryPredicate> getPredicates();

    /**
     * Sets a {@link List} of {@link QueryPredicate}s in AND clause
     *
     * @param predicates The {@link List} of {@link QueryPredicate}s
     * @throws NullPointerException if the given parameter is {@code null}.
     * @since 1.1.0
     */
    void setPredicates(@NotNull List<QueryPredicate> predicates);
}
