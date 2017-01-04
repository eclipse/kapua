package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.elasticsearch.client.Client;

public class EsEmbeddedClientProvider implements ElasticsearchClientProvider
{

    private EsEmbeddedEngine embeddedEngine;

    public EsEmbeddedClientProvider()
    {
        embeddedEngine = new EsEmbeddedEngine();
    }

    @Override
    public Client getClient()
    {
        return embeddedEngine.getClient();
    }

}
