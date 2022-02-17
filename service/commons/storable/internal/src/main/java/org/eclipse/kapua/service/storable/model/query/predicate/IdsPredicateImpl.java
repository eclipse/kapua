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
package org.eclipse.kapua.service.storable.model.query.predicate;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link IdsPredicate} implementation.
 *
 * @since 1.0.0
 */
public class IdsPredicateImpl extends StorablePredicateImpl implements IdsPredicate {

    private String type;
    private List<StorableId> ids;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public IdsPredicateImpl() {
    }

    /**
     * Constructor.
     *
     * @param type The type descriptor.
     * @since 1.0.0
     */
    public IdsPredicateImpl(String type) {
        this();

        setType(type);
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public IdsPredicate setType(String type) {
        this.type = type;

        return this;
    }

    @Override
    public List<StorableId> getIds() {
        if (ids == null) {
            ids = new ArrayList<>();
        }

        return ids;
    }


    @Override
    public IdsPredicate addId(StorableId id) {
        getIds().add(id);

        return this;
    }

    @Override
    public IdsPredicate addIds(List<StorableId> ids) {
        getIds().addAll(ids);

        return this;
    }

    @Override
    public IdsPredicate setIds(List<StorableId> ids) {
        this.ids = ids;

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
        ArrayNode idsList = newArrayNode(ids);

        ObjectNode idsNode = newObjectNode();
        idsNode.set(PredicateConstants.VALUES_KEY, idsList);

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.IDS_KEY, idsNode);
        return rootNode;
    }

}
