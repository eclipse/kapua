/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.commons.model.query.AbstractKapuaNamedQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.JobQuery;

/**
 * {@link JobQuery} implementation.
 *
 * @since 1.0.0
 */
public class JobQueryImpl extends AbstractKapuaNamedQuery implements JobQuery {

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.0.0
     */
    public JobQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
