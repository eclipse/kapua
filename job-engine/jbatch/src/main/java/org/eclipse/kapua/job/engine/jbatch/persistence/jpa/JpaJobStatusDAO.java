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
import org.eclipse.kapua.commons.jpa.EntityManager;

/**
 * Service DAO for {@link JpaJobStatus}
 *
 * @since 1.2.0
 */
public class JpaJobStatusDAO {

    private JpaJobStatusDAO() {
    }

    public static JpaJobStatus create(EntityManager em, long jobInstanceId) {
        JpaJobStatus jpaJobStatus = new JpaJobStatus();
        jpaJobStatus.setJobInstanceId(jobInstanceId);
        jpaJobStatus.setObj(new JobStatus(jobInstanceId));

        em.persist(jpaJobStatus);
        em.flush();
        em.refresh(jpaJobStatus);

        return jpaJobStatus;
    }

    public static JpaJobStatus update(EntityManager em, long jobInstanceId, JobStatus jobStatus) {
        JpaJobStatus jpaJobStatus = find(em, jobInstanceId);
        jpaJobStatus.setObj(jobStatus);

        em.merge(jpaJobStatus);
        em.flush();
        em.refresh(jpaJobStatus);

        return jpaJobStatus;
    }

    public static JpaJobStatus find(EntityManager em, long jobInstanceId) {
        return em.find(JpaJobStatus.class, jobInstanceId);
    }
}
