/*******************************************************************************
 * Copyright (c) 2020, 2021 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting.system;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class SystemSettingKeyTest extends Assert {

    @Test
    public void keyTest() {
        SystemSettingKey[] systemSettings = {SystemSettingKey.SYS_PROVISION_ACCOUNT_NAME, SystemSettingKey.SYS_ADMIN_ACCOUNT,
                SystemSettingKey.SYS_ADMIN_USERNAME, SystemSettingKey.VERSION, SystemSettingKey.BUILD_VERSION, SystemSettingKey.BUILD_NUMBER,
                SystemSettingKey.CHAR_ENCODING, SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER, SystemSettingKey.DB_NAME, SystemSettingKey.DB_USERNAME,
                SystemSettingKey.DB_PASSWORD, SystemSettingKey.DB_JDBC_DRIVER, SystemSettingKey.DB_CONNECTION_SCHEME, SystemSettingKey.DB_CONNECTION_HOST,
                SystemSettingKey.DB_CONNECTION_PORT, SystemSettingKey.DB_CONNECTION_USE_SSL, SystemSettingKey.DB_CONNECTION_TRUSTSTORE_URL,
                SystemSettingKey.DB_CONNECTION_TRUSTSTORE_PWD, SystemSettingKey.DB_CONNECTION_ADDITIONAL_OPTIONS, SystemSettingKey.DB_SCHEMA,
                SystemSettingKey.DB_SCHEMA_ENV, SystemSettingKey.DB_SCHEMA_UPDATE, SystemSettingKey.DB_USE_TIMEZONE, SystemSettingKey.DB_USE_LEGACY_DATETIME_CODE,
                SystemSettingKey.DB_SERVER_TIMEZONE, SystemSettingKey.DB_CHAR_ENCODING, SystemSettingKey.DB_POOL_SIZE_MIN, SystemSettingKey.DB_POOL_SIZE_MAX,
                SystemSettingKey.DB_POOL_IDLE_TIMEOUT, SystemSettingKey.DB_POOL_KEEPALIVE_TIME, SystemSettingKey.DB_POOL_TEST_QUERY, SystemSettingKey.DB_CHARACTER_ESCAPE,
                SystemSettingKey.DB_CHARACTER_WILDCARD_ANY, SystemSettingKey.DB_CHARACTER_WILDCARD_SINGLE, SystemSettingKey.BROKER_SCHEME, SystemSettingKey.BROKER_HOST,
                SystemSettingKey.METRICS_ENABLE_JMX, SystemSettingKey.KAPUA_KEY_SIZE, SystemSettingKey.KAPUA_INSERT_MAX_RETRY, SystemSettingKey.EVENT_BUS_URL,
                SystemSettingKey.EVENT_BUS_USERNAME, SystemSettingKey.EVENT_BUS_PASSWORD, SystemSettingKey.EVENT_BUS_PRODUCER_POOL_MIN_SIZE,
                SystemSettingKey.EVENT_BUS_PRODUCER_POOL_MAX_SIZE, SystemSettingKey.EVENT_BUS_PRODUCER_POOL_BORROW_WAIT_MAX, SystemSettingKey.EVENT_BUS_PRODUCER_EVICTION_INTERVAL,
                SystemSettingKey.EVENT_BUS_CONSUMER_POOL_SIZE, SystemSettingKey.EVENT_BUS_MESSAGE_SERIALIZER, SystemSettingKey.EVENT_BUS_TRANSPORT_USE_EPOLL,
                SystemSettingKey.HOUSEKEEPER_EXECUTION_WAIT_TIME, SystemSettingKey.HOUSEKEEPER_EVENT_SCAN_WINDOW, SystemSettingKey.HOUSEKEEPER_OLD_MESSAGES_TIME_WINDOW,
                SystemSettingKey.SETTINGS_HOTSWAP, SystemSettingKey.CACHING_PROVIDER, SystemSettingKey.TMETADATA_LOCAL_CACHE_SIZE_MAXIMUM, SystemSettingKey.CACHE_CONFIG_URL,
                SystemSettingKey.CACHE_TTL, SystemSettingKey.JCACHE_EXPIRY_POLICY};

        String[] expectedValue = {"commons.sys.provision.account.name", "commons.sys.admin.account", "commons.sys.admin.userName", "commons.version", "commons.build.version",
                "commons.build.number", "character.encoding", "commons.db.jdbcConnectionUrlResolver", "commons.db.name", "commons.db.username", "commons.db.password",
                "commons.db.jdbc.driver", "commons.db.connection.scheme", "commons.db.connection.host", "commons.db.connection.port", "commons.db.connection.useSsl",
                "commons.db.connection.trust.store.url", "commons.db.connection.trust.store.pwd", "commons.db.connection.additionalOptions", "commons.db.schema",
                "COMMONS_DB_SCHEMA", "commons.db.schema.update", "commons.db.useTimezone", "commons.db.useLegacyDatetimeCode", "commons.db.serverTimezone", "commons.db.characterEncoding",
                "commons.db.pool.size.min", "commons.db.pool.size.max", "commons.db.pool.idle.timeout", "commons.db.pool.keepalive.timeout", "commons.db.pool.test.query",
                "commons.db.character.escape", "commons.db.character.wildcard.any", "commons.db.character.wildcard.single", "broker.scheme", "broker.host", "metrics.enable.jmx",
                "commons.entity.key.size", "commons.entity.insert.max.retry", "commons.eventbus.url", "commons.eventbus.username",
                "commons.eventbus.password", "commons.eventbus.producerPool.minSize", "commons.eventbus.producerPool.maxSize", "commons.eventbus.producerPool.maxWaitOnBorrow",
                "commons.eventbus.producerPool.evictionInterval", "commons.eventbus.consumerPool.size", "commons.eventbus.messageSerializer", "commons.eventbus.transport.useEpoll",
                "commons.eventbus.houskeeper.waitTime", "commons.eventbus.houskeeper.eventScanWindow", "commons.eventbus.houskeeper.oldMessagesTimeWindow", "commons.settings.hotswap",
                "commons.cache.provider.classname", "commons.cache.local.tmetadata.maxsize", "commons.cache.config.url", "commons.cache.config.ttl", "commons.cache.config.expiryPolicy"};

        for (int i = 0; i < systemSettings.length; i++) {
            assertEquals("Expected and actual values should be the same.", expectedValue[i], systemSettings[i].key());
        }
    }
}
