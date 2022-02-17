/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.job.shared.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStep;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStepQuery;

@RemoteServiceRelativePath("jobStep")
public interface GwtJobStepService extends RemoteService {

    PagingLoadResult<GwtJobStep> query(PagingLoadConfig loadConfig, GwtJobStepQuery gwtJobQuery)
            throws GwtKapuaException;

    PagingLoadResult<GwtJobStep> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId)
            throws GwtKapuaException;

    GwtJobStep create(GwtXSRFToken xsrfToken, GwtJobStepCreator gwtJobStepCreator)
            throws GwtKapuaException;

    GwtJobStep find(String scopeId, String jobStepId)
            throws GwtKapuaException;

    void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtJobStepId)
            throws GwtKapuaException;

    /**
     * Updates a Job step in the database and returns the refreshed/reloaded entity instance.
     *
     * @param gwtJobStep
     * @return
     * @throws GwtKapuaException
     */
    public GwtJobStep update(GwtXSRFToken xsrfToken, GwtJobStep gwtJobStep)
            throws GwtKapuaException;

    // Just to make Gwt serialize GwtJobStepProperty
    GwtJobStepProperty trickGwt();
}
