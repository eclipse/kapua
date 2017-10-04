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
package org.eclipse.kapua.app.console.module.account.server;

import javax.xml.namespace.QName;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.sanselan.ImageFormat;
import org.apache.sanselan.Sanselan;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountCreator;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountStringListItem;
import org.eclipse.kapua.app.console.module.account.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.module.account.shared.util.GwtKapuaAccountModelConverter;
import org.eclipse.kapua.app.console.module.account.shared.util.KapuaGwtAccountModelConverter;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSetting;
import org.eclipse.kapua.app.console.module.api.setting.ConsoleSettingKeys;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.broker.core.BrokerDomain;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.commons.util.ThrowingRunnable;
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
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleFactory;
import org.eclipse.kapua.service.authorization.role.RoleService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server side implementation of the RPC service.
 */
public class GwtAccountServiceImpl extends KapuaRemoteServiceServlet implements GwtAccountService {

    private static final Logger logger = LoggerFactory.getLogger(GwtAccountServiceImpl.class);
    private static final long serialVersionUID = 3314502846487119577L;

    @Override
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

            // convertKapuaId to GwtAccount and return
            gwtAccount = KapuaGwtAccountModelConverter.convertAccount(account);

            // Create roles
            final RoleService roleService = locator.getService(RoleService.class);
            RoleFactory roleFactory = locator.getFactory(RoleFactory.class);
            PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
            Permission adminPermission = permissionFactory.newPermission(null, null, account.getId(), null, true);
            final RoleCreator adminRoleCreator = roleFactory.newCreator(account.getId());
            Set<Permission> adminPermissions = new HashSet<Permission>();
            adminPermissions.add(adminPermission);
            adminRoleCreator.setName("admin");
            adminRoleCreator.setScopeId(account.getId());
            adminRoleCreator.setPermissions(adminPermissions);
            KapuaSecurityUtils.doPrivileged(new ThrowingRunnable() {

                @Override
                public void run() throws Exception {
                    roleService.create(adminRoleCreator);
                }
            });

            final RoleCreator thingRoleCreator = roleFactory.newCreator(account.getId());
            Permission thingPermission = permissionFactory.newPermission(new BrokerDomain(), Actions.connect, account.getId(), null, false);
            Set<Permission> thingPermissions = new HashSet<Permission>();
            thingPermissions.add(thingPermission);
            thingRoleCreator.setName("thing");
            thingRoleCreator.setScopeId(account.getId());
            thingRoleCreator.setPermissions(thingPermissions);
            KapuaSecurityUtils.doPrivileged(new ThrowingRunnable() {

                @Override
                public void run() throws Exception {
                    roleService.create(thingRoleCreator);
                }
            });
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccount;
    }

    @Override
    public GwtAccount find(String accountIdString)
            throws GwtKapuaException {
        KapuaId accountId = KapuaEid.parseCompactId(accountIdString);

        GwtAccount gwtAccount = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            gwtAccount = KapuaGwtAccountModelConverter.convertAccount(accountService.find(accountId));
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtAccount;
    }

    @Override
    public ListLoadResult<GwtGroupedNVPair> getAccountInfo(String scopeIdString, String accountIdString)
            throws GwtKapuaException {
        final KapuaId scopeId = KapuaEid.parseCompactId(scopeIdString);
        final KapuaId accountId = KapuaEid.parseCompactId(accountIdString);

        KapuaLocator locator = KapuaLocator.getInstance();
        final AccountService accountService = locator.getService(AccountService.class);
        final UserService userService = locator.getService(UserService.class);

        List<GwtGroupedNVPair> accountPropertiesPairs = new ArrayList<GwtGroupedNVPair>();
        try {
            final Account account = accountService.find(scopeId, accountId);
            String brokerUri = KapuaSecurityUtils.doPrivileged(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    String[] accountHierarchy = account.getParentAccountPath().split("/");
                    URI defaultUri = SystemUtils.getBrokerURI();
                    switch (accountHierarchy.length) {
                    case 2:
                        // "", "1": root account
                        return StringUtils.isNotBlank(accountService.getConfigValues(accountId).get("deviceBrokerClusterUri").toString()) ?
                                accountService.getConfigValues(accountId).get("deviceBrokerClusterUri").toString() :
                                defaultUri.toString();
                    default:
                        // "", "1", "accountId": second level account (root child)
                        // "", "1", "accountId", ["otherAccountId", ...], "otherAccountId": third level or more (non root and non root child)
                        KapuaId rootAccountId = new KapuaEid(BigInteger.valueOf(Long.valueOf(accountHierarchy[1])));
                        KapuaId secondLevelAccountId = new KapuaEid(BigInteger.valueOf(Long.valueOf(accountHierarchy[2])));
                        String secondLevelBrokerUri = accountService.getConfigValues(secondLevelAccountId).get("deviceBrokerClusterUri").toString();
                        if (StringUtils.isNotBlank(secondLevelBrokerUri)) {
                            return secondLevelBrokerUri;
                        } else {
                            String rootBrokerUri = accountService.getConfigValues(rootAccountId).get("deviceBrokerClusterUri").toString();
                            if (StringUtils.isNotBlank(rootBrokerUri)) {
                                return rootBrokerUri;
                            } else {
                                return defaultUri.toString();
                            }
                        }
                    }
                }
            });

            User userCreatedBy = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, account.getCreatedBy());
                }
            });

            User userModifiedBy = KapuaSecurityUtils.doPrivileged(new Callable<User>() {

                @Override
                public User call() throws Exception {
                    return userService.find(scopeId, account.getModifiedBy());
                }
            });

            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountName", account.getName()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountModifiedOn", account.getModifiedOn().toString()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountModifiedBy", userModifiedBy.getName()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedOn", account.getCreatedOn().toString()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedBy", userCreatedBy.getName()));

            accountPropertiesPairs.add(new GwtGroupedNVPair("deploymentInfo", "deploymentBrokerURL", brokerUri));

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

    @Override
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

            // convertKapuaId to GwtAccount and return
            gwtAccountUpdated = KapuaGwtAccountModelConverter.convertAccount(account);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccountUpdated;
    }

    @Override
    public GwtAccount update(GwtXSRFToken xsrfToken, GwtAccount gwtAccount)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        GwtAccount gwtAccountUpdated = null;
        KapuaId scopeId = gwtAccount.getScopeId() != null ? KapuaEid.parseCompactId(gwtAccount.getScopeId()) : null;
        KapuaId accountId = KapuaEid.parseCompactId(gwtAccount.getId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = scopeId != null ? accountService.find(scopeId, accountId) : accountService.find(accountId);

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

            // convertKapuaId to GwtAccount and return
            gwtAccountUpdated = KapuaGwtAccountModelConverter.convertAccount(account);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccountUpdated;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, GwtAccount gwtAccount)
            throws GwtKapuaException {
        //
        // Checking validity of the given XSRF Token
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = gwtAccount.getScopeId() != null ? KapuaEid.parseCompactId(gwtAccount.getScopeId()) : null;
        KapuaId accountId = KapuaEid.parseCompactId(gwtAccount.getId());
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            AccountService accountService = locator.getService(AccountService.class);
            Account account = accountService.find(scopeId, accountId);

            if (account != null) {
                accountService.delete(account.getScopeId(), account.getId());
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    @Override
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
                gwtAccountList.add(KapuaGwtAccountModelConverter.convertAccount(account));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtAccount>(gwtAccountList);
    }

    @Override
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
                gwtAccountList.add(KapuaGwtAccountModelConverter.convertAccount(account));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BaseListLoadResult<GwtAccount>(gwtAccountList);
    }

    @Override
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
                gwtAccount = KapuaGwtAccountModelConverter.convertAccount(account);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtAccount;
    }

    @Override
    public List<GwtConfigComponent> findServiceConfigurations(String scopeId) throws GwtKapuaException {
        List<GwtConfigComponent> gwtConfigs = new ArrayList<GwtConfigComponent>();
        KapuaLocator locator = KapuaLocator.getInstance();
        try {
            KapuaId kapuaScopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeId);
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
                                Map<String, String> gwtEntries = new HashMap<String, String>();
                                for (Entry<QName, String> entry : ad.getOtherAttributes().entrySet()) {
                                    gwtEntries.put(entry.getKey().toString(), entry.getValue());
                                }
                                gwtParam.setOtherAttributes(gwtEntries);

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
                String tmpFileName = Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(iconResource.getBytes("UTF-8")));

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
        AccountQuery query = GwtKapuaAccountModelConverter.convertAccountQuery(loadConfig, gwtAccountQuery);

        try {
            accounts = accountService.query(query);
            if (!accounts.isEmpty()) {
                totalLength = Long.valueOf(accountService.count(query)).intValue();

                for (Account a : accounts.getItems()) {
                    gwtAccounts.add(KapuaGwtAccountModelConverter.convertAccount(a));
                }
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtAccount>(gwtAccounts, loadConfig.getOffset(), totalLength);
    }

    @Override
    public GwtAccount findRootAccount() throws GwtKapuaException {
        GwtAccount gwtAccount = null;
        try {
            gwtAccount = KapuaSecurityUtils.doPrivileged(new Callable<GwtAccount>() {

                @Override
                public GwtAccount call() throws Exception {
                    return findByAccountName(SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_ACCOUNT));
                }
            });
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtAccount;
    }

}
