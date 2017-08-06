/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
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

import javax.persistence.PersistenceException;

import org.eclipse.kapua.KapuaEntityExistsException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.event.service.EventScope;
import org.eclipse.kapua.commons.event.service.internal.KapuaEventStoreDAO;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.KapuaExceptionUtils;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.service.event.KapuaEvent;
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
                KapuaEvent kapuaEvent = appendKapuaEvent(null, manager);
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
                KapuaEvent kapuaEvent = appendKapuaEvent(result, manager);
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

                    KapuaEvent kapuaEvent = appendKapuaEvent(instance, manager);

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

    private <T> KapuaEvent appendKapuaEvent(Object instance, EntityManager manager) throws KapuaException {
        KapuaEvent persistedKapuaEvent = null;

        // If a kapua event is in scope then persist it along with the entity
        KapuaEvent kapuaEvent = EventScope.get();

        if (kapuaEvent!=null && instance instanceof KapuaEntity) {
            //TODO make sense to override the entity id and type without checking for previous empty values?
            //override only if parameters are not evaluated
            if (kapuaEvent.getEntityType() == null || kapuaEvent.getEntityType().trim().length()<=0) {
                //TODO remove log after test
                logger.debug("Kapua event - update entity type to '{}'", instance.getClass().getName());
                kapuaEvent.setEntityType(instance.getClass().getName());
            }
            if (kapuaEvent.getEntityId()==null) {
                //TODO remove log after test  
                logger.debug("Kapua event - update entity id to '{}'", ((KapuaEntity) instance).getId());
                kapuaEvent.setEntityId(((KapuaEntity) instance).getId());
            }
            logger.info("Entity '{}' with id '{}' found!", new Object[]{instance.getClass().getName(), ((KapuaEntity) instance).getId()});
        }

        if (kapuaEvent != null) {
            persistedKapuaEvent = KapuaEventStoreDAO.create(manager, kapuaEvent);
        }

        return persistedKapuaEvent;
    }

}