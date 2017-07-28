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
package org.eclipse.kapua.app.console.commons.server;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.app.console.commons.client.views.MainViewDescriptor;
import org.eclipse.kapua.app.console.commons.client.views.TabDescriptor;
import org.eclipse.kapua.app.console.commons.shared.service.GwtConsoleService;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

    @Override
    public List<MainViewDescriptor> getCustomEntityViews() {
        ServletContext context = getServletContext();
        List<MainViewDescriptor> views = new ArrayList<MainViewDescriptor>();
        try {
            List<String> viewDescriptorsClasses = FileUtils.readLines(new File(context.getRealPath("/WEB-INF/view-descriptors.txt")));
            for(String viewDescriptorClass : viewDescriptorsClasses) {
                views.add((MainViewDescriptor)Class.forName(viewDescriptorClass).newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(views);
        return views;
    }

    @Override
    public List<TabDescriptor> getCustomTabsForView(String viewClass) {
        ServletContext context = getServletContext();
        List<TabDescriptor> tabs = new ArrayList<TabDescriptor>();
        try {
            List<String> tabDescriptorsClasses = FileUtils.readLines(new File(context.getRealPath("/WEB-INF/tab-descriptors.txt")));
            for(String tabDescriptorClass : tabDescriptorsClasses) {
                String[] tabDescriptorParts = tabDescriptorClass.split("->");
                if (tabDescriptorParts[1].equals(viewClass)) {
                    tabs.add((TabDescriptor)Class.forName(tabDescriptorParts[0]).newInstance());
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(tabs);
        return tabs;
    }
}
