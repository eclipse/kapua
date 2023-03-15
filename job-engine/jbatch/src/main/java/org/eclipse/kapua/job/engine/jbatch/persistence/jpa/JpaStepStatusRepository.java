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
import org.eclipse.kapua.storage.TxContext;

public interface JpaStepStatusRepository {
    JpaStepStatus getStepStatusByJobInstance(TxContext tx, long jobInstanceId, String stepName);

    JpaStepStatus create(TxContext tx, long stepExecutionId);

    JpaStepStatus update(TxContext tx, long stepExecutionId, StepStatus stepStatus);

    JpaStepStatus find(TxContext tx, long jobInstanceId);
}
