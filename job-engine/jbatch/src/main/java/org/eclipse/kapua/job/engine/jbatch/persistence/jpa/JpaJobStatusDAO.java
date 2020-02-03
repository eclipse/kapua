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

import com.ibm.jbatch.container.status.JobStatus;
import org.eclipse.kapua.commons.jpa.EntityManager;

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

    public static void update(EntityManager em, long jobInstanceId, JobStatus jobStatus) {
        JpaJobStatus jpaJobStatus = find(em, jobInstanceId);
        jpaJobStatus.setObj(jobStatus);

        em.merge(jpaJobStatus);
        em.flush();
        em.refresh(jpaJobStatus);
    }

    public static JpaJobStatus find(EntityManager em, long jobInstanceId) {
        return em.find(JpaJobStatus.class, jobInstanceId);
    }
}
