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
package org.eclipse.kapua.app.console.server;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import org.eclipse.kapua.app.console.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.shared.GwtKapuaException;
import org.eclipse.kapua.app.console.shared.model.job.GwtJobStepDefinition;
import org.eclipse.kapua.app.console.shared.model.job.GwtJobStepProperty;
import org.eclipse.kapua.app.console.shared.service.GwtJobStepDefinitionService;
import org.eclipse.kapua.app.console.shared.util.GwtKapuaModelConverter;
import org.eclipse.kapua.app.console.shared.util.KapuaGwtModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionFactory;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionListResult;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;

import java.util.ArrayList;
import java.util.List;

public class GwtJobStepDefinitionServiceImpl extends KapuaRemoteServiceServlet implements GwtJobStepDefinitionService {

    @Override
    public ListLoadResult<GwtJobStepDefinition> findAll(String scopeId) throws GwtKapuaException {
        List<GwtJobStepDefinition> gwtJobStepDefinitionList = new ArrayList<GwtJobStepDefinition>();
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepDefinitionService jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
            JobStepDefinitionFactory jobStepDefinitionFactory = locator.getFactory(JobStepDefinitionFactory.class);
            JobStepDefinitionQuery jobStepDefinitionQuery = jobStepDefinitionFactory.newQuery(GwtKapuaModelConverter.convert(scopeId));
            JobStepDefinitionListResult result = jobStepDefinitionService.query(jobStepDefinitionQuery);
            for (JobStepDefinition jsd : result.getItems()) {
                gwtJobStepDefinitionList.add(KapuaGwtModelConverter.convertJobStepDefinition(jsd));
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return new BaseListLoadResult<GwtJobStepDefinition>(gwtJobStepDefinitionList);
    }

    @Override
    public GwtJobStepDefinition find(String gwtScopeId, String gwtJobStepDefinitionId) throws GwtKapuaException {
        KapuaId scopeId = KapuaEid.parseCompactId(gwtScopeId);
        KapuaId jobStepDefinitionId = KapuaEid.parseCompactId(gwtJobStepDefinitionId);

        GwtJobStepDefinition gwtJobStepDefinition = null;
        try {
            KapuaLocator locator = KapuaLocator.getInstance();
            JobStepDefinitionService jobStepDefinitionService = locator.getService(JobStepDefinitionService.class);
            JobStepDefinition jobStepDefinition = jobStepDefinitionService.find(scopeId, jobStepDefinitionId);
            if (jobStepDefinition != null) {
                gwtJobStepDefinition = KapuaGwtModelConverter.convertJobStepDefinition(jobStepDefinition);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }

        return gwtJobStepDefinition;
    }

    public GwtJobStepProperty trickGwt() {
        return null;
    }

}
