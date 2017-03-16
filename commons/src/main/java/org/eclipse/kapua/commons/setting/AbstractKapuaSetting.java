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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.commons.setting;

import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Setting reference abstract implementation.
 *
 * @param <K>
 *            setting key type
 *
 * @since 1.0
 *
 */
public abstract class AbstractKapuaSetting<K extends SettingKey> extends AbstractBaseKapuaSetting<K> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractKapuaSetting.class);

    private static DataConfiguration createCompositeSource(String configResourceName) throws ExceptionInInitializerError {
        CompositeConfiguration compositeConfig = new EnvFriendlyConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        compositeConfig.addConfiguration(new EnvironmentConfiguration());
        try {
            URL configLocalUrl = ResourceUtils.getResource(configResourceName);
            if (configLocalUrl == null) {
                logger.warn("Unable to locate resource '{}'", configResourceName);
                throw new IllegalArgumentException(String.format("Unable to locate resource: '%s'", configResourceName));
            }
            compositeConfig.addConfiguration(new PropertiesConfiguration(configLocalUrl));
        } catch (Exception e) {
            logger.error("Error loading PropertiesConfiguration", e);
            throw new ExceptionInInitializerError(e);
        }

        return new DataConfiguration(compositeConfig);
    }

    /**
     * Constructor
     *
     * @param configResourceName
     */
    protected AbstractKapuaSetting(String configResourceName) {
        super(createCompositeSource(configResourceName));
    }
}
