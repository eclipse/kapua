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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.job.engine.JobEngineFactory;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Arrays;

public class GwtJobEngineServiceImpl extends KapuaRemoteServiceServlet implements GwtJobEngineService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobEngineService JOB_ENGINE_SERVICE = LOCATOR.getService(JobEngineService.class);
    private static final JobEngineFactory JOB_ENGINE_FACTORY = LOCATOR.getFactory(JobEngineFactory.class);

    @Override
    public void start(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        start(gwtScopeId, gwtJobId, null);
    }

    @Override
    public void start(String gwtScopeId, String gwtJobId, String gwtJobTargetId) throws GwtKapuaException {

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);

        try {
            if (Strings.isNullOrEmpty(gwtJobTargetId)) {
                JOB_ENGINE_SERVICE.startJob(scopeId, jobId);
            } else {
                KapuaId jobTargetId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetId);

                JobStartOptions jobStartOptions = JOB_ENGINE_FACTORY.newJobStartOptions();
                jobStartOptions.setTargetIdSublist(Arrays.asList(jobTargetId));

                JOB_ENGINE_SERVICE.startJob(scopeId, jobId, jobStartOptions);
            }
        } catch (KapuaException kaex) {
            KapuaExceptionHandler.handle(kaex);
        }
    }

    @Override
    public void stop(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);

        try {
            JOB_ENGINE_SERVICE.stopJob(scopeId, jobId);
        } catch (KapuaException kaex) {
            KapuaExceptionHandler.handle(kaex);
        }
    }
}
