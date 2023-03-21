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

import com.ibm.jbatch.container.context.impl.StepContextImpl;
import org.eclipse.kapua.commons.jpa.JpaAwareTxContext;
import org.eclipse.kapua.storage.TxContext;

import javax.batch.runtime.StepExecution;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpaStepExecutionInstanceDataRepositoryImpl implements JpaStepExecutionInstanceDataRepository {

    @Override
    public JpaStepExecutionInstanceData insert(TxContext tx, long jobExecutionId, StepContextImpl stepContext) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaStepExecutionInstanceData jpaStepExecutionInstanceData = new JpaStepExecutionInstanceData();

        jpaStepExecutionInstanceData.setJobExecutionId(jobExecutionId);
        jpaStepExecutionInstanceData.readDataFromStepContext(stepContext);

        em.persist(jpaStepExecutionInstanceData);
        em.flush();
        em.refresh(jpaStepExecutionInstanceData);

        return jpaStepExecutionInstanceData;
    }

    @Override
    public JpaStepExecutionInstanceData update(TxContext tx, StepContextImpl stepContext) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        JpaStepExecutionInstanceData jpaStepExecutionInstanceData = JpaStepExecutionInstanceDataRepository.doFind(em, stepContext.getStepExecutionId());
        jpaStepExecutionInstanceData.readDataFromStepContext(stepContext);

        em.merge(jpaStepExecutionInstanceData);
        em.flush();
        em.refresh(jpaStepExecutionInstanceData);

        return jpaStepExecutionInstanceData;
    }

    @Override
    public JpaStepExecutionInstanceData find(TxContext tx, long stepExecutionId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        return JpaStepExecutionInstanceDataRepository.doFind(em, stepExecutionId);
    }

    @Override
    public Map<String, StepExecution> getExternalJobInstanceData(TxContext tx, long jobInstanceId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        TypedQuery<JpaStepExecutionInstanceData> selectQuery = em.createNamedQuery("StepExecutionInstanceData.mostRecentForJobInstance", JpaStepExecutionInstanceData.class);

        selectQuery.setParameter("jobInstanceId", jobInstanceId);

        List<JpaStepExecutionInstanceData> queryResult = selectQuery.getResultList();

        return queryResult.stream().collect(Collectors.toMap(JpaStepExecutionInstanceData::getStepName, JpaStepExecutionInstanceData::toStepExecution));
    }


    @Override
    public List<StepExecution> getStepExecutionsByJobExecution(TxContext tx, long jobExecutionId) {
        final EntityManager em = JpaAwareTxContext.extractEntityManager(tx);
        TypedQuery<JpaStepExecutionInstanceData> selectQuery = em.createNamedQuery("StepExecutionInstanceData.selectByJobExecId", JpaStepExecutionInstanceData.class);

        selectQuery.setParameter("jobExecutionId", jobExecutionId);

        List<JpaStepExecutionInstanceData> queryResult = selectQuery.getResultList();

        return queryResult.stream().map(JpaStepExecutionInstanceData::toStepExecution).collect(Collectors.toList());
    }
}
