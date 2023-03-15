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
import org.eclipse.kapua.storage.TxContext;

import javax.batch.runtime.StepExecution;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public interface JpaStepExecutionInstanceDataRepository {
    static JpaStepExecutionInstanceData doFind(EntityManager em, long stepExecutionId) {
        return em.find(JpaStepExecutionInstanceData.class, stepExecutionId);
    }

    JpaStepExecutionInstanceData insert(TxContext tx, long jobExecutionId, StepContextImpl stepContext);

    JpaStepExecutionInstanceData update(TxContext tx, StepContextImpl stepContext);

    JpaStepExecutionInstanceData find(TxContext tx, long stepExecutionId);

    Map<String, StepExecution> getExternalJobInstanceData(TxContext tx, long jobInstanceId);

    List<StepExecution> getStepExecutionsByJobExecution(TxContext tx, long jobExecutionId);
}
