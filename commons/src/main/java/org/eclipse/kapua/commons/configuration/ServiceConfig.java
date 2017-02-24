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
 *******************************************************************************/
package org.eclipse.kapua.commons.configuration;

import java.util.Properties;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.KapuaUpdatableEntity;

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
    default public String getType() {
        return TYPE;
    }

    /**
     * Return service pid
     *
     * @return
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
     * @return
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
