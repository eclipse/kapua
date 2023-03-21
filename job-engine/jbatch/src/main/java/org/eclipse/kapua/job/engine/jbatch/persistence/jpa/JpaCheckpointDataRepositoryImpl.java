/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

import com.ibm.jbatch.container.persistence.CheckpointData;
import com.ibm.jbatch.container.persistence.CheckpointDataKey;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class JpaCheckpointDataRepositoryImpl implements JpaCheckpointDataRepository {

    @Override
    public JpaCheckpointData create(TxContext tx, CheckpointDataKey checkpointDataKey, CheckpointData checkpointData) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        return doCreate(em, checkpointDataKey, checkpointData);
    }

    @Override
    public JpaCheckpointData doCreate(EntityManager em, CheckpointDataKey checkpointDataKey, CheckpointData checkpointData) {
        JpaCheckpointData jpaCheckpointData = new JpaCheckpointData();
        jpaCheckpointData.setJobInstanceId(checkpointDataKey.getJobInstanceId());
        jpaCheckpointData.setId(checkpointDataKey.getCommaSeparatedKey());
        jpaCheckpointData.setObj(checkpointData);

        em.persist(jpaCheckpointData);
        em.flush();
        em.refresh(jpaCheckpointData);

        return jpaCheckpointData;
    }

    @Override
    public JpaCheckpointData update(TxContext tx, CheckpointDataKey key, CheckpointData value) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);

        JpaCheckpointData jpaCheckpointData = em.find(JpaCheckpointData.class, key.getCommaSeparatedKey());

        if (jpaCheckpointData != null) {
            jpaCheckpointData.setObj(value);

            em.merge(jpaCheckpointData);
            em.flush();
            em.refresh(jpaCheckpointData);
        } else {
            jpaCheckpointData = doCreate(em, key, value);
        }

        return jpaCheckpointData;
    }

    @Override
    public JpaCheckpointData find(TxContext tx, CheckpointDataKey key) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        return em.find(JpaCheckpointData.class, key.getCommaSeparatedKey());
    }
}
