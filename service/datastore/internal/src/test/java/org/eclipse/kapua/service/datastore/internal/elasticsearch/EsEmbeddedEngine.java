package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.Settings.Builder;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

public class EsEmbeddedEngine
{

    private static final String DEFAULT_DATA_DIRECTORY = "target/elasticsearch/data";

    private Node node;

    public EsEmbeddedEngine()
    {
        Builder elasticsearchSettings = Settings.settingsBuilder()
                                                .put("http.enabled", "false")
                                                .put("path.data", DEFAULT_DATA_DIRECTORY)
                                                .put("path.home", ".");

        node = NodeBuilder.nodeBuilder()
                          .local(true)
                          .settings(elasticsearchSettings.build())
                          .node();
    }

    public Client getClient()
    {
        return node.client();
    }

    public void close()
    {
        if (node != null) {
            node.close();
            node = null;
        }
    }
}
