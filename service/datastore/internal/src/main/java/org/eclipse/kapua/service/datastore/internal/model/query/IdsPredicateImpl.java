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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.kapua.service.datastore.internal.schema.SchemaUtil;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.IdsPredicate;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Implementation of query predicate for matching identifier values fields
 * 
 * @since 1.0
 *
 */
public class IdsPredicateImpl implements IdsPredicate {

    private String type;
    private Set<StorableId> idSet = new HashSet<StorableId>();

    /**
     * Default constructor
     */
    public IdsPredicateImpl() {
    }

    /**
     * Construct an identifier predicate given the type
     * 
     * @param type
     */
    public IdsPredicateImpl(String type) {
        this();
        this.type = type;
    }

    /**
     * Construct an identifier predicate given the type and the identifier collection
     * 
     * @param type
     * @param ids
     */
    public IdsPredicateImpl(String type, Collection<StorableId> ids) {
        this(type);
        this.idSet.addAll(ids);
    }

    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Set the identifier type
     * 
     * @param type
     * @return
     */
    public IdsPredicate setType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public Set<StorableId> getIdSet() {
        return this.idSet;
    }

    /**
     * Add the storable identifier to the identifier set
     * 
     * @param id
     * @return
     */
    public IdsPredicate addValue(StorableId id) {
        this.idSet.add(id);
        return this;
    }

    /**
     * Add the storable identifier list to the identifier set
     * 
     * @param ids
     * @return
     */
    public IdsPredicate addValues(Collection<StorableId> ids) {
        this.idSet.addAll(ids);
        return this;
    }

    /**
     * Clear the storable identifier set
     * 
     * @return
     */
    public IdsPredicate clearValues() {
        this.idSet.clear();
        return this;
    }

    @Override
    /**
     * <pre>
     *  {
     *      "query": {
     *          "ids" : {
     *              "type" : "kapua_id",
     *              "values" : ["abcdef1234", "abcdef1235", "zzzyyyy1234"]
     *          }
     *      }
     *  }
     * </pre>
     */
    public ObjectNode toSerializedMap() {
        ObjectNode rootNode = SchemaUtil.getObjectNode();
        ObjectNode idsNode = SchemaUtil.getObjectNode();
        ArrayNode idsList = SchemaUtil.getArrayNode();
        for (StorableId id : idSet) {
            idsList.add(id.toString());
        }
        idsNode.set(PredicateConstants.TYPE_KEY, SchemaUtil.getTextNode(type));
        idsNode.set(PredicateConstants.VALUES_KEY, idsList);
        rootNode.set(PredicateConstants.IDS_KEY, idsNode);
        return rootNode;
    }

}
