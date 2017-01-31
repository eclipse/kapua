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
 *
 *******************************************************************************/
package org.eclipse.kapua.app.console.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccount;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountCreator;
import org.eclipse.kapua.app.console.shared.model.account.GwtAccountStringListItem;
import org.eclipse.kapua.app.console.shared.service.GwtAccountService;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.SystemUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountListResult;
import org.eclipse.kapua.service.account.AccountQuery;
import org.eclipse.kapua.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;

/**
 * The server side implementation of the RPC service.
 */
public class GwtAccountServiceImpl extends KapuaRemoteServiceServlet implements GwtAccountService {

    @SuppressWarnings("unused")
    private static final Logger s_logger = LoggerFactory.getLogger(GwtAccountServiceImpl.class);
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

            AccountCreator accountCreator = accountFactory.newAccountCreator(parentAccountId,
                    gwtAccountCreator.getAccountName());
            accountCreator.setAccountPassword(gwtAccountCreator.getAccountPassword());

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
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountModifiedBy", account.getModifiedBy().getId().toCompactId()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedOn", account.getCreatedOn().toString()));
            accountPropertiesPairs.add(new GwtGroupedNVPair("accountInfo", "accountCreatedBy", account.getCreatedBy().getId().toCompactId()));

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

}
