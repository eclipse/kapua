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

import com.ibm.jbatch.container.exception.PersistenceException;
import org.eclipse.kapua.storage.TxContext;

import javax.batch.runtime.BatchStatus;
import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public interface JpaExecutionInstanceDataRepository {
    static JpaExecutionInstanceData doFind(EntityManager em, long jobExecutionId) {
        try {
            return em.find(JpaExecutionInstanceData.class, jobExecutionId);
        } catch (Exception e) {
            throw new PersistenceException(e);
        }
    }

    JpaExecutionInstanceData create(TxContext tx, long jobInstanceId, Properties jobParameters, BatchStatus batchStatus, Timestamp timestamp);

    JpaExecutionInstanceData updateBatchStatus(TxContext tx, long jobExecutionId, BatchStatus batchStatus, Timestamp updatedOn);

    JpaExecutionInstanceData updateBatchStatusStarted(TxContext tx, long jobExecutionId, Timestamp startedOn);

    JpaExecutionInstanceData updateBatchStatusEnded(TxContext tx, long jobExecutionId, BatchStatus batchStatus, String exitStatus, Timestamp endedOn);

    JpaExecutionInstanceData find(TxContext tx, long jobExecutionId);

    List<JpaExecutionInstanceData> getJobExecutions(TxContext tx, long jobInstanceId);

    JpaExecutionInstanceData getMostRecentByJobInstance(TxContext tx, long jobInstanceId);

    Set<Long> getJobRunningExecutions(TxContext tx, String jobName);

    <T> T getJobExecutionField(TxContext tx, long jobExecutionId, JpaExecutionInstanceDataFields field);
}
