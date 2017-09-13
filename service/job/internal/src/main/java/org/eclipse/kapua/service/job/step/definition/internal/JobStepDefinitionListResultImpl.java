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
package org.eclipse.kapua.service.job.step.definition.internal;

import org.eclipse.kapua.commons.model.query.KapuaListResultImpl;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;

/**
 * {@link JobStepDefinitionListResult} definition.
 * 
 * @since 1.0.0
 *
 */
public class JobStepDefinitionListResultImpl extends KapuaListResultImpl<JobStepDefinition> implements JobStepDefinitionListResult {

    private static final long serialVersionUID = 977813250632719295L;

}
