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
import org.eclipse.kapua.service.datastore.ClientInfoRegistryService;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.DatastoreObjectFactory;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.EsSchema;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.AndPredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.RangePredicateImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.SortFieldImpl;
import org.eclipse.kapua.service.datastore.internal.model.query.StorableFieldImpl;
import org.eclipse.kapua.service.datastore.model.ClientInfo;
import org.eclipse.kapua.service.datastore.model.ClientInfoListResult;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ClientInfoQuery;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortDirection;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client information registry implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class ClientInfoRegistryServiceImpl extends AbstractKapuaConfigurableService implements ClientInfoRegistryService
{
    private static final long serialVersionUID = 6772144495298409738L;

    private static final Domain datastoreDomain = new DatastoreDomain();

    private static final Logger logger = LoggerFactory.getLogger(ClientInfoRegistryServiceImpl.class);

    private final AccountService           accountService;
    private final AuthorizationService     authorizationService;
    private final PermissionFactory        permissionFactory;
    private final ClientInfoRegistryFacade facade;
    private final MessageStoreService      messageStoreService;
    private final DatastoreObjectFactory   datastoreObjectFactory;

    public ClientInfoRegistryServiceImpl()
    {
        super(ClientInfoRegistryService.class.getName(), datastoreDomain, DatastoreEntityManagerFactory.getInstance());

        KapuaLocator locator = KapuaLocator.getInstance();
        accountService = locator.getService(AccountService.class);
        authorizationService = locator.getService(AuthorizationService.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
        messageStoreService = locator.getService(MessageStoreService.class);
        datastoreObjectFactory = KapuaLocator.getInstance().getFactory(DatastoreObjectFactory.class);

        MessageStoreService messageStoreService = KapuaLocator.getInstance().getService(MessageStoreService.class);
        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(messageStoreService, accountService);
        this.facade = new ClientInfoRegistryFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setClientInfoStoreFacade(this.facade);
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkAccess(scopeId, Actions.delete);

            this.facade.delete(scopeId, id);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public ClientInfo find(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkAccess(scopeId, Actions.read);

            ClientInfo clientInfo = this.facade.find(scopeId, id);

            // populate the lastMessageTimestamp
            updateLastPublishedFields(scopeId, clientInfo);

            return clientInfo;
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public ClientInfoListResult query(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkAccess(scopeId, Actions.read);

            ClientInfoListResult result = this.facade.query(scopeId, query);

            // populate the lastMessageTimestamp
            for (ClientInfo clientInfo : result) {
                updateLastPublishedFields(scopeId, clientInfo);
            }

            return result;
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public long count(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException
    {
        try {
            //
            // Argument Validation
            ArgumentValidator.notNull(scopeId, "scopeId");

            //
            // Check Access
            this.checkAccess(scopeId, Actions.read);

            return this.facade.count(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, ClientInfoQuery query)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkAccess(scopeId, Actions.delete);

            this.facade.delete(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    private void checkAccess(KapuaId scopeId, Actions action)
        throws KapuaException
    {
        //
        // Check Access
        Permission permission = permissionFactory.newPermission(datastoreDomain, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    /**
     * Update the last published date and last published message identifier for the specified client info, so it gets the timestamp and the message identifier of the last published message for the
     * account/clientId in the client info
     * 
     * @param scopeId
     * @param channelInfo
     * 
     * @throws KapuaException
     */
    private void updateLastPublishedFields(KapuaId scopeId, ClientInfo clientInfo) throws KapuaException
    {
        StorableId lastPublishedMessageId = null;
        Date lastPublishedMessageTimestamp = null;
        MessageQuery messageQuery = new MessageQueryImpl();
        messageQuery.setAskTotalCount(true);
        messageQuery.setFetchStyle(StorableFetchStyle.SOURCE_SELECT);
        messageQuery.setLimit(1);
        messageQuery.setOffset(0);
        List<SortField> sort = new ArrayList<SortField>();
        SortField sortTimestamp = new SortFieldImpl();
        sortTimestamp.setField(EsSchema.MESSAGE_TIMESTAMP);
        sortTimestamp.setSortDirection(SortDirection.DESC);
        sort.add(sortTimestamp);
        messageQuery.setSortFields(sort);
        AndPredicate andPredicate = new AndPredicateImpl();
        RangePredicate messageIdPredicate = new RangePredicateImpl(new StorableFieldImpl(EsSchema.CLIENT_TIMESTAMP), clientInfo.getFirstPublishedMessageTimestamp(), null);
        andPredicate.getPredicates().add(messageIdPredicate);
        TermPredicate accountNamePredicate = datastoreObjectFactory.newTermPredicate(MessageField.ACCOUNT, clientInfo.getAccount());
        andPredicate.getPredicates().add(accountNamePredicate);
        TermPredicate clientIdPredicate = datastoreObjectFactory.newTermPredicate(MessageField.CLIENT_ID, clientInfo.getClientId());
        andPredicate.getPredicates().add(clientIdPredicate);
        messageQuery.setPredicate(andPredicate);
        MessageListResult messageList = messageStoreService.query(scopeId, messageQuery);
        if (messageList.size() == 1) {
            lastPublishedMessageId = messageList.get(0).getDatastoreId();
            lastPublishedMessageTimestamp = messageList.get(0).getTimestamp();
        }
        else if (messageList.size() == 0) {
            // this condition could happens due to the ttl of the messages (so if it happens, it does not necessarily mean there has been an error!)
            logger.warn("Cannot find last timestamp for the specified client id '{}' - account '{}'", new Object[] { clientInfo.getAccount(), clientInfo.getClientId() });
        }
        else {
            // this condition shouldn't never happens since the query has a limit 1
            // if happens it means than an elasticsearch internal error happens and/or our driver didn't set it correctly!
            logger.error("Cannot find last timestamp for the specified client id '{}' - account '{}'. More than one result returned by the query!", new Object[] { clientInfo.getAccount(), clientInfo.getClientId() });
        }
        clientInfo.setLastPublishedMessageId(lastPublishedMessageId);
        clientInfo.setLastPublishedMessageTimestamp(lastPublishedMessageTimestamp);
    }
}
