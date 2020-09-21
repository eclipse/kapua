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
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of query "and" aggregation
 *
 * @since 1.0
 */
public class AndPredicateImpl extends StorablePredicateImpl implements AndPredicate {

    private List<StorablePredicate> predicates = new ArrayList<>();

    /**
     * Default constructor
     */
    public AndPredicateImpl() {
    }

    /**
     * Creates an and predicate for the given predicates collection
     *
     * @param predicates
     */
    public AndPredicateImpl(Collection<StorablePredicate> predicates) {
        predicates.addAll(predicates);
    }

    @Override
    public List<StorablePredicate> getPredicates() {
        return this.predicates;
    }

    /**
     * Add the storable predicate to the predicates collection
     *
     * @param predicate
     * @return
     */
    public AndPredicate addPredicate(StorablePredicate predicate) {
        this.predicates.add(predicate);
        return this;

    }

    /**
     * Clear the predicates collection
     *
     * @return
     */
    public AndPredicate clearPredicates() {
        this.predicates.clear();
        return this;
    }

    @Override
    public ObjectNode toSerializedMap() throws KapuaException {
        ArrayNode conditionsNode = newArrayNodeFromPredicates(predicates);

        ObjectNode termNode = newObjectNode();
        termNode.set(PredicateConstants.MUST_KEY, conditionsNode);

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.BOOL_KEY, termNode);
        return rootNode;
    }

}
