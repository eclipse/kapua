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

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecution;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecutionQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobExecutionService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobExecutionServiceImpl extends KapuaRemoteServiceServlet implements GwtJobExecutionService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final JobExecutionService EXECUTION_SERVICE = LOCATOR.getService(JobExecutionService.class);

    @Override
    public PagingLoadResult<GwtJobExecution> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId) throws GwtKapuaException {
        int totalLength = 0;
        List<GwtJobExecution> gwtJobExecutionList = new ArrayList<GwtJobExecution>();
        try {
            GwtJobExecutionQuery gwtJobExecutionQuery = new GwtJobExecutionQuery();
            gwtJobExecutionQuery.setScopeId(scopeId);
            gwtJobExecutionQuery.setJobId(jobId);

            JobExecutionQuery executionQuery = GwtKapuaJobModelConverter.convertJobExecutionQuery(loadConfig, gwtJobExecutionQuery);
            JobExecutionListResult jobExecutionList = EXECUTION_SERVICE.query(executionQuery);
            totalLength = jobExecutionList.getTotalCount().intValue();

            // Converto to GWT entity
            for (JobExecution jobExecution : jobExecutionList.getItems()) {
                gwtJobExecutionList.add(KapuaGwtJobModelConverter.convertJobExecution(jobExecution));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtJobExecution>(gwtJobExecutionList, loadConfig.getOffset(), totalLength);
    }
}
