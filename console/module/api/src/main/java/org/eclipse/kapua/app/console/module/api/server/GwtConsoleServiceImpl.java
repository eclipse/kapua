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

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.MainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.TabDescriptor;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtConsoleService;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.config.KapuaConfigurableService;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

    private static final ServiceLoader<MainViewDescriptor> MAIN_VIEW_DESCRIPTORS = ServiceLoader.load(MainViewDescriptor.class);
    private static final JsonObject TAB_DESCRIPTORS;
    private static JsonReader jsonReader;

    static {
        jsonReader = null;
        try {
            jsonReader = Json.createReader(ResourceUtils.openAsReader(ResourceUtils.getResource("org/eclipse/kapua/app/console/tab-descriptors.json"), Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        TAB_DESCRIPTORS = jsonReader.readObject();
    }

    @Override
    public List<MainViewDescriptor> getCustomEntityViews() throws GwtKapuaException {
        List<MainViewDescriptor> views = new ArrayList<MainViewDescriptor>();
        for (MainViewDescriptor descriptor : MAIN_VIEW_DESCRIPTORS) {
            views.add(descriptor);
        }
        Collections.sort(views);
        return views;
    }

    @Override
    public List<TabDescriptor> getCustomTabsForView(String viewClass) throws GwtKapuaException {
        List<TabDescriptor> tabs = new ArrayList<TabDescriptor>();
        try {
            JsonArray jsonArray = TAB_DESCRIPTORS.getJsonArray(viewClass);
            if (jsonArray != null) {
                for (JsonValue jsonValue : jsonArray) {
                    if (jsonValue != null && jsonValue instanceof JsonString) {
                        tabs.add((TabDescriptor) Class.forName(((JsonString) jsonValue).getString()).newInstance());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, e);
        } catch (InstantiationException e) {
            throw new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, e);
        } catch (ClassNotFoundException e) {
            throw new GwtKapuaException(GwtKapuaErrorCode.INTERNAL_ERROR, e);
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
                KapuaConfigurableService configurableService = (KapuaConfigurableService) service;
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
