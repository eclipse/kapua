/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.api.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import io.swagger.jaxrs.config.BeanConfig;

public class SwaggerBootstrapServlet extends HttpServlet {

    private static final long serialVersionUID = -1209653890542687016L;

    private static final String API_VERSION = "1.0.0";
    private static final String API_URL = "localhost:8090";
    private static final String API_BASE_PATH = "api/v1";
    private static final String API_RESOURCE_PACKAGE = "org.eclipse.kapua.app.api.v1.resources";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion(API_VERSION);
        beanConfig.setSchemes(new String[] { "http" });
        beanConfig.setHost(API_URL);
        beanConfig.setBasePath(API_BASE_PATH);
        beanConfig.setResourcePackage(API_RESOURCE_PACKAGE);
        beanConfig.setScan(true);
    }
}
