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

import javax.xml.bind.annotation.XmlRegistry;

import org.eclipse.kapua.locator.KapuaLocator;

/**
 * JobExecution xml factory class
 * 
 * @since 1.0
 *
 */
@XmlRegistry
public class JobExecutionXmlRegistry {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final JobExecutionFactory factory = locator.getFactory(JobExecutionFactory.class);

    /**
     * Creates a new job instance
     * 
     * @return
     */
    public JobExecution newJobTarget() {
        return factory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     * 
     * @return
     */
    public JobExecutionCreator newJobTargetCreator() {
        return factory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     * 
     * @return
     */
    public JobExecutionListResult newJobTargetListResult() {
        return factory.newListResult();
    }

    public JobExecutionQuery newQuery() {
        return factory.newQuery(null);
    }
}
