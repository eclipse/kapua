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
package org.eclipse.kapua.app.console.server;

import org.apache.commons.io.FileUtils;
import org.eclipse.kapua.app.console.commons.client.views.ViewDescriptor;
import org.eclipse.kapua.app.console.commons.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.shared.service.GwtConsoleService;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

    @Override
    public List<ViewDescriptor> getCustomEntityViews() {
        ServletContext context = getServletContext();
        List<ViewDescriptor> views = new ArrayList<ViewDescriptor>();
        try {
            List<String> viewDescriptorsClasses = FileUtils.readLines(new File(context.getRealPath("/WEB-INF/view-descriptors.txt")));
            for(String viewDescriptorClass : viewDescriptorsClasses) {
                views.add((ViewDescriptor)Class.forName(viewDescriptorClass).newInstance());
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
}
