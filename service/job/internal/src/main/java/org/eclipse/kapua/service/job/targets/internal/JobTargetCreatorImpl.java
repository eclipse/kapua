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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;

@KapuaProvider
public class JobTargetCreatorImpl extends AbstractKapuaUpdatableEntityCreator<JobTarget> implements JobTargetCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    private KapuaId jobId;
    private KapuaId jobTargetId;

    protected JobTargetCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

    @Override
    public KapuaId getJobTargetId() {
        return jobTargetId;
    }

    @Override
    public void setJobTargetId(KapuaId jobTargetId) {
        this.jobTargetId = jobTargetId;
    }

}
