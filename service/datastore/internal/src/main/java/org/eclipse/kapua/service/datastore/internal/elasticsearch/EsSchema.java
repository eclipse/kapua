/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.elasticsearch;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMetric;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;

public class EsSchema
{

    // TODO move builder methods in a new class to reduce number of lines
    //
    private static final Logger s_logger                        = LoggerFactory.getLogger(EsSchema.class);

    private static final String NULL_METADATA_MSG               = "No metadata available (use update(account, time))";

    public final static String  MESSAGE_TYPE_NAME               = "message";
    public final static String  MESSAGE_TIMESTAMP               = "timestamp";
    public final static String  MESSAGE_RECEIVED_ON             = "received_on";
    public final static String  MESSAGE_IP_ADDRESS              = "ip_address";
    public final static String  MESSAGE_ACCOUNT                 = "account";
    public final static String  MESSAGE_AS_NAME                 = "asset";
    public final static String  MESSAGE_SEM_TOPIC               = "topic";
    public final static String  MESSAGE_TOPIC_PARTS             = "topics";
    public final static String  MESSAGE_COLLECTED_ON            = "collected_on";
    public final static String  MESSAGE_POS                     = "position";
    public final static String  MESSAGE_POS_LOCATION            = "location";
    public final static String  MESSAGE_POS_LOCATION_FULL       = "position.location";
    public final static String  MESSAGE_POS_ALT                 = "alt";
    public final static String  MESSAGE_POS_ALT_FULL            = "position.alt";
    public final static String  MESSAGE_POS_PRECISION           = "precision";
    public final static String  MESSAGE_POS_PRECISION_FULL      = "position.precision";
    public final static String  MESSAGE_POS_HEADING             = "heading";
    public final static String  MESSAGE_POS_HEADING_FULL        = "position.heading";
    public final static String  MESSAGE_POS_SPEED               = "speed";
    public final static String  MESSAGE_POS_SPEED_FULL          = "position.speed";
    public final static String  MESSAGE_POS_TIMESTAMP           = "timestamp";
    public final static String  MESSAGE_POS_TIMESTAMP_FULL      = "position.timestamp";
    public final static String  MESSAGE_POS_SATELLITES          = "satellites";
    public final static String  MESSAGE_POS_SATELLITES_FULL     = "position.satellites";
    public final static String  MESSAGE_POS_STATUS              = "status";
    public final static String  MESSAGE_POS_STATUS_FULL         = "position.status";
    public final static String  MESSAGE_MTR                     = "metrics";
    public final static String  MESSAGE_BODY                    = "body";

    public final static String  TOPIC_TYPE_NAME                 = "topic";
    public final static String  TOPIC_SEM_NAME                  = "sem_topic";
    public final static String  TOPIC_ASSET                     = "asset";
    public final static String  TOPIC_ACCOUNT                   = "account";
    public final static String  TOPIC_TIMESTAMP                 = "timestamp";
    public final static String  TOPIC_MESSAGE_ID                = "message_id";

    public final static String  METRIC_TYPE_NAME            = "metric";
    public final static String  METRIC_SEM_NAME             = "sem_topic";
    public final static String  METRIC_ASSET                = "asset_name";
    public final static String  METRIC_ACCOUNT              = "account";
    public final static String  METRIC_MTR                  = "metric";
    public final static String  METRIC_MTR_NAME             = "name";
    public final static String  METRIC_MTR_NAME_FULL        = "metric.name";
    public final static String  METRIC_MTR_TYPE             = "type";
    public final static String  METRIC_MTR_TYPE_FULL        = "metric.type";
    public final static String  METRIC_MTR_VALUE            = "value";
    public final static String  METRIC_MTR_VALUE_FULL       = "metric.value";
    public final static String  METRIC_MTR_TIMESTAMP        = "timestamp";
    public final static String  METRIC_MTR_TIMESTAMP_FULL   = "metric.timestamp";
    public final static String  METRIC_MTR_MSG_ID           = "message_id";
    public final static String  METRIC_MTR_MSG_ID_FULL      = "metric.message_id";

    public final static String  ASSET_TYPE_NAME             = "asset";
    public final static String  ASSET_NAME                  = "asset_name";
    public final static String  ASSET_ACCOUNT               = "account";
    public final static String  ASSET_TIMESTAMP             = "timestamp";
    public final static String  ASSET_MESSAGE_ID            = "message_id";
//
//    public final static String  ASSET_TOPIC_TYPE_NAME           = "asset_topic";
//    public final static String  ASSET_TOPIC_AS_NAME             = "asset_name";
//    public final static String  ASSET_TOPIC_ACCOUNT             = "account";
//    public final static String  ASSET_TOPIC_TPC                 = "topic";
//    public final static String  ASSET_TOPIC_TPC_NAME            = "name";
//    public final static String  ASSET_TOPIC_TPC_NAME_FULL       = "topic.name";
//    public final static String  ASSET_TOPIC_TPC_TIMESTAMP       = "timestmp";
//    public final static String  ASSET_TOPIC_TPC_TIMESTAMP_FULL  = "topic.timestmp";
//    public final static String  ASSET_TOPIC_TPC_MSG_ID          = "message_id";
//    public final static String  ASSET_TOPIC_TPC_MSG_ID_FULL     = "topic.message_id";

    /**
     * @author stefano.morson
     *         For thread safetiness client code outside the parent class
     *         must not be able to change the values of the members
     */
    public class Metadata
    {

        // Info fields does not change within the same account name
        private String                messageTypeName;
        private String                topicTypeName;
        private String                metricTypeName;
        private String                assetTypeName;
        private String                indexName;
        private String                kapuaIndexName;
        //

        // Custom mappings can only increase within the same account
        // No removal of existing cached mappings or changes in the
        // existing mappings.
        private Map<String, EsMetric> messageMappingsCache;
        //

        private Map<String, EsMetric> getMessageMappingsCache()
        {
            return messageMappingsCache;
        }

        public Metadata()
        {
            messageTypeName = MESSAGE_TYPE_NAME;
            topicTypeName = TOPIC_TYPE_NAME;
            metricTypeName = METRIC_TYPE_NAME;
            assetTypeName = ASSET_TYPE_NAME;

            messageMappingsCache = new HashMap<String, EsMetric>(100);
        }

        public String getPublicIndexName()
        {
            return this.indexName;
        }

        public String getMessageTypeName()
        {
            return this.messageTypeName;
        }

        public String getPrivateIndexName()
        {
            return this.kapuaIndexName;
        }

        public String getTopicTypeName()
        {
            return this.topicTypeName;
        }

        public String getMetricTypeName()
        {
            return this.metricTypeName;
        }

        public String getAssetTypeName()
        {
            return this.assetTypeName;
        }
    }

    private Map<String, Metadata> schemaCache;
    private Object                schemaCacheSync;
    private Object                mappingsSync;

    private XContentBuilder getIndexSettings()
        throws IOException
    {

        DatastoreSettings config = DatastoreSettings.getInstance();
        String idxRefreshInterval = config.getString(DatastoreSettingKey.ELASTICSEARCH_IDX_REFRESH_INTERVAL);

        XContentBuilder builder = XContentFactory.jsonBuilder()
        .startObject()
            .startObject("index")
                .field("refresh_interval", idxRefreshInterval)
            .endObject()
        .endObject();

        return builder;
    }

    private XContentBuilder getAssetTypeBuilder(boolean allEnable, boolean sourceEnable)
        throws IOException
    {
        XContentBuilder builder = XContentFactory.jsonBuilder()
         .startObject()
             .startObject(ASSET_TYPE_NAME)
                 .startObject("_source")
                     .field("enabled", sourceEnable)
                 .endObject()
                 .startObject("_all")
                     .field("enabled", allEnable)
                 .endObject()
                 .startObject("properties")
                     .startObject(ASSET_NAME)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(ASSET_TIMESTAMP)
                         .field("type", "date")
                         // .field("format", "basic_date_time||basic_date_time_no_millis||epoch_millis")
                     .endObject()
                     .startObject(ASSET_ACCOUNT)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(ASSET_MESSAGE_ID)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                  .endObject() // End Of Properties
             .endObject() // End of type
         .endObject();

        return builder;
    }
//
//    private XContentBuilder getAssetTopicTypeBuilder(boolean allEnable, boolean sourceEnable)
//        throws IOException
//    {
//        XContentBuilder builder = XContentFactory.jsonBuilder()
//         .startObject()
//             .startObject(ASSET_TOPIC_TYPE_NAME)
//                 .startObject("_source")
//                     .field("enabled", sourceEnable)
//                 .endObject()
//                 .startObject("_all")
//                     .field("enabled", allEnable)
//                 .endObject()
//                 .startObject("properties")
//                     .startObject(ASSET_TOPIC_AS_NAME)
//                         .field("type", "string")
//                         .field("index", "not_analyzed")
//                     .endObject()
//                     .startObject(ASSET_TOPIC_ACCOUNT)
//                         .field("type", "string")
//                         .field("index", "not_analyzed")
//                     .endObject()
//                     .startObject(ASSET_TOPIC_TPC)
//                         .field("type", "object")
//                         .field("enabled", true)
//                         .field("dynamic", false)
//                         .field("include_in_all", false)
//                         .startObject("properties")
//                             .startObject(ASSET_TOPIC_TPC_NAME)
//                                 .field("type", "string")
//                                 .field("index", "not_analyzed")
//                             .endObject()
//                             .startObject(ASSET_TOPIC_TPC_TIMESTAMP)
//                                 .field("type", "date")
//                             .endObject()
//                             .startObject(ASSET_TOPIC_TPC_MSG_ID)
//                                 .field("type", "string")
//                                 .field("index", "not_analyzed")
//                             .endObject()
//                         .endObject() // End of properties
//                     .endObject() // End of topics
//                 .endObject() // End Of Properties
//             .endObject() // End of type
//         .endObject();
//
//        return builder;
//    }

    private XContentBuilder getMetricTypeBuilder(boolean allEnable, boolean sourceEnable)
        throws IOException
    {
        XContentBuilder builder = XContentFactory.jsonBuilder()
         .startObject()
             .startObject(METRIC_TYPE_NAME)
                 .startObject("_source")
                     .field("enabled", sourceEnable)
                 .endObject()
                 .startObject("_all")
                     .field("enabled", allEnable)
                 .endObject()
                 .startObject("properties")
                     .startObject(METRIC_ACCOUNT)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(METRIC_ASSET)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(METRIC_SEM_NAME)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(METRIC_MTR)
                         .field("type", "object")
                         .field("enabled", true)
                         .field("dynamic", false)
                         .field("include_in_all", false)
                         .startObject("properties")
                             .startObject(METRIC_MTR_NAME)
                                 .field("type", "string")
                                 .field("index", "not_analyzed")
                             .endObject()
                             .startObject(METRIC_MTR_TYPE)
                                 .field("type", "string")
                                 .field("index", "not_analyzed")
                             .endObject()
                             .startObject(METRIC_MTR_VALUE)
                                 .field("type", "string")
                                 .field("index", "not_analyzed")
                             .endObject()
                             .startObject(METRIC_MTR_TIMESTAMP)
                                 .field("type", "date")
                             .endObject()
                             .startObject(METRIC_MTR_MSG_ID)
                                 .field("type", "string")
                                 .field("index", "not_analyzed")
                             .endObject()
                         .endObject() // End of properties
                     .endObject() // End of metrics
                 .endObject() // End Of Properties
             .endObject() // End of type
         .endObject();

        return builder;
    }

    private XContentBuilder getTopicTypeBuilder(boolean allEnable, boolean sourceEnable)
        throws IOException
    {
        XContentBuilder builder = XContentFactory.jsonBuilder()
                                                 .startObject()
                                                 .startObject(TOPIC_TYPE_NAME)
                                                 .startObject("_source")
                                                 .field("enabled", sourceEnable)
                                                 .endObject()
                                                 .startObject("_all")
                                                 .field("enabled", allEnable)
                                                 .endObject()
                                                 .startObject("properties")
                                                 .startObject(TOPIC_ACCOUNT)
                                                 .field("type", "string")
                                                 .field("index", "not_analyzed")
                                                 .endObject()
                                                 .startObject(TOPIC_ASSET)
                                                 .field("type", "string")
                                                 .field("index", "not_analyzed")
                                                 .endObject()
                                                 .startObject(TOPIC_SEM_NAME)
                                                 .field("type", "string")
                                                 .field("index", "not_analyzed")
                                                 .endObject()
                                                 .startObject(TOPIC_TIMESTAMP)
                                                 .field("type", "date")
                                                 // .field("format", "basic_date_time||basic_date_time_no_millis||epoch_millis")
                                                 .endObject()
                                                 .startObject(TOPIC_MESSAGE_ID)
                                                 .field("type", "string")
                                                 .field("index", "not_analyzed")
                                                 .endObject()
                                                 .endObject() // End Of Properties
                                                 .endObject() // End of type
                                                 .endObject();

        return builder;
    }

    private XContentBuilder getMessageTypeBuilder(boolean allEnable, boolean sourceEnable)
        throws IOException
    {
        XContentBuilder builder = XContentFactory.jsonBuilder()
         .startObject()
             .startObject(MESSAGE_TYPE_NAME)
                 .startObject("_source")
                     .field("enabled", sourceEnable)
                 .endObject()
                 .startObject("_all")
                     .field("enabled", allEnable)
                 .endObject()
                 .startObject("properties")
                     .startObject(EsSchema.MESSAGE_TIMESTAMP)
                         .field("type", "date")
                         // .field("format", "basic_date_time||basic_date_time_no_millis||epoch_millis")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_RECEIVED_ON)
                         .field("type", "date")
                         // .field("format", "basic_date_time||basic_date_time_no_millis||epoch_millis")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_IP_ADDRESS)
                         .field("type", "ip")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_ACCOUNT)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_AS_NAME)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_SEM_TOPIC)
                         .field("type", "string")
                         .field("index", "not_analyzed")
                     .endObject()
                     // .startObject(EsSchema.MESSAGE_TOPIC_PARTS)
                         // .field("type", "string")
                         // .field("index", "not_analyzed")
                     // .endObject()
                     .startObject(EsSchema.MESSAGE_COLLECTED_ON)
                         .field("type", "date")
                     .endObject()
                     .startObject(EsSchema.MESSAGE_POS)
                         .field("type", "object")
                         .field("enabled", true)
                         .field("dynamic", false)
                         .field("include_in_all", false)
                         .startObject("properties")
                             .startObject(EsSchema.MESSAGE_POS_LOCATION)
                                 .field("type", "geo_point")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_ALT)
                                 .field("type", "double")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_PRECISION)
                                 .field("type", "double")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_HEADING)
                                 .field("type", "double")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_SPEED)
                                 .field("type", "double")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_TIMESTAMP)
                                 .field("type", "date")
                                 // .field("format", "basic_date_time||basic_date_time_no_millis||epoch_millis")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_SATELLITES)
                                 .field("type", "integer")
                             .endObject()
                             .startObject(EsSchema.MESSAGE_POS_STATUS)
                                 .field("type", "integer")
                             .endObject()
                         .endObject()
                     .endObject()
                     .startObject(EsSchema.MESSAGE_MTR)
                         .field("type", "object")
                         .field("enabled", true)
                         .field("dynamic", true)
                         .field("include_in_all", false)
                     .endObject()
                     .startObject(EsSchema.MESSAGE_BODY)
                         .field("type", "binary")
                         .field("index", "no")
                     .endObject()
                 .endObject() // End Of Properties
             .endObject();

        return builder;
    }

    private XContentBuilder getNewMessageMappingsBuilder(Map<String, EsMetric> esMetrics)
        throws IOException, ParseException
    {

        final int METRIC_TERM = 0;
        // final int TYPE_TERM = 1;

        if (esMetrics == null)
            return null;

        // It is assumed the mappings (key values) are all of the type
        // metrics.metric_name.type
        XContentBuilder builder = XContentFactory.jsonBuilder()
         .startObject()
         .startObject(MESSAGE_TYPE_NAME)
         .startObject("properties")
         .startObject(EsSchema.MESSAGE_MTR)
         .startObject("properties");

        // TODO precondition for the loop: there are no two consecutive mappings for the same field with
        // two different types (field are all different)

        String[] prevKeySplit = new String[] { "", "" };
        Set<String> keys = esMetrics.keySet();
        for (String key : keys) {

            EsMetric metric = esMetrics.get(key);
            String[] keySplit = key.split(Pattern.quote("."));

            if (!keySplit[METRIC_TERM].equals(prevKeySplit[METRIC_TERM])) {
                if (!prevKeySplit[METRIC_TERM].isEmpty()) {
                    builder.endObject(); // Previously open properties section
                    builder.endObject(); // Previously open metric-object section
                }
                builder.startObject(metric.getName()); // Start new metric object
                builder.startObject("properties"); // Start new object properties section
            }

            if (!keySplit[METRIC_TERM].equals(prevKeySplit[METRIC_TERM])) {
                builder.startObject(EsUtils.getEsTypeAcronym(metric.getType()));
                builder.field("type", metric.getType());
                if (metric.getType().equals("string"))
                    builder.field("index", "not_analyzed");
                builder.endObject();
            }

            prevKeySplit = keySplit;
        }

        if (keys.size() > 0) {
            if (!prevKeySplit[METRIC_TERM].isEmpty()) {
                builder.endObject(); // Previously open properties section
                builder.endObject(); // Previously open metrics-object section
            }
        }

        builder.endObject() // Properties
               .endObject() // Metrics
               .endObject() // Properties
               .endObject() // Type
               .endObject(); // Root

        return builder;
    }

    private void initMessageMappings(String indexName, boolean allEnable, boolean sourceEnable)
        throws IOException, EsDatastoreException
    {

        Client esClient = EsClient.getcurrent();

        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(indexName);
        GetMappingsResponse mappingsResponse = esClient.admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(indexName);
        MappingMetaData metadata = map.get(MESSAGE_TYPE_NAME);
        if (metadata == null) {
            XContentBuilder builder = this.getMessageTypeBuilder(allEnable, sourceEnable);
            esClient.admin().indices().preparePutMapping(indexName).setType(MESSAGE_TYPE_NAME).setSource(builder).execute().actionGet();
            s_logger.trace("Message mapping created: " + builder.string());
        }
    }

    private void initTopicMappings(String indexName, boolean allEnable, boolean sourceEnable)
        throws IOException, EsDatastoreException
    {

        Client esClient = EsClient.getcurrent();

        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(indexName);
        GetMappingsResponse mappingsResponse = esClient.admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(indexName);
        MappingMetaData metadata = map.get(TOPIC_TYPE_NAME);
        if (metadata == null) {
            XContentBuilder builder = this.getTopicTypeBuilder(allEnable, sourceEnable);
            esClient.admin().indices().preparePutMapping(indexName).setType(TOPIC_TYPE_NAME).setSource(builder).execute().actionGet();
            s_logger.trace("Topic mapping created: " + builder.string());
        }
    }

    private void initMetricMappings(String indexName, boolean allEnable, boolean sourceEnable)
        throws IOException, EsDatastoreException
    {

        Client esClient = EsClient.getcurrent();

        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(indexName);
        GetMappingsResponse mappingsResponse = esClient.admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(indexName);
        MappingMetaData metadata = map.get(METRIC_TYPE_NAME);
        if (metadata == null) {
            XContentBuilder builder = this.getMetricTypeBuilder(allEnable, sourceEnable);
            esClient.admin().indices().preparePutMapping(indexName).setType(METRIC_TYPE_NAME).setSource(builder).execute().actionGet();
            s_logger.trace("Topic_metric mapping created: " + builder.string());
        }
    }

    private void initAssetMappings(String indexName, boolean allEnable, boolean sourceEnable)
        throws IOException, EsDatastoreException
    {

        Client esClient = EsClient.getcurrent();

        // Check message type mapping
        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(indexName);
        GetMappingsResponse mappingsResponse = esClient.admin().indices().getMappings(mappingsRequest).actionGet();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(indexName);
        MappingMetaData metadata = map.get(ASSET_TYPE_NAME);
        if (metadata == null) {
            XContentBuilder builder = this.getAssetTypeBuilder(allEnable, sourceEnable);
            esClient.admin().indices().preparePutMapping(indexName).setType(ASSET_TYPE_NAME).setSource(builder).execute().actionGet();
            s_logger.trace("Asset mapping created: " + builder.string());
        }
    }
//
//    private void initAssetTopicMappings(String indexName, boolean allEnable, boolean sourceEnable)
//        throws IOException, EsDatastoreException
//    {
//
//        Client esClient = EsClient.getcurrent();
//
//        // Check message type mapping
//        GetMappingsRequest mappingsRequest = new GetMappingsRequest().indices(indexName);
//        GetMappingsResponse mappingsResponse = esClient.admin().indices().getMappings(mappingsRequest).actionGet();
//        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = mappingsResponse.getMappings();
//        ImmutableOpenMap<String, MappingMetaData> map = mappings.get(indexName);
//        MappingMetaData metadata = map.get(ASSET_TOPIC_TYPE_NAME);
//        if (metadata == null) {
//            XContentBuilder builder = this.getAssetTopicTypeBuilder(allEnable, sourceEnable);
//            esClient.admin().indices().preparePutMapping(indexName).setType(ASSET_TOPIC_TYPE_NAME).setSource(builder).execute().actionGet();
//            s_logger.trace("Asset_topic mapping created: " + builder.string());
//        }
//    }

    private Map<String, EsMetric> getMessageMappingDiffs(Metadata currentMetadata, Map<String, EsMetric> esMetrics)
    {

        if (esMetrics == null || esMetrics.size() == 0)
            return null;

        Entry<String, EsMetric> el;
        Map<String, EsMetric> diffs = null;
        Iterator<Entry<String, EsMetric>> iter = esMetrics.entrySet().iterator();
        while (iter.hasNext()) {

            el = iter.next();
            if (!currentMetadata.getMessageMappingsCache().containsKey(el.getKey())) {

                if (diffs == null)
                    diffs = new HashMap<String, EsMetric>(100);

                currentMetadata.getMessageMappingsCache().put(el.getKey(), el.getValue());
                diffs.put(el.getKey(), el.getValue());
            }
        }

        return diffs;
    }

    public EsSchema()
    {
        schemaCache = new HashMap<String, Metadata>();
        schemaCacheSync = new Object();
        mappingsSync = new Object();
    }

    public Metadata synch(String accountName, long time)
        throws IOException, EsDatastoreException
    {

        String newIndex = EsUtils.getActualIndexName(accountName, time);

        synchronized (schemaCacheSync) {
            if (schemaCache.containsKey(newIndex)) {
                Metadata currentMetadata = schemaCache.get(newIndex);
                return currentMetadata;
            }
        }

        s_logger.info("Before entering updating metadata");

        Metadata currentMetadata = null;
        synchronized (mappingsSync) {
            s_logger.info("Entered updating metadata");
            currentMetadata = new Metadata();

            IndicesExistsResponse existsResponse = null;
            Client esClient = EsClient.getcurrent();

            // Check existence of the data index
            existsResponse = esClient.admin().indices()
                                     .exists(new IndicesExistsRequest(newIndex))
                                     .actionGet();

            boolean indexExists = existsResponse.isExists();
            if (!indexExists) {
                esClient.admin().indices()
                        .prepareCreate(newIndex)
                        .setSettings(this.getIndexSettings())
                        .execute()
                        .actionGet();

                s_logger.info("Data index created: " + newIndex);
            }

            boolean enableAllField = false;
            boolean enableSourceField = true;

            this.initMessageMappings(newIndex, enableAllField, enableSourceField);

            // Check existence of the kapua internal index
            String newKapuaMetadataIdx = EsUtils.getActualKapuaIndexName(accountName, time);
            existsResponse = esClient.admin().indices()
                                     .exists(new IndicesExistsRequest(newKapuaMetadataIdx))
                                     .actionGet();

            indexExists = existsResponse.isExists();
            if (!indexExists) {
                esClient.admin()
                        .indices()
                        .prepareCreate(newKapuaMetadataIdx)
                        .setSettings(this.getIndexSettings())
                        .execute()
                        .actionGet();

                s_logger.info("Metadata index created: " + newKapuaMetadataIdx);

                this.initTopicMappings(newKapuaMetadataIdx, enableAllField, enableSourceField);
                this.initMetricMappings(newKapuaMetadataIdx, enableAllField, enableSourceField);
                this.initAssetMappings(newKapuaMetadataIdx, enableAllField, enableSourceField);
            }

            currentMetadata.indexName = newIndex;
            currentMetadata.kapuaIndexName = newKapuaMetadataIdx;
            s_logger.info("Leaving updating metadata");
        }

        synchronized (schemaCacheSync) {
            // Current metadata can only increase the custom mappings
            // other fields does not change within the same account id
            // and custom mappings are not and must not be exposed to
            // outside this class to preserve thread safetyness
            schemaCache.put(newIndex, currentMetadata);
        }

        return currentMetadata;
    }

    public void updateMessageMappings(String accountName, long time, Map<String, EsMetric> esMetrics)
        throws IOException, ParseException, EsDatastoreException
    {

        if (esMetrics.size() == 0)
            return;

        Metadata currentMetadata = null;
        synchronized (schemaCacheSync) {
            String newIndex = EsUtils.getActualIndexName(accountName, time);
            currentMetadata = schemaCache.get(newIndex);
            assert currentMetadata != null : NULL_METADATA_MSG;
        }

        XContentBuilder builder = null;
        Map<String, EsMetric> diffs = null;

        synchronized (mappingsSync) {

            // Update mappings only if a metric is new (not in cache)
            diffs = this.getMessageMappingDiffs(currentMetadata, esMetrics);
            if (diffs == null || diffs.size() == 0)
                return;

            builder = this.getNewMessageMappingsBuilder(diffs);
        }

        s_logger.trace("Sending dynamic message mappings: " + builder.string());
        Client esClient = EsClient.getcurrent();
        esClient.admin().indices().preparePutMapping(currentMetadata.indexName)
                .setType(MESSAGE_TYPE_NAME)
                .setSource(builder)
                .execute().actionGet();
    }
}
