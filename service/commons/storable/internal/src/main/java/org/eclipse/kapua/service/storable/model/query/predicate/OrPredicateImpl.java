/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.service.storable.exception.MappingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of query "or" aggregation
 *
 * @since 1.0
 */
public class OrPredicateImpl extends StorablePredicateImpl implements OrPredicate {

    private List<StorablePredicate> predicates = new ArrayList<>();

    /**
     * Default constructor
     */
    public OrPredicateImpl() {
    }

    /**
     * Creates an "or" predicate for the given predicates collection
     *
     * @param predicates
     */
    public OrPredicateImpl(Collection<StorablePredicate> predicates) {
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
    public OrPredicate addPredicate(StorablePredicate predicate) {
        this.predicates.add(predicate);
        return this;

    }

    /**
     * Clear the predicates collection
     *
     * @return
     */
    public OrPredicate clearPredicates() {
        this.predicates.clear();
        return this;
    }

    @Override
    public ObjectNode toSerializedMap() throws MappingException {
        ArrayNode conditionsNode = newArrayNodeFromPredicates(predicates);

        ObjectNode termNode = newObjectNode();
        termNode.set(PredicateConstants.SHOULD_KEY, conditionsNode);

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.BOOL_KEY, termNode);
        return rootNode;
    }

}
