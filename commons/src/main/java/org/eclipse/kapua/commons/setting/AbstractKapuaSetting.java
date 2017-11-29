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

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DataConfiguration;
import org.apache.commons.configuration.EnvironmentConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.eclipse.kapua.commons.util.KapuaFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;

/**
 * Setting reference abstract implementation.
 *
 * @param <K> Setting key type
 * @since 1.0.0
 */
public abstract class AbstractKapuaSetting<K extends SettingKey> extends AbstractBaseKapuaSetting<K> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractKapuaSetting.class);

    private static final String EXTERNAL_CONFIG_URL_PARAM = "kapua.config.url";
    private static final String EXTERNAL_CONFIG_DIR_PARAM = "kapua.config.dir";

    /**
     * Constructor.
     *
     * @param configResourceName
     * @since 1.0.0
     */
    protected AbstractKapuaSetting(String configResourceName) {
        super(createCompositeSource(configResourceName));
    }

    private static DataConfiguration createCompositeSource(String configResourceName) throws ExceptionInInitializerError {
        CompositeConfiguration compositeConfig = new EnvFriendlyConfiguration();
        compositeConfig.addConfiguration(new SystemConfiguration());
        compositeConfig.addConfiguration(new EnvironmentConfiguration());

        try {
            // External configuration file loading
            String externalConfigResourceUrl = System.getProperty(EXTERNAL_CONFIG_URL_PARAM);
            if (externalConfigResourceUrl != null) {
                loadConfigResource(compositeConfig, externalConfigResourceUrl);
            }

            // External configuration directory loading
            String externalConfigResourceDir = System.getProperty(EXTERNAL_CONFIG_DIR_PARAM);
            if (externalConfigResourceDir != null) {
                loadConfigResources(compositeConfig, externalConfigResourceDir);
            }

            // Default configuration file loading
            loadConfigResource(compositeConfig, configResourceName);
        } catch (Exception e) {
            LOG.error("Error loading PropertiesConfiguration", e);
            throw new ExceptionInInitializerError(e);
        }

        return new DataConfiguration(compositeConfig);
    }

    /**
     * Search the configuration resources in the given directory name and loads them into the given {@link CompositeConfiguration} parameter.<br>
     * If the given directory name refer to a file or a not existing directory an error will be thrown.
     * If the directory exists and it is empty, no error will be thrown.
     * Note that this can only work with local files.
     *
     * @param compositeConfig       The {@link CompositeConfiguration} where load the resources.
     * @param configResourceDirName The directory path to scan.
     * @throws KapuaSettingException When directory is not found, or loading them causes an exception.
     * @see #loadConfigResource(CompositeConfiguration, String)
     * @since 1.0.0
     */
    private static void loadConfigResources(CompositeConfiguration compositeConfig, String configResourceDirName) throws KapuaSettingException {

        if (!configResourceDirName.endsWith("/")) {
            configResourceDirName = configResourceDirName.concat("/");
        }

        File configsDir = new File(configResourceDirName);
        if (configsDir.exists() || configsDir.isDirectory()) {
            // Exclude hidden files
            String[] configFileNames = configsDir.list((dir, name) -> !name.startsWith("."));

            if (configFileNames != null && configFileNames.length > 0) {
                for (String configFileName : configFileNames) {

                    String fileFullPath = String.join("", "file://", configResourceDirName, configFileName);

                    // Ignore files that arent named '*.properties'
                    if (fileFullPath.endsWith(".properties")) {
                        loadConfigResource(compositeConfig, fileFullPath);
                    } else {
                        LOG.warn(String.format("Ignored file: '%s'", fileFullPath));
                    }
                }
            } else {
                LOG.warn(String.format("Empty config directory: '%s'", configResourceDirName));
            }
        } else {
            LOG.error(String.format("Unable to locate directory: '%s'", configResourceDirName));
            throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, null, configResourceDirName);
        }
    }

    /**
     * Search the configuration resource name and loads it into the given {@link CompositeConfiguration} parameter.<br>
     * It can handle resources on the class path and file in the file system (prefixed by 'file://')
     * or file over HTTP/HTTPS (prefixed by 'http://'|'https://')
     *
     * @param compositeConfig    The {@link CompositeConfiguration} where load the given resource.
     * @param configResourceName The resource name to search and load.
     * @throws KapuaSettingException When error occurs while loading setting resource.
     * @since 1.0.0
     */
    private static void loadConfigResource(CompositeConfiguration compositeConfig, String configResourceName) throws KapuaSettingException {

        URL configUrl = null;
        try {
            configUrl = KapuaFileUtils.getAsURL(configResourceName);

            if (configUrl != null) {
                compositeConfig.addConfiguration(new PropertiesConfiguration(configUrl));
                LOG.debug("Loaded configuration resource: '{}'", configResourceName);
            } else {
                LOG.error("Unable to locate configuration resource: '{}'", configResourceName);
                throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, null, configResourceName);
            }
        } catch (ConfigurationException ce) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_FILE, ce, configUrl);
        }
    }

}
