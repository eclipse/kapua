Elasticsearch Indices Migrator
==========

## Introduction

This module contains a Java Application that leverages the Elasticsearch High Level REST Client to change the Datastore
indices structure so that they are compatible with the new releases of Elasticsearch as the dependency is upgraded

## Background

Kapua, up until version 1.3, uses Elasticsearch 5.3, which supports multiple mapping types for a single index.
Specifically, the "registry" index for every account( named `.scopeId`, where `scopeId` is replaced by the actual Scope
ID) supports three different mapping types since it contains data from different, heterogeneous nature (`channel`,
`client` and `metric`); as said, such structure is no longer allowed in Elasticsearch 6.x
since [Mapping Types have been removed](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/removal-of-types.html)
.

Also, In Elasticsearch 7, indices starting with a `.` have been deprecated, so additional renaming will be needed.

## Changes in Kapua 1.4

In Kapua 1.4, the `.scopeId` index will be split in three different indices (`.scopeId-channel`, `.scopeId-client` and
`.scopeId-metric`, again with `scopeId` replaced by the actual Scope ID). Anyway, the change is not retroactive, meaning
that it won't apply automatically to already existing indices, that will make Elasticsearch 6.8 to fail during the
startup phase. Moreover, the Date format supported until Elasticsearch 5.x has been deprecated in 6.0 and will be
removed in 7.0, so in Kapua 1.4 a change to make the Datastore compatible with the new format will be released.

## Changes in Kapua 1.5

Kapua 1.5 upgrades its Elasticsearch version to 7.8.1. In Elasticsearch 7, all non-internal indices starting with a `.`
are being deprecated so that only internal indices can start with a `.`. As a result, all the indices will be renamed
according to the following pattern:

| Kapua 1.4 (ES 6) | Kapua 1.5 (ES 7)|
|---|---|
|`.scopeId-channel`|`scopeId-data-channel`|
|`.scopeId-client`|`scopeId-data-client`|
|`.scopeId-metric`|`scopeId-data-metric`|
|`scopeId-year-week`|`scopeId-data-message-year-week`|

## The ES indices Migration tool

This tool has been developed to migrate data in already existing Elasticsearch clusters from the old structure to the
new one.

According to the Elasticsearch version found at the specified endpoints, it will perform the needed operations in order
to have a working Kapua deployment without losing any data when updating to a new version. Please note that if you need
to upgrade from Kapua 1.3.x to Kapua 1.5.x, you CANNOT do a direct upgrade but you MUST upgrade to an intermediate Kapua
1.4.x and run the Migration tool once per every Kapua upgrade (so once you upgrade to Kapua 1.4.x, and again when
upgrading to Kapua 1.5.0). The Migration Tool implementation is different between Kapua 1.4.x and Kapua 1.5.x, so make
sure to use the correct version of the tool according to the currently deployed Kapua (and Elasticsearch) version.

When running in a Kapua 1.4.x (Elasticsearch 6) deployment, the application will first gather all Account IDs connecting
directly to the SQL database, and then scan all indices in a given Elasticsearch cluster and, for every registry index,
will create three new indices as described above and process a `reindex` operation of existing documents in new indices.
For every data index, instead, a new temporary index will be created with the new Date format, and a `reindex` will take
place; once completed, the old index will be deleted, and a new one with the same settings of the temporary one will be
created, with another `reindex` following, to restore the original name of the index. Old registry indices will not be
deleted, but rather closed.

When running in a Kapua 1.5.x (Elasticsearch 7) deployment, the tool will scan once again for the available Account IDs
and then proceed to rename all the indices as mentioned above

If your Kapua instance is configured to use a prefix for the Elasticsearch indices, via the `datastore.index.prefix`
system property, make sure to set the same `datastore.index.prefix` when running the Migration Tool, otherwise the
migration could fail. If your Elasticsearch cluster contains indices with multiple prefixes, enter all the values
separated with a `,`.

### Settings

The following settings are available as system properties when running the migration tool:

|Name|Description|Default Value|
|----|-----------|-------------|
|elasticsearch.cluster.nodes|The address of the ES cluster|`localhost:9200`|
|elasticsearch.cluster.ssl|Use HTTPS when reaching Elasticsearch|`false`|
|elasticsearch.cluster.ssl.ignore-certificate|Skip validation on the HTTPS certificate when reaching Elasticsearch|`false`|
|elasticsearch.username|The Elasticsearch Username|*empty value*|
|elasticsearch.password|The Elasticsearch Password|*empty value*|
|elasticsearch.socket-timeout|The Socket Timeout for the Elasticsearch client. See [docs](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/7.8/_timeouts.html)|`30000`|
|elasticsearch.batch-size|The batch size for bulk Elasticsearch reindex operations. See [docs](https://www.elastic.co/guide/en/elasticsearch/reference/7.8/docs-reindex.html#docs-reindex-routing)|`100`|
|elasticsearch.task-polling-interval|The time to wait to get updates about a long running Elasticsearch task|`30000`|
|datastore.index.refresh_interval|Elasticsearch Refresh interval for the new indices|`5s`|
|datastore.index.number_of_shards|Elasticsearch Number of Shards for the new indices|`1`|
|datastore.index.number_of_replicas|Elasticsearch Number of Replicas for the new indices|`0`|
|datastore.index.prefix|Elasticsearch Index Prefix; multiple values are allowed|*empty string*|
|datastore.index.migration-complete.action|The action to perform on the old index when successfully completing a reindex operation. Allowed values: `delete`, `close`, `none`|`delete`|
|migrator.report-to-file|Write a final report to file in the `reports` directory|`false`|
|migrator.jdbc.connection-string|The connection string to use to connect to the SQL database to scan for Account IDs|`jdbc:h2:tcp://localhost:3306/kapuadb;schema=kapuadb`|
|migrator.jdbc.username|The JDBC Username|`kapua`|
|migrator.jdbc.password|The JDBC Password|`kapua`|
