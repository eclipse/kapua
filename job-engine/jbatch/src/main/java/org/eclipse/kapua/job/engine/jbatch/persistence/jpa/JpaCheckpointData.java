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

import com.ibm.jbatch.container.persistence.CheckpointData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * JPA counterpart of the {@link CheckpointData} object.
 *
 * @since 1.2.0
 */
@Entity(name = "CheckpointData")
@Table(name = "jbtc_checkpoint_data")
public class JpaCheckpointData extends AbstractJpaJbatchEntity {

    @Column(name = "jobinstanceid")
    private long jobInstanceId;

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Lob
    @Column(name = "obj")
    private byte[] obj;

    public JpaCheckpointData() {
        // Required by JPA
    }

    public long getJobInstanceId() {
        return jobInstanceId;
    }

    public void setJobInstanceId(long jobInstanceId) {
        this.jobInstanceId = jobInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getObj() {
        return obj;
    }

    public void setObj(byte[] obj) {
        this.obj = obj;
    }

    public void setObj(CheckpointData checkpointData) {
        setObj(writeObject(checkpointData));
    }

    public CheckpointData toCheckpointData() {
        return readObject(getObj());
    }
}
