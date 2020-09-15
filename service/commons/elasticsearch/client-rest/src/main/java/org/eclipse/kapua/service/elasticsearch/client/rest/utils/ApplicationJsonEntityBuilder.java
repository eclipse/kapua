/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.elasticsearch.client.rest.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;

/**
 * Utility class that builds an {@link HttpEntity} with {@link EntityBuilder#setContentType(ContentType)} set to {@link ContentType#APPLICATION_JSON}.
 * <p>
 * Hence the name, what a surprise!
 *
 * @since 1.3.0
 */
public class ApplicationJsonEntityBuilder {

    private ApplicationJsonEntityBuilder() {
    }

    public static HttpEntity buildFrom(String jsonText) {
        return EntityBuilder.create()
                .setText(jsonText)
                .setContentType(ContentType.APPLICATION_JSON)
                .build();
    }
}
