/*******************************************************************************
 * Copyright (c) 2017, 2019 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.step.definition;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link JobStepDefinitionFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface JobStepDefinitionFactory extends KapuaEntityFactory<JobStepDefinition, JobStepDefinitionCreator, JobStepDefinitionQuery, JobStepDefinitionListResult> {

    /**
     * Instantiates a new {@link JobStepProperty}.
     *
     * @param name  The name to set into the {@link JobStepProperty}.
     * @param type  The type to set into the {@link JobStepProperty}.
     * @param value The value to set into the {@link JobStepProperty}.
     * @param exampleValue The example value to set into the {@link JobStepProperty}
     * @return The newly instantiated {@link JobStepProperty}.
     * @since 1.1.0
     */
    JobStepProperty newStepProperty(String name, String type, String value, String exampleValue);

    /**
     * Instantiates a new {@link JobStepProperty}.
     *
     * @param name  The name to set into the {@link JobStepProperty}.
     * @param type  The type to set into the {@link JobStepProperty}.
     * @param value The value to set into the {@link JobStepProperty}.
     * @return The newly instantiated {@link JobStepProperty}.
     * @since 1.0.0
     */
    JobStepProperty newStepProperty(String name, String type, String value);

}
