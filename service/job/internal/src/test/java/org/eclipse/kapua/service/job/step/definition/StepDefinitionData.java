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
package org.eclipse.kapua.service.job.step.definition;

import org.eclipse.kapua.model.id.KapuaId;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class StepDefinitionData {

    // Step Definition scratchpad data
    public JobStepDefinition stepDefinition;
    public JobStepDefinitionCreator stepDefinitionCreator;
    public JobStepProperty stepProperty;
    public List<JobStepProperty> stepPropertyList;
    public KapuaId currentStepDefinitionId;

    public StepDefinitionData() {
        cleanup();
    }

    public void cleanup() {
        stepDefinitionCreator = null;
        stepDefinition = null;
        stepProperty = null;
        stepPropertyList = new ArrayList<>();
        currentStepDefinitionId = null;
    }
}
