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
 * {@link JobContextPropertyNames} definition.
 *
 * @since 1.0.0
 */
public class JobContextPropertyNames {

    private JobContextPropertyNames() {
    }

    /**
     * @since 1.0.0
     */
    public static final String JOB_SCOPE_ID = "job.scopeId";

    /**
     * @since 1.0.0
     */
    public static final String JOB_ID = "job.id";

    /**
     * @since 1.0.0
     */
    public static final String JOB_TARGET_SUBLIST = "job.target.sublist";

    /**
     * @since 1.1.0
     */
    public static final String RESUMED_KAPUA_EXECUTION_ID = "job.execution.resumedId";

    /**
     * since 1.1.0
     */
    public static final String RESET_STEP_INDEX = "job.step.resetIndex";

    /**
     * @since 1.0.0
     */
    public static final String JOB_STEP_FROM_INDEX = "job.step.fromIndex";

    /**
     * @since 1.1.0
     */
    public static final String ENQUEUE = "job.enqueue";

    /**
     * @since 1.1.0
     */
    public static final String KAPUA_EXECUTION_ID = "job.execution.id";
}
