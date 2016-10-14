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
package org.eclipse.kapua.service.datastore.internal;

import java.util.ArrayList;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.AssetInfoStoreService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsClient;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsMessageField;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsUtils;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.LocalServicePlan;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.dao.EsAssetDAO;
import org.eclipse.kapua.service.datastore.internal.model.AssetInfoListResultImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.AssetInfoQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.IdsPredicateImpl;
import org.eclipse.kapua.service.datastore.model.AssetInfo;
import org.eclipse.kapua.service.datastore.model.AssetInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AssetInfoQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetInfoStoreServiceImpl extends AbstractKapuaConfigurableService implements AssetInfoStoreService
{
    private static final long   serialVersionUID = 6772144495298409738L;

    // @SuppressWarnings("unused")
    private static final Logger logger           = LoggerFactory.getLogger(AssetInfoStoreServiceImpl.class);

    private static final long   DAY_SECS         = 24 * 60 * 60;
    private static final long   DAY_MILLIS       = DAY_SECS * 1000;

    private AccountService      accountService;
    AuthorizationService        authorizationService;
    PermissionFactory           permissionFactory;

    public AssetInfoStoreServiceImpl()
    {
        super(AssetInfoStoreService.class.getName(), DatastoreDomain.DATASTORE, DatastoreEntityManagerFactory.getInstance());

        KapuaLocator locator = KapuaLocator.getInstance();
        accountService = locator.getService(AccountService.class);
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
    }
    //
    // @Override
    // public StorableId store(KapuaId scopeId, AssetInfoCreator creator)
    // throws KapuaException
    // {
    // // TODO DAOs are ready, need to evaluate if this functionality
    // // have to be available or not. Currently entries are added by
    // // the message service directy
    // throw KapuaException.internalError("Not implemented");
    // }
    //
    // @Override
    // public StorableId update(KapuaId scopeId, AssetInfo assetInfo)
    // throws KapuaException
    // {
    // // TODO DAOs are ready, need to evaluate if this functionality
    // // have to be available or not. Currently entries are updated by
    // // the message service directy
    // throw KapuaException.internalError("Not implemented");
    // }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.delete);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, return", scopeName);
            return;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            EsAssetDAO.connection(EsClient.getcurrent())
                      .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                      .deleteById(id.toString());
        }
        catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    @Override
    public AssetInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);

        AssetInfoQueryImpl q = new AssetInfoQueryImpl();
        q.setLimit(1);

        ArrayList<StorableId> ids = new ArrayList<StorableId>();
        ids.add(id);

        AndPredicateImpl allPredicates = new AndPredicateImpl();
        allPredicates.addPredicate(new IdsPredicateImpl(EsMessageField.ID, ids));

        AssetInfoListResult result = this.query(scopeId, q);
        if (result == null || result.size() == 0)
            return null;

        AssetInfo assetInfo = result.get(0);
        return assetInfo;
    }

    @Override
    public AssetInfoListResult query(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);
        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeName);
            return new AssetInfoListResultImpl();
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            AssetInfoListResult result = null;
            result = EsAssetDAO.connection(EsClient.getcurrent())
                               .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                               .query(query);

            return result;
        }
        catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    @Override
    public long count(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.read);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, returning empty result", scopeName);
            return 0;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            long result;
            result = EsAssetDAO.connection(EsClient.getcurrent())
                               .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                               .count(query);

            return result;
        }
        catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    @Override
    public void delete(KapuaId scopeId, AssetInfoQuery query)
        throws KapuaException
    {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(query, "query");
        //
        // Check Access
        this.checkDataAccess(scopeId, Actions.delete);

        //
        // Do the find
        AccountInfo accountInfo = getAccountServicePlan(scopeId);
        String scopeName = accountInfo.getAccount().getName();
        LocalServicePlan accountServicePlan = accountInfo.getServicePlan();
        long ttl = accountServicePlan.getDataTimeToLive() * DAY_MILLIS;

        if (!accountServicePlan.getDataStorageEnabled() || ttl == LocalServicePlan.DISABLED) {
            logger.debug("Storage not enabled for account {}, skipping delete", scopeName);
            return;
        }

        try {
            String everyIndex = EsUtils.getAnyIndexName(scopeName);
            EsAssetDAO.connection(EsClient.getcurrent())
                      .instance(everyIndex, EsSchema.ASSET_TYPE_NAME)
                      .deleteByQuery(query);

            return;
        }
        catch (Exception exc) {
            // TODO manage exeception
            // CassandraUtils.handleException(e);
            throw KapuaException.internalError(exc);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
        throws KapuaException
    {
        //
        // Check Access
        // TODO add enum for actions
        Permission permission = permissionFactory.newPermission(DatastoreDomain.DATASTORE, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    private AccountInfo getAccountServicePlan(KapuaId scopeId)
        throws KapuaException
    {
        Account account = accountService.find(scopeId);
        return new AccountInfo(account, new LocalServicePlan(this.getConfigValues(account.getId())));
    }
}
