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

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.utils.KeyValueEntry;

/**
 * {@link ExistsPredicate} implementation.
 *
 * @since 1.0.0
 */
public class ExistsPredicateImpl extends StorablePredicateImpl implements ExistsPredicate {

    protected String name;

    /**
     * Constructor.
     *
     * @param name The field name to check existence.
     * @since 1.0.0
     */
    public ExistsPredicateImpl(String name) {
        this.name = name;
    }

    /**
     * Constructor.
     * <p>
     * Creates an exists predicate concatenating the given fields name with a dot (useful for composite fileds)
     *
     * @param names The field names to check existence.
     * @since 1.0.0
     */
    public ExistsPredicateImpl(String... names) {
        StringBuilder builder = new StringBuilder();
        for (String str : names) {
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
    public ExistsPredicate setName(String name) {
        this.name = name;

        return this;
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
    public ObjectNode toSerializedMap() throws MappingException {
        ObjectNode termNode = newObjectNode(new KeyValueEntry[]{new KeyValueEntry(PredicateConstants.FIELD_KEY, name)});

        ObjectNode rootNode = newObjectNode();
        rootNode.set(PredicateConstants.EXISTS_KEY, termNode);
        return rootNode;
    }

}
