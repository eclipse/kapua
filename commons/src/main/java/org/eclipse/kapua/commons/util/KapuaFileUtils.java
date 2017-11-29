/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.util;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.commons.setting.KapuaSettingErrorCodes;
import org.eclipse.kapua.commons.setting.KapuaSettingException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility class that allows loading files from different locations according to the
 * path given.
 * <p>
 * <ul>
 * <li>External file: "file://*"</li>
 * <li>HTTP/HTTPS link: "(http|https)://*"</li>
 * <li>.jar resource: any other path</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class KapuaFileUtils {

    private KapuaFileUtils() {
    }

    /**
     * Gets from the given {@code filePath} parameter the resouce and returns its {@link URL} reference.
     *
     * @param filePath The file path to load.
     * @return The file's {@link URL} reference
     * @throws KapuaSettingException When the given {@code filePath} parameter is malformed or does not exits.
     */
    public static URL getAsURL(String filePath) throws KapuaSettingException {

        URL fileURL;
        try {
            if (hasValidScheme(filePath)) {
                fileURL = new URL(filePath);
            } else {
                fileURL = ResourceUtils.getResource(filePath);
            }

            if (fileURL == null) {
                throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, null, filePath);
            }
        } catch (MalformedURLException mue) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, mue, filePath);
        }

        return fileURL;
    }

    /**
     * Gets from the given {@code filePath} parameter the resouce and returns its {@link File} reference.
     *
     * @param filePath The file path to load.
     * @return The file's {@link File} reference.
     * @throws KapuaSettingException When the given {@code filePath} parameter is malformed or not accessible.
     */
    public static File getAsFile(String filePath) throws KapuaSettingException {
        File file;
        try {
            URL url = getAsURL(filePath);

            if (hasHttpScheme(filePath)) {
                // FIXME:
                file = File.createTempFile("file-" + System.currentTimeMillis(), ".tmp");
                FileUtils.copyURLToFile(url, file);
            } else {
                file = new File(url.toURI());
            }
        } catch (IOException ioe) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.RESOURCE_NOT_FOUND, ioe, filePath);
        } catch (URISyntaxException use) {
            throw new KapuaSettingException(KapuaSettingErrorCodes.INVALID_RESOURCE_NAME, use, filePath);
        }

        return file;
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
     * @param stringPath The path in {@link String} form to check
     * @return {@code true} if it is a valid {@link URL}, false otherwise
     * @since 1.0.0
     */
    private static boolean hasValidScheme(String stringPath) {
        return stringPath != null && (hasFileScheme(stringPath) || hasHttpScheme(stringPath));
    }

    private static boolean hasFileScheme(String stringPath) {
        return stringPath.startsWith("file://");
    }

    private static boolean hasHttpScheme(String stringPath) {
        return stringPath.startsWith("http://") || stringPath.startsWith("https://");
    }
}
