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

import com.google.common.collect.Sets;
import com.ibm.jbatch.container.exception.PersistenceException;
import org.eclipse.kapua.commons.jpa.EntityManager;

import javax.batch.runtime.BatchStatus;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Service DAO for {@link JpaExecutionInstanceData}.
 *
 * @since 1.2.0
 */
public class JpaExecutionInstanceDataDAO {

    private JpaExecutionInstanceDataDAO() {
    }

    public static JpaExecutionInstanceData create(EntityManager em, long jobInstanceId, Properties jobParameters, BatchStatus batchStatus, Timestamp timestamp) {
        try {
            JpaExecutionInstanceData jpaExecutionInstanceData = new JpaExecutionInstanceData();
            jpaExecutionInstanceData.setJobInstanceId(jobInstanceId);
            jpaExecutionInstanceData.setCreateTime(timestamp);
            jpaExecutionInstanceData.setUpdateTime(timestamp);
            jpaExecutionInstanceData.setBatchStatus(batchStatus);
            jpaExecutionInstanceData.setParameters(jobParameters);

            em.persist(jpaExecutionInstanceData);
            em.flush();
            em.refresh(jpaExecutionInstanceData);

            return jpaExecutionInstanceData;
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static JpaExecutionInstanceData updateBatchStatus(EntityManager em, long jobExecutionId, BatchStatus batchStatus, Timestamp updatedOn) {
        try {
            JpaExecutionInstanceData jpaExecutionInstanceData = find(em, jobExecutionId);

            if (jpaExecutionInstanceData != null) {
                jpaExecutionInstanceData.setBatchStatus(batchStatus);
                jpaExecutionInstanceData.setUpdateTime(updatedOn);

                em.merge(jpaExecutionInstanceData);
                em.flush();
                em.refresh(jpaExecutionInstanceData);
            }

            return jpaExecutionInstanceData;
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static JpaExecutionInstanceData updateBatchStatusStarted(EntityManager em, long jobExecutionId, Timestamp startedOn) {
        try {
            JpaExecutionInstanceData jpaExecutionInstanceData = find(em, jobExecutionId);

            if (jpaExecutionInstanceData != null) {
                jpaExecutionInstanceData.setBatchStatus(BatchStatus.STARTED);
                jpaExecutionInstanceData.setStartTime(startedOn);
                jpaExecutionInstanceData.setUpdateTime(startedOn);

                em.merge(jpaExecutionInstanceData);
                em.flush();
                em.refresh(jpaExecutionInstanceData);
            }

            return jpaExecutionInstanceData;
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static JpaExecutionInstanceData updateBatchStatusEnded(EntityManager em, long jobExecutionId, BatchStatus batchStatus, String exitStatus, Timestamp endedOn) {
        try {
            JpaExecutionInstanceData jpaExecutionInstanceData = find(em, jobExecutionId);

            if (jpaExecutionInstanceData != null) {
                jpaExecutionInstanceData.setBatchStatus(batchStatus);
                jpaExecutionInstanceData.setExitStatus(exitStatus);
                jpaExecutionInstanceData.setEndTime(endedOn);
                jpaExecutionInstanceData.setUpdateTime(endedOn);

                em.merge(jpaExecutionInstanceData);
                em.flush();
                em.refresh(jpaExecutionInstanceData);
            }

            return jpaExecutionInstanceData;
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static JpaExecutionInstanceData find(EntityManager em, long jobExecutionId) {
        try {
            return em.find(JpaExecutionInstanceData.class, jobExecutionId);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    public static List<JpaExecutionInstanceData> getJobExecutions(EntityManager em, long jobInstanceId) {
        TypedQuery<JpaExecutionInstanceData> selectQuery = em.createNamedQuery("ExecutionInstanceData.getByJobInstance", JpaExecutionInstanceData.class);
        selectQuery.setParameter("jobInstanceId", jobInstanceId);

        return selectQuery.getResultList();
    }

    public static JpaExecutionInstanceData getMostRecentByJobInstance(EntityManager em, long jobInstanceId) {
        TypedQuery<JpaExecutionInstanceData> selectQuery = em.createNamedQuery("ExecutionInstanceData.getByJobInstance", JpaExecutionInstanceData.class);
        selectQuery.setParameter("jobInstanceId", jobInstanceId);

        return selectQuery.getSingleResult();
    }

    public static Set<Long> getJobRunningExecutions(EntityManager em, String jobName) {
        TypedQuery<Long> selectQuery = em.createNamedQuery("ExecutionInstanceData.getRunningByJobName", Long.class);
        selectQuery.setParameter("status1", BatchStatus.STARTED);
        selectQuery.setParameter("status2", BatchStatus.STARTING);
        selectQuery.setParameter("status3", BatchStatus.STOPPING);
        selectQuery.setParameter("jobName", jobName);

        return Sets.newHashSet(selectQuery.getResultList());
    }

    public static <T> T getJobExecutionField(EntityManager em, long jobExecutionId, JpaExecutionInstanceDataFields field) {
        JpaExecutionInstanceData jpaExecutionInstanceData = em.find(JpaExecutionInstanceData.class, jobExecutionId);

        switch (field) {
            case JOB_EXEC_ID:
                return (T) Long.valueOf(jpaExecutionInstanceData.getId());
            case JOB_INSTANCE_ID:
                return (T) Long.valueOf(jpaExecutionInstanceData.getJobInstanceId());
            case CREATE_TIME:
                return (T) jpaExecutionInstanceData.getCreateTime();
            case START_TIME:
                return (T) jpaExecutionInstanceData.getStartTime();
            case UPDATE_TIME:
                return (T) jpaExecutionInstanceData.getUpdateTime();
            case END_TIME:
                return (T) jpaExecutionInstanceData.getEndTime();
            case PARAMETERS:
                return (T) jpaExecutionInstanceData.getParametersAsProperties();
            case BATCH_STATUS:
                return (T) jpaExecutionInstanceData.getBatchStatus();
            case EXIT_STATUS:
                return (T) jpaExecutionInstanceData.getExitStatus();
            default:
                throw new IllegalArgumentException(field.name());
        }
    }
}
