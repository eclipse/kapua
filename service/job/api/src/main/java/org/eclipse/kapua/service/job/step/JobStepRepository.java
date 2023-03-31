/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.storage.KapuaNamedEntityRepository;
import org.eclipse.kapua.storage.TxContext;

public interface JobStepRepository
        extends KapuaNamedEntityRepository<JobStep, JobStepListResult> {
    long countOtherEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, KapuaId jobStepId, String name, KapuaId jobId) throws KapuaException;

    long countEntitiesWithNameInScope(TxContext tx, KapuaId scopeId, String name, KapuaId jobId) throws KapuaException;
}
