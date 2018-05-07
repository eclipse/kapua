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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link JobStep} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class JobStepXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobStepFactory JOB_STEP_FACTORY = LOCATOR.getFactory(JobStepFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobStep newJobStep() {
        return JOB_STEP_FACTORY.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobStepCreator newJobStepCreator() {
        return JOB_STEP_FACTORY.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobStepListResult newJobStepListResult() {
        return JOB_STEP_FACTORY.newListResult();
    }

    public JobStepQuery newQuery() {
        return JOB_STEP_FACTORY.newQuery(null);
    }
}
