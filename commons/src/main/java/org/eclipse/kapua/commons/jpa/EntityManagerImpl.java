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
package org.eclipse.kapua.commons.jpa;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kapua JPA entity manager wrapper
 * 
 * @since 1.0
 */
public class EntityManagerImpl implements EntityManager {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractEntityManagerFactory.class);

    private javax.persistence.EntityManager javaxPersitenceEntityManager;

    /**
     * Constructs a new entity manager wrapping the given {@link javax.persistence.EntityManager}
     * 
     * @param javaxPersitenceEntityManager
     */
    public EntityManagerImpl(javax.persistence.EntityManager javaxPersitenceEntityManager) {
        this.javaxPersitenceEntityManager = javaxPersitenceEntityManager;
    }

    @Override
    public void beginTransaction()
            throws KapuaException {
        if (javaxPersitenceEntityManager == null) {
            throw KapuaException.internalError(new NullPointerException(), "null EntityManager");
        }
        javaxPersitenceEntityManager.getTransaction().begin();
    }

    @Override
    public void commit()
            throws KapuaException {
        if (javaxPersitenceEntityManager == null) {
            throw KapuaException.internalError("null EntityManager");
        }
        if (!javaxPersitenceEntityManager.getTransaction().isActive()) {
            throw KapuaException.internalError("Transaction Not Active");
        }

        try {
            javaxPersitenceEntityManager.getTransaction().commit();
        } catch (Exception e) {
            throw KapuaException.internalError(e, "Commit Error");
        }
    }

    @Override
    public void rollback() {
        try {
            if (javaxPersitenceEntityManager != null &&
                    javaxPersitenceEntityManager.getTransaction().isActive()) {
                javaxPersitenceEntityManager.getTransaction().rollback();
            }
        } catch (Exception e) {
            LOG.warn("Rollback Error", e);
        }
    }

    @Override
    public boolean isTransactionActive() {
        return (javaxPersitenceEntityManager != null &&
                javaxPersitenceEntityManager.getTransaction().isActive());
    }

    @Override
    public void close() {
        if (javaxPersitenceEntityManager != null) {
            javaxPersitenceEntityManager.close();
        }
    }

    @Override
    public <E extends KapuaEntity> void persist(E entity) {
        javaxPersitenceEntityManager.persist(entity);
    }

    @Override
    public void flush() {
        javaxPersitenceEntityManager.flush();
    }

    @Override
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id) {
        KapuaEid eid = KapuaEid.parseKapuaId(id);
        return javaxPersitenceEntityManager.find(clazz, eid);
    }

    @Override
    public <E extends KapuaEntity> void merge(E entity) {
        javaxPersitenceEntityManager.merge(entity);
    }

    @Override
    public <E extends KapuaEntity> void refresh(E entity) {
        javaxPersitenceEntityManager.refresh(entity);
    }

    @Override
    public <E extends KapuaEntity> void remove(E entity) {
        javaxPersitenceEntityManager.remove(entity);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return javaxPersitenceEntityManager.getCriteriaBuilder();
    }

    @Override
    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaSelectQuery) {
        return javaxPersitenceEntityManager.createQuery(criteriaSelectQuery);
    }

    @Override
    public <E> TypedQuery<E> createNamedQuery(String queryName, Class<E> clazz) {
        return javaxPersitenceEntityManager.createNamedQuery(queryName, clazz);
    }

    @Override
    public <E> Query createNativeQuery(String querySelectUuidShort) {
        return javaxPersitenceEntityManager.createNativeQuery(querySelectUuidShort);
    }
}