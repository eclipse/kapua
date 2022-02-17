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
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobExecution;

@RemoteServiceRelativePath("jobExecution")
public interface GwtJobExecutionService extends RemoteService {

    /**
     * Returns the list of executions by their jobId
     *
     * @param scopeId
     * @param jobId
     * @return
     * @throws GwtKapuaException
     */
    PagingLoadResult<GwtJobExecution> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId)
            throws GwtKapuaException;
}
