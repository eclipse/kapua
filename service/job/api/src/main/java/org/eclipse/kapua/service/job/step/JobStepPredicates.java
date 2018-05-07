/*******************************************************************************
 * Copyright (c) 2017, 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.model.KapuaNamedEntityPredicates;

public interface JobStepPredicates extends KapuaNamedEntityPredicates {

    String JOB_ID = "jobId";
    String STEP_INDEX = "stepIndex";
    String JOB_STEP_DEFINITION_ID = "jobStepDefinitionId";
    String JOB_STEP_NAME = "name";

}
