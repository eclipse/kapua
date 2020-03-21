/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
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

import com.ibm.jbatch.container.status.StepStatus;
import org.eclipse.kapua.commons.jpa.EntityManager;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Service DAO for {@link JpaStepStatus}.
 *
 * @since 1.2.0
 */
public class JpaStepStatusDAO {

    private JpaStepStatusDAO() {
    }

    public static JpaStepStatus create(EntityManager em, long stepExecutionId) {
        JpaStepStatus jpaStepStatus = new JpaStepStatus();
        jpaStepStatus.setStepExecutionId(stepExecutionId);
        jpaStepStatus.setObj(new StepStatus(stepExecutionId));

        em.persist(jpaStepStatus);
        em.flush();
        em.refresh(jpaStepStatus);

        return jpaStepStatus;
    }

    public static void update(EntityManager em, long stepExecutionId, StepStatus stepStatus) {
        JpaStepStatus jpaStepStatus = find(em, stepExecutionId);
        jpaStepStatus.setObj(stepStatus);

        em.merge(jpaStepStatus);
        em.flush();
        em.refresh(jpaStepStatus);
    }

    public static JpaStepStatus find(EntityManager em, long jobInstanceId) {
        return em.find(JpaStepStatus.class, jobInstanceId);
    }

    public static JpaStepStatus getStepStatusByJobInstance(EntityManager em, long jobInstanceId, String stepName) {
        TypedQuery<JpaStepStatus> selectQuery = em.createNamedQuery("StepStatus.findByJobInstanceIdStepName", JpaStepStatus.class);

        selectQuery.setParameter("jobInstanceId", jobInstanceId);
        selectQuery.setParameter("stepName", stepName);

        List<JpaStepStatus> queryResult = selectQuery.getResultList();

        return queryResult.isEmpty() ? null : queryResult.get(0);
    }
}
