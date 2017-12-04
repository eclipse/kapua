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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.metric.MetricServiceFactory;
import org.eclipse.kapua.commons.metric.MetricsService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.KapuaDateUtils;
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
import org.eclipse.kapua.service.datastore.client.ClientCommunicationException;
import org.eclipse.kapua.service.datastore.client.ClientUnavailableException;
import org.eclipse.kapua.service.datastore.internal.mediator.ConfigurationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreCommunicationException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreErrorCodes;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreException;
import org.eclipse.kapua.service.datastore.internal.mediator.DatastoreMediator;
import org.eclipse.kapua.service.datastore.internal.mediator.MessageField;
import org.eclipse.kapua.service.datastore.internal.model.query.MessageQueryImpl;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettingKey;
import org.eclipse.kapua.service.datastore.internal.setting.DatastoreSettings;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.datastore.model.MessageListResult;
import org.eclipse.kapua.service.datastore.model.StorableId;
import org.eclipse.kapua.service.datastore.model.query.AndPredicate;
import org.eclipse.kapua.service.datastore.model.query.ExistsPredicate;
import org.eclipse.kapua.service.datastore.model.query.MessageQuery;
import org.eclipse.kapua.service.datastore.model.query.RangePredicate;
import org.eclipse.kapua.service.datastore.model.query.SortField;
import org.eclipse.kapua.service.datastore.model.query.StorableFetchStyle;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicate;
import org.eclipse.kapua.service.datastore.model.query.StorablePredicateFactory;
import org.eclipse.kapua.service.datastore.model.query.TermPredicate;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.Timer.Context;

/**
 * Message store service implementation.
 * 
 * @since 1.0.0
 */
@KapuaProvider
public class MessageStoreServiceImpl extends AbstractKapuaConfigurableService implements MessageStoreService {

    private final static String DATA_TTL_KEY = "dataTTL";
    protected static final Domain DATASTORE_DOMAIN = new DatastoreDomain();
    protected static final String METRIC_COMPONENT_NAME = "datastore";


    protected static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
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

    private static final StorablePredicateFactory STORABLE_PREDICATE_FACTORY = LOCATOR.getFactory(StorablePredicateFactory.class);
    private final AccountService accountService = LOCATOR.getService(AccountService.class);
    private final AuthorizationService authorizationService = LOCATOR.getService(AuthorizationService.class);
    private final PermissionFactory permissionFactory = LOCATOR.getFactory(PermissionFactory.class);
    private final static Integer MAX_ENTRIES_ON_DELETE = DatastoreSettings.getInstance().get(Integer.class, DatastoreSettingKey.CONFIG_MAX_ENTRIES_ON_DELETE);

    protected final MessageStoreFacade messageStoreFacade;

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
    public StorableId store(KapuaMessage<?, ?> message)
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
    public StorableId store(KapuaMessage<?, ?> message, String datastoreId)
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
            MessageQuery queryCopy = adjustStartDate(query);
            return messageStoreFacade.query(queryCopy);
        } catch (Exception e) {
            throw new DatastoreException(KapuaErrorCodes.INTERNAL_ERROR, e);
        }
    }

    @Override
    public long count(MessageQuery query)
            throws KapuaException {
        checkDataAccess(query.getScopeId(), Actions.read);
        try {
            MessageQuery queryCopy = adjustStartDate(query);
            return messageStoreFacade.count(queryCopy);
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

    protected void checkDataAccess(KapuaId scopeId, Actions action)
            throws KapuaException {
        Permission permission = permissionFactory.newPermission(DATASTORE_DOMAIN, action, scopeId);
        authorizationService.checkPermission(permission);
    }

    protected MessageQuery adjustStartDate(MessageQuery query) throws KapuaException {
        MessageQueryImpl queryCopy = new MessageQueryImpl(query.getScopeId());
        queryCopy.setFetchStyle(query.getFetchStyle());
        queryCopy.setLimit(query.getLimit());
        queryCopy.setOffset(query.getOffset());
        queryCopy.setSortFields(query.getSortFields() != null ? new ArrayList<SortField>(query.getSortFields()) : null);
        queryCopy.setAskTotalCount(query.isAskTotalCount());
        queryCopy.setPredicate(copyCompositePredicate(query.getPredicate()));

        Map<String, Object> datastoreConfiguration = getConfigValues(query.getScopeId());
        Integer dataTtl = (Integer) datastoreConfiguration.get(DATA_TTL_KEY);
        if (dataTtl != null) {
            RangePredicate ttlPredicate = rangePredicateFromTtl(dataTtl);
            StorablePredicate predicate = queryCopy.getPredicate();
            if (predicate != null) {
                if (predicate instanceof AndPredicate) {
                    AndPredicate andPredicate = (AndPredicate) predicate;
                    andPredicate.getPredicates().add(ttlPredicate);
                } else {
                    AndPredicate andPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
                    andPredicate.getPredicates().add(ttlPredicate);
                    andPredicate.getPredicates().add(predicate);
                    queryCopy.setPredicate(andPredicate);
                }
            } else {
                AndPredicate andPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
                andPredicate.getPredicates().add(ttlPredicate);
                queryCopy.setPredicate(andPredicate);
            }
        }

        return queryCopy;
    }

    private StorablePredicate copyCompositePredicate(StorablePredicate predicate) throws DatastoreException {        
        if (predicate == null) {
            return null;
        }

        StorablePredicate copyPredicate = null;
        if (predicate instanceof AndPredicate) {
            copyPredicate = copyAndPredicate((AndPredicate)predicate);
        } else if (predicate instanceof RangePredicate) {
            copyPredicate = copyRangePredicate((RangePredicate) predicate);
        } else if (predicate instanceof TermPredicate) {
            copyPredicate = copyTermPredicate((TermPredicate) predicate);
        } else if (predicate instanceof ExistsPredicate) {
            copyPredicate = copyExistsPredicate((ExistsPredicate) predicate);
        } else {
            throw new DatastoreException(DatastoreErrorCodes.UNHANDLED_QUERY_PREDICATE_TYPE, "Unhadled predicate type: " + predicate.getClass().getName());
        }

        return copyPredicate;
    }

    private AndPredicate copyAndPredicate(AndPredicate andPredicate) throws DatastoreException {
        if (andPredicate == null) {
            return null;
        }

        AndPredicate copyPredicate = STORABLE_PREDICATE_FACTORY.newAndPredicate();
        for (StorablePredicate tmpPredicate : andPredicate.getPredicates()) {
            copyPredicate.getPredicates().add(copyCompositePredicate(tmpPredicate));
        }

        return copyPredicate;
    }

    @SuppressWarnings("unchecked")
    private RangePredicate copyRangePredicate(RangePredicate rangePredicate) {
        if (rangePredicate == null) {
            return null;
        }

        return STORABLE_PREDICATE_FACTORY.newRangePredicate(rangePredicate.getField(), rangePredicate.getMinValue(Comparable.class), rangePredicate.getMaxValue(Comparable.class));
    }

    private TermPredicate copyTermPredicate(TermPredicate termPredicate) {
        if (termPredicate == null) {
            return null;
        }

        return STORABLE_PREDICATE_FACTORY.newTermPredicate(termPredicate.getField(), termPredicate.getValue());
    }

    private ExistsPredicate copyExistsPredicate(ExistsPredicate existsPredicate) {
        if (existsPredicate == null) {
            return null;
        }

        return STORABLE_PREDICATE_FACTORY.newExistsPredicate(existsPredicate.getName());
    }

    private RangePredicate rangePredicateFromTtl(Integer dataTtl) {
        Instant currentDate = KapuaDateUtils.getKapuaSysDate();
        Date dateTo = Date.from(currentDate);
        Date dateFrom = Date.from(currentDate.minus(dataTtl, ChronoUnit.DAYS));
        return STORABLE_PREDICATE_FACTORY.newRangePredicate(MessageField.TIMESTAMP.field(), dateFrom, dateTo);
    }
}
