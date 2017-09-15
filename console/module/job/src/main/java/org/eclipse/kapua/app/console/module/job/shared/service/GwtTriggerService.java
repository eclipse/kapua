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
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTrigger;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.job.GwtTriggerProperty;

@RemoteServiceRelativePath("trigger")
public interface GwtTriggerService extends RemoteService {

    /**
     * Returns the list of all User which belong to an account.
     *
     * @param gwtScopeId
     * @param gwtJobId
     * @return
     * @throws GwtKapuaException
     *
     */
     PagingLoadResult<GwtTrigger> findByJobId(PagingLoadConfig loadConfig, String gwtScopeId, String gwtJobId)
            throws GwtKapuaException;

     GwtTrigger create(GwtXSRFToken xsfrToken, GwtTriggerCreator gwtTriggerCreator)
            throws GwtKapuaException;

    /**
     * Updates a Trigger in the database and returns the refreshed/reloaded entity instance.
     *
     * @param gwtTrigger
     * @return
     * @throws GwtKapuaException
     */
    GwtTrigger update(GwtXSRFToken xsrfToken, GwtTrigger gwtTrigger)
            throws GwtKapuaException;

    /**
     * Delete the supplied Trigger.
     *
     * @param gwtTriggerId
     * @param gwtScopeId
     * @throws GwtKapuaException
     */
    void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtTriggerId)
            throws GwtKapuaException;

    boolean validateCronExpression(String cronExpression);

    GwtTriggerProperty trickGwt();

}
