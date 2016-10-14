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
package org.eclipse.kapua.commons.setting.system;

import org.eclipse.kapua.commons.setting.SettingKey;

public enum SystemSettingKey implements SettingKey
{
    SYS_PROVISION_ACCOUNT_NAME("commons.sys.provision.account.name"),
    SYS_ADMIN_ACCOUNT("commons.sys.admin.account"),

    VERSION("commons.version"),
    BUILD_VERSION("commons.build.version"),
    BUILD_NUMBER("commons.build.number"),

    CHAR_ENCODING("character.encoding"),

    DB_JDBC_CONNECTION_URL_RESOLVER("commons.db.jdbcConnectionUrlResolver"),

    DB_NAME("commons.db.name"),
    DB_USERNAME("commons.db.username"),
    DB_PASSWORD("commons.db.password"),

    DB_JDBC_DRIVER("commons.db.jdbc.driver"),
    DB_CONNECTION_SCHEME("commons.db.connection.scheme"),
    DB_CONNECTION_HOST("commons.db.connection.host"),
    DB_CONNECTION_PORT("commons.db.connection.port"),
    DB_CONNECTION_USESSL("commons.db.connection.useSsl"),
    DB_CONNECTION_VERIFYSSL("commons.db.connection.sslVerify"),
    DB_CONNECTION_TRUSTSTORE_URL("commons.db.connection.trust.store.url"),
    DB_CONNECTION_TRUSTSTORE_PWD("commons.db.connection.trust.store.pwd"),

    DB_SCHEMA("commons.db.schema"),
    DB_USE_TIMEZIONE("commons.db.useTimezone"),
    DB_USE_LEGACY_DATETIME_CODE("commons.db.useLegacyDatetimeCode"),
    DB_SERVER_TIMEZONE("commons.db.serverTimezone"),
    DB_CHAR_ENCODING("commons.db.characterEncoding"),

    DB_POOL_SIZE_INITIAL("commons.db.pool.size.initial"),
    DB_POOL_SIZE_MIN("commons.db.pool.size.min"),
    DB_POOL_SIZE_MAX("commons.db.pool.size.max"),
    DB_POOL_BORROW_TIMEOUT("commons.db.pool.borrow.timeout"),

    BROKER_SCHEME("broker.scheme"),
    BROKER_HOST("broker.host"),
    BROKER_PORT("broker.port"),

    OSGI_CONTEXT("commons.osgi.context");

    private String key;

    private SystemSettingKey(String key)
    {
        this.key = key;
    }

    public String key()
    {
        return key;
    }
}
