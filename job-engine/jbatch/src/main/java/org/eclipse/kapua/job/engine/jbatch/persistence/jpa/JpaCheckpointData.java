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

import com.ibm.jbatch.container.persistence.CheckpointData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity(name = "CheckpointData")
@Table(name = "CHECKPOINTDATA")
public class JpaCheckpointData extends AbstractJpaJbatchEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Lob
    @Column(name = "obj")
    private byte[] obj;

    public JpaCheckpointData() {
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
