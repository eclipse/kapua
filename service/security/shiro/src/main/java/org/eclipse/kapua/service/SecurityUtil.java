/*******************************************************************************
 * Copyright (c) 2021 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.env.Environment;
import org.apache.shiro.mgt.SecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to setup Security stuff like the Shiro Security Manager
 *
 */
public class SecurityUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

    private SecurityUtil() {
    }

    public static void initSecurityManager() {
        try {
            Environment env = new BasicIniEnvironment("classpath:shiro.ini");
            SecurityManager securityManager = env.getSecurityManager();
            SecurityUtils.setSecurityManager(securityManager);
        } catch (Exception e) {
            logger.error("Error loading Shiro configuration.", e);
            throw new SecurityException(e);
        }
    }

}
