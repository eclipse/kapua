/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.io.IOException;
import java.net.URL;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.admin.indices.refresh.RefreshAction;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.client.Client;

public final class Elasticsearch {

    private Elasticsearch() {
    }

    /**
     * Refresh all Elasticsearch indices
     * 
     * @param baseUrl
     *            the base URL to the Elasticsearch instance
     * @throws IOException
     *             In case anything goes wrong
     */
    public static void refreshAllIndices(final URL baseUrl) throws IOException {
        final HttpPost request = new HttpPost(baseUrl + "/_refresh");
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            try (CloseableHttpResponse response = client.execute(request)) {
                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new IOException(String.format("Failed to refresh indices: %s", response.getStatusLine()));
                }
            }
        }
    }

    /**
     * Refresh all Elasticsearch indices
     * 
     * @param client
     *            the client to use
     */
    public static void refreshAllIndices(final Client client) {
        final RefreshRequestBuilder request = RefreshAction.INSTANCE.newRequestBuilder(client);
        request.execute().actionGet();
    }
}
