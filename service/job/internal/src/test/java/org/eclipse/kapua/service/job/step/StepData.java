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
package org.eclipse.kapua.service.job.step;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.internal.JobStepListResultImpl;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class StepData {

    // Step scratchpad data
    public JobStep step;
    public JobStepCreator stepCreator;
    public JobStepProperty stepProperty;
    public List<JobStepProperty> stepPropertyList;
    public JobStepListResult stepList;
    public KapuaId currentStepId;

    public StepData() {
        cleanup();
    }

    public void cleanup() {
        stepCreator = null;
        step = null;
        stepProperty = null;
        stepList = new JobStepListResultImpl();
        stepPropertyList = new ArrayList<>();
        currentStepId = null;
    }
}
