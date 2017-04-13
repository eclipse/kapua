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
package org.eclipse.kapua.app.console.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GwtExpiresFilter implements Filter {
    // One year in milliseconds.  (Actually, just short of on year, since
    // RFC 2616 says Expires should not be more than one year out, so
    // cutting back just to be safe.)
    public static final long ONE_YEAR_MILLIS = 31363200000L;


    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
    }

    public void destroy() {
        // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
    throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUri = httpRequest.getRequestURI();
        if (requestUri.contains(".nocache.")) {

            // add Expires header -1 for IE cache management
            httpResponse.setHeader("Expires", "-1");
            httpResponse.addHeader("Cache-Control", "public, max-age=0, must-revalidate");
        } else if (requestUri.contains(".cache.")) {

            // add cache expiration headers
            long currentTime = System.currentTimeMillis();
            httpResponse.setDateHeader("Expires",   currentTime + ONE_YEAR_MILLIS);
            httpResponse.setHeader("Cache-Control", "max-age=290304000, public");
        }

        chain.doFilter(request, response);
    }
}
