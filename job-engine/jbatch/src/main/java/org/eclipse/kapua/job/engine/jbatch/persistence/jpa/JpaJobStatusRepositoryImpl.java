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

import com.ibm.jbatch.container.status.JobStatus;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public class JpaJobStatusRepositoryImpl implements JpaJobStatusRepository {

    @Override
    public JpaJobStatus create(TxContext tx, long jobInstanceId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaJobStatus jpaJobStatus = new JpaJobStatus();
        jpaJobStatus.setJobInstanceId(jobInstanceId);
        jpaJobStatus.setObj(new JobStatus(jobInstanceId));

        em.persist(jpaJobStatus);
        em.flush();
        em.refresh(jpaJobStatus);

        return jpaJobStatus;
    }

    @Override
    public JpaJobStatus update(TxContext tx, long jobInstanceId, JobStatus jobStatus) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaJobStatus jpaJobStatus = doFind(em, jobInstanceId);
        jpaJobStatus.setObj(jobStatus);

        em.merge(jpaJobStatus);
        em.flush();
        em.refresh(jpaJobStatus);

        return jpaJobStatus;
    }

    @Override
    public JpaJobStatus find(TxContext tx, long jobInstanceId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        return doFind(em, jobInstanceId);
    }

    @Override
    public JpaJobStatus doFind(EntityManager em, long jobInstanceId) {
        return em.find(JpaJobStatus.class, jobInstanceId);
    }
}
