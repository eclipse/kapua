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
package org.eclipse.kapua.job.engine.commons.wrappers;

/**
 * {@link StepContextPropertyNames} definition.
 *
 * @since 1.0.0
 */
public interface StepContextPropertyNames {

    /**
     * @since 1.0.0
     */
    String STEP_INDEX = "step.stepIndex";

    /**
     * @since 1.0.0
     */
    String STEP_NEXT_INDEX = "step.nextStepIndex";

    /**
     * @since 2.0.0
     */
    String STEP_NAME = "step.name";
}
