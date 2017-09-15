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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.commons.model.query.predicate.AbstractKapuaQuery;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;

/**
 * {@link JobStepDefinitionQuery} definition.
 * 
 * @since 1.0.0
 * 
 */
public class JobStepQueryImpl extends AbstractKapuaQuery<JobStep> implements JobStepQuery {

    /**
     * Constructor
     *
     * @param scopeId
     */
    public JobStepQueryImpl(KapuaId scopeId) {
        super(scopeId);
    }
}
