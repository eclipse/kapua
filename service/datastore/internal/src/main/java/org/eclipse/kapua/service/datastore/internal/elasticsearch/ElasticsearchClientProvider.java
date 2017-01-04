package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.elasticsearch.client.Client;

/**
 * Elasticsearch transport client wrapper definition.
 * 
 * @since 1.0
 *
 */
public interface ElasticsearchClientProvider
{

    /**
     * Get a new Elasticsearch client instance
     * 
     * @return
     */
    public Client getClient();
}
