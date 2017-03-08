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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.permission.Actions;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomain;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.elasticsearch.DatastoreMediator;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message store service implementation.
 * 
 * @since 1.0
 *
 */
@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService
{
    private static final long serialVersionUID = 4142282449826005424L;

    private static final Domain datastoreDomain = new DatastoreDomain();

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(MessageStoreServiceImpl.class);

    private static final KapuaLocator locator = KapuaLocator.getInstance();

    private final AccountService       accountService       = locator.getService(AccountService.class);
    private final AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
    private final PermissionFactory    permissionFactory    = locator.getFactory(PermissionFactory.class);

    private final MessageStoreFacade esMessageStoreFacade;

    public MessageStoreServiceImpl()
    {
        super(MessageStoreService.class.getName(), datastoreDomain, DatastoreEntityManagerFactory.getInstance());

        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(this, accountService);
        this.esMessageStoreFacade = new MessageStoreFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMessageStoreFacade(esMessageStoreFacade);
    }

    @Override
    public StorableId store(KapuaMessage<?, ?> message)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(message.getScopeId(), "scopeId");

            this.checkDataAccess(message.getScopeId(), Actions.write);

            return this.esMessageStoreFacade.store(message);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkDataAccess(scopeId, Actions.delete);

            this.esMessageStoreFacade.delete(scopeId, id);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkDataAccess(scopeId, Actions.read);

            return this.esMessageStoreFacade.find(scopeId, id, fetchStyle);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MessageListResult query(KapuaId scopeId, MessageQuery query)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkDataAccess(scopeId, Actions.read);

            return this.esMessageStoreFacade.query(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public long count(KapuaId scopeId, MessageQuery query)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkDataAccess(scopeId, Actions.read);

            return this.esMessageStoreFacade.count(scopeId, query);
        }
        catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, MessageQuery query)
        throws KapuaException
    {
        try {
            ArgumentValidator.notNull(scopeId, "scopeId");

            this.checkDataAccess(scopeId, Actions.delete);

            this.esMessageStoreFacade.delete(scopeId, query);
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
}
