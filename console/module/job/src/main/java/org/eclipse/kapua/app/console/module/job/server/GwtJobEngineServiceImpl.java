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
package org.eclipse.kapua.app.console.module.job.server;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStartOptions;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.ArrayList;

public class GwtJobEngineServiceImpl extends KapuaRemoteServiceServlet implements GwtJobEngineService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);

    @Override
    public void start(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        start(gwtScopeId, gwtJobId, null);
    }

    @Override
    public void start(String gwtScopeId, String gwtJobId, GwtJobStartOptions gwtJobStartOptions) throws GwtKapuaException {

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);

        try {
            if (gwtJobStartOptions == null) {
                JOB_ENGINE_SERVICE.startJob(scopeId, jobId);
            } else {
                JobStartOptions jobStartOptions = GwtKapuaJobModelConverter.convertJobStartOptions(gwtJobStartOptions);
                JOB_ENGINE_SERVICE.startJob(scopeId, jobId, jobStartOptions);
            }
        } catch (KapuaException kaex) {
            KapuaExceptionHandler.handle(kaex);
        }
    }

    @Override
    public void stop(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        stopExecution(gwtScopeId, gwtJobId, null);
    }

    @Override
    public void stopExecution(String gwtScopeId, String gwtJobId, String gwtJobExecutionId) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);
        KapuaId jobExecutionId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobExecutionId);

        try {
            JOB_ENGINE_SERVICE.stopJobExecution(scopeId, jobId, jobExecutionId);
        } catch (KapuaException kaex) {
            KapuaExceptionHandler.handle(kaex);
        }
    }

    @Override
    public void restart(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        GwtJobStartOptions gwtJobStartOptions = new GwtJobStartOptions();
        gwtJobStartOptions.setFromStepIndex(0);
        gwtJobStartOptions.setTargetIdSublist(new ArrayList<String>());
        start(gwtScopeId, gwtJobId, gwtJobStartOptions);
    }
}
