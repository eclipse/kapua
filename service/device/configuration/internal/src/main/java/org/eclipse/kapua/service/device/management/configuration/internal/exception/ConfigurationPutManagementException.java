/*******************************************************************************
 * Copyright (c) 2018 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.configuration.internal.exception;

import org.eclipse.kapua.service.device.management.response.KapuaResponseCode;

public class ConfigurationPutManagementException extends ConfigurationManagementResponseException {

    public ConfigurationPutManagementException(KapuaResponseCode responseCode, String exceptionMessage, String exceptionStack) {
        super(ConfigurationManagementResponseErrorCodes.CONFIGURATION_PUT_ERROR, responseCode, exceptionMessage, exceptionStack);
    }
}
