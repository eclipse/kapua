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
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobEngineService;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;

public class GwtJobEngineServiceImpl extends KapuaRemoteServiceServlet implements GwtJobEngineService {

    @Override
    public void start(String gwtScopeId, String gwtJobId) throws GwtKapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        JobEngineService jobEngineService = locator.getService(JobEngineService.class);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobId);

        try {
            jobEngineService.startJob(scopeId, jobId);
        } catch (KapuaException kaex) {
            KapuaExceptionHandler.handle(kaex);
        }

        System.out.println();
    }
}
