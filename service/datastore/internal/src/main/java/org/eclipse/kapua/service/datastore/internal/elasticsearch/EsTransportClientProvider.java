package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * Elasticsearch transport client implementation.
 * 
 * @since 1.0
 *
 */
public class EsTransportClientProvider implements ElasticsearchClientProvider
{

    private static final int DEFAULT_PORT = 9300;

    private Client client;

    private static String[] getNodeParts(String node)
    {
        if (node == null)
            return new String[] {};

        String[] split = node.split(":");
        return split;
    }

    private static Client getEsClient(String hostname, int port, String clustername) throws UnknownHostException
    {

        Settings settings = Settings.settingsBuilder()
                                    .put("cluster.name", clustername).build();

        InetSocketTransportAddress ita = new InetSocketTransportAddress(InetAddress.getByName(hostname), port);
        Client esClient = TransportClient.builder().settings(settings).build().addTransportAddress(ita);

        return esClient;
    }

    /**
     * Create a new Elasticsearch transport client based on the configuration parameters ({@link DatastoreSettingKey})
     * 
     * @throws EsClientUnavailableException
     */
    public EsTransportClientProvider()
        throws EsClientUnavailableException
    {
        DatastoreSettings config = DatastoreSettings.getInstance();
        Map<String, String> map = config.getMap(String.class, DatastoreSettingKey.ELASTICSEARCH_NODES, "[0-9]+");
        String[] esNodes = new String[] {};
        if (map != null)
            esNodes = map.values().toArray(new String[] {});

        if (esNodes == null || esNodes.length == 0)
            throw new EsClientUnavailableException("No elasticsearch nodes found");

        String[] nodeParts = getNodeParts(esNodes[0]);
        String esHost = null;
        int esPort = DEFAULT_PORT;

        if (nodeParts.length > 0)
            esHost = nodeParts[0];

        if (nodeParts.length > 1) {
            try {
                Integer.parseInt(nodeParts[1]);
            }
            catch (NumberFormatException e) {
                throw new EsClientUnavailableException("Could not parse elasticsearch port: " + nodeParts[1]);
            }
        }

        Client theClient = null;
        try {
            theClient = getEsClient(esHost, esPort, config.getString(DatastoreSettingKey.ELASTICSEARCH_CLUSTER));
        }
        catch (UnknownHostException e) {
            throw new EsClientUnavailableException("Unknown elasticsearch node host", e);
        }

        client = theClient;

    }

    @Override
    public Client getClient()
    {
        return client;
    }

}
