/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.extras.migrator.encryption.job;

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.service.job.step.JobStepQuery;

/**
 * {@link JobStepQuery} definition.
 *
 * @since 1.0.0
 */
public class JobStepMigratorQueryImpl extends AbstractKapuaNamedQuery implements JobStepQuery {

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JobStepMigratorQueryImpl() {
        super();
    }
}
