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
package org.eclipse.kapua.job.engine.queue.jbatch;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionQuery;
import org.eclipse.kapua.model.id.KapuaId;

public class QueuedJobExecutionQueryImpl extends AbstractKapuaQuery<QueuedJobExecution> implements QueuedJobExecutionQuery {

    /**
     * Constructor
     *
     * @param scopeId
     */
    public QueuedJobExecutionQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
