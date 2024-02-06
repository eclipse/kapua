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

    private final JobExecutionFactory jobExecutionFactory = KapuaLocator.getInstance().getFactory(JobExecutionFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobExecution newJobExecution() {
        return jobExecutionFactory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobExecutionCreator newJobExecutionCreator() {
        return jobExecutionFactory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobExecutionListResult newJobExecutionListResult() {
        return jobExecutionFactory.newListResult();
    }

    public JobExecutionQuery newQuery() {
        return jobExecutionFactory.newQuery(null);
    }
}
