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

import com.ibm.jbatch.container.status.StepStatus;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaStepStatusRepositoryImpl implements JpaStepStatusRepository {

    @Override
    public JpaStepStatus getStepStatusByJobInstance(TxContext tx, long jobInstanceId, String stepName) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        TypedQuery<JpaStepStatus> selectQuery = em.createNamedQuery("StepStatus.findByJobInstanceIdStepName", JpaStepStatus.class);

        selectQuery.setParameter("jobInstanceId", jobInstanceId);
        selectQuery.setParameter("stepName", stepName);

        List<JpaStepStatus> queryResult = selectQuery.getResultList();

        return queryResult.isEmpty() ? null : queryResult.get(0);
    }

    @Override
    public JpaStepStatus create(TxContext tx, long stepExecutionId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaStepStatus jpaStepStatus = new JpaStepStatus();
        jpaStepStatus.setStepExecutionId(stepExecutionId);
        jpaStepStatus.setObj(new StepStatus(stepExecutionId));

        em.persist(jpaStepStatus);
        em.flush();
        em.refresh(jpaStepStatus);

        return jpaStepStatus;
    }

    @Override
    public JpaStepStatus update(TxContext tx, long stepExecutionId, StepStatus stepStatus) {
        final javax.persistence.EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaStepStatus jpaStepStatus = doFind(em, stepExecutionId);
        jpaStepStatus.setObj(stepStatus);

        em.merge(jpaStepStatus);
        em.flush();
        em.refresh(jpaStepStatus);

        return jpaStepStatus;
    }

    @Override
    public JpaStepStatus find(TxContext tx, long jobInstanceId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        return doFind(em, jobInstanceId);
    }

    private static JpaStepStatus doFind(EntityManager em, long jobInstanceId) {
        return em.find(JpaStepStatus.class, jobInstanceId);
    }

}
