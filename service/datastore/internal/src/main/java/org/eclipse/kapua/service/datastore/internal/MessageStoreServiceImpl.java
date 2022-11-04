/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal;

import com.codahale.metrics.Timer.Context;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomains;
import org.eclipse.kapua.service.datastore.MessageStoreService;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreCommunicationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingsKey;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.elasticsearch.client.exception.ClientCommunicationException;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.eclipse.kapua.service.storable.model.query.StorableFetchStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.UUID;

/**
 * Message store service implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService {

    private static final Logger logger = LoggerFactory.getLogger(MessageStoreServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    protected final AccountService accountService = LOCATOR.getService(AccountService.class);
    protected final AuthorizationService authorizationService = LOCATOR.getService(AuthorizationService.class);
    protected final PermissionFactory permissionFactory = LOCATOR.getFactory(PermissionFactory.class);
    protected static final Integer MAX_ENTRIES_ON_DELETE = DatastoreSettings.getInstance().getInt(DatastoreSettingsKey.CONFIG_MAX_ENTRIES_ON_DELETE);

    protected final MessageStoreFacade messageStoreFacade;

    //TODO inject!!!
    private MetricsDatastore metrics;

    /**
     * Constructor.
     * <p>
     * Initializes {@link ConfigurationProvider} and {@link MetricsService}
     *
     * @since 1.0.0
     */
    public MessageStoreServiceImpl() {
        super(MessageStoreService.class.getName(),
                DatastoreDomains.DATASTORE_DOMAIN,
                DatastoreEntityManagerFactory.getInstance(),
                null,
                null,
                null,
                null);
        metrics = MetricsDatastore.getInstance();
        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(this, accountService);
        messageStoreFacade = new MessageStoreFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMessageStoreFacade(messageStoreFacade);
    }

    @Override
    public StorableId store(KapuaMessage<?, ?> message)
            throws KapuaException {
        String datastoreId = UUID.randomUUID().toString();
        Context metricDataSaveTimeContext = metrics.getDataSaveTime().time();
        try {
            checkDataAccess(message.getScopeId(), Actions.write);
            metrics.getMessage().inc();
            return messageStoreFacade.store(message, datastoreId, true);
        } catch (ConfigurationException e) {
            metrics.getConfigurationError().inc();
            metrics.getQueueConfigurationError().inc();
            throw e;
        } catch (KapuaIllegalArgumentException e) {
            metrics.getValidationError().inc();
            metrics.getQueueGenericError().inc();
            throw e;
        } catch (ClientCommunicationException e) {
            metrics.getCommunicationError().inc();
            metrics.getQueueCommunicationError().inc();
            throw new DatastoreCommunicationException(datastoreId, e);
        } catch (Exception e) {
            metrics.getGenericError().inc();
            metrics.getQueueGenericError().inc();
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        } finally {
            metricDataSaveTimeContext.stop();
        }
    }

    @Override
    public StorableId store(KapuaMessage<?, ?> message, String datastoreId)
            throws KapuaException {
        ArgumentValidator.notEmptyOrNull(datastoreId, "datastoreId");
        Context metricDataSaveTimeContext = metrics.getDataSaveTime().time();
        try {
            checkDataAccess(message.getScopeId(), Actions.write);
            metrics.getMessage().inc();
            return messageStoreFacade.store(message, datastoreId, false);
        } catch (ConfigurationException e) {
            metrics.getConfigurationError().inc();
            metrics.getQueueConfigurationError().inc();
            throw e;
        } catch (KapuaIllegalArgumentException e) {
            metrics.getValidationError().inc();
            metrics.getQueueGenericError().inc();
            throw e;
        } catch (ClientCommunicationException e) {
            metrics.getCommunicationError().inc();
            metrics.getQueueCommunicationError().inc();
            throw new DatastoreCommunicationException(datastoreId, e);
        } catch (Exception e) {
            metrics.getGenericError().inc();
            metrics.getQueueGenericError().inc();
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        } finally {
            metricDataSaveTimeContext.stop();
        }
    }

    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id) throws KapuaException {
        return find(scopeId, id, StorableFetchStyle.SOURCE_FULL);
    }

    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle) throws KapuaException {
        checkDataAccess(scopeId, Actions.read);
        try {
            return messageStoreFacade.find(scopeId, id, fetchStyle);
        } catch (Exception e) {
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        }
    }

    @Override
    public MessageListResult query(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return messageStoreFacade.query(query);
        } catch (Exception e) {
            logException(e);
            throw new DatastoreException(
                    KapuaErrorCodes.INTERNAL_ERROR,
                    e,
                    e.getCause().getMessage() != null ? e.getCause().getMessage() : e.getMessage()
            );
        }
    }

    @Override
    public long count(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return messageStoreFacade.count(query);
        } catch (Exception e) {
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaException {
        checkDataAccess(scopeId, Actions.delete);
        try {
            messageStoreFacade.delete(scopeId, id);
        } catch (Exception e) {
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        }
    }

    @Override
    public void delete(MessageQuery query)
            throws KapuaException {
        ArgumentValidator.numRange(query.getLimit(), 0, MAX_ENTRIES_ON_DELETE, "limit");

        checkDataAccess(query.getScopeId(), Actions.delete);
        try {
            messageStoreFacade.delete(query);
        } catch (Exception e) {
            logException(e);
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e, e.getMessage());
        }
    }

    @Override
    public boolean isServiceEnabled(KapuaId scopeId) {
        return !DatastoreSettings.getInstance().getBoolean(DatastoreSettingsKey.DISABLE_DATASTORE, false);
    }

    protected void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        Permission permission = permissionFactory.newPermission(DatastoreDomains.DATASTORE_DOMAIN, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    private void logException(Exception e) {
        if (e instanceof RuntimeException) {
            logger.debug("", e);
        }
    }
}
