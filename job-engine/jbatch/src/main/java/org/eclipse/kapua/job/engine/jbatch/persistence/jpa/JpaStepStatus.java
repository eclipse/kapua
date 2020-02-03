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

import com.ibm.jbatch.container.status.StepStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name = "StepStatus")
@Table(name = "STEPSTATUS")
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
