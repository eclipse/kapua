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
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.GwtTriggerProperty;
import org.eclipse.kapua.app.console.module.job.shared.model.scheduler.definition.GwtTriggerDefinition;

@RemoteServiceRelativePath("triggerDefinition")
public interface GwtTriggerDefinitionService extends RemoteService {

    ListLoadResult<GwtTriggerDefinition> findAll()
            throws GwtKapuaException;

    GwtTriggerDefinition find(String triggerDefinitionId)
            throws GwtKapuaException;

    // Just to make Gwt serialize GwtTriggerProperty
    GwtTriggerProperty trickGwt();
}
