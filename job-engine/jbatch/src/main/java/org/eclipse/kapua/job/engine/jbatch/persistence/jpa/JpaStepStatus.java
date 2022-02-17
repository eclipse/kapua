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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA counterpart of {@link StepStatus}.
 *
 * @since 1.2.0
 */
@Entity(name = "StepStatus")
@Table(name = "jbtc_step_status")
@NamedQueries({
        @NamedQuery(name = "StepStatus.findByJobInstanceIdStepName",
                query = "SELECT ss " +
                        "FROM StepStatus ss " +
                        "WHERE " +
                        "ss.stepExecutionId IN (" +
                        "SELECT seid.id " +
                        "FROM ExecutionInstanceData eid INNER JOIN StepExecutionInstanceData seid ON eid.id = seid.jobExecutionId " +
                        "WHERE " +
                        "eid.jobInstanceId = :jobInstanceId and seid.stepName = :stepName" +
                        ")")
})
public class JpaStepStatus extends AbstractJpaJbatchEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private long stepExecutionId;

    @Lob
    @Column(name = "obj")
    private byte[] obj;

    public JpaStepStatus() {
        // Required by JPA
    }

    public long getStepExecutionId() {
        return stepExecutionId;
    }

    public void setStepExecutionId(long stepExecutionId) {
        this.stepExecutionId = stepExecutionId;
    }

    public byte[] getObj() {
        return obj;
    }

    public StepStatus getObjAsStepStatus() {
        return readObject(getObj());
    }

    public void setObj(byte[] obj) {
        this.obj = obj;
    }

    public void setObj(StepStatus stepStatus) {
        setObj(writeObject(stepStatus));
    }

    public StepStatus toStepStatus() {
        return getObjAsStepStatus();
    }
}
