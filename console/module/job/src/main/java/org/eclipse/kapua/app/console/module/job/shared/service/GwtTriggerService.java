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
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;

@RemoteServiceRelativePath("trigger")
public interface GwtTriggerService extends RemoteService {

    PagingLoadResult<GwtTrigger> findByJobId(PagingLoadConfig loadConfig, String gwtScopeId, String gwtJobId) throws GwtKapuaException;

    GwtTrigger create(GwtXSRFToken xsfrToken, GwtTriggerCreator gwtTriggerCreator) throws GwtKapuaException;

    void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtTriggerId) throws GwtKapuaException;

    boolean validateCronExpression(String cronExpression) throws GwtKapuaException;

    GwtTriggerProperty trickGwt();

}
