/*******************************************************************************
 * Copyright (c) 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.jbatch.persistence.jpa;

import com.ibm.jbatch.container.context.impl.StepContextImpl;
import org.eclipse.kapua.commons.jpa.EntityManager;

import javax.batch.runtime.StepExecution;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JpaStepExecutionInstanceDataDAO {

    private JpaStepExecutionInstanceDataDAO() {
    }

    public static JpaStepExecutionInstanceData insert(EntityManager em, long jobExecutionId, StepContextImpl stepContext) {
        JpaStepExecutionInstanceData jpaStepExecutionInstanceData = new JpaStepExecutionInstanceData();

        jpaStepExecutionInstanceData.setJobExecutionId(jobExecutionId);
        jpaStepExecutionInstanceData.readDataFromStepContext(stepContext);

        em.persist(jpaStepExecutionInstanceData);
        em.flush();
        em.refresh(jpaStepExecutionInstanceData);

        return jpaStepExecutionInstanceData;
    }

    public static JpaStepExecutionInstanceData update(EntityManager em, StepContextImpl stepContext) {
        JpaStepExecutionInstanceData jpaStepExecutionInstanceData = find(em, stepContext.getStepExecutionId());
        jpaStepExecutionInstanceData.readDataFromStepContext(stepContext);

        em.merge(jpaStepExecutionInstanceData);
        em.flush();
        em.refresh(jpaStepExecutionInstanceData);

        return jpaStepExecutionInstanceData;
    }

    public static JpaStepExecutionInstanceData find(EntityManager em, long stepExecutionId) {
        return em.find(JpaStepExecutionInstanceData.class, stepExecutionId);
    }

    public static Map<String, StepExecution> getExternalJobInstanceData(EntityManager em, long jobInstanceId) {
        TypedQuery<JpaStepExecutionInstanceData> selectQuery = em.createNamedQuery("StepExecutionInstanceData.mostRecentForJobInstance", JpaStepExecutionInstanceData.class);

        selectQuery.setParameter("jobInstanceId", jobInstanceId);

        List<JpaStepExecutionInstanceData> queryResult = selectQuery.getResultList();

        return queryResult.stream().collect(Collectors.toMap(JpaStepExecutionInstanceData::getStepName, JpaStepExecutionInstanceData::toStepExecution));
    }


    public static List<StepExecution> getStepExecutionsByJobExecution(EntityManager em, long jobExecutionId) {
        TypedQuery<JpaStepExecutionInstanceData> selectQuery = em.createNamedQuery("StepExecutionInstanceData.selectByJobExecId", JpaStepExecutionInstanceData.class);

        selectQuery.setParameter("jobExecutionId", jobExecutionId);

        List<JpaStepExecutionInstanceData> queryResult = selectQuery.getResultList();

        return queryResult.stream().map(JpaStepExecutionInstanceData::toStepExecution).collect(Collectors.toList());
    }
}
