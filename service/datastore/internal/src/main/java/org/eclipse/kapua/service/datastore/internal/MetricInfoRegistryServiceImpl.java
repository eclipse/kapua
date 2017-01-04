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
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ExistsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.SortFieldImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.StorableFieldImpl;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Metric information registry implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class MetricInfoRegistryServiceImpl extends AbstractKapuaConfigurableService implements MetricInfoRegistryService
{
    private static final long serialVersionUID = 7490084233555473342L;

    private static final Domain datastoreDomain = new DatastoreDomain();

    private static final Logger logger = LoggerFactory.getLogger(MetricInfoRegistryServiceImpl.class);

    private final AccountService           accountService;
    private final AuthorizationService     authorizationService;
    private final PermissionFactory        permissionFactory;
    private final MetricInfoRegistryFacade metricInfoStoreFacade;
    private final MessageStoreService      messageStoreService;
    private final DatastoreObjectFactory   datastoreObjectFactory;

    public MetricInfoRegistryServiceImpl()
    {
        super(MetricInfoRegistryService.class.getName(), datastoreDomain, DatastoreEntityManagerFactory.getInstance());

        KapuaLocator locator = KapuaLocator.getInstance();
        accountService = locator.getService(AccountService.class);
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        messageStoreService = locator.getService(MessageStoreService.class);
        datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);

        MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);
        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(messageStoreService, accountService);
        this.metricInfoStoreFacade = new MetricInfoRegistryFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMetricInfoStoreFacade(metricInfoStoreFacade);
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");
            //
            // Check Access
            this.checkDataAccess(scopeId, Actions.delete);

            this.metricInfoStoreFacade.delete(scopeId, id);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MetricInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");
            //
            // Check Access
            this.checkDataAccess(scopeId, Actions.read);

            // populate the lastMessageTimestamp
            MetricInfo metricInfo = this.metricInfoStoreFacade.find(scopeId, id);

            updateLastPublishedFields(scopeId, metricInfo);

            return metricInfo;
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MetricInfoListResult query(KapuaId scopeId, MetricInfoQuery query)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");
            //
            // Check Access
            this.checkDataAccess(scopeId, Actions.read);

            MetricInfoListResult result = this.metricInfoStoreFacade.query(scopeId, query);

            // populate the lastMessageTimestamp
            for (MetricInfo metricInfo : result) {
                updateLastPublishedFields(scopeId, metricInfo);
            }

            return result;
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public long count(KapuaId scopeId, MetricInfoQuery query)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");
            //
            // Check Access
            this.checkDataAccess(scopeId, Actions.read);

            return this.metricInfoStoreFacade.count(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, MetricInfoQuery query)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");

            //
            // Check Access
            this.checkDataAccess(scopeId, Actions.read);

            this.metricInfoStoreFacade.delete(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
        throws KapuaException
    {
        //
        // Check Access
        Permission permission = permissionFactory.newPermission(datastoreDomain, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    /**
     * Update the last published date and last published message identifier for the specified metric info, so it gets the timestamp and the message identifier of the last published message for the
     * account/clientId in the metric info
     * 
     * @param scopeId
     * @param channelInfo
     * 
     * @throws KapuaException
     */
    private void updateLastPublishedFields(KapuaId scopeId, MetricInfo metricInfo) throws KapuaException
    {
        StorableId lastPublishedMessageId = null;
        Date lastPublishedMessageTimestamp = null;
        MessageQuery messageQuery = new MessageQueryImpl();
        messageQuery.setAskTotalCount(true);
        messageQuery.setFetchStyle(StorableFetchStyle.SOURCE_FULL);
        messageQuery.setLimit(1);
        messageQuery.setOffset(0);
        List<SortField> sort = new ArrayList<SortField>();
        SortField sortTimestamp = new SortFieldImpl();
        sortTimestamp.setField(EsSchema.MESSAGE_TIMESTAMP);
        sortTimestamp.setSortDirection(SortDirection.DESC);
        sort.add(sortTimestamp);
        messageQuery.setSortFields(sort);
        AndPredicate andPredicate = new AndPredicateImpl();
        // TODO check if this field is correct (EsSchema.METRIC_MTR_TIMESTAMP)!
        RangePredicate messageIdPredicate = new RangePredicateImpl(new StorableFieldImpl(EsSchema.METRIC_MTR_TIMESTAMP), metricInfo.getFirstPublishedMessageTimestamp(), null);
        andPredicate.getPredicates().add(messageIdPredicate);
        TermPredicate accountNamePredicate = datastoreObjectFactory.newTermPredicate(MessageField.ACCOUNT, metricInfo.getAccount());
        andPredicate.getPredicates().add(accountNamePredicate);
        TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(MessageField.CLIENT_ID, metricInfo.getClientId());
        andPredicate.getPredicates().add(clientIdPredicate);
        ExistsPredicate metricPredicate = new ExistsPredicateImpl(MessageField.METRICS.field(), metricInfo.getName());
        andPredicate.getPredicates().add(metricPredicate);
        messageQuery.setPredicate(andPredicate);
        MessageListResult messageList = messageStoreService.query(scopeId, messageQuery);
        if (messageList.size() == 1) {
            lastPublishedMessageId = messageList.get(0).getDatastoreId();
            lastPublishedMessageTimestamp = messageList.get(0).getTimestamp();
        }
        else if (messageList.size() == 0) {
            // this condition could happens due to the ttl of the messages (so if it happens, it does not necessarily mean there has been an error!)
            logger.warn("Cannot find last timestamp for the specified client id '{}' - account '{}'", new Object[] { metricInfo.getAccount(), metricInfo.getClientId() });
        }
        else {
            // this condition shouldn't never happens since the query has a limit 1
            // if happens it means than an elasticsearch internal error happens and/or our driver didn't set it correctly!
            logger.error("Cannot find last timestamp for the specified client id '{}' - account '{}'. More than one result returned by the query!", new Object[] { metricInfo.getAccount(), metricInfo.getClientId() });
        }
        metricInfo.setLastPublishedMessageId(lastPublishedMessageId);
        metricInfo.setLastPublishedMessageTimestamp(lastPublishedMessageTimestamp);
    }

}
