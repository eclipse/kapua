/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.transport.model;

import org.eclipse.kapua.service.elasticsearch.client.ModelContext;
import org.eclipse.kapua.service.elasticsearch.client.exception.DatamodelMappingException;

import java.util.Map;

/**
 * No op {@link ModelContext} implementation for test purposes.
 *
 * @since 1.3.0
 */
public class NoOpModelContext implements ModelContext {
    @Override
    public <T> T unmarshal(Class<T> clazz, Map<String, Object> serializedObject) throws DatamodelMappingException {
        return null;
    }

    @Override
    public Map<String, Object> marshal(Object object) throws DatamodelMappingException {
        return null;
    }

    @Override
    public String getIdKeyName() {
        return null;
    }
}
