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
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message store service implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService {

    private static final Domain datastoreDomain = new DatastoreDomain();

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(MessageStoreServiceImpl.class);

    private static final KapuaLocator locator = KapuaLocator.getInstance();

    private final AccountService accountService = locator.getService(AccountService.class);
    private final AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
    private final PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);

    private final MessageStoreFacade esMessageStoreFacade;

    /**
     * Default constructor
     * 
     * @since 1.0.0
     */
    public MessageStoreServiceImpl() {
        super(MessageStoreService.class.getName(), datastoreDomain, DatastoreEntityManagerFactory.getInstance());

        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(this, accountService);
        this.esMessageStoreFacade = new MessageStoreFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMessageStoreFacade(esMessageStoreFacade);
    }

    @Override
    public StorableId store(KapuaMessage<?, ?> message)
            throws KapuaException {
        ArgumentValidator.notNull(message, "message");
        ArgumentValidator.notNull(message.getScopeId(), "message.scopeId");

        checkDataAccess(message.getScopeId(), Actions.write);

        try {
            return esMessageStoreFacade.store(message);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");

        checkDataAccess(scopeId, Actions.delete);

        try {
            esMessageStoreFacade.delete(scopeId, id);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle)
            throws KapuaException {
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(id, "id");
        ArgumentValidator.notNull(fetchStyle, "fetchStyle");

        checkDataAccess(scopeId, Actions.read);

        try {
            return esMessageStoreFacade.find(scopeId, id, fetchStyle);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MessageListResult query(MessageQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.read);

        try {
            return esMessageStoreFacade.query(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public long count(MessageQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.read);

        try {
            return esMessageStoreFacade.count(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(MessageQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");

        checkDataAccess(query.getScopeId(), Actions.delete);

        try {
            esMessageStoreFacade.delete(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        //
        // Check Access
        Permission permission = permissionFactory.newPermission(datastoreDomain, action, scopeId);
        authorizationService.checkPermission(permission);
    }
}
