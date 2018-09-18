/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

import javax.persistence.PersistenceException;

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
     * Execute the action on a new entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to the entityManagerActionCallback.<br>
     * This method performs only a rollback (if the transaction is active and an error occurred)!<br>
     *
     * @param entityManagerActionCallback
     * @throws KapuaException
     */
    public <T> void onAction(EntityManagerActionCallback entityManagerActionCallback) throws KapuaException {
        internalOnAction(entityManagerActionCallback, notTransacted);
    }

    /**
     * Execute the action on a new entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality is managed by this method so the called entityManagerActionCallback must leave the transaction open<br>
     *
     * @param entityManagerActionCallback
     * @throws KapuaException
     */
    public <T> void onTransactedAction(EntityManagerActionCallback entityManagerActionCallback) throws KapuaException {
        internalOnAction(entityManagerActionCallback, transacted);
    }

    private <T> void internalOnAction(EntityManagerActionCallback entityManagerActionCallback, TransactionManager transactionManager) throws KapuaException {
        EntityManager manager = null;
        try {
            manager = entityManagerFactory.createEntityManager();
            transactionManager.beginTransaction(manager);
            entityManagerActionCallback.onAction(manager);

            if (manager.isTransactionActive()) {
                appendKapuaEvent(manager);
            }

            transactionManager.commit(manager);
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
    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to the entityManagerResultCallback.<br>
     * This method performs only a rollback (if the transaction is active and an error occurred)!<br>
     *
     * @param entityManagerResultCallback
     * @return
     * @throws KapuaException
     */
    public <T> T onResult(EntityManagerResultCallback<T> entityManagerResultCallback) throws KapuaException {
        return internalOnResult(entityManagerResultCallback, notTransacted);
    }

    /**
     * Return the execution result invoked on a new entity manager.<br>
     * <br>
     * WARNING!<br>
     * The transactionality is managed by this method so the called entityManagerResultCallback must leave the transaction open<br>
     *
     * @param entityManagerResultCallback
     * @return
     * @throws KapuaException
     */
    public <T> T onTransactedResult(EntityManagerResultCallback<T> entityManagerResultCallback) throws KapuaException {
        return internalOnResult(entityManagerResultCallback, transacted);
    }

    private <T> T internalOnResult(EntityManagerResultCallback<T> entityManagerResultCallback, TransactionManager transactionManager) throws KapuaException {
        EntityManager manager = null;
        try {
            manager = entityManagerFactory.createEntityManager();
            transactionManager.beginTransaction(manager);
            T result = entityManagerResultCallback.onResult(manager);

            if (manager.isTransactionActive()) {
                appendKapuaEvent(result, manager);
            }

            transactionManager.commit(manager);
            return result;
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
    }

    /**
     * Return the insert execution result invoked on a new entity manager.<br>
     * This method differs from the onEntityManagerResult because it reiterates the execution if it fails due to {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * WARNING!<br>
     * The transactionality (if needed by the code) must be managed internally to the entityManagerInsertCallback.<br>
     * This method performs only a rollback (if the transaction is active and an error occurred)!<br>
     *
     * @param entityManagerInsertCallback
     * @return
     * @throws KapuaException
     */
    public <T> T onInsert(EntityManagerInsertCallback<T> entityManagerInsertCallback) throws KapuaException {
        return internalOnInsert(entityManagerInsertCallback, notTransacted);
    }

    /**
     * Return the insert execution result invoked on a new entity manager.<br>
     * This method differs from the onEntityManagerResult because it reiterates the execution if it fails due to {@link KapuaEntityExistsException} for a maximum retry.<br>
     * The maximum allowed retry is set by {@link SystemSettingKey#KAPUA_INSERT_MAX_RETRY}.<br>
     * <br>
     * WARNING!<br>
     * The transactionality is managed by this method so the called entityManagerInsertCallback must leave the transaction open<br>
     *
     * @param entityManagerInsertCallback
     * @return
     * @throws KapuaException
     */
    public <T> T onTransactedInsert(EntityManagerInsertCallback<T> entityManagerInsertCallback) throws KapuaException {
        return internalOnInsert(entityManagerInsertCallback, transacted);
    }

    private <T> T internalOnInsert(EntityManagerInsertCallback<T> entityManagerInsertCallback, TransactionManager transactionManager) throws KapuaException {
        boolean succeeded = false;
        int retry = 0;
        EntityManager manager = entityManagerFactory.createEntityManager();
        T instance = null;
        try {
            do {
                try {
                    transactionManager.beginTransaction(manager);
                    instance = entityManagerInsertCallback.onInsert(manager);

                    appendKapuaEvent(instance, manager);

                    transactionManager.commit(manager);
                    succeeded = true;
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
                } catch (PersistenceException e) {
                    if (manager != null) {
                        manager.rollback();
                    }
                    throw KapuaExceptionUtils.convertPersistenceException(e);
                }
            } while (!succeeded);
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
        return instance;
    }

    private <T> EventStoreRecord appendKapuaEvent(EntityManager manager) throws KapuaException {
        return appendKapuaEvent(null, manager);
    }

    private <T> EventStoreRecord appendKapuaEvent(Object instance, EntityManager em) throws KapuaException {
        EventStoreRecord persistedKapuaEvent = null;

        //persist the kapua event only if the instance is not a kapua event instance
        if (!(instance instanceof EventStoreRecord)) {

            // If a kapua event is in scope then persist it along with the entity
            org.eclipse.kapua.event.ServiceEvent serviceEventBus = ServiceEventScope.get();
            if (serviceEventBus != null) {
                if (instance instanceof KapuaEntity) {
                    //make sense to override the entity id and type without checking for previous empty values?
                    //override only if parameters are not evaluated
                    if (serviceEventBus.getEntityType() == null || serviceEventBus.getEntityType().trim().length() <= 0) {
                        logger.debug("Kapua event - update entity type to '{}'", instance.getClass().getName());
                        serviceEventBus.setEntityType(instance.getClass().getName());
                    }
                    if (serviceEventBus.getEntityId() == null) {
                        logger.debug("Kapua event - update entity id to '{}'", ((KapuaEntity) instance).getId());
                        serviceEventBus.setEntityId(((KapuaEntity) instance).getId());
                    }
                    logger.info("Entity '{}' with id '{}' found!", instance.getClass().getName(), ((KapuaEntity) instance).getId());
                }

                //insert the kapua event only if it's a new entity
                if (isNewEvent(serviceEventBus)) {
                    persistedKapuaEvent = EventStoreDAO.create(em, ServiceEventUtil.fromServiceEventBus(serviceEventBus));
                } else {
                    persistedKapuaEvent = EventStoreDAO.update(em,
                            ServiceEventUtil.mergeToEntity(EventStoreDAO.find(em, serviceEventBus.getScopeId(), KapuaEid.parseCompactId(serviceEventBus.getId())), serviceEventBus));
                }
                // update event id on Event
                // persistedKapuaEvent.getId() cannot be null since is generated by the database
                serviceEventBus.setId(persistedKapuaEvent.getId().toCompactId());
            }
        }
        return persistedKapuaEvent;
    }

    private boolean isNewEvent(org.eclipse.kapua.event.ServiceEvent event) {
        return (event.getId() == null);
    }

}