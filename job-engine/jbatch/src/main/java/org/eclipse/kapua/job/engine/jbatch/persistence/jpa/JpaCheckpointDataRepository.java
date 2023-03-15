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
import com.ibm.jbatch.container.persistence.CheckpointDataKey;
import org.eclipse.kapua.storage.TxContext;

import javax.persistence.EntityManager;

public interface JpaCheckpointDataRepository {
    JpaCheckpointData create(TxContext tx, CheckpointDataKey checkpointDataKey, CheckpointData checkpointData);

    JpaCheckpointData doCreate(EntityManager em, CheckpointDataKey checkpointDataKey, CheckpointData checkpointData);

    JpaCheckpointData update(TxContext tx, CheckpointDataKey key, CheckpointData value);

    JpaCheckpointData find(TxContext tx, CheckpointDataKey key);
}
