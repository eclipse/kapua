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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
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
 *            Setting key type
 *
 * @since 1.0.0
 */
public abstract class AbstractKapuaSetting<K extends SettingKey> extends AbstractBaseKapuaSetting<K> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractKapuaSetting.class);

    private static final String EXTERNAL_CONFIG_FILE_PARAM = "kapua.config.file";
    private static final String EXTERNAL_CONFIG_DIR_PARAM = "kapua.config.dir";

    /**
     * Constructor.
     *
     * @param configResourceName
     * 
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
            String externalConfigResourceName = System.getProperty(EXTERNAL_CONFIG_FILE_PARAM);
            if (externalConfigResourceName != null) {
                loadConfigResource(compositeConfig, externalConfigResourceName);
            }

            // External configuration folder loading
            String externalConfigResourceFolder = System.getProperty(EXTERNAL_CONFIG_DIR_PARAM);
            if (externalConfigResourceFolder != null) {
                loadConfigResources(compositeConfig, externalConfigResourceFolder);
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
     * Search the configuration resources in the given folder name and loads them into the given {@link CompositeConfiguration} parameter.<br>
     * If the given folder name refer to a file or a not existing folder an error will be thrown.
     * If the folder exists and it is empty, no error will be thrown.
     * Note that this can only work with local files.
     * 
     * @param compositeConfig
     *            The {@link CompositeConfiguration} where load the resources.
     * @param configResourceFolderName
     *            The folder path to scan.
     * @throws KapuaSettingException
     *             When folder is not found, or loading them causes an exception.
     * 
     * @see #loadConfigResource(CompositeConfiguration, String)
     * 
     * @since 1.0.0
     */
    private static void loadConfigResources(CompositeConfiguration compositeConfig, String configResourceFolderName) throws KapuaSettingException {

        if (!configResourceFolderName.endsWith("/")) {
            configResourceFolderName = configResourceFolderName.concat("/");
        }

        File configsDir = new File(configResourceFolderName);
        if (configsDir.exists() || configsDir.isDirectory()) {
            // Exclude hidden files
            String[] configFileNames = configsDir.list((dir, name) -> !name.startsWith("."));

            if (configFileNames != null && configFileNames.length > 0) {
                for (String configFileName : configFileNames) {

                    String fileFullPath = String.join("", "file://", configResourceFolderName, configFileName);

                    // Ignore files that arent named '*.properties'
                    if (fileFullPath.endsWith(".properties")) {
                        loadConfigResource(compositeConfig, fileFullPath);
                    } else {
                        LOG.warn(String.format("Ignored file: '%s'", fileFullPath));
                    }
                }
            } else {
                LOG.warn(String.format("Empty config folder: '%s'", configResourceFolderName));
            }
        } else {
            LOG.error(String.format("Unable to locate folder: '%s'", configResourceFolderName));
            throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, null, configResourceFolderName);
        }
    }

    /**
     * Search the configuration resource name and loads it into the given {@link CompositeConfiguration} parameter.<br>
     * It can handle resources on the class path and file in the file system (prefixed by 'file://')
     * or file over HTTP/HTTPS (prefixed by 'http://'|'https://')
     * 
     * @param compositeConfig
     *            The {@link CompositeConfiguration} where load the given resource.
     * @param configResourceName
     *            The resource name to search and load.
     * @throws KapuaSettingException
     *             When error occurs while loading setting resource.
     * 
     * @since 1.0.0
     */
    private static void loadConfigResource(CompositeConfiguration compositeConfig, String configResourceName) throws KapuaSettingException {

        URL configUrl = null;
        try {
            if (hasValidScheme(configResourceName)) {
                configUrl = new URL(configResourceName);
            } else {
                configUrl = ResourceUtils.getResource(configResourceName);
            }

            if (configUrl != null) {
                compositeConfig.addConfiguration(new PropertiesConfiguration(configUrl));
                LOG.debug("Loaded configuration resource: '{}'", configResourceName);
            } else {
                LOG.error("Unable to locate configuration resource: '{}'", configResourceName);
                throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, null, configResourceName);
            }
        } catch (MalformedURLException mue) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, mue, configResourceName);
        } catch (ConfigurationException ce) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_FILE, ce, configUrl);
        }
    }

    /**
     * Scans the given string URL to check that contains a valid scheme.<br>
     * Scheme accepted are:
     * <ul>
     * <li>file://</li>
     * <li>http://</li>
     * <li>https://</li>
     * </ul>
     * 
     * @param stringURL
     *            The URL in {@link String} form to check
     * @return {@code true} if it is a valid {@link URL}, false otherwise
     * 
     * @since 1.0.0
     */
    private static boolean hasValidScheme(String stringURL) {
        return stringURL != null && (stringURL.startsWith("file://") || stringURL.startsWith("http://") || stringURL.startsWith("https://"));
    }
}
