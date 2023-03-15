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
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public interface JpaJobStatusRepository {
    JpaJobStatus create(TxContext tx, long jobInstanceId);

    JpaJobStatus update(TxContext tx, long jobInstanceId, JobStatus jobStatus);

    JpaJobStatus find(TxContext tx, long jobInstanceId);

    JpaJobStatus doFind(EntityManager em, long jobInstanceId);
}
