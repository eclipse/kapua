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
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTargetCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobTargetQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobTargetServiceImpl extends KapuaRemoteServiceServlet implements GwtJobTargetService {

    @Override
    public PagingLoadResult<GwtJobTarget> query(PagingLoadConfig loadConfig, GwtJobTargetQuery gwtJobTargetQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJobTarget> gwtJobTargetList = new ArrayList<GwtJobTarget>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobTargetService jobTargetService = locator.getService(JobTargetService.class);

            // Convert from GWT entity
            JobTargetQuery jobTargetQuery = GwtKapuaJobModelConverter.convertJobTargetQuery(gwtJobTargetQuery, loadConfig);

            // query
            JobTargetListResult jobTargetList = jobTargetService.query(jobTargetQuery);

            // If there are results
            if (!jobTargetList.isEmpty()) {
                // count
                if (jobTargetList.getSize() >= loadConfig.getLimit()) {
                    totalLength = Long.valueOf(jobTargetService.count(jobTargetQuery)).intValue();
                } else {
                    totalLength = jobTargetList.getSize();
                }

                // Converto to GWT entity
                for (JobTarget jt : jobTargetList.getItems()) {
                    gwtJobTargetList.add(KapuaGwtJobModelConverter.convertJobTarget(jt));
                }
            }

        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return new BasePagingLoadResult<GwtJobTarget>(gwtJobTargetList, loadConfig.getOffset(), totalLength);
    }

    @Override
    public PagingLoadResult<GwtJobTarget> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId) throws GwtKapuaException {
        if (jobId != null) {
            GwtJobTargetQuery gwtJobTargetQuery = new GwtJobTargetQuery();
            gwtJobTargetQuery.setScopeId(scopeId);
            gwtJobTargetQuery.setJobId(jobId);
            return query(loadConfig, gwtJobTargetQuery);
        } else {
            return new BasePagingLoadResult<GwtJobTarget>(new ArrayList<GwtJobTarget>(), 0, 0);
        }
    }

    @Override
    public List<GwtJobTarget> findByJobId(String scopeId, String jobId, boolean fetchDetails) throws GwtKapuaException {
        GwtJobTargetQuery gwtJobTargetQuery = new GwtJobTargetQuery();
        gwtJobTargetQuery.setScopeId(scopeId);
        gwtJobTargetQuery.setJobId(jobId);
        if (fetchDetails) {
            // TODO fetch details
        }
        return query(null, gwtJobTargetQuery).getData();
    }

    @Override
    public List<GwtJobTarget> create(GwtXSRFToken xsrfToken, List<GwtJobTargetCreator> gwtJobTargetCreatorList) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        List<GwtJobTarget> gwtJobTargetList = new ArrayList<GwtJobTarget>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobTargetFactory jobTargetFactory = locator.getFactory(JobTargetFactory.class);

            for (GwtJobTargetCreator gwtJobTargetCreator : gwtJobTargetCreatorList) {
                KapuaId scopeId = KapuaEid.parseCompactId(gwtJobTargetCreator.getScopeId());
                JobTargetCreator jobTargetCreator = jobTargetFactory.newCreator(scopeId);
                jobTargetCreator.setJobId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetCreator.getJobId()));
                jobTargetCreator.setJobTargetId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetCreator.getJobTargetId()));

                //
                // Create the Job Target
                JobTargetService jobTargetService = locator.getService(JobTargetService.class);
                JobTarget jobTarget = jobTargetService.create(jobTargetCreator);

                // convert to GwtJobTarget and return
                gwtJobTargetList.add(KapuaGwtJobModelConverter.convertJobTarget(jobTarget));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJobTargetList;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtJobTargetId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
        KapuaId jobTargetId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetId);

        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobTargetService jobTargetService = locator.getService(JobTargetService.class);
            jobTargetService.delete(scopeId, jobTargetId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}
