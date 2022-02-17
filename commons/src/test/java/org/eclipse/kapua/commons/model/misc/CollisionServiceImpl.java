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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.model.query.KapuaQuery;

public class CollisionServiceImpl extends AbstractKapuaService implements CollisionEntityService {

    public CollisionServiceImpl() {
        super(CollisionEntityManagerFactory.getInstance(), null);
    }

    public CollisionEntity insert(String testField) throws KapuaException {
        CollisionEntityCreator collisionEntityCreator = new CollisionEntityCreator(testField);
        return entityManagerSession.doAction(EntityManagerContainer.<CollisionEntity>create().onResultHandler(em -> {
            CollisionEntity collisionEntity = null;
            try {
                em.beginTransaction();
                collisionEntity = CollisionEntityDAO.create(em, collisionEntityCreator);
                em.commit();
                return collisionEntity;
            } catch (Exception e) {
                if (em != null) {
                    em.rollback();
                }
                throw e;
            }
        }));
    }

    @Override
    public CollisionEntity create(CollisionEntityCreator creator) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CollisionEntity find(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public KapuaListResult<CollisionEntity> query(KapuaQuery query) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId entityId) throws KapuaException {
        // TODO Auto-generated method stub

    }

    @Override
    public CollisionEntity findByName(String name) throws KapuaException {
        // TODO Auto-generated method stub
        return null;
    }

}
