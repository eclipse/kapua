/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.execution;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link JobExecution} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class JobExecutionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobExecutionFactory JOB_EXECUTION_FACTORY = LOCATOR.getFactory(JobExecutionFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobExecution newJobExecution() {
        return JOB_EXECUTION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobExecutionCreator newJobExecutionCreator() {
        return JOB_EXECUTION_FACTORY.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobExecutionListResult newJobExecutionListResult() {
        return JOB_EXECUTION_FACTORY.newListResult();
    }

    public JobExecutionQuery newQuery() {
        return JOB_EXECUTION_FACTORY.newQuery(null);
    }
}
