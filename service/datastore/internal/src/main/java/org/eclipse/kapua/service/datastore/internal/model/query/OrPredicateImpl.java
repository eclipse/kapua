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
package org.eclipse.kapua.service.datastore.internal.model.query;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.query.OrPredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of query "or" aggregation
 *
 * @since 1.0
 */
public class OrPredicateImpl implements OrPredicate {

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
    public ObjectNode toSerializedMap() throws DatamodelMappingException {
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode termNode = SchemaUtil.getObjectNode();
        ArrayNode conditionsNode = SchemaUtil.getArrayNode();
        for (StorablePredicate predicate : predicates) {
            conditionsNode.add(predicate.toSerializedMap());
        }
        termNode.set(PredicateConstants.SHOULD_KEY, conditionsNode);
        rootNode.set(PredicateConstants.BOOL_KEY, termNode);
        return rootNode;
    }

}
