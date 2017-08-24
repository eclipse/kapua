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
package org.eclipse.kapua.app.console.module.api.server;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.MainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.TabDescriptor;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleService;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

    @Override
    public List<MainViewDescriptor> getCustomEntityViews() throws GwtKapuaException {
        ServletContext context = getServletContext();
        List<MainViewDescriptor> views = new ArrayList<MainViewDescriptor>();
        try {
            List<String> viewDescriptorsClasses = FileUtils.readLines(new File(context.getRealPath("/WEB-INF/view-descriptors.txt")));
            for (String viewDescriptorClass : viewDescriptorsClasses) {
                views.add((MainViewDescriptor) Class.forName(viewDescriptorClass).newInstance());
            }
        } catch (InstantiationException e) {
            KapuaExceptionHandler.handle(e);
        } catch (IllegalAccessException e) {
            KapuaExceptionHandler.handle(e);
        } catch (ClassNotFoundException e) {
            KapuaExceptionHandler.handle(e);
        } catch (IOException e) {
            KapuaExceptionHandler.handle(e);
        }
        Collections.sort(views);
        return views;
    }

    @Override
    public List<TabDescriptor> getCustomTabsForView(String viewClass) throws GwtKapuaException {
        ServletContext context = getServletContext();
        List<TabDescriptor> tabs = new ArrayList<TabDescriptor>();
        try {
            List<String> tabDescriptorsClasses = FileUtils.readLines(new File(context.getRealPath("/WEB-INF/tab-descriptors.txt")));
            for (String tabDescriptorClass : tabDescriptorsClasses) {
                String[] tabDescriptorParts = tabDescriptorClass.split("->");
                if (tabDescriptorParts.length == 2 && tabDescriptorParts[1].equals(viewClass)) {
                    tabs.add((TabDescriptor) Class.forName(tabDescriptorParts[0]).newInstance());
                }
            }
        } catch (InstantiationException e) {
            KapuaExceptionHandler.handle(e);
        } catch (IllegalAccessException e) {
            KapuaExceptionHandler.handle(e);
        } catch (ClassNotFoundException e) {
            KapuaExceptionHandler.handle(e);
        } catch (IOException e) {
            KapuaExceptionHandler.handle(e);
        }
        Collections.sort(tabs);
        return tabs;
    }

    @Override
    public void updateComponentConfiguration(GwtXSRFToken xsrfToken, String scopeId, String parentScopeId, GwtConfigComponent configComponent) throws GwtKapuaException {
        String serviceClassName = configComponent.getComponentId();
        KapuaLocator locator = KapuaLocator.getInstance();
        try {
            Class<? extends KapuaService> serviceClass = Class.forName(serviceClassName).asSubclass(KapuaService.class);
            KapuaService service = locator.getService(serviceClass);
            if (service instanceof KapuaConfigurableService) {
                KapuaConfigurableService configurableService = (KapuaConfigurableService)service;
                //
                // Checking validity of the given XSRF Token
                checkXSRFToken(xsrfToken);
                KapuaId kapuaScopeId = GwtKapuaCommonsModelConverter.convertKapuaId(scopeId);
                KapuaId kapuaParentId = GwtKapuaCommonsModelConverter.convertKapuaId(parentScopeId);

                // execute the update
                Map<String, Object> configParameters = GwtKapuaCommonsModelConverter.convertConfigComponent(configComponent);
                configurableService.setConfigValues(kapuaScopeId, kapuaParentId, configParameters);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }
}
