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
package org.eclipse.kapua.service.job.step.definition;

import org.eclipse.kapua.locator.KapuaLocator;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * {@link JobStepDefinition} xml factory class
 *
 * @since 1.0
 */
@XmlRegistry
public class JobStepDefinitionXmlRegistry {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobStepDefinitionFactory JOB_STEP_DEFINITION_FACTORY = LOCATOR.getFactory(JobStepDefinitionFactory.class);

    /**
     * Creates a new job instance
     *
     * @return
     */
    public JobStepDefinition newJobStepDefinition() {
        return JOB_STEP_DEFINITION_FACTORY.newEntity(null);
    }

    /**
     * Creates a new job creator instance
     *
     * @return
     */
    public JobStepDefinitionCreator newJobStepDefinitionCreator() {
        return JOB_STEP_DEFINITION_FACTORY.newCreator(null);
    }

    /**
     * Creates a new job list result instance
     *
     * @return
     */
    public JobStepDefinitionListResult newJobStepDefinitionListResult() {
        return JOB_STEP_DEFINITION_FACTORY.newListResult();
    }

    public JobStepDefinitionQuery newQuery() {
        return JOB_STEP_DEFINITION_FACTORY.newQuery(null);
    }
}
