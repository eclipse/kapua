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

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;

/**
 * No op {@link QueryConverter} implementation for test purposes.
 *
 * @since 1.3.0
 */
public class NoOpQueryConverter implements QueryConverter {
    @Override
    public JsonNode convertQuery(Object query) {
        return null;
    }

    @Override
    public Object getFetchStyle(Object query) {
        return null;
    }
}
