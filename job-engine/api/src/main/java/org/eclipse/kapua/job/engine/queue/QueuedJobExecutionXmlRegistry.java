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
