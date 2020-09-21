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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;

/**
 * Implementation of query predicate for checking if a field exists
 *
 * @since 1.0
 */
public class ExistsPredicateImpl extends StorablePredicateImpl implements ExistsPredicate {

    protected String name;

    /**
     * Creates an exists predicate for the given field name
     *
     * @param name
     */
    public ExistsPredicateImpl(String name) {
        this.name = name;
    }

    /**
     * Creates an exists predicate concatenating the given fields name with a dot (useful for composite fileds)
     *
     * @param paths
     */
    public ExistsPredicateImpl(String... paths) {
        StringBuilder builder = new StringBuilder();
        for (String str : paths) {
            builder.append(str);
            builder.append('.');
        }
        name = builder.substring(0, builder.length() - 1);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    /**
     * <pre>
     *  {
     *      "query": {
     *          "exists" : { "field" : "metric" }
     *      }
     *   }
     * </pre>
     */
    public ObjectNode toSerializedMap() throws KapuaException {
        ObjectNode termNode = getField(new KeyValueEntry[]{new KeyValueEntry(PredicateConstants.FIELD_KEY, name)});

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.EXISTS_KEY, termNode);
        return rootNode;
    }

}
