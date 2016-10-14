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
package org.eclipse.kapua.app.api.auth;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

public class KapuaBasicHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected UsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
        
    	AuthenticationToken authcToken = super.createToken(request, response);
        if (!(authcToken instanceof UsernamePasswordToken)) {
            throw new AuthenticationException("!(authcToken instanceof UsernamePasswordToken");
        }

        UsernamePasswordToken userPassToken = (UsernamePasswordToken) authcToken;

        // TODO Add implement for login with username@account notation

        return userPassToken;
    }
    
}
