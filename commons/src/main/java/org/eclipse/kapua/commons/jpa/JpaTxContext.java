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
package org.eclipse.kapua.commons.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

public class JpaTxContext implements JpaAwareTxContext {
    public final EntityManagerFactory entityManagerFactory;
    Optional<EntityManager> entityManager = Optional.empty();

    public JpaTxContext(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public EntityManager getEntityManager() {
        if (!entityManager.isPresent()) {
            entityManager = Optional.of(entityManagerFactory.createEntityManager());
            entityManager.get().getTransaction().begin();
        }
        return entityManager.get();
    }
}
