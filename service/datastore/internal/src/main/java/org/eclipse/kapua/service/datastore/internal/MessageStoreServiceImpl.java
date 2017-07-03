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
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;

/**
 * Message store service implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService {

    private static final Domain DATASTORE_DOMAIN = new DatastoreDomain();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final AccountService accountService = LOCATOR.getService(AccountService.class);
    private final AuthorizationService authorizationService = LOCATOR.getService(AuthorizationService.class);
    private final PermissionFactory permissionFactory = LOCATOR.getFactory(PermissionFactory.class);
    private final static Integer MAX_ENTRIES_ON_DELETE = DatastoreSettings.getInstance().get(Integer.class, DatastoreSettingKey.CONFIG_MAX_ENTRIES_ON_DELETE);

    private final MessageStoreFacade messageStoreFacade;

    /**
     * Default constructor
     * 
     * @throws ClientUnavailableException
     */
    public MessageStoreServiceImpl() throws ClientUnavailableException {
        super(MessageStoreService.class.getName(), DATASTORE_DOMAIN, DatastoreEntityManagerFactory.getInstance());

        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(this, accountService);
        messageStoreFacade = new MessageStoreFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMessageStoreFacade(messageStoreFacade);
    }

    @Override
    public InsertResponse store(KapuaMessage<?, ?> message)
            throws KapuaException {
        ArgumentValidator.notNull(message.getScopeId(), "message.scopeId");

        checkDataAccess(message.getScopeId(), Actions.write);
        try {
            return messageStoreFacade.store(message);
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
            messageStoreFacade.delete(scopeId, id);
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
            return messageStoreFacade.find(scopeId, id, fetchStyle);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public MessageListResult query(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return messageStoreFacade.query(query);
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
            return messageStoreFacade.count(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    @Override
    public void delete(MessageQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");
        ArgumentValidator.notNull(query.getScopeId(), "query.scopeId");
        ArgumentValidator.numRange(query.getLimit(), 0, MAX_ENTRIES_ON_DELETE, "limit");

        checkDataAccess(query.getScopeId(), Actions.delete);
        try {
            messageStoreFacade.delete(query);
        } catch (Exception e) {
            throw KapuaException.internalError(e);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        Permission permission = permissionFactory.newPermission(DATASTORE_DOMAIN, action, scopeId);
        authorizationService.checkPermission(permission);
    }
}
