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
package org.eclipse.kapua.service.authentication.shiro;

import org.eclipse.kapua.commons.configuration.ServiceConfigurationManager;

public interface CredentialServiceConfigurationManager extends ServiceConfigurationManager {

    /**
     * The minimum password length specified for the whole system. If not defined, assume 12; if defined and less than 12, assume 12.
     */
    int getSystemMinimumPasswordLength();
}
