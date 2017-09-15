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

import org.eclipse.kapua.model.KapuaUpdatableEntityPredicates;

public interface JobExecutionPredicates extends KapuaUpdatableEntityPredicates {

    public String JOB_ID = "jobId";
    public String STARTED_ON = "startedOn";
    public String ENDED_ON = "endedOn";
}
