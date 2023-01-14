/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;

import java.util.Properties;

/**
 * Configuration service definition.
 *
 * @since 1.0
 */
public interface ServiceConfig extends KapuaUpdatableEntity {

    /**
     * Service configuration type
     */
    public static final String TYPE = "scfg";

    /**
     * Return the service type
     */
    @Override
    public default String getType() {
        return TYPE;
    }

    /**
     * Return service pid
     *
     * @return test
     */
    public String getPid();

    /**
     * Set service pid
     *
     * @param pid
     */
    public void setPid(String pid);

    /**
     * Return service configurations
     *
     * @return test
     * @throws KapuaException
     */
    public Properties getConfigurations() throws KapuaException;

    /**
     * Set service configurations
     *
     * @param configurations
     * @throws KapuaException
     */
    public void setConfigurations(Properties configurations) throws KapuaException;
}
