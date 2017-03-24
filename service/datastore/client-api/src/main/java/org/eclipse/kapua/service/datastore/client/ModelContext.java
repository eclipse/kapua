/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *  
 * Contributors:
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.client;

import java.util.Map;

/**
 * Model context definition. This object is responsible for translating datastore model objects from/to client objects
 * 
 * @since 1.0
 */
public interface ModelContext {

    /**
     * Type descriptor key
     */
    public String TYPE_DESCRIPTOR_KEY = "type_descriptor";
    /**
     * Datastore object id descriptor key
     */
    public String DATASTORE_ID_KEY = "datastore_id";

    /**
     * Convert the serialized object (from client domain) to the specific datastore object.
     * 
     * @param clazz
     *            datastore object type
     * @param serializedObject
     * @return
     * @throws DatamodelMappingException
     */
    public <T> T unmarshal(Class<T> clazz, Map<String, Object> serializedObject) throws DatamodelMappingException;

    /**
     * Convert the datastore object to the client object
     * 
     * @param object
     * @return
     * @throws DatamodelMappingException
     */
    public Map<String, Object> marshal(Object object) throws DatamodelMappingException;

}
