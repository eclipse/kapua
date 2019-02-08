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

import org.eclipse.kapua.job.engine.queue.QueuedJobExecution;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionListResult;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionQuery;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link QueuedJobExecutionFactory} implementation.
 */
@KapuaProvider
public class QueuedJobExecutionFactoryImpl implements QueuedJobExecutionFactory {

    @Override
    public QueuedJobExecution newEntity(KapuaId scopeId) {
        return new QueuedJobExecutionImpl(scopeId);
    }

    @Override
    public QueuedJobExecutionCreator newCreator(KapuaId scopeId) {
        return new QueuedJobExecutionCreatorImpl(scopeId);
    }

    @Override
    public QueuedJobExecutionQuery newQuery(KapuaId scopeId) {
        return new QueuedJobExecutionQueryImpl(scopeId);
    }

    @Override
    public QueuedJobExecutionListResult newListResult() {
        return new QueuedJobExecutionListResultImpl();
    }

}
