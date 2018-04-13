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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.MetricInfoRegistryService;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.ExistsPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.StorableFieldImpl;
import org.eclipse.kapua.service.datastore.internal.schema.MessageSchema;
import org.eclipse.kapua.service.datastore.internal.schema.MetricInfoSchema;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.MetricInfo;
import org.eclipse.kapua.service.datastore.model.MetricInfoListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.MetricInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Metric information registry implementation.
 *
 * @since 1.0.0
 */
@KapuaProvider
public class MetricInfoRegistryServiceImpl extends AbstractKapuaConfigurableService implements MetricInfoRegistryService {

    private static final Logger LOG = LoggerFactory.getLogger(MetricInfoRegistryServiceImpl.class);

    private final AccountService accountService;
    private final AuthorizationService authorizationService;
    private final PermissionFactory permissionFactory;
    private final MetricInfoRegistryFacade metricInfoRegistryFacade;
    private final MessageStoreService messageStoreService;
    private final StorablePredicateFactory storablePredicateFactory;

    /**
     * Default constructor
     *
     * @throws ClientUnavailableException
     */
    public MetricInfoRegistryServiceImpl() throws ClientUnavailableException {
        super(MetricInfoRegistryService.class.getName(), DATASTORE_DOMAIN, DatastoreEntityManagerFactory.getInstance());

        KapuaLocator locator = KapuaLocator.getInstance();
        accountService = locator.getService(AccountService.class);
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        messageStoreService = locator.getService(MessageStoreService.class);
        storablePredicateFactory = KapuaLocator.getInstance().getFactory(StorablePredicateFactory.class);

        MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);
        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(messageStoreService, accountService);
        metricInfoRegistryFacade = new MetricInfoRegistryFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMetricInfoStoreFacade(metricInfoRegistryFacade);
    }

    @Override
    public MetricInfo find(KapuaId scopeId, StorableId id)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        checkDataAccess(scopeId, Actions.read);
        try {
            // populate the lastMessageTimestamp
            MetricInfo metricInfo = metricInfoRegistryFacade.find(scopeId, id);
            if (metricInfo != null) {

                updateLastPublishedFields(metricInfo);
            }
            return metricInfo;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MetricInfoListResult query(MetricInfoQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            MetricInfoListResult result = metricInfoRegistryFacade.query(query);
            if (result != null) {
                // populate the lastMessageTimestamp
                for (MetricInfo metricInfo : result.getItems()) {
                    updateLastPublishedFields(metricInfo);
                }
            }
            return result;
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public long count(MetricInfoQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return metricInfoRegistryFacade.count(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    void delete(MetricInfoQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.delete);
        try {
            metricInfoRegistryFacade.delete(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    void delete(KapuaId scopeId, StorableId id)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        checkDataAccess(scopeId, Actions.delete);
        try {
            metricInfoRegistryFacade.delete(scopeId, id);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        Permission permission = permissionFactory.newPermission(DATASTORE_DOMAIN, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    /**
     * Update the last published date and last published message identifier for the specified metric info, so it gets the timestamp and the message identifier of the last published message for the
     * account/clientId in the metric info
     *
     * @throws KapuaException
     */
    private void updateLastPublishedFields(MetricInfo metricInfo) throws KapuaException {
        List<SortField> sort = new ArrayList<>();
        sort.add(SortField.descending(MessageSchema.MESSAGE_TIMESTAMP));

        MessageQuery messageQuery = new MessageQueryImpl(metricInfo.getScopeId());
        messageQuery.setAskTotalCount(true);
        messageQuery.setFetchStyle(StorableFetchStyle.FIELDS);
        messageQuery.setLimit(1);
        messageQuery.setOffset(0);
        messageQuery.setSortFields(sort);

        RangePredicate messageIdPredicate = new RangePredicateImpl(new StorableFieldImpl(MetricInfoSchema.METRIC_MTR_TIMESTAMP), metricInfo.getFirstMessageOn(), null);
        TermPredicate clientIdPredicate = storablePredicateFactory.newTermPredicate(MessageField.CLIENT_ID, metricInfo.getClientId());
        ExistsPredicate metricPredicate = new ExistsPredicateImpl(MessageField.METRICS.field(), metricInfo.getName());

        AndPredicate andPredicate = new AndPredicateImpl();
        andPredicate.getPredicates().add(messageIdPredicate);
        andPredicate.getPredicates().add(clientIdPredicate);
        andPredicate.getPredicates().add(metricPredicate);
        messageQuery.setPredicate(andPredicate);

        MessageListResult messageList = messageStoreService.query(messageQuery);

        StorableId lastPublishedMessageId = null;
        Date lastPublishedMessageTimestamp = null;
        if (messageList.getSize() == 1) {
            lastPublishedMessageId = messageList.getFirstItem().getDatastoreId();
            lastPublishedMessageTimestamp = messageList.getFirstItem().getTimestamp();
        } else if (messageList.isEmpty()) {
            // this condition could happens due to the ttl of the messages (so if it happens, it does not necessarily mean there has been an error!)
            LOG.warn("Cannot find last timestamp for the specified client id '{}' - account '{}'", new Object[] { metricInfo.getClientId(), metricInfo.getScopeId() });
        } else {
            // this condition shouldn't never happens since the query has a limit 1
            // if happens it means than an elasticsearch internal error happens and/or our driver didn't set it correctly!
            LOG.error("Cannot find last timestamp for the specified client id '{}' - account '{}'. More than one result returned by the query!",
                    new Object[] { metricInfo.getClientId(), metricInfo.getScopeId() });
        }
        metricInfo.setLastMessageId(lastPublishedMessageId);
        metricInfo.setLastMessageOn(lastPublishedMessageTimestamp);
    }

}
