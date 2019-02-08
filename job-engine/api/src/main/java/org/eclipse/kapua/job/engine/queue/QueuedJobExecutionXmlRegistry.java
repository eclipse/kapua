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
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link QueuedJobExecution} xml factory class
 *
 * @since 1.1.0
 */
@XmlRegistry
public class QueuedJobExecutionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final QueuedJobExecutionFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(QueuedJobExecutionFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public QueuedJobExecution newQueuedJobExecution() {
        return JOB_TARGET_FACTORY.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public QueuedJobExecutionCreator newQueuedJobExecutionCreator() {
        return JOB_TARGET_FACTORY.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public QueuedJobExecutionListResult newQueuedJobExecutionListResult() {
        return JOB_TARGET_FACTORY.newListResult();
    }

    public QueuedJobExecutionQuery newQuery() {
        return JOB_TARGET_FACTORY.newQuery(null);
    }
}
