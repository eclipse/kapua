Elasticsearch 6.8 Indexes Migrator
==========

## Introduction

This module contains a Java Application that leverages the Elasticsearch 6.8 High Level REST Client to change the
Datastore indexes structure so that they are compatible with Elasticsearch 6.8

## Background

Kapua, up until version 1.3, uses Elasticsearch 5.3, which supports multiple mapping types for a single index.
Specifically, the "registry" index for every account( named `.scopeId`, where `scopeId` is replaced by the actual
Scope ID) supports three different mapping types since it contains data from different, heterogeneous nature (`channel`,
`client` and `metric`); as said, such structure is no longer allowed in Elasticsearch 6.x since [Mapping Types have been
removed](https://www.elastic.co/guide/en/elasticsearch/reference/6.8/removal-of-types.html).

## Changes in Kapua 1.4

In Kapua 1.4, the `.scopeId` index will be split in three different indexes (`.scopeId-channel`, `.scopeId-client` and
`.scopeId-metric`, again with `scopeId` replaced by the actual Scope ID). Anyway, the change is not retroactive,
meaning that it won't apply automatically to already existing indexes, that will make Elasticsearch 6.8 to fail during
the startup phase. Moreover, the Date format supported until Elasticsearch 5.x has been deprecated in 6.0 and will be
removed in 7.0, so in Kapua 1.4 a change to make the Datastore compatible with the new format will be released.

## The ES Indexes Migration tool

This tool has been developed to migrate data in already existing Elasticsearch clusters from the old structure to the
new one.

The application will scan all indexes in a given Elasticsearch cluster and, for every registry index, will create three
new indexes as described above and process a `reindex` operation of existing documents in new indexes. For every data
index, instead, a new temporary index will be created with the new Date format, and a `reindex` will take place; once
completed, the old index will be deleted, and a new one with the same settings of the temporary one will be created,
with another `reindex` following, to restore the original name of the index. Old registry indexes will not be deleted,
but rather closed.

If your Kapua instance is configured to use a prefix for the Elasticsearch indexes, via the `datastore.index.prefix`
system property, make sure to set the same `datastore.index.prefix` when running the Migration Tool, otherwise the
migration could fail. If your Elasticsearch cluster contains indexes with multiple prefixes, enter all the values
separated with a `,`.

### Settings

The following settings are available as system properties when running the migration tool:

|Name|Description|Default Value|
|----|-----------|-------------|
|elasticsearch.cluster.address|The address of the ES cluster|localhost|
|elasticsearch.cluster.port|The port of the ES cluster|9200|
|elasticsearch.cluster.scheme|The scheme of the ES cluster. Can be `http` or `https`|http|
|datastore.index.refresh_interval|ES Refresh interval for the new indexes|5s|
|datastore.index.number_of_shards|ES Number of Shards for the new indexes|1|
|datastore.index.number_of_replicas|ES Number of Replicas for the new indexes|0|
|datastore.index.prefix|ES Index Prefix; multiple values are allowed|*empty string*|
