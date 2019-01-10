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

import org.eclipse.kapua.model.KapuaUpdatableEntityAttributes;

public class JobExecutionAttributes extends KapuaUpdatableEntityAttributes {

    public static final String JOB_ID = "jobId";
    public static final String STARTED_ON = "startedOn";
    public static final String ENDED_ON = "endedOn";
    public static final String TARGET_IDS = "targetIds";
}
