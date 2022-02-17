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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobStartOptions;

@RemoteServiceRelativePath("jobEngine")
public interface GwtJobEngineService extends RemoteService {

    void start(String gwtScopeId, String gwtJobId)
            throws GwtKapuaException;

    void start(String gwtScopeId, String gwtJobId, GwtJobStartOptions gwtJobStartOptions)
            throws GwtKapuaException;

    void stop(String gwtScopeId, String gwtJobId)
            throws GwtKapuaException;

    void stopExecution(String gwtScopeId, String gwtJobId, String gwtJobExecutionId)
            throws GwtKapuaException;

    void restart(String gwtScopeId, String gwtJobId)
            throws GwtKapuaException;

    void restart(String gwtScopeId, String gwtJobId, String gwtJobTargetId)
            throws GwtKapuaException;
}
