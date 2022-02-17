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

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

public class JobExecutionAttributes extends KapuaUpdatableEntityAttributes {

    public static final String JOB_ID = "jobId";
    public static final String STARTED_ON = "startedOn";
    public static final String ENDED_ON = "endedOn";
    public static final String TARGET_IDS = "targetIds";
}
