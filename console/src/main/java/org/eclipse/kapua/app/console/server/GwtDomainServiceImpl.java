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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.Sanselan;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtAction;
import org.eclipse.kapua.app.console.shared.model.GwtPermission.GwtDomain;
import org.eclipse.kapua.app.console.shared.service.GwtDomainService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authorization.domain.*;
import org.eclipse.kapua.service.authorization.domain.shiro.DomainPredicates;
import org.eclipse.kapua.service.authorization.permission.Action;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;

public class GwtDomainServiceImpl extends KapuaRemoteServiceServlet implements GwtDomainService {

    /**
     *
     */
    private static final long serialVersionUID = -699492835893299489L;
    private static Logger logger = LoggerFactory.getLogger(GwtDeviceManagementServiceImpl.class);


    @Override
    public List<GwtDomain> findAll() throws GwtKapuaException {
        List<GwtDomain> gwtDomainList = new ArrayList<GwtDomain>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainService domainService = locator.getService(DomainService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery();
            DomainListResult list = domainService.query(query);

            for (Domain domain : list.getItems()) {
                gwtDomainList.add(KapuaGwtModelConverter.convertDomain(domain.getName()));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        Collections.sort(gwtDomainList);
        return gwtDomainList;
    }

    @Override
    public List<GwtAction> findActionsByDomainName(String domainName) throws GwtKapuaException {
        List<GwtAction> gwtActionList = new ArrayList<GwtAction>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            DomainService domainService = locator.getService(DomainService.class);
            DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
            DomainQuery query = domainFactory.newQuery();
            query.setPredicate(new AttributePredicate<String>(DomainPredicates.NAME, domainName));
            DomainListResult queryResult = domainService.query(query);
            if (!queryResult.isEmpty()) {
                for (Action action : queryResult.getFirstItem().getActions()) {
                    gwtActionList.add(KapuaGwtModelConverter.convertAction(action));
                }
                Collections.sort(gwtActionList);
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtActionList;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<GwtConfigComponent> findServiceConfigurations(String scopeId) throws GwtKapuaException {
        List<GwtConfigComponent> gwtConfigs = new ArrayList<>();
        try {
            KapuaId kapuaScopeId = GwtKapuaModelConverter.convert(scopeId);

            //
            // List Domains

            List<GwtDomain> gwtDomains = findAll();
            for (GwtDomain gwtDomain : gwtDomains) {
                String serviceName = getServiceName(null, gwtDomain.toString());
                if (serviceName != null && serviceName.startsWith("org.")) {
                    KapuaLocator locator = KapuaLocator.getInstance();
                    Class serviceClass = Class.forName(serviceName);
                    KapuaService service = locator.getService(serviceClass);
                    if (service instanceof KapuaConfigurableService) {
                        KapuaConfigurableService configurableService = (KapuaConfigurableService)service;
                        KapuaTocd tocd = configurableService.getConfigMetadata();
                        if (tocd != null) {
                            GwtConfigComponent gwtConfig = new GwtConfigComponent();
                            gwtConfig.setId(tocd.getId());
                            gwtConfig.setName(tocd.getName());
                            gwtConfig.setDescription(tocd.getDescription());
                            if (tocd.getIcon() != null && tocd.getIcon().size() > 0) {
                                KapuaTicon icon = tocd.getIcon().get(0);

                                checkIconResource(icon);

                                gwtConfig.setComponentIcon(icon.getResource());
                            }

                            List<GwtConfigParameter> gwtParams = new ArrayList<>();
                            gwtConfig.setParameters(gwtParams);
                            for (KapuaTad ad : tocd.getAD()) {
                                if (ad != null) {
                                    Map<String, Object> values = configurableService.getConfigValues(kapuaScopeId);
                                    GwtConfigParameter gwtParam = new GwtConfigParameter();
                                    gwtParam.setId(ad.getId());
                                    gwtParam.setName(ad.getName());
                                    gwtParam.setDescription(ad.getDescription());
                                    gwtParam.setType(GwtConfigParameter.GwtConfigParameterType.fromString(ad.getType().value()));
                                    gwtParam.setRequired(ad.isRequired());
                                    gwtParam.setCardinality(ad.getCardinality());
                                    if (ad.getOption() != null && ad.getOption().size() > 0) {
                                        Map<String, String> options = new HashMap<>();
                                        for (KapuaToption option : ad.getOption()) {
                                            options.put(option.getLabel(), option.getValue());
                                        }
                                        gwtParam.setOptions(options);
                                    }
                                    gwtParam.setMin(ad.getMin());
                                    gwtParam.setMax(ad.getMax());

                                    if (!values.isEmpty()) {
                                        int cardinality = ad.getCardinality();
                                        Object value = values.get(ad.getId());
                                        if (value != null) {
                                            if (cardinality == 0 || cardinality == 1 || cardinality == -1) {
                                                gwtParam.setValue(value.toString());
                                            } else {
                                                // this could be an array value
                                                if (value instanceof Object[]) {
                                                    Object[] objValues = (Object[]) value;
                                                    List<String> strValues = new ArrayList<>();
                                                    for (Object v : objValues) {
                                                        if (v != null) {
                                                            strValues.add(v.toString());
                                                        }
                                                    }
                                                    gwtParam.setValues(strValues.toArray(new String[] {}));
                                                }
                                            }
                                        }
                                        gwtParams.add(gwtParam);
                                    }
                                }
                            }
                            gwtConfigs.add(gwtConfig);
                        }
                    }
                }
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtConfigs;
    }

    private String getServiceName(KapuaId scopeId, String domainName) throws GwtKapuaException{
        String serviceName = null;
        KapuaLocator locator = KapuaLocator.getInstance();
        DomainService domainService = locator.getService(DomainService.class);
        DomainFactory domainFactory = locator.getFactory(DomainFactory.class);
        DomainQuery domainQuery = domainFactory.newQuery();
        domainQuery.setScopeId(scopeId);
        domainQuery.setPredicate(new AttributePredicate<>(DomainPredicates.NAME, domainName));
        try {
            DomainListResult result = domainService.query(domainQuery);
            if (!result.isEmpty()) {
                serviceName = result.getItem(0).getServiceName();
            }
        } catch (KapuaException e) {
            KapuaExceptionHandler.handle(e);
        }
        return serviceName;
    }

    @SuppressWarnings("Duplicates")
    private void checkIconResource(KapuaTicon icon) {
        ConsoleSetting config = ConsoleSetting.getInstance();

        String iconResource = icon.getResource();

        //
        // Check if the resource is an HTTP URL or not
        if (iconResource != null &&
                (iconResource.toLowerCase().startsWith("http://") ||
                        iconResource.toLowerCase().startsWith("https://"))) {
            File tmpFile = null;

            try {
                logger.info("Got configuration component icon from URL: {}", iconResource);

                //
                // Tmp file name creation
                String systemTmpDir = System.getProperty("java.io.tmpdir");
                String iconResourcesTmpDir = config.getString(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_FOLDER);
                String tmpFileName = org.apache.commons.codec.binary.Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(iconResource.getBytes("UTF-8")));

                // Conversions needed got security reasons!
                // On the file servlet we use the regex [0-9A-Za-z]{1,} to validate the given file id.
                // This validation prevents the caller of the file servlet to try to move out of the directory where the icons are stored.
                tmpFileName = tmpFileName.replaceAll("/", "a");
                tmpFileName = tmpFileName.replaceAll("\\+", "m");
                tmpFileName = tmpFileName.replaceAll("=", "z");

                //
                // Tmp dir check and creation
                StringBuilder tmpDirPathSb = new StringBuilder().append(systemTmpDir);
                if (!systemTmpDir.endsWith("/")) {
                    tmpDirPathSb.append("/");
                }
                tmpDirPathSb.append(iconResourcesTmpDir);

                File tmpDir = new File(tmpDirPathSb.toString());
                if (!tmpDir.exists()) {
                    logger.info("Creating tmp dir on path: {}", tmpDir.toString());
                    tmpDir.mkdir();
                }

                //
                // Tmp file check and creation
                tmpDirPathSb.append("/")
                        .append(tmpFileName);
                tmpFile = new File(tmpDirPathSb.toString());

                // Check date of modification to avoid caching forever
                if (tmpFile.exists()) {
                    long lastModifiedDate = tmpFile.lastModified();

                    long maxCacheTime = config.getLong(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_CACHE_TIME);

                    if (System.currentTimeMillis() - lastModifiedDate > maxCacheTime) {
                        logger.info("Deleting old cached file: {}", tmpFile.toString());
                        tmpFile.delete();
                    }
                }

                // If file is not cached, download it.
                if (!tmpFile.exists()) {
                    // Url connection
                    URL iconUrl = new URL(iconResource);
                    URLConnection urlConnection = iconUrl.openConnection();
                    urlConnection.setConnectTimeout(2000);
                    urlConnection.setReadTimeout(2000);

                    // Length check
                    String contentLengthString = urlConnection.getHeaderField("Content-Length");

                    long maxLength = config.getLong(ConsoleSettingKeys.DEVICE_CONFIGURATION_ICON_SIZE_MAX);

                    try {
                        Long contentLength = Long.parseLong(contentLengthString);
                        if (contentLength > maxLength) {
                            logger.warn("Content lenght exceeded ({}/{}) for URL: {}",
                                    new Object[] { contentLength, maxLength, iconResource });
                            throw new IOException("Content-Length reported a length of " + contentLength + " which exceeds the maximum allowed size of " + maxLength);
                        }
                    } catch (NumberFormatException nfe) {
                        logger.warn("Cannot get Content-Length header!");
                    }

                    logger.info("Creating file: {}", tmpFile.toString());
                    tmpFile.createNewFile();

                    // Icon download
                    InputStream is = urlConnection.getInputStream();
                    OutputStream os = new FileOutputStream(tmpFile);
                    byte[] buffer = new byte[4096];
                    try {
                        int len;
                        while ((len = is.read(buffer)) > 0) {
                            os.write(buffer, 0, len);

                            maxLength -= len;

                            if (maxLength < 0) {
                                logger.warn("Maximum content lenght exceeded ({}) for URL: {}",
                                        new Object[] { maxLength, iconResource });
                                throw new IOException("Maximum content lenght exceeded (" + maxLength + ") for URL: " + iconResource);
                            }
                        }
                    } finally {
                        os.close();
                    }

                    logger.info("Downloaded file: {}", tmpFile.toString());

                    // Image metadata content checks
                    ImageFormat imgFormat = Sanselan.guessFormat(tmpFile);

                    if (imgFormat.equals(ImageFormat.IMAGE_FORMAT_BMP) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_GIF) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_JPEG) ||
                            imgFormat.equals(ImageFormat.IMAGE_FORMAT_PNG)) {
                        logger.info("Detected image format: {}", imgFormat.name);
                    } else if (imgFormat.equals(ImageFormat.IMAGE_FORMAT_UNKNOWN)) {
                        logger.error("Unknown file format for URL: {}", iconResource);
                        throw new IOException("Unknown file format for URL: " + iconResource);
                    } else {
                        logger.error("Usupported file format ({}) for URL: {}", imgFormat, iconResource);
                        throw new IOException("Unknown file format for URL: {}" + iconResource);
                    }

                    logger.info("Image validation passed for URL: {}", iconResource);
                } else {
                    logger.info("Using cached file: {}", tmpFile.toString());
                }

                //
                // Injecting new URL for the icon resource
                String newResourceURL = new StringBuilder().append("img://console/file/icons?id=")
                        .append(tmpFileName)
                        .toString();

                logger.info("Injecting configuration component icon: {}", newResourceURL);
                icon.setResource(newResourceURL);
            } catch (Exception e) {
                if (tmpFile != null &&
                        tmpFile.exists()) {
                    tmpFile.delete();
                }

                icon.setResource("Default");

                logger.error("Error while checking component configuration icon. Using the default plugin icon.", e);
            }
        }
        //
        // If not, all is fine.
    }

}