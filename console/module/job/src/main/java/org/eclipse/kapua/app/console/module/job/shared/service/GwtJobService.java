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

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobQuery;

@RemoteServiceRelativePath("job")
public interface GwtJobService extends RemoteService {

    PagingLoadResult<GwtJob> query(PagingLoadConfig loadConfig, GwtJobQuery gwtJobQuery)
            throws GwtKapuaException;

    /**
     * Creates a new job under the account specified in the JobCreator.
     *
     * @param gwtJobCreator
     * @return
     * @throws GwtKapuaException
     */
    GwtJob create(GwtXSRFToken xsrfToken, GwtJobCreator gwtJobCreator) throws GwtKapuaException;

    /**
     * Returns a Job by its Id or null if a job with such Id does not exist.
     *
     * @param jobId
     * @return
     * @throws GwtKapuaException
     */
    GwtJob find(String accountId, String jobId) throws GwtKapuaException;

    /**
     * Updates a Job in the database and returns the refreshed/reloaded entity instance.
     *
     * @param gwtJob
     * @return
     * @throws GwtKapuaException
     */
    GwtJob update(GwtXSRFToken xsrfToken, GwtJob gwtJob) throws GwtKapuaException;

    /**
     * Delete the supplied Job.
     *
     * @param gwtJobId
     * @throws GwtKapuaException
     */
    void delete(GwtXSRFToken xsfrToken, String accountId, String gwtJobId) throws GwtKapuaException;

    /**
     * Delete the supplied Job forcibly.
     *
     * @param gwtJobId
     * @throws GwtKapuaException
     */
    void deleteForced(GwtXSRFToken xsfrToken, String accountId, String gwtJobId) throws GwtKapuaException;


    ListLoadResult<GwtGroupedNVPair> findJobDescription(String gwtScopeId, String gwtJobId) throws GwtKapuaException;
}
