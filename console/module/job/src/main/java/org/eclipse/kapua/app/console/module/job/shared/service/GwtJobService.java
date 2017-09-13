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
package org.eclipse.kapua.app.console.module.job.shared.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJob;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobQuery;

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
    GwtJob create(GwtXSRFToken xsrfToken, GwtJobCreator gwtJobCreator)
            throws GwtKapuaException;

    /**
     * Returns a Job by its Id or null if a job with such Id does not exist.
     *
     * @param jobId
     * @return
     * @throws GwtKapuaException
     *
     */
     GwtJob find(String accountId, String jobId)
            throws GwtKapuaException;

    /**
     * Updates a Job in the database and returns the refreshed/reloaded entity instance.
     *
     * @param gwtJob
     * @return
     * @throws GwtKapuaException
     */
     GwtJob update(GwtXSRFToken xsrfToken, GwtJob gwtJob)
            throws GwtKapuaException;

    /**
     * Delete the supplied Job.
     *
     * @param gwtJobId
     * @throws GwtKapuaException
     */
     void delete(GwtXSRFToken xsfrToken, String accountId, String gwtJobId)
            throws GwtKapuaException;
}
