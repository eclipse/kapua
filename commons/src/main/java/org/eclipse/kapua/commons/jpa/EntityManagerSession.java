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
 *     Red Hat
 *******************************************************************************/
package org.eclipse.kapua.commons.jpa;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.ServiceEventScope;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.ServiceEventUtil;
import org.eclipse.kapua.commons.service.event.store.internal.EventStoreDAO;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entity manager session reference implementation.
 *
 * @since 1.0
 */
public class EntityManagerSession {

    private static final Logger logger = LoggerFactory.getLogger(EntityManagerSession.class);

    private final EntityManagerFactory entityManagerFactory;
    private static final int MAX_INSERT_ALLOWED_RETRY = SystemSetting.getInstance().getInt(SystemSettingKey.KAPUA_INSERT_MAX_RETRY);

    private TransactionManager transacted = new TransactionManagerTransacted();
    private TransactionManager notTransacted = new TransactionManagerNotTransacted();

    /**
     * Constructor
     *
     * @param entityManagerFactory
     */
    public EntityManagerSession(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * If the requested action is an insert, it reiterates the execution if it fails due to
     * {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to the entityManagerCallback.<br>
     * This method performs only a rollback (if the transaction is active and an error occurred)!<br>
     *
     * @param resultHandler
     * @return
     * @throws KapuaException
     */
    public <T> T doAction(EntityManagerCallback<T> resultHandler) throws KapuaException {
        return internalOnResult(EntityManagerContainer.<T>create().onResultHandler(resultHandler), notTransacted, false);
    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * If the requested action is an insert, it reiterates the execution if it fails due to
     * {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * WARNING!<br>
     * The transactionality is managed by this method so the called entityManagerResultCallback must leave the transaction open<br>
     *
     * @param resultHandler
     * @return
     * @throws KapuaException
     */
    public <T> T doTransactedAction(EntityManagerCallback<T> resultHandler) throws KapuaException {
        return internalOnResult(EntityManagerContainer.<T>create().onResultHandler(resultHandler), transacted, true);
    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * If the requested action is an insert, it reiterates the execution if it fails due to
     * {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * This method allows to set the before and after result handler calls
     *
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to the entityManagerCallback.<br>
     * This method performs only a rollback (if the transaction is active and an error occurred)!<br>
     *
     * @param container
     * @return
     * @throws KapuaException
     */
    public <T> T doAction(EntityManagerContainer<T> container) throws KapuaException {
        return internalOnResult(container, notTransacted, false);
    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * If the requested action is an insert, it reiterates the execution if it fails due to
     * {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * This method allows to set the before and after result handler calls
     *
     * WARNING!<br>
     * The transactionality is managed by this method so the called entityManagerResultCallback must leave the transaction open<br>
     *
     * @param container
     * @return
     * @throws KapuaException
     */
    public <T> T doTransactedAction(EntityManagerContainer<T> container) throws KapuaException {
        return internalOnResult(container, transacted, true);
    }

    private <T> T internalOnResult(EntityManagerContainer<T> container, TransactionManager transactionManager, boolean transacted) throws KapuaException {
        boolean succeeded = false;
        int retry = 0;
        T instance = container.onBefore();
        if (instance == null) {
            EntityManager manager = entityManagerFactory.createEntityManager();
            try {
                do {
                    try {
                        transactionManager.beginTransaction(manager);
                        instance = container.onResult(manager);

                        if (manager.isTransactionActive()) {
                            appendKapuaEvent(instance, manager, getServiceEventIfPresent(instance));
                        }

                        transactionManager.commit(manager);
                        succeeded = true;
                        if (manager instanceof KapuaEntity) {
                            manager.detach((KapuaEntity) instance);
                            // TODO: check behavior without the detach (when all caches are implemented)
                        }
                        container.onAfter(instance);
                    } catch (KapuaEntityExistsException e) {
                        if (manager != null) {
                            manager.rollback();
                        }
                        if (++retry < MAX_INSERT_ALLOWED_RETRY) {
                            logger.warn("Entity already exists. Cannot insert the entity, try again!");
                        } else {
                            manager.rollback();
                            throw KapuaExceptionUtils.convertPersistenceException(e);
                        }
                    }
                }
                while (!succeeded);
            } catch (Exception e) {
                if (manager != null) {
                    manager.rollback();
                }
                throw KapuaExceptionUtils.convertPersistenceException(e);
            } finally {
                if (manager != null) {
                    manager.close();
                }
            }
        } else {
            //if the onBeforeResult return an entity we need to check if the method has annotations to throw event and, in this case, we must sent it
            //e.g. we executed a find (so with a cache hit) annotated to throw events. We must send the event (in this case there is not too much advantage using the cache)
            if (transacted) {
                appendKapuaEvent(instance, transactionManager);
            }
        }
        return instance;
    }

    private org.eclipse.kapua.event.ServiceEvent getServiceEventIfPresent(Object instance) {
        if (!(instance instanceof EventStoreRecord)) {
            return ServiceEventScope.get();
        } else {
            return null;
        }
    }

    private <T> EventStoreRecord appendKapuaEvent(Object instance, EntityManager em, org.eclipse.kapua.event.ServiceEvent serviceEvent) throws KapuaException {
        EventStoreRecord persistedKapuaEvent = null;
        if (serviceEvent != null) {
            persistedKapuaEvent = persistServiceEvent(em, serviceEvent, instance);
        }
        return persistedKapuaEvent;
    }

    private <T> EventStoreRecord appendKapuaEvent(Object instance, TransactionManager transactionManager) throws KapuaException {
        EventStoreRecord persistedKapuaEvent = null;
        org.eclipse.kapua.event.ServiceEvent serviceEvent = getServiceEventIfPresent(instance);
        if (serviceEvent != null) {
            EntityManager manager = entityManagerFactory.createEntityManager();
            transactionManager.beginTransaction(manager);
            persistedKapuaEvent = appendKapuaEvent(instance, manager, serviceEvent);
            transactionManager.commit(manager);
        }
        return persistedKapuaEvent;
    }

    private EventStoreRecord persistServiceEvent(EntityManager em, org.eclipse.kapua.event.ServiceEvent serviceEvent, Object instance) throws KapuaException {
        EventStoreRecord persistedKapuaEvent;
        if (instance instanceof KapuaEntity) {
            KapuaEntity kapuaEntity = (KapuaEntity) instance;
            //make sense to override the entity id and type without checking for previous empty values?
            //override only if parameters are not evaluated
            logger.info("Updating service event entity infos (type, id and scope id) if missing...");
            if (serviceEvent.getEntityType() == null || serviceEvent.getEntityType().trim().length() <= 0) {
                logger.info("Kapua event - update entity type to '{}'", kapuaEntity.getClass().getName());
                serviceEvent.setEntityType(kapuaEntity.getClass().getName());
            }
            if (serviceEvent.getEntityId() == null) {
                logger.info("Kapua event - update entity id to '{}'", kapuaEntity.getId());
                serviceEvent.setEntityId(kapuaEntity.getId());
            }
            if (serviceEvent.getEntityScopeId() == null) {
                logger.info("Kapua event - update entity scope id to '{}'", kapuaEntity.getScopeId());
                serviceEvent.setEntityScopeId(kapuaEntity.getScopeId());
            }
            logger.info("Updating service event entity infos (type, id and scope id) if missing... DONE");
            logger.info("Entity '{}' with id '{}' and scope id '{}' found!", instance.getClass().getName(), kapuaEntity.getId(), kapuaEntity.getScopeId());
        }

        //insert the kapua event only if it's a new entity
        if (isNewEvent(serviceEvent)) {
            persistedKapuaEvent = EventStoreDAO.create(em, ServiceEventUtil.fromServiceEventBus(serviceEvent));
        } else {
            persistedKapuaEvent = EventStoreDAO.update(em,
                    ServiceEventUtil.mergeToEntity(EventStoreDAO.find(em, serviceEvent.getScopeId(), KapuaEid.parseCompactId(serviceEvent.getId())), serviceEvent));
        }
        // update event id on Event
        // persistedKapuaEvent.getId() cannot be null since is generated by the database
        serviceEvent.setId(persistedKapuaEvent.getId().toCompactId());
        return persistedKapuaEvent;
    }

    private boolean isNewEvent(org.eclipse.kapua.event.ServiceEvent event) {
        return (event.getId() == null);
    }

}
