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

    private final JobStepFactory jobStepFactory = KapuaLocator.getInstance().getFactory(JobStepFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobStep newJobStep() {
        return jobStepFactory.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobStepCreator newJobStepCreator() {
        return jobStepFactory.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobStepListResult newJobStepListResult() {
        return jobStepFactory.newListResult();
    }

    public JobStepQuery newQuery() {
        return jobStepFactory.newQuery(null);
    }

    public JobStepProperty newJobStepProperty() {
        return jobStepFactory.newStepProperty(null, null, null);
    }

}
