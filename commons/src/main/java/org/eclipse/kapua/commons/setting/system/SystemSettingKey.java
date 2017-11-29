/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.setting.system;

import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Locale;

import javax.persistence.EntityExistsException;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * Setting system key implementation.
 *
 * @since 1.0
 */
public enum SystemSettingKey implements SettingKey {
    /**
     * Provisioning account name
     */
    SYS_PROVISION_ACCOUNT_NAME("commons.sys.provision.account.name"),
    /**
     * System administration account name
     */
    SYS_ADMIN_ACCOUNT("commons.sys.admin.account"),
    /**
     * System administration user name
     */
    SYS_ADMIN_USERNAME("commons.sys.admin.userName"),

    /**
     * Software version
     */
    VERSION("commons.version"),
    /**
     * Build version
     */
    BUILD_VERSION("commons.build.version"),
    /**
     * Build number
     */
    BUILD_NUMBER("commons.build.number"),

    /**
     * Character encoding
     */
    CHAR_ENCODING("character.encoding"),

    /**
     * Jdbc url connection resolver implementation
     */
    DB_JDBC_CONNECTION_URL_RESOLVER("commons.db.jdbcConnectionUrlResolver"),

    /**
     * Database name
     */
    DB_NAME("commons.db.name"),
    /**
     * Database username
     */
    DB_USERNAME("commons.db.username"),
    /**
     * Database password
     */
    DB_PASSWORD("commons.db.password"),

    /**
     * Database jdbc driver class
     */
    DB_JDBC_DRIVER("commons.db.jdbc.driver"),
    /**
     * Database connection schema
     */
    DB_CONNECTION_SCHEME("commons.db.connection.scheme"),
    /**
     * Database host
     */
    DB_CONNECTION_HOST("commons.db.connection.host"),
    /**
     * Database port
     */
    DB_CONNECTION_PORT("commons.db.connection.port"),
    /**
     * Database use ssl connection
     */
    DB_CONNECTION_USESSL("commons.db.connection.useSsl"),
    /**
     * Database verify ssl connection (trust server/client certificates)
     */
    DB_CONNECTION_VERIFYSSL("commons.db.connection.sslVerify"),
    /**
     * Database truststore url
     */
    DB_CONNECTION_TRUSTSTORE_URL("commons.db.connection.trust.store.url"),
    /**
     * Database truststore password
     */
    DB_CONNECTION_TRUSTSTORE_PWD("commons.db.connection.trust.store.pwd"),

    /**
     * Database schema name
     */
    DB_SCHEMA("commons.db.schema"),

    /**
     * Database schema name
     */
    DB_SCHEMA_ENV("COMMONS_DB_SCHEMA"),

    /**
     * Update database schema on startup
     */
    DB_SCHEMA_UPDATE("commons.db.schema.update"),

    /**
     * Database timezone to use
     */
    DB_USE_TIMEZONE("commons.db.useTimezone"),
    /**
     * Database use legacy date time flag
     */
    DB_USE_LEGACY_DATETIME_CODE("commons.db.useLegacyDatetimeCode"),
    /**
     * Database server timezone
     */
    DB_SERVER_TIMEZONE("commons.db.serverTimezone"),
    /**
     * Database character encoding
     */
    DB_CHAR_ENCODING("commons.db.characterEncoding"),

    /**
     * Database pool initial pool size
     */
    DB_POOL_SIZE_INITIAL("commons.db.pool.size.initial"),
    /**
     * Database pool minimum pool size
     */
    DB_POOL_SIZE_MIN("commons.db.pool.size.min"),
    /**
     * Database pool maximum pool size
     */
    DB_POOL_SIZE_MAX("commons.db.pool.size.max"),
    /**
     * Database pool connection borrow timeout
     */
    DB_POOL_BORROW_TIMEOUT("commons.db.pool.borrow.timeout"),

    /**
     * Broker schema (e.g. mqtt, mqtts, ..)
     */
    BROKER_SCHEME("broker.scheme"),
    /**
     * Broker host
     */
    BROKER_HOST("broker.host"),
    /**
     * Broker port
     */
    BROKER_PORT("broker.port"),

    /**
     * Metrics JMX disabled
     */
    METRICS_ENABLE_JMX("metrics.enable.jmx"),

    /**
     * Set the Kapua key size (the size is expressed in bits)
     */
    KAPUA_KEY_SIZE("commons.entity.key.size"),

    /**
     * Maximum allowed retry (due to a {@link EntityExistsException}, so already exists key) on insert operation
     */
    KAPUA_INSERT_MAX_RETRY("commons.entity.insert.max.retry"),

    /**
     * Locale zone ID (Default UTC). See {@link java.time.ZoneId} for more information about allowed settings for this parameter
     */
    LOCALE_ZONE_ID("commons.locale.zoneId"),
    /**
     * Locale ID (Default en_US). See {@link Locale} for more information about allowed settings for this parameter
     */
    LOCALE_ID("commons.locale.id"),
    /**
     * Local minimal days in first week (Deafault 7). See {@link WeekFields} for more information about allowed settings for this parameter
     */
    LOCALE_MINIMAL_DAYS_IN_FIRST_WEEK("commons.locale.minimalDaysInFirstWeek"),
    /**
     * Locale first day of the week. (Default 1 - 1-MONDAY/7-SUNDAY). See {@link DayOfWeek} for more information about allowed settings for this parameter
     */
    //1 MONDAY - 7 SUNDAY
    LOCALE_FIRST_DAY_OF_THE_WEEK("commons.locale.firstDayOfTheWeek"),

    /**
     * Url of the event bus
     */
    EVENT_BUS_URL("commons.eventbus.url"),

    /**
     * Username to connect to the event bus
     */
    EVENT_BUS_USERNAME("commons.eventbus.username"),

    /**
     * Password to connect to the event bus
     */
    EVENT_BUS_PASSWORD("commons.eventbus.password"),

    /**
     * Producers pool min size (per microservice)
     */
    EVENT_BUS_PRODUCER_POOL_MIN_SIZE("commons.eventbus.producerPool.minSize"),

    /**
     * Producers pool max size (per microservice)
     */
    EVENT_BUS_PRODUCER_POOL_MAX_SIZE("commons.eventbus.producerPool.maxSize"),

    /**
     * Producers pool max wait on borrow
     */
    EVENT_BUS_PRODUCER_POOL_BORROW_WAIT_MAX("commons.eventbus.producerPool.maxWaitOnBorrow"),

    /**
     * Producers pool eviction interval
     */
    EVENT_BUS_PRODUCER_EVICTION_INTERVAL("commons.eventbus.producerPool.evictionInterval"),

    /**
     * Consumers pool size (per microservice)
     */
    EVENT_BUS_CONSUMER_POOL_SIZE("commons.eventbus.consumerPool.size"),

    /**
     * Event message serializer class
     */
    EVENT_BUS_MESSAGE_SERIALIZER("commons.eventbus.messageSerializer"),

    /**
     * If true the transport will use the native Epoll layer when available instead of the NIO layer.
     */
    EVENT_BUS_TRANSPORT_USE_EPOLL("commons.eventbus.transport.useEpoll"),

    /**
     * Wait time between housekeeper executions (in milliseconds)
     */
    HOUSEKEEPER_EXECUTION_WAIT_TIME("commons.eventbus.houskeeper.waitTime"),
    /**
     * Housekeeper event scan window
     */
    HOUSEKEEPER_EVENT_SCAN_WINDOW("commons.eventbus.houskeeper.eventScanWindow"),
    /**
     * Time window to consider FIRED messages as "old" messages so ready to be processed by the housekeeper (in milliseconds)
     */
    HOUSEKEEPER_OLD_MESSAGES_TIME_WINDOW("commons.eventbus.houskeeper.oldMessagesTimeWindow");

    private String key;

    /**
     * Constructor
     *
     * @param key
     */
    private SystemSettingKey(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
