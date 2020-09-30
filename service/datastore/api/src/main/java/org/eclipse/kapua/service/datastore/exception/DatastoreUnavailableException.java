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
package org.eclipse.kapua.service.datastore.exception;

import org.eclipse.kapua.service.elasticsearch.client.ElasticsearchClientProvider;

/**
 * {@link DatastoreServiceException} to {@code throw} when the underling Elasticsearch is not available.
 *
 * @since 1.3.0
 */
public class DatastoreUnavailableException extends DatastoreServiceException {

    private final String elasticSearchProviderName;

    /**
     * Constructor.
     *
     * @param cause @param cause The root {@link Throwable} of this {@link DatastoreUnavailableException}.
     * @since 1.3.0
     */
    public DatastoreUnavailableException(Throwable cause, String elasticSearchProviderName) {
        super(DatastoreServiceErrorCodes.DATASTORE_UNAVAILABLE_EXCEPTION, cause, elasticSearchProviderName);

        this.elasticSearchProviderName = elasticSearchProviderName;
    }

    /**
     * Gets the {@link ElasticsearchClientProvider} name.
     *
     * @return The {@link ElasticsearchClientProvider} name.
     * @since 1.3.0
     */
    public String getElasticSearchProviderName() {
        return elasticSearchProviderName;
    }
}
