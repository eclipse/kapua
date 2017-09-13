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
package org.eclipse.kapua.app.console.module.api.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.MainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.TabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;

import java.util.List;

@RemoteServiceRelativePath("console")
public interface GwtConsoleService extends RemoteService {
    List<MainViewDescriptor> getCustomEntityViews()
            throws GwtKapuaException;

    List<TabDescriptor> getCustomTabsForView(String viewClass)
            throws GwtKapuaException;

    void updateComponentConfiguration(GwtXSRFToken xsrfToken, String scopeId, String parentScopeId, GwtConfigComponent configComponent)
            throws GwtKapuaException;
}
