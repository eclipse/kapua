/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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

import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionCreator;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionFactory;
import org.eclipse.kapua.job.engine.queue.QueuedJobExecutionService;

public class JobEngineQueueJbatchModule extends AbstractKapuaModule {
    @Override
    protected void configureModule() {
        bind(QueuedJobExecutionCreator.class).to(QueuedJobExecutionCreatorImpl.class);
        bind(QueuedJobExecutionFactory.class).to(QueuedJobExecutionFactoryImpl.class);
        bind(QueuedJobExecutionService.class).to(QueuedJobExecutionServiceImpl.class);
    }
}
