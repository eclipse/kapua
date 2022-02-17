/*******************************************************************************
 * Copyright (c) 2016, 2022 Red Hat and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JdbcConnectionUrlResolvers {

    private static final Logger LOG = LoggerFactory.getLogger(JdbcConnectionUrlResolvers.class);

    private JdbcConnectionUrlResolvers() {
    }

    public static String resolveJdbcUrl() {
        SystemSetting config = SystemSetting.getInstance();
        String connectionUrlResolverType = config.getString(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER, "DEFAULT");
        LOG.debug("The following JDBC connection URL resolver type will be used: {}", connectionUrlResolverType);
        switch (connectionUrlResolverType) {
            case "DEFAULT":
                return new DefaultConfigurableJdbcConnectionUrlResolver().connectionUrl();
            case "H2":
                return new H2JdbcConnectionUrlResolver().connectionUrl();
            case "MariaDB":
                return new MariaDBJdbcConnectionUrlResolver().connectionUrl();
            default:
                throw new IllegalArgumentException("Unknown JDBC connection URL resolver type: " + connectionUrlResolverType);
        }
    }

}
