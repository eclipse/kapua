/*******************************************************************************
 * Copyright (c) 2019, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.job.engine.queue;

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

/**
 * {@link QueuedJobExecutionAttributes} {@link org.eclipse.kapua.model.KapuaEntityAttributes}.
 *
 * @since 1.1.0
 */
public class QueuedJobExecutionAttributes extends KapuaUpdatableEntityAttributes {

    public static final String JOB_ID = "jobId";

    public static final String JOB_EXECUTION_ID = "jobExecutionId";

    public static final String WAIT_FOR_JOB_EXECUTION_ID = "waitForJobExecutionId";
}
