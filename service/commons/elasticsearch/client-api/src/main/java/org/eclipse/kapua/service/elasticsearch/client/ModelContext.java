/*******************************************************************************
 * Copyright (c) 2017, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client;

import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;

import java.util.Map;

/**
 * Model context definition.
 * <p>
 * This object is responsible for translating document model objects from/to client objects
 *
 * @since 1.0.0
 */
public interface ModelContext {

    /**
     * Type descriptor key
     *
     * @since 1.0.0
     */
    String TYPE_DESCRIPTOR_KEY = "type_descriptor";

    /**
     * Converts the serialized object (from client domain) to the specific Elasticsearch object.
     *
     * @param clazz            The client object type
     * @param serializedObject The serialized objetc as {@link Map}
     * @return The object
     * @throws DatamodelMappingException if there are error in resource mapping.
     * @since 1.0.0
     */
    <T> T unmarshal(Class<T> clazz, Map<String, Object> serializedObject) throws DatamodelMappingException;

    /**
     * Converts the Elasticsearch object to the client object
     *
     * @param object The object to convert.
     * @return The converted object as a {@link Map}
     * @throws DatamodelMappingException if there are error in resource mapping.
     * @since 1.0.0
     */
    Map<String, Object> marshal(Object object) throws DatamodelMappingException;

    /**
     * Gets the key name of the id field.
     *
     * @return The key name of the id field.
     * @since 1.3.0
     */
    String getIdKeyName();

}
