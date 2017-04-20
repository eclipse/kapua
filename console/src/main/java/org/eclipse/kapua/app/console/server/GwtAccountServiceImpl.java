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
package org.eclipse.kapua.app.console.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.Sanselan;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountCreator;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountQuery;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountStringListItem;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;

/**
 * The server side implementation of the RPC service.
 */
public class GwtAccountServiceImpl extends KapuaRemoteServiceServlet implements GwtAccountService {

    private static final Logger logger = LoggerFactory.getLogger(GwtAccountServiceImpl.class);
    private static final long serialVersionUID = 3314502846487119577L;

    public GwtAccount create(GwtXSRFToken xsrfToken, GwtAccountCreator gwtAccountCreator)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        GwtAccount gwtAccount = null;
        KapuaId parentAccountId = KapuaEid.parseCompactId(gwtAccountCreator.getParentAccountId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountFactory accountFactory = locator.getFactory(AccountFactory.class);

            AccountCreator accountCreator = accountFactory.newCreator(parentAccountId,
                    gwtAccountCreator.getAccountName());

            accountCreator.setOrganizationName(gwtAccountCreator.getOrganizationName());
            accountCreator.setOrganizationPersonName(gwtAccountCreator.getOrganizationPersonName());
            accountCreator.setOrganizationEmail(gwtAccountCreator.getOrganizationEmail());
            accountCreator.setOrganizationPhoneNumber(gwtAccountCreator.getOrganizationPhoneNumber());
            accountCreator.setOrganizationAddressLine1(gwtAccountCreator.getOrganizationAddressLine1());
            accountCreator.setOrganizationAddressLine2(gwtAccountCreator.getOrganizationAddressLine2());
            accountCreator.setOrganizationCity(gwtAccountCreator.getOrganizationCity());
            accountCreator.setOrganizationZipPostCode(gwtAccountCreator.getOrganizationZipPostCode());
            accountCreator.setOrganizationStateProvinceCounty(gwtAccountCreator.getOrganizationStateProvinceCounty());
            accountCreator.setOrganizationCountry(gwtAccountCreator.getOrganizationCountry());

            // create the Account
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.create(accountCreator);

            // convert to GwtAccount and return
            gwtAccount = KapuaGwtModelConverter.convert(account);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccount;
    }

    public GwtAccount find(String accountIdString)
            throws GwtKapuaException {
        KapuaId accountId = KapuaEid.parseCompactId(accountIdString);

        GwtAccount gwtAccount = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            gwtAccount = KapuaGwtModelConverter.convert(accountService.find(accountId));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtAccount;
    }

    public ListLoadResult<GwtGroupedNVPair> getAccountInfo(String accountIdString)
            throws GwtKapuaException {
        KapuaId accountId = KapuaEid.parseCompactId(accountIdString);

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);

        List<GwtGroupedNVPair> accountPropertiesPairs = new ArrayList<GwtGroupedNVPair>();
        try {
            Account account = accountService.find(accountId);

            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountName", account.getName()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountModifiedOn", account.getModifiedOn().toString()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountModifiedBy", account.getModifiedBy().toCompactId()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedOn", account.getCreatedOn().toString()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedBy", account.getCreatedBy().toCompactId()));

            accountPropertiesPairs.add(new GwtGroupedNVPair("deploymentInfo", "deploymentBrokerURL", SystemUtils.getBrokerURI().toString()));

            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationName", account.getOrganization().getName()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationPersonName", account.getOrganization().getPersonName()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationEmail", account.getOrganization().getEmail()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationPhoneNumber", account.getOrganization().getPhoneNumber()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationAddress1", account.getOrganization().getAddressLine1()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationAddress2", account.getOrganization().getAddressLine2()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationZip", account.getOrganization().getZipPostCode()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationCity", account.getOrganization().getCity()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationState", account.getOrganization().getStateProvinceCounty()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("organizationInfo", "organizationCountry", account.getOrganization().getCountry()));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtGroupedNVPair>(accountPropertiesPairs);
    }

    public GwtAccount updateAccountProperties(GwtXSRFToken xsrfToken, GwtAccount gwtAccount)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        GwtAccount gwtAccountUpdated = null;
        KapuaId scopeId = KapuaEid.parseCompactId(gwtAccount.getId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.find(scopeId);

            // update properties
            Properties property = account.getEntityProperties();
            if (property == null) {
                property = new Properties();
            }

            account.setEntityProperties(property);
            account = accountService.update(account);

            // convert to GwtAccount and return
            gwtAccountUpdated = KapuaGwtModelConverter.convert(account);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccountUpdated;
    }

    public GwtAccount update(GwtXSRFToken xsrfToken, GwtAccount gwtAccount)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        GwtAccount gwtAccountUpdated = null;
        KapuaId scopeId = KapuaEid.parseCompactId(gwtAccount.getId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.find(scopeId);

            account.getOrganization().setName(gwtAccount.getGwtOrganization().getName());
            account.getOrganization().setPersonName(gwtAccount.getGwtOrganization().getPersonName());
            account.getOrganization().setEmail(gwtAccount.getGwtOrganization().getEmailAddress());
            account.getOrganization().setPhoneNumber(gwtAccount.getGwtOrganization().getPhoneNumber());
            account.getOrganization().setAddressLine1(gwtAccount.getGwtOrganization().getAddressLine1());
            account.getOrganization().setAddressLine2(gwtAccount.getGwtOrganization().getAddressLine2());
            account.getOrganization().setZipPostCode(gwtAccount.getGwtOrganization().getZipPostCode());
            account.getOrganization().setCity(gwtAccount.getGwtOrganization().getCity());
            account.getOrganization().setStateProvinceCounty(gwtAccount.getGwtOrganization().getStateProvinceCounty());
            account.getOrganization().setCountry(gwtAccount.getGwtOrganization().getCountry());
            account.setOptlock(gwtAccount.getOptlock());

            account = accountService.update(account);

            // convert to GwtAccount and return
            gwtAccountUpdated = KapuaGwtModelConverter.convert(account);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccountUpdated;
    }

    public void delete(GwtXSRFToken xsrfToken, GwtAccount gwtAccount)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        KapuaId kapuaId = KapuaEid.parseCompactId(gwtAccount.getId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.find(kapuaId);

            if (account != null) {
                accountService.delete(account.getScopeId(), account.getId());
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    public ListLoadResult<GwtAccount> findAll(String scopeIdString)
            throws GwtKapuaException {

        List<GwtAccount> gwtAccountList = new ArrayList<GwtAccount>();
        KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            AccountFactory accountFactory = locator.getFactory(AccountFactory.class);
            AccountQuery query = accountFactory.newQuery(scopeId);

            KapuaListResult<Account> list = accountService.query(query);
            for (Account account : list.getItems()) {
                gwtAccountList.add(KapuaGwtModelConverter.convert(account));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtAccount>(gwtAccountList);
    }

    public ListLoadResult<GwtAccount> findChildren(String parentAccountId, boolean recoursive)
            throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(parentAccountId);

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        AccountFactory accountFactory = locator.getFactory(AccountFactory.class);

        List<GwtAccount> gwtAccountList = new ArrayList<GwtAccount>();
        try {
            AccountQuery query = accountFactory.newQuery(scopeId);

            KapuaListResult<Account> list = accountService.query(query);
            for (Account account : list.getItems()) {
                gwtAccountList.add(KapuaGwtModelConverter.convert(account));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtAccount>(gwtAccountList);
    }

    public ListLoadResult<GwtAccountStringListItem> findChildrenAsStrings(String parentAccountId, boolean recoursive)
            throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(parentAccountId);

        List<GwtAccountStringListItem> gwtAccountStrings = new ArrayList<GwtAccountStringListItem>();

        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        AccountListResult list;
        try {
            list = accountService.findChildsRecursively(scopeId);
            for (Account account : list.getItems()) {
                GwtAccountStringListItem item = new GwtAccountStringListItem();
                item.setId(account.getId().toCompactId());
                item.setValue(account.getName());
                item.setHasChildAccount(false); // FIXME: add check to see if account has or noe childs

                gwtAccountStrings.add(item);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtAccountStringListItem>(gwtAccountStrings);
    }

    @Override
    public GwtAccount findByAccountName(String accountName)
            throws GwtKapuaException {
        GwtAccount gwtAccount = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.findByName(accountName);
            if (account != null) {
                gwtAccount = KapuaGwtModelConverter.convert(account);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtAccount;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public List<GwtConfigComponent> findServiceConfigurations(String scopeId) throws GwtKapuaException {
        List<GwtConfigComponent> gwtConfigs = new ArrayList<GwtConfigComponent>();
        KapuaLocator locator = KapuaLocator.getInstance();
        try {
            KapuaId kapuaScopeId = GwtKapuaModelConverter.convert(scopeId);
            for (KapuaService service : locator.getServices()) {
                if (service instanceof KapuaConfigurableService) {
                    KapuaConfigurableService configurableService = (KapuaConfigurableService) service;
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

                        List<GwtConfigParameter> gwtParams = new ArrayList<GwtConfigParameter>();
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
                                    Map<String, String> options = new HashMap<String, String>();
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
                                                List<String> strValues = new ArrayList<String>();
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
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtConfigs;
    }

    @Override
    public void updateComponentConfiguration(GwtXSRFToken xsrfToken, String scopeId, String parentId, GwtConfigComponent configComponent) throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);
        KapuaId kapuaScopeId = GwtKapuaModelConverter.convert(scopeId);
        KapuaId kapuaParentId = GwtKapuaModelConverter.convert(parentId);
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            Class configurableServiceClass = Class.forName(configComponent.<String> get("componentId"));
            KapuaConfigurableService configurableService = (KapuaConfigurableService) locator.getService(configurableServiceClass);

            // execute the update
            Map<String, Object> parameters = GwtKapuaModelConverter.convert(configComponent);

            configurableService.setConfigValues(kapuaScopeId, kapuaParentId, parameters);

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

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
                String tmpFileName = Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(iconResource.getBytes(StandardCharsets.UTF_8)));

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
                            logger.warn("Content lenght exceeded ({}/{}) for URL: {}", contentLength, maxLength, iconResource);
                            throw new IOException("Content-Length reported a length of " + contentLength + " which exceeds the maximum allowed size of " + maxLength);
                        }
                    } catch (NumberFormatException nfe) {
                        logger.warn("Cannot get Content-Length header!");
                    }

                    logger.info("Creating file: {}", tmpFile.toString());
                    tmpFile.createNewFile();

                    // Icon download
                    final InputStream is = urlConnection.getInputStream();
                    try {
                        byte[] buffer = new byte[4096];
                        final OutputStream os = new FileOutputStream(tmpFile);
                        try {
                            int len;
                            while ((len = is.read(buffer)) > 0) {
                                os.write(buffer, 0, len);

                                maxLength -= len;

                                if (maxLength < 0) {
                                    logger.warn("Maximum content lenght exceeded ({}) for URL: {}", maxLength, iconResource);
                                    throw new IOException("Maximum content lenght exceeded (" + maxLength + ") for URL: " + iconResource);
                                }
                            }
                        } finally {
                            os.close();
                        }
                    } finally {
                        is.close();
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
                String newResourceURL = "img://console/file/icons?id=" +
                        tmpFileName;

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

    @Override
    public PagingLoadResult<GwtAccount> query(PagingLoadConfig loadConfig, GwtAccountQuery gwtAccountQuery) throws GwtKapuaException {
        KapuaListResult<Account> accounts;
        List<GwtAccount> gwtAccounts = new ArrayList<GwtAccount>();
        int totalLength = 0;
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountService accountService = locator.getService(AccountService.class);
        AccountQuery query = GwtKapuaModelConverter.convertAccountQuery(loadConfig, gwtAccountQuery);

        try {
            accounts = accountService.query(query);
            if (accounts.getSize() >= loadConfig.getLimit()) {
                totalLength = new Long(accountService.count(query)).intValue();
            } else {
                totalLength = accounts.getSize();
            }

            for (Account a : accounts.getItems()) {
                gwtAccounts.add(KapuaGwtModelConverter.convert(a));
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtAccount>(gwtAccounts, loadConfig.getOffset(), totalLength);
    }

}
