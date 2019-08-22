/*******************************************************************************
 * Copyright (c) 2016, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.apache.commons.lang.StringUtils;

import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;

/**
 * H2 Jdbc url connection resolver implementation
 *
 * @since 1.0
 */
public class H2JdbcConnectionUrlResolver implements JdbcConnectionUrlResolver {

    @Override
    public String connectionUrl() {

        String connectionUrl = "jdbc:h2:mem:kapua;MODE=MySQL";
        SystemSetting config = SystemSetting.getInstance();
        String additionalOptions = config.getString(SystemSettingKey.DB_CONNECTION_ADDITIONAL_OPTIONS);
        if (StringUtils.isNotBlank(additionalOptions)) {
            connectionUrl += ";" + additionalOptions;
        }

        return connectionUrl;
    }

}
