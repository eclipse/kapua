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

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepDefinition;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtJobStepProperty;

@RemoteServiceRelativePath("jobStepDefinition")
public interface GwtJobStepDefinitionService extends RemoteService {

    ListLoadResult<GwtJobStepDefinition> findAll(String scopeId)
            throws GwtKapuaException;

    GwtJobStepDefinition find(String scopeId, String jobStepDefinitionId)
            throws GwtKapuaException;

    // Just to make Gwt serialize GwtJobStepProperty
    GwtJobStepProperty trickGwt();
}
