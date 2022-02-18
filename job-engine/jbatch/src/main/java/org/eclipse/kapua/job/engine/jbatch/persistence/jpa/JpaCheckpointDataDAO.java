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
import org.eclipse.kapua.commons.jpa.EntityManager;

/**
 * Service DAO for {@link JpaCheckpointData}
 *
 * @since 1.2.0
 */
public class JpaCheckpointDataDAO {

    private JpaCheckpointDataDAO() {
    }

    public static JpaCheckpointData create(EntityManager em, CheckpointDataKey checkpointDataKey, CheckpointData checkpointData) {

        JpaCheckpointData jpaCheckpointData = new JpaCheckpointData();
        jpaCheckpointData.setJobInstanceId(checkpointDataKey.getJobInstanceId());
        jpaCheckpointData.setId(checkpointDataKey.getCommaSeparatedKey());
        jpaCheckpointData.setObj(checkpointData);

        em.persist(jpaCheckpointData);
        em.flush();
        em.refresh(jpaCheckpointData);

        return jpaCheckpointData;
    }

    public static JpaCheckpointData update(EntityManager em, CheckpointDataKey key, CheckpointData value) {

        JpaCheckpointData jpaCheckpointData = em.find(JpaCheckpointData.class, key.getCommaSeparatedKey());

        if (jpaCheckpointData != null) {
            jpaCheckpointData.setObj(value);

            em.merge(jpaCheckpointData);
            em.flush();
            em.refresh(jpaCheckpointData);
        } else {
            jpaCheckpointData = create(em, key, value);
        }

        return jpaCheckpointData;
    }

    public static JpaCheckpointData find(EntityManager em, CheckpointDataKey key) {
        return em.find(JpaCheckpointData.class, key.getCommaSeparatedKey());
    }
}
