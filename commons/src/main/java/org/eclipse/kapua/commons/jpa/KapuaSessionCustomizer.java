/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.jpa;

import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KapuaSessionCustomizer implements org.eclipse.persistence.config.SessionCustomizer {

    private static final Logger logger = LoggerFactory.getLogger(KapuaSessionCustomizer.class);

    public KapuaSessionCustomizer() {
        logger.info("Initializing KapuaSessionCustomizer id:{}", this.hashCode());
    }

    @Override
    public void customize(Session session) throws Exception {
        DatabaseLogin dbLogin = session.getLogin();
        logger.info(
                "\tValidate on error: " + dbLogin.isConnectionHealthValidatedOnError() +
                " - pingSQL: " + dbLogin.getPingSQL() +
                " - attempt: " + dbLogin.getQueryRetryAttemptCount() +
                " - delay: " + dbLogin.getDelayBetweenConnectionAttempts());
        dbLogin.setPingSQL("SELECT 1;");
        dbLogin.setDelayBetweenConnectionAttempts(2500);
        logger.info(
                "\tAFTER PingSQL set - validate on error: " + dbLogin.isConnectionHealthValidatedOnError() +
                " - pingSQL: " + dbLogin.getPingSQL() + " - delay: " + dbLogin.getDelayBetweenConnectionAttempts());
        dbLogin.setConnectionHealthValidatedOnError(true);
    }

}
