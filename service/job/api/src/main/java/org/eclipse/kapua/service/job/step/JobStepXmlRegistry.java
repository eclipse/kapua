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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;

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

    public JobStepProperty newJobStepProperty() {
        return JOB_STEP_FACTORY.newStepProperty(null, null, null);
    }

}
