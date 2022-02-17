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
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtDomain;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission.GwtAction;

import java.util.List;

@RemoteServiceRelativePath("domain")
public interface GwtDomainRegistryService extends RemoteService {

    /**
     * Returns the list of all Domains which belong to an account.
     *
     * @return a list of {@link org.eclipse.kapua.service.authorization.domain.Domain} objects
     * @throws GwtKapuaException
     */
    public List<GwtDomain> findAll()
            throws GwtKapuaException;

    /**
     * Returns the list of all Actions which belong to a Domain.
     *
     * @param domainName the name of the domain
     * @return a list of {@link org.eclipse.kapua.model.domain.Actions} objects
     * @throws GwtKapuaException
     */
    public List<GwtAction> findActionsByDomainName(String domainName)
            throws GwtKapuaException;

}
