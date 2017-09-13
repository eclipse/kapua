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
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import java.util.List;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;

@RemoteServiceRelativePath("domain")
public interface GwtDomainService extends RemoteService {

    /**
     * Returns the list of all Domains which belong to an account.
     *
     * @return a list of {@link org.eclipse.kapua.service.authorization.domain.Domain} objects
     * @throws GwtKapuaException
     * 
     */
    public List<GwtDomain> findAll()
            throws GwtKapuaException;

    /**
     * Returns the list of all Actions which belong to a Domain.
     *
     * @param domainName
     *            the name of the domain
     * @return a list of {@link org.eclipse.kapua.service.authorization.permission.Action} objects
     * @throws GwtKapuaException
     * 
     */
    public List<GwtAction> findActionsByDomainName(String domainName)
            throws GwtKapuaException;

}
