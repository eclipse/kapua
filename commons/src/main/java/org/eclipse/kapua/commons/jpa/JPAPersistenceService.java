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
import org.eclipse.kapua.commons.core.ComponentProvider;
import org.eclipse.kapua.locator.inject.Service;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;

@ComponentProvider
@Service(provides = {EntityManager.class, PersistenceService.class})
public class JPAPersistenceService implements EntityManager, PersistenceService {
    
    ThreadLocal<EntityManager> entityManagerThdLocal = new ThreadLocal<>();
    ThreadLocal<Integer> referenceCountThdLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };
    
    public JPAPersistenceService() {    
    }
    
    @Override
    public void begin(EntityManagerFactory factory) throws KapuaException {
        if (entityManagerThdLocal.get() == null) {
            EntityManager em = factory.createEntityManager();
            entityManagerThdLocal.set(em);
        }
        referenceCountThdLocal.set(referenceCountThdLocal.get()+1);
    }
    
    @Override
    public EntityManager get() {
        return entityManagerThdLocal.get();
    }

    @Override
    public void end() {
        if (referenceCountThdLocal.get() > 0) {
            referenceCountThdLocal.set(referenceCountThdLocal.get()-1);
            if (referenceCountThdLocal.get() == 0) {
                if (entityManagerThdLocal.get() != null) {
                    entityManagerThdLocal.get().close();
                    entityManagerThdLocal.set(null);
                }
            }
        }
    }

    @Override
    public void beginTransaction() throws KapuaException {
        entityManagerThdLocal.get().beginTransaction();
    }

    @Override
    public void commit() throws KapuaException {
        entityManagerThdLocal.get().commit();
    }

    @Override
    public void rollback() {
        entityManagerThdLocal.get().rollback();
    }

    @Override
    public boolean isTransactionActive() {
        return entityManagerThdLocal.get().isTransactionActive();
    }

    @Override
    public void close() {
        entityManagerThdLocal.get().close();
    }

    @Override
    public <E extends KapuaEntity> void persist(E entity) {
        entityManagerThdLocal.get().persist(entity);
    }

    @Override
    public void flush() {
        entityManagerThdLocal.get().flush();
    }

    @Override
    public <E extends KapuaEntity> E find(Class<E> clazz, KapuaId id) {
        return entityManagerThdLocal.get().find(clazz, id);
    }

    @Override
    public <E extends KapuaEntity> void merge(E entity) {
        entityManagerThdLocal.get().merge(entity);
    }

    @Override
    public <E extends KapuaEntity> void refresh(E entity) {
        entityManagerThdLocal.get().refresh(entity);
    }

    @Override
    public <E extends KapuaEntity> void remove(E entity) {
        entityManagerThdLocal.get().remove(entity);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return entityManagerThdLocal.get().getCriteriaBuilder();
    }

    @Override
    public <E> TypedQuery<E> createQuery(CriteriaQuery<E> criteriaSelectQuery) {
        return entityManagerThdLocal.get().createQuery(criteriaSelectQuery);
    }

    @Override
    public <E> TypedQuery<E> createNamedQuery(String queryName, Class<E> clazz) {
        return entityManagerThdLocal.get().createNamedQuery(queryName, clazz);
    }

    @Override
    public <E> Query createNativeQuery(String querySelectUuidShort) {
        return entityManagerThdLocal.get().createNativeQuery(querySelectUuidShort);
    }
}
