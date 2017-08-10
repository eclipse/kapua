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

import java.util.UUID;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
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
import org.eclipse.kapua.service.datastore.client.ClientCommunicationException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.client.model.InsertResponse;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreCommunicationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

import javax.inject.Inject;

/**
 * Message store service implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService {

    private static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    private static final String METRIC_COMPONENT_NAME = "datastore";

    // metrics
    private final Counter metricMessageCount;
    private final Counter metricCommunicationErrorCount;
    private final Counter metricConfigurationErrorCount;
    private final Counter metricGenericErrorCount;
    private final Counter metricValidationErrorCount;
    // store timers
    private final Timer metricDataSaveTime;
    // queues counters
    private final Counter metricQueueCommunicationErrorCount;
    private final Counter metricQueueConfigurationErrorCount;
    private final Counter metricQueueGenericErrorCount;

    @Inject
    private AccountService accountService;
    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    private final static Integer MAX_ENTRIES_ON_DELETE = DatastoreSettings.getInstance().get(Integer.class, DatastoreSettingKey.CONFIG_MAX_ENTRIES_ON_DELETE);

    private final MessageStoreFacade messageStoreFacade;

    /**
     * Default constructor
     * 
     * @throws ClientUnavailableException
     */
    @Inject
    public MessageStoreServiceImpl(AccountService accountService) throws ClientUnavailableException {
        super(MessageStoreService.class.getName(), DATASTORE_DOMAIN, DatastoreEntityManagerFactory.getInstance());
        ConfigurationProviderImpl configurationProvider = new ConfigurationProviderImpl(this, accountService);
        messageStoreFacade = new MessageStoreFacade(configurationProvider, DatastoreMediator.getInstance());
        DatastoreMediator.getInstance().setMessageStoreFacade(messageStoreFacade);
        // data message
        MetricsService metricService = MetricServiceFactory.getInstance();
        metricMessageCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "count");
        metricCommunicationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "communication", "error", "count");
        metricConfigurationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "configuration", "error", "count");
        metricGenericErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "generic", "error", "count");
        metricValidationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "validation", "error", "count");
        // error messages queues size
        metricQueueCommunicationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "communication", "error", "count");
        metricQueueConfigurationErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "configuration", "error", "count");
        metricQueueGenericErrorCount = metricService.getCounter(METRIC_COMPONENT_NAME, "datastore", "store", "queue", "generic", "error", "count");
        // store timers
        metricDataSaveTime = metricService.getTimer(METRIC_COMPONENT_NAME, "datastore", "store", "messages", "time", "s");
    }

    @Override
    public InsertResponse store(KapuaMessage<?, ?> message)
            throws KapuaException {
        String datastoreId = UUID.randomUUID().toString();
        Context metricDataSaveTimeContext = metricDataSaveTime.time();
        try {
            checkDataAccess(message.getScopeId(), Actions.write);
            metricMessageCount.inc();
            return messageStoreFacade.store(message, datastoreId, true);
        } catch (ConfigurationException e) {
            metricConfigurationErrorCount.inc();
            metricQueueConfigurationErrorCount.inc();
            throw e;
        } catch (KapuaIllegalArgumentException e) {
            metricValidationErrorCount.inc();
            metricQueueGenericErrorCount.inc();
            throw e;
        } catch (ClientCommunicationException e) {
            metricCommunicationErrorCount.inc();
            metricQueueCommunicationErrorCount.inc();
            throw new DatastoreCommunicationException(datastoreId, e);
        } catch (Exception e) {
            metricGenericErrorCount.inc();
            metricQueueGenericErrorCount.inc();
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        } finally {
            metricDataSaveTimeContext.stop();
        }
    }

    @Override
    public InsertResponse store(KapuaMessage<?, ?> message, String datastoreId)
            throws KapuaException {
        ArgumentValidator.notEmptyOrNull(datastoreId, "datastoreId");
        Context metricDataSaveTimeContext = metricDataSaveTime.time();
        try {
            checkDataAccess(message.getScopeId(), Actions.write);
            metricMessageCount.inc();
            return messageStoreFacade.store(message, datastoreId, false);
        } catch (ConfigurationException e) {
            metricConfigurationErrorCount.inc();
            metricQueueConfigurationErrorCount.inc();
            throw e;
        } catch (KapuaIllegalArgumentException e) {
            metricValidationErrorCount.inc();
            metricQueueGenericErrorCount.inc();
            throw e;
        } catch (ClientCommunicationException e) {
            metricCommunicationErrorCount.inc();
            metricQueueCommunicationErrorCount.inc();
            throw new DatastoreCommunicationException(datastoreId, e);
        } catch (Exception e) {
            metricGenericErrorCount.inc();
            metricQueueGenericErrorCount.inc();
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        } finally {
            metricDataSaveTimeContext.stop();
        }
    }

    @Override
    public void delete(KapuaId scopeId, StorableId id)
            throws KapuaException {
        checkDataAccess(scopeId, Actions.delete);
        try {
            messageStoreFacade.delete(scopeId, id);
        } catch (Exception e) {
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public DatastoreMessage find(KapuaId scopeId, StorableId id, StorableFetchStyle fetchStyle)
            throws KapuaException {
        checkDataAccess(scopeId, Actions.read);
        try {
            return messageStoreFacade.find(scopeId, id, fetchStyle);
        } catch (Exception e) {
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public MessageListResult query(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return messageStoreFacade.query(query);
        } catch (Exception e) {
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public long count(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            return messageStoreFacade.count(query);
        } catch (Exception e) {
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
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
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    private void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        Permission permission = permissionFactory.newPermission(DATASTORE_DOMAIN, action, scopeId);
        authorizationService.checkPermission(permission);
    }
}
