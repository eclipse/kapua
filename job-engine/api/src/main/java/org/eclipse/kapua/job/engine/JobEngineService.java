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
package org.eclipse.kapua.job.engine;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaDomainService;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.job.JobDomain;

public interface JobEngineService extends KapuaService, KapuaDomainService<JobDomain> {

    public static final JobDomain JOB_DOMAIN = new JobDomain();

    @Override
    public default JobDomain getServiceDomain() {
        return JOB_DOMAIN;
    }

    public void startJob(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    public boolean isRunning(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    //    public void pauseJob(KapuaId scopeId, KapuaId jobId) throws KapuaException;

    public void stopJob(KapuaId scopeId, KapuaId jobId) throws KapuaException;

}
