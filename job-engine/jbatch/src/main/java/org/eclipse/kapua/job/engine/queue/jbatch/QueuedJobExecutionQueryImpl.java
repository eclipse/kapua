/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.queue.jbatch;

import org.eclipse.kapua.commons.model.query.AbstractKapuaQuery;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionQuery;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link QueuedJobExecutionQuery} implementation.
 *
 * @since 1.1.0
 */
public class QueuedJobExecutionQueryImpl extends AbstractKapuaQuery implements QueuedJobExecutionQuery {

    /**
     * Constructor.
     *
     * @param scopeId The {@link #getScopeId()}.
     * @since 1.1.0
     */
    public QueuedJobExecutionQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
